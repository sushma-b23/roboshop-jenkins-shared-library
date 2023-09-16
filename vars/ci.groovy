def call() {
  try {
    pipeline {

        agent {
            label 'workstation'
        }
        stages {
            stage('Compile/Build') {
                steps {
                    echo 'compile'
                }
                script {
                    common.compile
                }
            }
            stage('Unit Tests') {
                steps {
                    echo 'Unit Tests'
                script {
                    common.unittests()
                }
                }
            }
            stage('quality Control') {
                SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user --with-decryption --query parameters[0].value | sed \'s/"//g\')'
                SONAR_PASS = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.pass --with-decryption --query parameters[0].value | sed \'s/"//g\')'
            }
             steps {
                 script {
                     SONAR_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.pass --with-decryption --query parameters[0].value | sed \'s/"//g\'', returnstdout: true).trim()
                     wrap([$class: 'MaskPasswordsBuildWrapper', varspasswordpairs: [[password: "${mypassword}", var: 'PASSWORD']]]) {
                         println "Password = ${SONAR_PASS}"
                         sh "echo sh password = ${SONAR_PASS}"
                         ssh "sonar-scanner -Dsonar.host.url=http://172.31.31.111:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectkey=cart"

                    }
                }
            }
            stage('Upload Code to Centralized Place') {
                steps {
                    echo 'Upload'
                }
            }
        }
    }
}
  catch(Exception e) {
      common.email("Failed")
  }
}

