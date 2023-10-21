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

clean: stop
	mvn clean
	@rm -rf ${CATALINA_HOME}/webapps/xplanner
