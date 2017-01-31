# PoorManScriptingLanguageWeb

A stateless workflow engine ;-)

Pre-alpha - use at your own risk...

## Sample Workflow

```
workflow foo 
	state node1 
		leave 
			x=111 
		end 
		transition to node2 
	end 
	
	state node2 
		leave 
			y=222 
		end 
		
		transition to node3 
	end 
	
	node node3 
		enter 
			z=333 
		end
	end 
end
```

## Create Workflow Instance

### Command

```
curl -s -d 'workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end' -H'Content-Type:text/plain' https://pmsl.furthermore.ch/workflow
```

### Result (json_pp)

```
{
   "workflow" : "workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end",
   "token" : {
      "node" : "node1",
      "children" : [],
      "vars" : {
         "id" : "96f6f1ae-3f8e-4db3-a34d-85b0c9c71ddb"
      }
   }
}
```

## Signal Workflow Instance

### Command

```
curl -s -d '{"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node1","vars":{"id":"96f6f1ae-3f8e-4db3-a34d-85b0c9c71ddb"},"children":[]}}' -H'Content-Type:application/json' https://pmsl.furthermore.ch/instance
```

### Result (json_pp)

```
{
   "workflow" : "workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end",
   "token" : {
      "node" : "node2",
      "children" : [],
      "vars" : {
         "x" : 111,
         "id" : "96f6f1ae-3f8e-4db3-a34d-85b0c9c71ddb"
      }
   }
}
```

## Signal Workflow Instance

### Command

```
curl -s -d '{"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node2","vars":{"x":111,"id":"96f6f1ae-3f8e-4db3-a34d-85b0c9c71ddb"},"children":[]}}' -H'Content-Type:application/json' https://pmsl.furthermore.ch/instance
```

### Result (json_pp)

```
{
   "workflow" : "workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end",
   "token" : {
      "children" : [],
      "vars" : {
         "z" : 333,
         "x" : 111,
         "id" : "96f6f1ae-3f8e-4db3-a34d-85b0c9c71ddb",
         "y" : 222
      },
      "node" : "node3"
   }
}
```
