package br.com.prevent.logmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.LogRepository;

@Service
public class LogService implements CRUDService<Log, Long> {

	@Autowired
	private LogRepository repository;

	@Override
	public void adicionar(Log log) {
		repository.adicionar(log);
	}

	@Override
	public void editar(Log log) {
		repository.editar(log);
	}

	@Override
	public List<Log> listar() {
		return repository.listar();
	}

	@Override
	public Log buscarPorId(Long id) {
		Optional<Log> obj = repository.buscarPorId(id);
		obj.orElseThrow(() -> new RuntimeException("Objeto nao encontrado. ID: " + id + ". Tipo: Log"));
		return obj.get();
	}

	@Override
	public void remover(Long id) {
		repository.remover(id);
	}

}
