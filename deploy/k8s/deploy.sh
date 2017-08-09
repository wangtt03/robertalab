#!/bin/bash

DEPLOYDIR="/home/stemuser/deploy/robertalab/k8s/"

cd $DEPLOYDIR

ROBERTALAB_VERSION=$1

sed -i "s|<robertalab_version>|${ROBERTALAB_VERSION}|g" robertalab_deployment.yaml

kubectl delete -f robertalab_service.yaml
kubectl delete -f robertalab_deployment.yaml

kubectl create -f robertalab_deployment.yaml
kubectl create -f robertalab_service.yaml
