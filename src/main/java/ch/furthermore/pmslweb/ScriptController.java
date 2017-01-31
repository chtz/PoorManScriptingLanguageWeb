package ch.furthermore.pmslweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.ScriptException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.furthermore.pmsl.ScriptFunction;

/**
 * Build & Run Samples
 * <pre>
 * mvn package docker:build
 * docker run -p 8080:8080 -t chtzdc/pmsl-web
 * </pre>
 * 
 * Invoke Samples
 * </pre>
 * curl -s -d 'def foo() ret "hallo" end' -H"Content-Type:text/plain" http://localhost:8080/ 
 * </pre>
 */
@Controller
@EnableAutoConfiguration
public class ScriptController {
	@RequestMapping("/")
	@ResponseBody
	String home() throws NoSuchMethodException, IOException, ScriptException {
		String sample = "curl -s -d 'def foo() ret \"hallo\" end' -H'Content-Type:text/plain' http://<host>:<post>/".replaceAll("\"", "\\\\\"");
		return executeScript("def hello() ret \"" + sample + "\" end");
	}
	
	@RequestMapping(path="/",consumes="text/plain",produces="text/plain",method=RequestMethod.POST)
	@ResponseBody
	String executeScript(@RequestBody String script) {
		try {
			ScriptFunction f = new ScriptFunction(script);
			return f.invoke().toString();
		}
		catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			return sw.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ScriptController.class, args);
	}
}
