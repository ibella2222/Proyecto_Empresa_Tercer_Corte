@echo off
set MAVEN_VERSION=3.8.6
setlocal
set WRAPPER_DIR=%~dp0
java -jar %WRAPPER_DIR%\.mvn\wrapper\maven-wrapper.jar %*