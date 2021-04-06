package br.com.prevent.logmanager.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.service.LogService;

@SpringBootTest
@AutoConfigureMockMvc
public class LogResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LogService service;

	@Test
	void testAdicionar() throws Exception {

		Log log = new Log("123.123.123.123", "GET", "200", "Crome");

		mockMvc.perform(
				post("/logs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(log)))
				.andExpect(status().isCreated());
	}

	@Test
	void testEditar() throws Exception {

		Log log = this.service.buscarPorId(1l);
		log.setIp("200.200.200.200");

		mockMvc.perform(
				put("/logs/{id}", log.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(log)))
				.andExpect(status().isNoContent());
	}
	
	@Test
	void testRemover() throws Exception {
		
		Log log = this.service.buscarPorId(2l);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/logs/{id}", log.getId()))
				.andExpect(status().isNoContent());
	}

}
