podTemplate(label: 'robertalab-pod', containers: [
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'docker', image: 'docker:17.06.0-dind', privileged: true, ttyEnabled: true)
  ],
  volumes: [
    nfsVolume(mountPath: '/data/config', serverAddress: '10.240.255.5', serverPath: '/var/nfs/jenkinsslave', readOnly: true)
]) {

    node('robertalab-pod') {
        def succ = true
        def err = ''
        try {
            stage('Build Stage: ') {
                slackSend channel: "#build_status", message: "Build Started - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
                
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
                    sh("ssh -i /data/config/id_rsa -oStrictHostKeyChecking=no stemuser@stemgarden.chinanorth.cloudapp.chinacloudapi.cn 'rm -fr /home/stemuser/deploy/robertalab/k8s'")
                    sh("ssh -i /data/config/id_rsa stemuser@stemgarden.chinanorth.cloudapp.chinacloudapi.cn 'mkdir -p /home/stemuser/deploy/robertalab/k8s'")
                    sh("scp -i /data/config/id_rsa -r ./deploy/k8s/* stemuser@stemgarden.chinanorth.cloudapp.chinacloudapi.cn:/home/stemuser/deploy/robertalab/k8s/")
                    sh("ssh -i /data/config/id_rsa stemuser@stemgarden.chinanorth.cloudapp.chinacloudapi.cn '/home/stemuser/deploy/robertalab/k8s/deploy.sh ${env.BUILD_NUMBER}'")
                }
            }
        } catch (error) {
            succ = false
            err = "error: ${error}"
            throw error
        } finally {
            notifyStatus(succ, err)
        }
    }
}

def notifyStatus(success, error){
    def label = success? "SUCCESS":"FAILED"
    mail (to: 'tiantiaw@microsoft.com',
        subject: "Job '${env.JOB_NAME}' (${env.BUILD_NUMBER}) is ${label}",
        body: "msg: ${error}");

    if (label == 'SUCCESS') {
        color = 'GREEN'
        colorCode = '#00FF00'
    } else {
        color = 'RED'
        colorCode = '#FF0000'
    }

    slackSend color: colorCode, channel: "#build_status", message: "Build ${label} - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
}