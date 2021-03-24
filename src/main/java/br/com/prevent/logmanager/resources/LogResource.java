package br.com.prevent.logmanager.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.prevent.logmanager.domain.Log;

@RestController
@RequestMapping(value = "/logs")
public class LogResource {

	@RequestMapping(method = RequestMethod.GET)
	public List<Log> listar() {
		
		Log log1 = new Log("123.456.565.123", "GET", "200", "Device"); 
		Log log2 = new Log("123.456.565.000", "GET", "201", "Device");
		
		return Arrays.asList(log1, log2); 
	}
}
