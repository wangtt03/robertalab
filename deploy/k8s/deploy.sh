#!/bin/bash

DEPLOYDIR="/home/stemuser/deploy/robertalab/k8s/"

cd $DEPLOYDIR

ROBERTALAB_VERSION=$1

sed -i "s|<robertalab_version>|${ROBERTALAB_VERSION}|g" robertalab.yaml

kubectl delete -f dockerregsecret.yaml --namespace=robertalabns
kubectl delete -f robertalab.yaml --namespace=robertalabns
kubectl delete namespace robertalabns

kubectl create namespace robertalabns
kubectl apply -f dockerregsecret.yaml --namespace=robertalabns
kubectl apply -f robertalab.yaml --namespace=robertalabns
