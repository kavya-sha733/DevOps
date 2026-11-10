param(
  [string]$Image = "kavyashan2006dev47/self-healing-app:2.0.0",
  [string]$Version = "3.0.0",
  [string]$Break = "false",
  [int]$TimeoutSeconds = 300
)
$ns  = "selfhealing"
$dep = "self-healing-app"
$t0 = Get-Date
Write-Host "[$(Get-Date -Format HH:mm:ss)] Releasing version $Version (image $Image, APP_BREAK=$Break)"
kubectl rollout pause deployment/$dep -n $ns
kubectl set image deployment/$dep app=$Image -n $ns
kubectl set env deployment/$dep APP_VERSION=$Version APP_BREAK=$Break -n $ns
kubectl rollout resume deployment/$dep -n $ns
kubectl rollout status deployment/$dep -n $ns "--timeout=${TimeoutSeconds}s"
if ($LASTEXITCODE -ne 0) {
  Write-Host "[$(Get-Date -Format HH:mm:ss)] RELEASE FAILED - rolling back automatically" -ForegroundColor Red
  kubectl rollout undo deployment/$dep -n $ns
  kubectl rollout status deployment/$dep -n $ns --timeout=300s
  Write-Host "[$(Get-Date -Format HH:mm:ss)] ROLLED BACK to the last working version" -ForegroundColor Green
} else {
  Write-Host "[$(Get-Date -Format HH:mm:ss)] RELEASE OK" -ForegroundColor Green
}
Write-Host ("Total time: {0:N0}s" -f ((Get-Date) - $t0).TotalSeconds)
kubectl get pods -n $ns
