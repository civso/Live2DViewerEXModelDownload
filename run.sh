#!/bin/bash

mvn compile
mvn exec:java -Dexec.mainClass="org.Live2DViewerEX.ModelDownload.Main"

read -p "Press Enter to exit..."