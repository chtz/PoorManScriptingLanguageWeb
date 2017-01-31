package ch.furthermore.pmslweb;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.furthermore.pmsl.Parser;
import ch.furthermore.pmsl.Scanner;
import ch.furthermore.pmsl.wf.SerializedToken;
import ch.furthermore.pmsl.wf.Token;
import ch.furthermore.pmsl.wf.WFWorkflow;
import ch.furthermore.pmslweb.ScriptController.SimpleRequest;

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
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(path="/workflow",consumes="text/plain",produces="application/json",method=RequestMethod.POST)
	@ResponseBody
	Workflow startWorkflow(@RequestBody String workflow) {
		try {
			Parser p = new Parser(new Scanner(new StringReader(workflow)));
			WFWorkflow wf = p.wfWorkflow();
			Token t = new Token(wf, new SimpleRequest(request));
			t.signal();
			
			return new Workflow(workflow, new SerializedToken(t));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(path="/instance",consumes="application/json",produces="application/json",method=RequestMethod.POST)
	@ResponseBody
	Workflow signalWorkflow(@RequestBody Workflow workflow) {
		try {
			Parser p = new Parser(new Scanner(new StringReader(workflow.getWorkflow())));
			WFWorkflow wf = p.wfWorkflow();
			Token t = workflow.getToken().token(wf, new SimpleRequest(request));
			t.signal();
			
			return new Workflow(workflow.getWorkflow(), new SerializedToken(t));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class Workflow {
		private String workflow;
		private SerializedToken token;
		
		public Workflow() {}
		
		public Workflow(String workflow, SerializedToken token) {
			this.workflow = workflow;
			this.token = token;
		}

		public String getWorkflow() {
			return workflow;
		}
		
		public void setWorkflow(String workflow) {
			this.workflow = workflow;
		}
		
		public SerializedToken getToken() {
			return token;
		}
		
		public void setToken(SerializedToken token) {
			this.token = token;
		}
	}
}
