package ch.furthermore.pmslweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Build Samples:
 * <pre>
 * mvn package docker:build
 * docker push dockerchtz/pmsl-web:latest
 * </pre>
 * 
 * Run Samples:
 * <pre>
 * docker run -p 8080:8080 -d dockerchtz/pmsl-web
 * </pre>
 */
@SpringBootApplication
public class Application {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
