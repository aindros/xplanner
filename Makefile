CATALINA_HOME = tomcat9
CATALINA      = ${CATALINA_HOME}/bin/catalina.sh
APPNAME       = target/xplanner.war
JPDA_OPTS     = -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=y

all: ${APPNAME}

${APPNAME}:
	mvn clean package -DskipTests=true

deploy: ${APPNAME}
	cp ${APPNAME} ${CATALINA_HOME}/webapps

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

