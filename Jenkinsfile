pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }
        stage('Test') {
            steps {
                sh 'mvn verify'
            }
        }
        stage('Deployment') {
            steps {
                withKubeConfig([credentialsId: 'kubeconfig']) {
                    sh 'kubectl apply -f bulletinboard.yaml'
                }z
            }
        }
    }
}