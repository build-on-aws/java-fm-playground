#!/usr/bin/env bash

cd infrastructure

echo "Deploying Backend..."
cdk deploy Backend

echo "Updating config.js..."
api_base_url_placeholder="YOUR_API_BASE_URL"

source="./templates/config.js"
destination="../frontend/config.js"
cp $source $destination

key="apiBaseUrl"
api_base_url=$(aws cloudformation describe-stacks --stack-name JavaFMPlayground-Backend --query "Stacks[0].Outputs[?ExportName=='ApiBaseUrl'].OutputValue" --output text --region us-east-1)

sed -i "s|\($key: \)\(\".*\"\)|\1\"$api_base_url\"|" $destination

echo "Deploying Frontend..."
cdk deploy Frontend

frontendUrl=$(aws cloudformation describe-stacks --stack-name JavaFMPlayground-Backend --query "Stacks[0].Outputs[?ExportName=='FrontendUrl'].OutputValue" --output text --region us-east-1)
echo "Deployment succeeded. Navigate to https://${frontendUrl}"