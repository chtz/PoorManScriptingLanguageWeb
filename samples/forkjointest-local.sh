#!/bin/bash
curl -s --data-binary @forkjointest.pmsl -H'Content-Type:text/plain' http://localhost:8080/workflow > instance.json
token_id=$(cat instance.json | jq -r '.token.children[0].vars.id')
#echo $token_id 
#cat instance.json | jq ".token" | json_pp

curl -s --data-binary @instance.json -H'Content-Type:application/json' http://localhost:8080/instance/$token_id?x=foo > instance2.json
token_id=$(cat instance2.json | jq -r '.token.children[0].vars.id')
#echo $token_id 
#cat instance2.json | jq ".token" | json_pp

curl -s --data-binary @instance2.json -H'Content-Type:application/json' http://localhost:8080/instance/$token_id?x=bar > instance3.json
cat instance3.json | jq ".token" | json_pp
