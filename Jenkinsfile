pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE_NAME = 'eatout-webservice'
        DOCKER_IMAGE_TAG = "${env.BUILD_NUMBER}" // Use Jenkins build number as tag
        DOCKER_NETWORK = 'eatout-db-network'
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Checkout your Spring Boot project from GitHub
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                // Build the Spring Boot application
                sh './mvnw package'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Build the Docker image using Dockerfile with unique tag
                sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
            }
        }
        
        stage('Run Docker Container') {
            steps {
                // Create the network if it doesn't exist
                sh "docker network create ${DOCKER_NETWORK} || true"
                
                // Run the Docker container, connecting it to the network
                sh "docker run -d -p 8081:8081 --network=${DOCKER_NETWORK} --name ${DOCKER_IMAGE_NAME}-container ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
            }
        }
    }
}
