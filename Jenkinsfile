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
                    sh 'mvn package -DskipTests=true'
                }
            }
        }

        stage('deploy') {
            steps {
                script {
                    scp target/xplanner.war xplanner@86.107.98.155:~/xplanner.war
                }
            }
        }
    }
}
