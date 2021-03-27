package br.com.prevent.logmanager.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
	}

	@Test
	void testListar() {
	}

	@Test
	void testBuscarPorId() {
	}

	@Test
	void testRemover() {
	}

}
