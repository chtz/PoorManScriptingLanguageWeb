package ch.furthermore.pmslweb;

import javax.servlet.http.HttpServletRequest;

import ch.furthermore.pmsl.BuiltIn;

public class RequestBuiltIns {
	private final HttpServletRequest request;

	public RequestBuiltIns(HttpServletRequest request) {
		this.request = request;
	}
	
	@BuiltIn
	public String getParam(String name) {
		return request.getParameter(name);
	}
}
