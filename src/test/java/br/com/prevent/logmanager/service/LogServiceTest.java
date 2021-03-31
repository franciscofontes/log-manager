package br.com.prevent.logmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.domain.Page;

@SpringBootTest
class LogServiceTest {

	@Autowired
	private LogService service;

	@Test
	void testAdicionar() throws MethodArgumentNotValidException {
		Log log = new Log("123.123.123.123", "GET", "200", "Crome");
		service.adicionar(log);
		assertTrue(log.getId() > 0);
	}

	@Test
	void testEditar() throws MethodArgumentNotValidException {
		Log log = new Log("123.123.123.125", "GET", "200", "Crome");
		Long id = 4l;
		log.setId(id);
		service.editar(log);
		assertEquals(service.buscarPorId(id).getIp(), log.getIp());
	}

	@Test
	void testListar() {
		List<Log> logs = service.listar();
		assertFalse(logs.isEmpty());
	}

	@Test
	void testBuscarPorId() {
		Log log = service.buscarPorId(1l);
		assertFalse(log == null);
	}

	@Test
	void testRemover() throws MethodArgumentNotValidException {
		Long id = 1l;
		service.remover(id);
	}	
	
	@Test
	void testListarPorPagina() {
		Page<Log> page = service.listarPorPagina(2, 12, "ip", "desc");
		System.out.println(page);
		assertFalse(page.getContent().isEmpty());
	}
	
	@Test
	void testlistarPorFiltro() throws ParseException {
		String dataString = "";	
		String ip = "";
		String status = "";
		String request = "GET";
		String userAgent = "Teste";
		List<Log> logs = service.listarPorFiltro(dataString, ip, status, request, userAgent, 1, 10, "id", "asc");
		for (Log log : logs) {
			System.out.println(log);
		}
		assertFalse(logs.isEmpty());
	}
	
	@Test
	void testGetLogsPeloArquivo() throws MethodArgumentNotValidException, IOException, ParseException {
		List<Log> logs = service.getLogsPeloArquivo("C:\\tmp\\log\\access.log", "\\|");
		assertFalse(logs.isEmpty());		
	}
	
	@Test
	void testAdicionarLogsPeloArquivo() throws MethodArgumentNotValidException, IOException, ParseException {
		service.adicionarLogsPeloArquivo("C:\\tmp\\log\\access-poucosdados.log", "\\|");
	}

}
