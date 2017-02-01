# PoorManScriptingLanguageWeb

A STATELESS workflow engine (i.e. client is responsible for storing workflow instance state).

This is pre-alpha stuff - use it at your own risk.

Pre-Cond: https://github.com/chtz/PoorManScriptingLanguage "maven installed locally" (this is only the web wrapper project).

## Sample

Cmd:
```
cat forkjointest.pmsl	
```
Result:
```
workflow forkjointest
	node start
		transition to left
		transition to right
		
		enter
			left = ""
			right = ""
		end
	end
	
	state left
		transition to done
		
		enter
			left = "enter"
		end
		
		leave
			left = "leave " + getParam("x")
		end
	end
	
	state right
		transition to done
	
		enter
			right = "enter"
		end
		
		leave
			right = "leave " + getParam("x")
		end
	end
	
	join done
		enter
		  done = "enter " + getParam("x")
		end
	end
end
```
Cmd:
```
curl -s --data-binary @forkjointest.pmsl -H'Content-Type:text/plain' https://pmsl.furthermore.ch/workflow > instance.json
token_id=$(cat instance.json | jq -r '.token.children[0].vars.id')
cat instance.json | jq ".token" | json_pp
echo First child token: $token_id
```
Result:
```
{
   "children" : [
      {
         "children" : [],
         "node" : "left",
         "vars" : {
            "id" : "71a62615-32f1-49aa-9293-edb4d110075b"
         }
      },
      {
         "node" : "right",
         "vars" : {
            "id" : "547428d4-38d3-49e8-a9b1-66a6aee40ee4"
         },
         "children" : []
      }
   ],
   "node" : "start",
   "vars" : {
      "right" : "enter",
      "id" : "ac9f93c5-f133-4604-b395-38b95cf81479",
      "left" : "enter"
   }
}
First child token: 71a62615-32f1-49aa-9293-edb4d110075b
```
Cmd:
```
curl -s --data-binary @instance.json -H'Content-Type:application/json' https://pmsl.furthermore.ch/instance/$token_id?x=foo > instance2.json
token_id=$(cat instance2.json | jq -r '.token.children[0].vars.id')
cat instance2.json | jq ".token" | json_pp
echo Second child token: $token_id
```
Result:
```
{
   "children" : [
      {
         "vars" : {
            "id" : "547428d4-38d3-49e8-a9b1-66a6aee40ee4"
         },
         "node" : "right",
         "children" : []
      }
   ],
   "node" : "start",
   "vars" : {
      "left" : "leave foo",
      "id" : "ac9f93c5-f133-4604-b395-38b95cf81479",
      "right" : "enter"
   }
}
Second child token: 547428d4-38d3-49e8-a9b1-66a6aee40ee4
```
Cmd:
```
curl -s --data-binary @instance2.json -H'Content-Type:application/json' https://pmsl.furthermore.ch/instance/$token_id?x=bar > instance3.json
cat instance3.json | jq ".token" | json_pp
```
Result:
```
{
   "children" : [],
   "node" : "done",
   "vars" : {
      "left" : "leave foo",
      "right" : "leave bar",
      "id" : "ac9f93c5-f133-4604-b395-38b95cf81479",
      "done" : "enter bar"
   }
}
```
