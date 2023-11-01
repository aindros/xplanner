pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    def pomFile = readFile(file: 'pom.xml')
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Pacakge') {
            steps {
                script {
                    sh 'make update-revision'
                    sh 'mvn package -DskipTests=true'
                }
            }
        }

        stage('Stop tomcat9') {
            steps {
                script {
                    sh "ssh xplanner@86.107.98.155 '~/tomcat9/bin/catalina.sh stop'"
                }
            }
        }

        stage('deploy') {
            steps {
                script {
                    sh "ssh xplanner@86.107.98.155 'rm -rf ~/tomcat9/webapps/xplanner'"
                    sh "ssh xplanner@86.107.98.155 'rm -rf ~/tomcat9/webapps/xplanner.war'"
                    sh "scp target/xplanner.war xplanner@86.107.98.155:~/tomcat9/webapps/xplanner.war"
                }
            }
        }

        stage('Start tomcat9') {
	        steps {
	            script {
	                sh "ssh xplanner@86.107.98.155 '~/tomcat9/bin/catalina.sh start'"
	            }
	        }
	    }
    }
}
