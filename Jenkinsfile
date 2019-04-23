#!/usr/bin/env groovy
def VERSION = 'UNKNOWN'

pipeline {
  agent none
  environment {
    DOCKER_REPO_NAME = "cmays/twitter-client"
  }

  tools {
    jdk "Java11"
    maven "M3"
  }

  stages {
    stage('Build') {
      agent any
      steps {
        script  {
          VERSION = sh(script: 'mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout --batch-mode',returnStdout: true)
        }
        echo "${VERSION}"
        #sh 'mvn verify sonar:sonar -Dsonar.host.url=http://cicdsonar.marathon.l4lb.thisdcos.directory:9000'
      }
    }

    stage('Make Container') {
      agent any
      steps {
        sh "docker build -t ${DOCKER_REPO_NAME}:${VERSION} ."
      }
    }

    stage('Push Container') {
      agent any
      when {
        branch 'master'
      }
      steps {
        withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh "docker login -u ${USERNAME} -p ${PASSWORD}"
          sh "docker push ${DOCKER_REPO_NAME}:${VERSION}"
          echo "${VERSION}"
        }
      }
    }
  }
}

