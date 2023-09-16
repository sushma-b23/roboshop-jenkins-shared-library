def call() {
  try {
      node('workstation') {
          stage('Cleanup') {
              cleanWs()
          }
          stage('Compile/Build') {
              common.compile()
          }
          stage('Unit Tests') {
              common.tests()

          }
          stage('Quality Control') {
              SONAR_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.pass --with-decryption --query parameters[0].value | sed \'s/"//g\'', returnstdout: true).trim()
              SONAR_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.user --with-decryption --query parameters[0].value | sed \'s/"//g\'', returnstdout: true).trim()
              wrap([$class: 'MaskPasswordsBuildWrapper', varsPasswordPairs: [[password: "${SONAR_PASS}", vars: 'SECRET']]]) {
                  sh "sonar-scanner -Dsonar.host.url=http://172.31.31.111:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectkey=cart"
              }
          }


          stage('Upload Code to Centralized Place') {
              echo 'Upload'
              }
          }
      } catch(Exception e) {
          common.email("Failed")
      }
  }



