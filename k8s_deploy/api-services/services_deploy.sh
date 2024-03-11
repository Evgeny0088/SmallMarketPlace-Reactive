#!/bin/bash

cat configMaps/service_configs.yml | sed "s/{{PROJECT_NAME}}/$PROJECT_NAME/g" > s_copy_1.yml
cat s_copy_1.yml | sed "s/{{PROJECT_PASS}}/$PROJECT_PASS/g" > s_copy_2.yml
cat s_copy_2.yml | sed "s/{{SECRET}}/$SECRET/g" > s_copy_3.yml
cat s_copy_3.yml | sed "s/{{EMAIL}}/$EMAIL/g" > s_copy_4.yml
cat s_copy_4.yml | sed "s/{{EMAIL_PASS}}/$EMAIL_PASS/g" > s_copy_5.yml

kubectl apply -f s_copy_5.yml
kubectl apply -f service_deployments/service-ingress.yml
kubectl apply -f service_deployments/config-service_deploy.yml
kubectl apply -f service_deployments/gateway-service_deploy.yml
kubectl apply -f service_deployments/auth-service_deploy.yml
kubectl apply -f service_deployments/profile-service_deploy.yml
kubectl apply -f service_deployments/item-service_deploy.yml
kubectl apply -f service_deployments/sale-service_deploy.yml

rm -r s_copy_*.yml

