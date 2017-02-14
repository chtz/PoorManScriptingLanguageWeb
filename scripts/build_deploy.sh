#!/bin/bash
mvn package docker:build
docker push dockerchtz/pmsl-web:latest
ssh root@pmsl '(docker rm -f pmsl-web; true) && docker rmi dockerchtz/pmsl-web && docker run -p 8080:8080 --name pmsl-web -d dockerchtz/pmsl-web'
