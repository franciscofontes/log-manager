package br.com.prevent.logmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.prevent.logmanager.domain.Log;

@SpringBootTest
class LogServiceTest {

	@Autowired
	private LogService service;

	@Test
	void testAdicionar() {
		Log log = new Log("123.123.123.123", "GET", "200", "Crome");
		service.adicionar(log);
		assertTrue(log.getId() > 0);
	}

	@Test
	void testEditar() {
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
	void testRemover() {
		Long id = 1l;
		service.remover(id);
	}

}
