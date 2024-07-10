This is homework project for Rohlik interview. Look at the assignment in pdf file for details.
Tech Stack:
Micronaut Data, Kotlin, Postgres, Docker, Kubernetes

1. To install Docker in clean `wsl` (`wsl --unregister Ubuntu`, `wsl --install`), run this script

         # remove docker
         sudo service docker stop
         sudo apt-get purge -y docker-engine docker docker.io docker-ce docker-ce-cli containerd runc
         sudo apt-get autoremove -y --purge docker-engine docker docker.io docker-ce
         sudo rm -rf /var/lib/docker
         sudo rm -rf /etc/docker
         sudo rm -rf /var/run/docker.sock
         sudo rm -rf /usr/local/bin/docker-compose
         docker --version

         # install docker
         sudo apt update
         sudo apt install apt-transport-https ca-certificates curl software-properties-common
         curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
         sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
         sudo apt update
         sudo apt install docker-ce docker-ce-cli containerd.io
         sudo groupadd docker
         sudo usermod -aG docker ${USER}
         docker --version
         exit

2. To install k8s, switch to dir `k8s` and run in `wsl`:

         #sudo service docker start
         #sudo systemctl enable docker
         curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
         sudo install minikube-linux-amd64 /usr/local/bin/minikube
         curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
         sudo install kubectl /usr/local/bin/kubectl
         minikube start --driver=docker
         minikube status
         kubectl get nodes

3. To run in docker-compose or debug locally, rebuild project `rebuild.bat` and run in `wsl`:

         ./ss.sh    # start docker compose with postgres and knuspr app on http://localhost:8000 (if failed, ctrl+c and run again)
         ./q.sh     # to shut down docker compose

4. To debug, run app in IDE on default port 8080. It connects to postgres in previous docker-compose container.

5. To test docker app, run docker and go to `http://localhost:8000/products`

7. To debug FE, run step 3 and then run `npm run start` in dir `frontend` in IDE. It connects to previous docker-compose knuspr app for data.

8. To run in k8s, clean docker with `./dp.sh` and run `./ss.sh` to create local docker images `cablo/knuspr-postgres-image` and `cablo/knuspr-app-image`. Then switch to dir `k8s` and run

         ./images-publish.sh

   Then use script `ss.sh` for

   start minikube: `minikube start`

   start dashboard: `minikube dashboard`

   and deploy to k8s cluster: `kubectl apply -f k8s-deployment.yaml`

   To shut down k8s, use commands from `q.sh`.

9. Try to run more replicas for `knusper` and see connection count in postgres

         SELECT * FROM pg_stat_database

   There is a default limit of 100 connections in postgres, so more than 10 replicas (each one has 10 connections) will fail


