pipeline {
	    agent any

	    environment {
            DOCKER_IMAGE = 'cinematch-backend'
            DOCKER_TAG = "${BUILD_NUMBER}"
            DB_CREDS = credentials('DB_CREDENTIALS')
            DB_URL = credentials('DB_URL')
            S3_BUCKET_NAME = credentials('S3_BUCKET_NAME')
	    }

	    stages {
            stage('Build') {
                steps {
                    // Build with Maven
                    sh """
                        export DATABASE_URL=${DB_URL}
                        export USERNAME=${DB_CREDS_USR}
                        export PASSWORD=${DB_CREDS_PSW}
                        mvn clean package
                    """
                }
	        }

            stage('Docker Build') {
                steps {
                    script {
                        // Build new image
                        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    }
                }
            }

            stage('Deploy') {
                steps {
                    script {
                        // Stop existing container
                        sh "docker stop ${DOCKER_IMAGE} || true"
                        sh "docker rm ${DOCKER_IMAGE} || true"

                        // Run new container with environment variables
                        sh """
                            docker run -d \\
                            --name ${DOCKER_IMAGE} \\
                            -p 8082:8080 \\
                            -e DATABASE_URL=${DB_URL} \\
                            -e USERNAME=${DB_CREDS_USR} \\
                            -e PASSWORD=${DB_CREDS_PSW} \\
                            -e s3.bucket.name=${S3_BUCKET_NAME} \\
                            --restart unless-stopped \\
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                        """
                    }
                }
            }
        }

	    post {
            success {
                echo 'Pipeline succeeded!'
            }
            failure {
                echo 'Pipeline failed!'
            }
	    }
	}