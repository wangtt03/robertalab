#!/bin/bash

DEPLOYDIR="/home/stemuser/deploy/robertalab/k8s/"

cd $DEPLOYDIR

ROBERTALAB_VERSION=$1

sed -i "s|<robertalab_version>|${ROBERTALAB_VERSION}|g" robertalab.yaml

kubectl apply -f dockerregsecret.yaml
kubectl apply -f robertalab.yaml
