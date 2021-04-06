package br.com.prevent.logmanager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

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

	@Autowired
	private Environment env;

	@Override
	public void adicionar(Log log) throws MethodArgumentNotValidException {
		try {
			log.setDataCadastro(new Date());
			if (!log.isExtraidoDeArquivo()) {
				log.setData(new Date());
			}
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

	public Page<Log> listarPorFiltro(String de, String ate, String ip, String status, String request, String userAgent,
			int pageNumber, int linesPerPage, String orderBy, String direction) throws MethodArgumentNotValidException {
		try {
			return repository.listarPorFiltro(de, ate, ip, status, request, userAgent, pageNumber, linesPerPage, orderBy,
					direction);
		} catch (ParseException e) {
			throw new ArquivoLogException("Data nao esta no formato correto");
		}
	}
	
	public Integer buscarQuantidadeLogs() {
		return repository.buscarQuantidadeLogs();
	}	
	
	public Integer buscarQuantidadeIpsUnicos() {
		return repository.buscarQuantidadeIpsUnicos();
	}
	
	public Integer buscarQuantidadeUserAgentsUnicos() {
		return repository.buscarQuantidadeUserAgentsUnicos();
	}
	
	public List<String> listarIpsUnicos() {
		return repository.listarIpsUnicos();
	}
	
	public List<String> listarUserAgentsUnicos() {
		return repository.listarUserAgentsUnicos();
	}

	public List<Log> getLogsPeloArquivo(MultipartFile file, String delimitador)
			throws ArquivoLogException, MethodArgumentNotValidException {
		List<Log> logs = new ArrayList<>();
		int nrLinha = 1;
		String dateFormat = LogValidatorUtil.DATE_FORMAT;
		try {
			LogValidatorUtil validator = new LogValidatorUtil();
			String[] linhas = getLinhasArquivo(file, delimitador);
			System.out.println("linhas:" + linhas.length);
			for (String linha : linhas) {
				String[] resultado = linha.split(delimitador);
				String data = resultado[0];
				String ip = resultado[1];
				String request = resultado[2].replace("\"", "");
				String status = resultado[3];
				String userAgent = resultado[4].replace("\"", "");
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
		} catch (ParseException e) {
			throw new ArquivoLogException("Data nao esta no formato: " + dateFormat, nrLinha, "data");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArquivoLogException("Arquivo vazio ou esta faltando algum atributo", nrLinha);
		}
		return logs;
	}

	public String[] getLinhasArquivo(MultipartFile file, String delimitador)
			throws ArquivoLogException, MethodArgumentNotValidException {
		try {
			String conteudo = new BufferedReader(new InputStreamReader(file.getInputStream())).lines()
					.collect(Collectors.joining("\n"));
			String[] linhas = conteudo.trim().split("\n");
			return linhas;
		} catch (IOException e) {
			throw new ArquivoLogException("Ocorreu um erro ao importar o arquivo");
		}
	}

	@Transactional
	public void adicionarLogsPeloArquivo(MultipartFile file, String delimitador)
			throws ArquivoLogException, MethodArgumentNotValidException {
		List<Log> logs = getLogsPeloArquivo(file, delimitador);
		int size = Integer.parseInt(env.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
		for (int i = 0; i < logs.size(); i++) {
			Log log = logs.get(i);
			if (i > 0 && i % size == 0) {
				repository.flush();
				repository.clear();
			}
			log.setNomeArquivo(file.getOriginalFilename());
			adicionar(log);
		}
	}

}
