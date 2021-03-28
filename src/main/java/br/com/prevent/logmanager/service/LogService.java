package br.com.prevent.logmanager.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.LogRepository;
import br.com.prevent.logmanager.service.exception.ArquivoLogException;
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

	public List<Log> getLogsPeloArquivo(String url, String delimitador) {
		List<Log> logs = new ArrayList<>();
		int nrLinha = 1;
		String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		try {
			Path path = Paths.get(url);
			for (String linha : Files.readAllLines(path)) {
				String[] resultado = linha.split(delimitador);
				String data = resultado[0];
				String ip = resultado[1];
				String request = resultado[2];
				String status = resultado[3];
				String userAgent = resultado[4];
				logs.add(new Log(new SimpleDateFormat().parse(data), ip, request, status,
						userAgent));
				nrLinha++;
			}
		} catch (IOException e) {
			throw new ArquivoLogException("Ocorreu um erro ao importar o arquivo");
		} catch (ParseException e) {
			throw new ArquivoLogException("Data nao esta no formato: " + dateFormat, nrLinha, "data");
		}
		return logs;
	}

	public void adicionarLogsPeloArquivo(String url, String delimitador) throws MethodArgumentNotValidException {
		List<Log> logs = getLogsPeloArquivo(url, delimitador);
		for (Log log : logs) {
			adicionar(log);
		}
	}

}
