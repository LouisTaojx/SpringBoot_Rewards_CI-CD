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
              set -eu

              JAR="$(ls -1 target/*.jar | head -n 1)"
              echo "Deploying $JAR"
              cp "$JAR" /opt/myapp/current/app.jar

              sudo -n /bin/systemctl restart myapp

              # must match sudoers exactly: no extra flags
              STATUS="$(sudo -n /bin/systemctl is-active myapp || true)"
              if [ "$STATUS" != "active" ]; then
                echo "myapp is not active: $STATUS"
                sudo -n /bin/systemctl status myapp
                exit 1
              fi

              echo "myapp is active."
            '''
          }
        }



    }
}
