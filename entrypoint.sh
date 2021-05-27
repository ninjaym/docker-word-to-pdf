#!/usr/bin/env sh

mkdir -p /data

echo "starting ${APP_NAME}..."
CMD="java -jar /opt/${APP_NAME}/${APP_NAME}-1.0-SNAPSHOT.jar"
echo $CMD
eval $CMD