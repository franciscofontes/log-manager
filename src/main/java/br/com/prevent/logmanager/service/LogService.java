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

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.LogRepository;
import br.com.prevent.logmanager.repository.domain.Page;
import br.com.prevent.logmanager.service.exception.ArquivoLogException;
import br.com.prevent.logmanager.service.exception.DataIntegrityException;
import br.com.prevent.logmanager.service.exception.ObjectNotFoundException;
import br.com.prevent.logmanager.service.validation.util.LogValidatorUtil;

@Service
public class LogService implements CRUDService<Log, Long> {

	@Autowired
	private LogRepository repository;

	@Override
	public void adicionar(Log log) throws MethodArgumentNotValidException {
		try {
			repository.adicionar(log);
		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
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

	@Override
	public Page<Log> listarPorPagina(int pageNumber, int linesPerPage, String orderBy, String direction) {
		return repository.listarPorPagina(pageNumber, linesPerPage, orderBy, direction);
	}

	public Page<Log> listarPorFiltro(String data, String ip, String status, String request, String userAgent,
			int pageNumber, int linesPerPage, String orderBy, String direction) {
		return repository.listarPorFiltro(data, ip, status, request, userAgent, pageNumber, linesPerPage, orderBy,
				direction);
	}

	public List<Log> getLogsPeloArquivo(String url, String delimitador) {
		List<Log> logs = new ArrayList<>();
		int nrLinha = 1;
		String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		LogValidatorUtil validator = new LogValidatorUtil();
		try {
			Path path = Paths.get(url);
			for (String linha : Files.readAllLines(path)) {
				String[] resultado = linha.split(delimitador);
				String data = resultado[0];
				String ip = resultado[1];
				String request = resultado[2];
				String status = resultado[3];
				String userAgent = resultado[4];
				Log log = new Log(new SimpleDateFormat(dateFormat).parse(data), ip, request, status, userAgent);
				if (!validator.isIpValid(log.getIp())) {
					throw new ArquivoLogException(LogValidatorUtil.MSG_IP_INVALIDO, nrLinha, "ip");
				}
				if (!validator.isStatusValid(log.getStatus())) {
					throw new ArquivoLogException(LogValidatorUtil.MSG_STATUS_INVALIDO, nrLinha, "status");
				}
				logs.add(log);
				nrLinha++;
			}
		} catch (IOException e) {
			throw new ArquivoLogException("Ocorreu um erro ao importar o arquivo");
		} catch (ParseException e) {
			throw new ArquivoLogException("Data nao esta no formato: " + dateFormat, nrLinha, "data");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArquivoLogException("Esta faltando algum atributo", nrLinha);
		}
		return logs;
	}

	public void adicionarLogsPeloArquivo(String url, String delimitador)
			throws ArquivoLogException, MethodArgumentNotValidException {
		List<Log> logs = getLogsPeloArquivo(url, delimitador);
		for (Log log : logs) {
			adicionar(log);
		}
	}
	
}
