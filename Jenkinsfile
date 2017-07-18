podTemplate(label: 'mypod', containers: [
    containerTemplate(name: 'maven', image: 'csdiregistry.azurecr.io/maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'docker', image: 'docker:17.06.0-dind', privileged: true, ttyEnabled: true),
    containerTemplate(name: 'ubuntu', image: 'ubuntu:16.04', ttyEnabled: true),
  ], imagePullSecrets: {name: 'csdiregsecret'}) {

    node('mypod') {
        stage('Build Stage: ') {
            container('maven') {
                stage('Clone repository') {
                    checkout scm
                }
                
                stage('Clean & Install') {
                    sh 'cd OpenRobertaParent && mvn install -DskipTests'
                }
            }
        }
        
        stage('Publish Stage: ') {
            def robertalab
            container('docker') {
                stage('Build Docker Image') {
                    // sh 'docker build -t csdiregistry.azurecr.io/demo/javademo .'
                    docker.withRegistry('https://csdiregistry.azurecr.io/', 'csdiregistryuser') {
                        def imageName = 'stem/robertalab'
                        sh 'docker login '
                        sh "docker build -t ${imageName} ."
                        robertalab = docker.image(imageName)
                    }
                }
                
                stage('Publish Docker Image') {
                    docker.withRegistry('https://csdiregistry.azurecr.io/', 'csdiregistryuser') {
                        robertalab.push("${env.BUILD_NUMBER}")
                    }
                    // sh 'docker login -u csdiregistry -p p+rd=e///+==v/Z6pMf/F/x/JKpVH5Vh csdiregistry.azurecr.io'
                    // sh 'docker push csdiregistry.azurecr.io/demo/javademo:latest'
                }
            }
            
        }

        stage('Deploy TestEnv Stage: ') {
            container('ubuntu'){
                stage('Deploy') {
                    /*ssh to master node and run docker stack deploy*/
                    sh("chmod a+x ./deploy/k8s/deploy.sh")
                    sh("ssh stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn 'rm -fr /home/stemuser/deploy/robertalab/k8s'")
                    sh("ssh stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn 'mkdir -p /home/stemuser/deploy/robertalab/k8s'")
                    sh("scp -r ./deploy/k8s/* stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn:/home/stemuser/deploy/robertalab/k8s/")
                    sh("ssh stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn '/home/stemuser/deploy/robertalab/k8s/deploy.sh ${env.BUILD_NUMBER}'")
                }
            }
        }

    }
}
