package ch.furthermore.pmslweb;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.furthermore.pmsl.Parser;
import ch.furthermore.pmsl.Scanner;
import ch.furthermore.pmsl.wf.SerializedToken;
import ch.furthermore.pmsl.wf.Token;
import ch.furthermore.pmsl.wf.WFWorkflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample interaction
 * <pre>
 * curl -s -d 'workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end' -H'Content-Type:text/plain' http://localhost:8080/workflow
 * 
 * {"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node1","vars":{"id":"3d96f87a-23ee-4f4a-9a43-2402af1a7307"},"children":[]}}
 * 
 * curl -s -d '{"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node1","vars":{"id":"3d96f87a-23ee-4f4a-9a43-2402af1a7307"},"children":[]}}' -H'Content-Type:application/json' http://localhost:8080/instance
 * 
 * {"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node2","vars":{"x":111,"id":"3d96f87a-23ee-4f4a-9a43-2402af1a7307"},"children":[]}}
 * 
 * curl -s -d '{"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node2","vars":{"x":111,"id":"3d96f87a-23ee-4f4a-9a43-2402af1a7307"},"children":[]}}' -H'Content-Type:application/json' http://localhost:8080/instance
 * 
 * {"workflow":"workflow foo state node1 leave x=111 end transition to node2 end state node2 leave y=222 end transition to node3 end node node3 enter z=333 end end end","token":{"node":"node3","vars":{"x":111,"y":222,"z":333,"id":"3d96f87a-23ee-4f4a-9a43-2402af1a7307"},"children":[]}}
 * </pre>
 */
@Controller
public class WorkflowController {
	private final static Logger log = LoggerFactory.getLogger(WorkflowController.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(path="/workflow",consumes="text/plain",produces="application/json",method=RequestMethod.POST)
	@ResponseBody
	WorkflowInstance startWorkflow(@RequestBody String workflowText, @RequestParam(name="autoStart", required=false) String autoStart) {
		try {
			WFWorkflow wf = workflow(workflowText);
			
			Token t = new Token(wf, new RequestBuiltIns(request));
			
			if (autoStart == null || !"false".equals(autoStart)) {
				t.signal();
			}
			
			return new WorkflowInstance(workflowText, new SerializedToken(t));
		}
		catch (Exception e) {
			log.warn("start error", e); //FIXME debug log
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(path="/instance",consumes="application/json",produces="application/json",method=RequestMethod.POST)
	@ResponseBody
	WorkflowInstance signalWorkflowRootToken(@RequestBody WorkflowInstance workflow) {
		try {
			WFWorkflow wf = workflow(workflow.getWorkflow());
			
			Token t = workflow.getToken().token(wf, new RequestBuiltIns(request));
			t.signal();
			
			return new WorkflowInstance(workflow.getWorkflow(), new SerializedToken(t));
		}
		catch (Exception e) {
			log.warn("signal root error", e); //FIXME debug log
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(path="/instance/{tokenId}",consumes="application/json",produces="application/json",method=RequestMethod.POST)
	@ResponseBody
	WorkflowInstance signalWorkflowToken(@RequestBody WorkflowInstance workflow, @PathVariable("tokenId") String tokenId) {
		try {
			WFWorkflow wf = workflow(workflow.getWorkflow());
			
			Token t = workflow.getToken().token(wf, new RequestBuiltIns(request));
			t.findById(tokenId).signal();
			
			return new WorkflowInstance(workflow.getWorkflow(), new SerializedToken(t));
		}
		catch (Exception e) {
			log.warn("signal error. wf={}, t={}", workflow, tokenId, e); //FIXME debug log
			throw new RuntimeException(e);
		}
	}
	
	private WFWorkflow workflow(String workflowText) throws IOException {
		Parser p = new Parser(new Scanner(new StringReader(workflowText)));
		return p.wfWorkflow();
	}
}
