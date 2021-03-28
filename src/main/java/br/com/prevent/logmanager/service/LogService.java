package br.com.prevent.logmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.LogRepository;
import br.com.prevent.logmanager.service.exception.DataIntegrityException;
import br.com.prevent.logmanager.service.exception.ObjectNotFoundException;

@Service
public class LogService implements CRUDService<Log, Long> {

	@Autowired
	private LogRepository repository;

	@Override
	public void adicionar(Log log) throws MethodArgumentNotValidException {
		try {
			repository.adicionar(log);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(DataIntegrityException.MSG_ADD, null, Log.class);
		}
	}

	@Override
	public void editar(Log log) throws MethodArgumentNotValidException {
		buscarPorId(log.getId());
		try {
			repository.editar(log);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(DataIntegrityException.MSG_EDITAR, log.getId().toString(), Log.class);
		}
	}

	@Override
	public List<Log> listar() {
		return repository.listar();
	}

	@Override
	public Log buscarPorId(Long id) {
		try {
			Optional<Log> obj = repository.buscarPorId(id);
			return obj.get();
		} catch (RuntimeException e) {
			throw new ObjectNotFoundException(id.toString(), Log.class);
		}
	}

	@Override
	public void remover(Long id) throws MethodArgumentNotValidException {
		buscarPorId(id);
		try {
			repository.remover(id);
		} catch (DataIntegrityException e) {
			throw new DataIntegrityException(DataIntegrityException.MSG_REMOVER, id.toString(), Log.class);
		}
	}

}
