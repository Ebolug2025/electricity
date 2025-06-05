pipeline {
   agent none

    environment {
        NEXUS_CREDS = credentials('nexus_creds')
        NEXUS_DOCKER_REPO = '172.16.12.5:8082'
        GIT_COMMIT_ID='env.GIT_COMMIT'
        PROJECT_NAME='vasgate_electricity_service'
        IMAGE_NAME="$NEXUS_DOCKER_REPO/$PROJECT_NAME:$BUILD_NUMBER"
        ANSIBLE_EXTRAS="-vvv --extra-vars  image_name=$IMAGE_NAME"


    }

    stages {

       stage('Build') {
            agent {

label 'Jenkins_Agent1'

//label 'Ansible_Agent'

    }
            steps {

                stash includes: 'ansible_deploy.yaml', name: 'ans_yaml'
                    echo 'Building docker Image'

             sh 'docker build -t "${IMAGE_NAME}" .'

             ///       jiraSendBuildInfo()
                }
        }

       stage('Docker Login') {
            agent {

label 'Jenkins_Agent1'



    }
            steps {
                echo 'Nexus Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'nexus_creds', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                       sh ' echo $PASS | docker login -u $USER --password-stdin $NEXUS_DOCKER_REPO'
                    }

                }


            }

        }

        stage('Docker Push') {
                agent {

label 'Jenkins_Agent1'

//label 'Ansible_Agent'

    }
            steps {
                echo 'Pushing Imgaet to docker hub - updated2'
            // sh 'docker push $NEXUS_DOCKER_REPO/card_creation_minimal:$BUILD_NUMBER'
              sh 'docker push "${IMAGE_NAME}"'
            }
        }


            stage('Docker Release') {
           agent {
  label 'Ansible_Agent'

}

            steps {
                echo 'Run docker using ansible agent 1'



        sh 'ansible-galaxy collection install -r requirements.yml'


   echo GIT_COMMIT_ID

   ansiblePlaybook disableHostKeyChecking: true, extras: '${ANSIBLE_EXTRAS}', become: true, becomeUser: 'jenkins', credentialsId: 'jenkins', installation: 'ansible_on_172.16.12.5', inventory: 'hosts', playbook: 'ansible_deploy.yaml', vaultTmpPath: ''


            }
        }

    }
}