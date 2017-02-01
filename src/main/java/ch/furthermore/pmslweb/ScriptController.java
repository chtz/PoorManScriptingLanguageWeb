package ch.furthermore.pmslweb;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.furthermore.pmsl.ScriptFunction;

/**
 * Invoke Samples:
 * </pre>
 * curl -s -d 'def foo() ret "hallo" end' -H"Content-Type:text/plain" http://localhost:8080/script
 * </pre>
 */
@Controller
public class ScriptController {
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(path="/script",consumes="text/plain",produces="text/plain",method=RequestMethod.POST)
	@ResponseBody
	String executeScript(@RequestBody String script) {
		try {
			ScriptFunction f = new ScriptFunction(script, new RequestBuiltIns(request));
			return f.invoke().toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
