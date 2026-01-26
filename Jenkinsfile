pipeline {
    agent any

    tools {
        jdk 'jdk17'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh '''
                  chmod +x mvnw || true
                  ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                  set -e
                  JAR=$(ls target/*.jar | head -n 1)
                  sudo cp "$JAR" /opt/myapp/current/app.jar
                  sudo systemctl restart myapp
                '''
            }
        }
    }
}
