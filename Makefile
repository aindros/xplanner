CATALINA_HOME = tomcat9
CATALINA      = ${CATALINA_HOME}/bin/catalina.sh
APPNAME       = target/xplanner.war

all: ${APPNAME}

${APPNAME}:
	mvn clean package -DskipTests=true

deploy: ${APPNAME}
	cp ${APPNAME} ${CATALINA_HOME}/webapps

start stop:
	${CATALINA} $@

tail:
	tail -f ${CATALINA_HOME}/logs/catalina.out

clean: stop
	mvn clean
	@rm -rf ${CATALINA_HOME}/webapps/xplanner
