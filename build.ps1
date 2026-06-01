$ErrorActionPreference = "Stop"
$SrcDir = "src"
$BinDir = "bin"
$JarName = "esame.jar"
$MainClass = "esame.Main"

Write-Host "--- Starting Build ---" -ForegroundColor Cyan

Write-Host "Removing old build..."
if (Test-Path $BinDir) {
  Remove-Item -Recurse -Force $BinDir
}
New-Item -ItemType Directory -Path $BinDir | Out-Null

Write-Host "Locating source files..."
$JavaFiles = Get-ChildItem -Path $SrcDir -Filter "*.java" -Recurse | ForEach-Object { 
  $CleanPath = $_.FullName -replace '\\', '/'
  "`"$CleanPath`""
}
if ($JavaFiles.Count -eq 0) {
  Write-Error "No .java files found in $SrcDir"
  Exit
}

$SourceListFile = "sources.txt"
$JavaFiles | Out-File -FilePath $SourceListFile -Encoding utf8NoBOM

Write-Host "Compiling source files..."
javac -verbose -d "$BinDir" "@$SourceListFile"
if ($LASTEXITCODE -ne 0) { throw "javac failed." }
Remove-Item $SourceListFile

Write-Host "Packaging into $BinDir/$JarName..."
jar --verbose -c -f "$BinDir/$JarName" -e $MainClass -C $BinDir .
if ($LASTEXITCODE -ne 0) { throw "jar failed." }

Write-Host "--- Build Successful! ---" -ForegroundColor Green
Write-Host "Run using: java -jar $BinDir/$JarName"