CATALINA_HOME = tomcat9
CATALINA      = ${CATALINA_HOME}/bin/catalina.sh
JPDA_OPTS     = -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=y
VERSION_POM  != grep '<version' pom.xml | head -1 | sed -E 's/.*<.*>(.*)<.*>/\1/g'
APPNAME       = xplanner
WARPACKAGE    = target/${APPNAME}.war
SRCPACKAGE    = target/${APPNAME}-${VERSION_POM}-sources.tar.gz

all: ${WARPACKAGE}

${WARPACKAGE}:
	mvn clean package -DskipTests=true

${SRCPACKAGE}:
	@mkdir -p target/sources
	@cp Jenkinsfile target/sources/
	@cp LICENSE     target/sources/
	@cp Makefile    target/sources/
	@cp README.md   target/sources/
	@cp pom.xml     target/sources/
	@cp -r src      target/sources/
	@cp -r scripts      target/sources/
	@tar cfvz $@ -C target/sources/ .

release: ${WARPACKAGE} ${SRCPACKAGE}
	@cp ${WARPACKAGE} target/${APPNAME}-${VERSION_POM}.war

tag-release:
	@scripts/update-version.sh

deploy: ${WARPACKAGE}
	cp ${WARPACKAGE} ${CATALINA_HOME}/webapps

debug:
	JPDA_OPTS=${JPDA_OPTS} ${CATALINA} jpda start

start stop:
	${CATALINA} $@

tail:
	tail -f ${CATALINA_HOME}/logs/catalina.out

clean: stop
	mvn clean
	@rm -rf ${CATALINA_HOME}/webapps/xplanner
	@rm -rf ${CATALINA_HOME}/webapps/xplanner.war
	@rm -rf ${CATALINA_HOME}/logs/*.log
	@rm -rf ${CATALINA_HOME}/logs/*.txt

yoda: clean deploy start tail
yodad: clean deploy debug tail

kill:
	@for pid in `ps aux | grep java | grep -Ev 'grep|intelli' | awk '{print $2}'`; do kill -9 $pid; done
