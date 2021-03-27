package br.com.prevent.logmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.prevent.logmanager.domain.Log;

@SpringBootTest
class LogRepositoryTest {

	@Autowired
	private LogRepository repository;

	@Test
	void testAdicionar() {
		Log log = new Log("123.123.123.123", "GET", "200", "Crome");
		repository.adicionar(log);
		assertTrue(log.getId() > 0);
	}

	@Test
	void testEditar() {
		Log log = new Log("123.123.123.125", "GET", "200", "Crome");
		Long id = 1l;
		log.setId(id);
		repository.editar(log);
		assertEquals(repository.buscarPorId(id).get().getIp(), log.getIp());
	}

	@Test
	void testListar() {
		List<Log> logs = repository.listar();
		assertFalse(logs.isEmpty());
	}

	@Test
	void testBuscarPorId() {
		try {
			Optional<Log> log = repository.buscarPorId(1l);
			assertTrue(log.isPresent());
		} catch (NullPointerException e) {
			fail();
		}
	}

	@Test
	void testRemover() {
		Long id = 3l;
		try {
			repository.remover(id);
		} catch (NullPointerException e) {	
			fail();
		}
	}

}
