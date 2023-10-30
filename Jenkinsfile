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
                build job: "xplanner-deploy", wait: true
            }
        }
    }
}
