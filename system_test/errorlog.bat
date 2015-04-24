@echo off

:loop
if "%~1" neq "" (
  echo Logging %1 ...
  java -enableassertions -jar jar/Words.jar %1 -testmode -displayerror 2>&1 | findstr /i "error" >%1.log 

  shift
  goto :loop
)
