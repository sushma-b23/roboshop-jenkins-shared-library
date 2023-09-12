def call() {

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
                }
            }
            stage('quality Control') {
                steps {
                    echo 'Quality Control'
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