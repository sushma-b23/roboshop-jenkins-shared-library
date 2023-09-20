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
    if (app_lang == "nodejs") {
        sh "zip -r cart-${TAG_NAME}.zip node_modules server.js"
    }
    sh 'ls -l'
}