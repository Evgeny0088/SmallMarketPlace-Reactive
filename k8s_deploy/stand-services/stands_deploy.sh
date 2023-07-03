#!/bin/bash
NODE_IP=$(minikube ip)

cat configMaps/stand-config-map.yml | sed "s/{{NODE_IP}}/$NODE_IP/g" > s_copy_1.yml
cat s_copy_1.yml | sed "s/{{DB_HOST}}/$NODE_IP/g" > s_copy_2.yml
cat s_copy_2.yml | sed "s/{{DB_NAME}}/$POSTGRES_DB/g" > s_copy_3.yml
cat s_copy_3.yml | sed "s/{{DB_USER}}/$POSTGRES_USER/g" > s_copy_4.yml
cat s_copy_4.yml | sed "s/{{DB_PASS}}/$POSTGRES_PASS/g" > s_copy_5.yml
cat s_copy_5.yml | sed "s/{{REDIS_PASS}}/$REDIS_PASS/g" > s_copy_6.yml
cat s_copy_6.yml | sed "s/{{REDIS_HOST}}/$NODE_IP/g" > s_copy_7.yml

kubectl apply -f s_copy_7.yml
kubectl apply -f configMaps/prometheus-config-map.yml
kubectl apply -f kafka/kafka_deploy.yml
kubectl apply -f kafka/zookeeper_deploy.yml
kubectl apply -f postgres/marketplaceDB_deploy.yml
kubectl apply -f redis/redis_deploy.yml
kubectl apply -f grafana-stack/grafana_deploy.yml
kubectl apply -f grafana-stack/prometheus-deploy.yml

rm -r s_copy_*.yml

# loki deploy with heml:
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm search repo loki
helm upgrade --install loki  grafana/loki-stack -f grafana-stack/loki-values.yaml

### if required ###
#helm show values grafana/loki > loki-values.yaml

