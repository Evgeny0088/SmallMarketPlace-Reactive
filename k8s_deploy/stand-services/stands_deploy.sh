#!/bin/bash
NODE_IP=$(minikube ip)

cat configMaps/stand-config-map.yml | sed "s/{{SERVICE_HOST_CUBER}}/$NODE_IP/g" > s_copy_1.yml
cat s_copy_1.yml | sed "s/{{DB_NAME}}/$POSTGRES_DB/g" > s_copy_2.yml
cat s_copy_2.yml | sed "s/{{DB_USER}}/$POSTGRES_USER/g" > s_copy_3.yml
cat s_copy_3.yml | sed "s/{{DB_PASS}}/$POSTGRES_PASS/g" > s_copy_4.yml
cat s_copy_4.yml | sed "s/{{REDIS_PASS}}/$REDIS_PASS/g" > s_copy_5.yml

kubectl apply -f s_copy_5.yml
kubectl apply -f kafka/zookeeper_deploy.yml
kubectl apply -f kafka/kafka_deploy.yml
kubectl apply -f postgres/marketplaceDB_deploy.yml
kubectl apply -f redis/redis_deploy.yml
kubectl apply -f grafana-stack/grafana_deploy.yml
kubectl apply -f grafana-stack/service_monitor.yml
rm -r s_copy_*.yml

# loki deploy with helm when repo is updated:
helm upgrade --install loki grafana/loki-stack -f grafana-stack/loki-values.yaml
### if helm does not exist then:
#helm repo add grafana https://grafana.github.io/helm-charts
#helm repo update
#helm search repo loki
#helm show values grafana/loki > loki-values.yaml

# prometheus helm install when helm repo is updated
helm upgrade --install prometheus prometheus-community/kube-prometheus-stack -f grafana-stack/prometheus_values.yml

#### if helm repo does not exist then:
#helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
#helm repo update
#helm show values prometheus-community/kube-prometheus-stack > prometheus_values.yml
#helm install -f prometheus_values.yaml prometheus prometheus-community/kube-prometheus-stack

