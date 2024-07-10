#!/bin/bash

kubectl delete -f k8s-deployment.yaml

kubectl delete --all pods --all-namespaces
kubectl delete --all deployments --all-namespaces
kubectl delete --all services --all-namespaces
#  minikube delete