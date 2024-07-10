#!/bin/bash

#  minikube delete
#  minikube start
#  minikube dashboard
kubectl apply -f k8s-deployment.yaml
kubectl get deployments
kubectl get pods
kubectl get services
#   minikube service postgres-service
#   minikube service knuspr-service

