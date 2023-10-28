Set-Location .\infrastructure

Write-Host "Deploying Backend..."
cdk deploy Backend

Write-Host "Updating config.js..."
$api_base_url_placeholder = "YOUR_API_BASE_URL"
$source = ".\templates\config.js"
$destination = "..\frontend\config.js"
Copy-Item $source $destination

$key = "apiBaseUrl"
$api_base_url = aws cloudformation describe-stacks --stack-name JavaFMPlayground-Backend --query "Stacks[0].Outputs[?ExportName=='ApiBaseUrl'].OutputValue" --output text --region us-east-1

$content = Get-Content -Path $destination
$content = $content -replace $api_base_url_placeholder, $api_base_url
Set-Content -Path $destination -Value $content

Write-Host "Deploying Frontend..."
cdk deploy Frontend

$frontendUrl = aws cloudformation describe-stacks --stack-name JavaFMPlayground-Backend --query "Stacks[0].Outputs[?ExportName=='FrontendUrl'].OutputValue" --output text --region us-east-1
Write-Host "Deployment succeeded. Navigate to https://$frontendUrl"

Set-Location ..\