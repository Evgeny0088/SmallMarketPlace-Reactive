#!/bin/bash
NODE_IP=$(minikube ip)

kafka_config_map=`cat "configMaps/kafkaConfigMap.yml" | sed "s/{{NODE_IP}}/$NODE_IP/g"`

cat secrets/secrets.yml | sed "s/{{DB_HOST}}/$(echo $NODE_IP | base64)/g" > s_copy_1.yml
cat s_copy_1.yml | sed "s/{{DB_NAME}}/$(echo $POSTGRES_DB | base64)/g" > s_copy_2.yml
cat s_copy_2.yml | sed "s/{{DB_USER}}/$(echo $POSTGRES_USER | base64)/g" > s_copy_3.yml
cat s_copy_3.yml | sed "s/{{DB_PASS}}/$(echo $POSTGRES_PASS | base64)/g" > s_copy_4.yml
cat s_copy_4.yml | sed "s/{{REDIS_PASS}}/$(echo $REDIS_PASS | base64)/g" > s_copy_5.yml
cat s_copy_5.yml | sed "s/{{REDIS_HOST}}/$(echo $NODE_IP | base64)/g" > s_copy_6.yml

secrets=`cat s_copy_6.yml`
rm -r s_copy_*.yml

echo "$kafka_config_map" | kubectl apply -f -
echo "$secrets" | kubectl apply -f -
kubectl apply -f configMaps/prometheus-config-map.yml
kubectl apply -f kafka/kafka_deploy.yml
kubectl apply -f kafka/zookeeper_deploy.yml
kubectl apply -f postgres/marketplaceDB_deploy.yml
kubectl apply -f redis/redis_deploy.yml
kubectl apply -f grafana-stack/grafana_deploy.yml
kubectl apply -f grafana-stack/prometheus-deploy.yml
# loki deploy with heml:
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm search repo loki
helm upgrade --install loki  grafana/loki-stack -f grafana-stack/loki-values.yaml

### if required ###
#helm show values grafana/loki > loki-values.yaml

