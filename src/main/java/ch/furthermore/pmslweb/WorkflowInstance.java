package ch.furthermore.pmslweb;

import ch.furthermore.pmsl.wf.SerializedToken;

public class WorkflowInstance {
	private String workflow;
	private SerializedToken token;
	
	public WorkflowInstance() {}
	
	public WorkflowInstance(String workflow, SerializedToken token) {
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
