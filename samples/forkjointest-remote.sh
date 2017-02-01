#!/bin/bash
echo Sample Workflow:
cat forkjointest.pmsl

curl -s --data-binary @forkjointest.pmsl -H'Content-Type:text/plain' https://pmsl.furthermore.ch/workflow > instance.json
token_id=$(cat instance.json | jq -r '.token.children[0].vars.id')

echo Result:
cat instance.json | jq ".token" | json_pp
echo First child token: $token_id

curl -s --data-binary @instance.json -H'Content-Type:application/json' https://pmsl.furthermore.ch/instance/$token_id?x=foo > instance2.json
token_id=$(cat instance2.json | jq -r '.token.children[0].vars.id')

echo Result:
cat instance2.json | jq ".token" | json_pp
echo Second child token: $token_id

curl -s --data-binary @instance2.json -H'Content-Type:application/json' https://pmsl.furthermore.ch/instance/$token_id?x=bar > instance3.json

echo Result:
cat instance3.json | jq ".token" | json_pp
