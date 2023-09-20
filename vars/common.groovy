def compile() {
    if (app_lang == "nodejs") {
        sh 'npm install'
    }
    if (app_lang == "maven") {
        sh 'mvn package'
    }
}

def unittests() {
    if (app_lang == "nodejs") {
    //Developer is missing unit test cases in our project, he need to add them as best practice. we are skipping to proceed further
        sh 'npm test'
    }
    if (app_lang == "maven") {
        sh 'mvn test'
    }
    if(app_lang == "python") {
        sh 'python -m unittest'
    }
}
def email(email_note) {
    sh 'echo ${email_note}'
}
def artifactpush() {
    sh "echo ${TAG_NAME} >VERSION"
    if (app_lang == "nodejs") {
        sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js"
    }


    NEXUS_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names nexus.pass --with-decryption --query parameters[0].value | sed \'s/"//g\'' , returnStdout: true).trim()
    NEXUS_USER = sh ( script: 'aws ssm get-parameters --region us-east-1 --names nexus.user --with-decryption --query parameters[0].value | sed \'s/"//g\'' , returnStdout: true).trim()
    wrap([$class: 'MaskPasswordsBuildWrapper', VarPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
        sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload.file pom.xml http://localhost:8081/repository/${component}/${component}-{TAG_NAME}.zip"
    }

}