@echo off
chcp 65001

call mvn compile
call mvn exec:java -Dexec.mainClass="org.Live2DViewerEX.ModelDownload.Main"

pause