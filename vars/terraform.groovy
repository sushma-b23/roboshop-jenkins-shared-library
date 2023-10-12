def call() {
    pipeline {
        agent {
            node {
                label 'workstation'

            }
        }
        parameters {
            string(name: 'INFRA_ENV', defaultvalue: '', description: 'Enter Env like dev or prod')

        }
        stages {
            stage('Terraform Init') {
                steps {
                    sh "terraform init -backend-config=env-${INFR-ENV}/state.tfvars"
                }
            }
        }
    }
}