CATALINA_HOME = tomcat9
CATALINA      = ${CATALINA_HOME}/bin/catalina.sh
APPNAME       = target/xplanner.war

all: ${APPNAME}

${APPNAME}:
	mvn clean package -DskipTests=true
