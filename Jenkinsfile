podTemplate(label: 'mypod', containers: [
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'docker', image: 'docker:17.06.0-dind', privileged: true, ttyEnabled: true),
    containerTemplate(name: 'ubuntu', image: 'ubuntu:16.04', ttyEnabled: true),
  ],
  volumes: [
    nfsVolume(mountPath: '/data/config', serverAddress: '10.240.255.5', serverPath: '/var/nfs/jenkinsslave', readOnly: true)
]) {

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
            stage('Deploy') {
                /*ssh to master node and run docker stack deploy*/
                sh("chmod a+x ./deploy/k8s/deploy.sh")
                sh("ssh -i /data/config/id_rsa -oStrictHostKeyChecking=no stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn 'rm -fr /home/stemuser/deploy/robertalab/k8s'")
                sh("ssh -i /data/config/id_rsa stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn 'mkdir -p /home/stemuser/deploy/robertalab/k8s'")
                sh("scp -i /data/config/id_rsa -r ./deploy/k8s/* stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn:/home/stemuser/deploy/robertalab/k8s/")
                sh("ssh -i /data/config/id_rsa stemuser@tiantiaw-poctest.chinanorth.cloudapp.chinacloudapi.cn '/home/stemuser/deploy/robertalab/k8s/deploy.sh ${env.BUILD_NUMBER}'")
            }
        }

    }
}