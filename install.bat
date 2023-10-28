@echo off
cd infrastructure

echo Deploying Backend...
call cdk deploy Backend

echo Updating config.js...
set api_base_url_placeholder=YOUR_API_BASE_URL
set source=.\templates\config.js
set destination=..\frontend\config.js
copy %source% %destination%

set key=apiBaseUrl

for /f %%i in ('aws cloudformation describe-stacks --stack-name JavaFMPlayground-Backend --query "Stacks[0].Outputs[?ExportName=='ApiBaseUrl'].OutputValue" --output text --region us-east-1') do set api_base_url=%%i

powershell -Command "(Get-Content %destination%) -replace '%api_base_url_placeholder%', '%api_base_url%' | Set-Content %destination%"

echo Deploying Frontend...
call cdk deploy Frontend

for /f %%j in ('aws cloudformation describe-stacks --stack-name JavaFMPlayground-Backend --query "Stacks[0].Outputs[?ExportName=='FrontendUrl'].OutputValue" --output text --region us-east-1') do set frontendUrl=%%j

echo Deployment succeeded. Navigate to https://%frontendUrl%

cd ..