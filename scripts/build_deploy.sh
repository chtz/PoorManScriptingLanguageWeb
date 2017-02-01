#!/bin/bash
mvn package docker:build
docker push dockerchtz/pmsl-web:latest
ssh root@pmsl 'docker rm -f $(docker ps -a -q) && docker rmi dockerchtz/pmsl-web && docker run -p 8080:8080 -d dockerchtz/pmsl-web'

