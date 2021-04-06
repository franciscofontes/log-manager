package br.com.prevent.logmanager.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.domain.LogEstatistica;
import br.com.prevent.logmanager.dto.LogDTO;
import br.com.prevent.logmanager.repository.domain.Page;
import br.com.prevent.logmanager.service.LogService;

@RestController
@RequestMapping(value = "/logs")
public class LogResource implements CrudResource<Log, LogDTO, Long> {

	@Autowired
	private LogService service;

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> adicionar(@Valid @RequestBody Log log) throws MethodArgumentNotValidException {		
		service.adicionar(log);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(log.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editar(@Valid @RequestBody Log log, @PathVariable Long id)
			throws MethodArgumentNotValidException {
		log.setId(id);
		service.editar(log);
		return ResponseEntity.noContent().build();
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<LogDTO> listar() {
		List<Log> logs = service.listar();
		List<LogDTO> dtos = logs.stream().map(log -> new LogDTO(log)).collect(Collectors.toList());
		return dtos;
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Log> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.buscarPorId(id));
	}	
	
	@Override
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<LogDTO>> listarPorPagina(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "lines", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) {
		Page<Log> pageLogs = service.listarPorPagina(page, linesPerPage, orderBy, direction);
		List<LogDTO> dtos = pageLogs.getContent().stream().map(log -> new LogDTO(log)).collect(Collectors.toList());
		Page<LogDTO> pageDtos = new Page<>(dtos, pageLogs.getNumber(), pageLogs.isFirst(), pageLogs.isLast(),
				pageLogs.getSize(), pageLogs.getTotalPages(), pageLogs.getTotalElements());
		return ResponseEntity.ok().body(pageDtos);
	}
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remover(@PathVariable Long id) throws MethodArgumentNotValidException {
		service.remover(id);
		return ResponseEntity.noContent().build();
	}	

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<Page<LogDTO>> listarPorFiltro(
			@RequestParam(value = "de", defaultValue = "") String de,
			@RequestParam(value = "ate", defaultValue = "") String ate,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "status", defaultValue = "") String status,
			@RequestParam(value = "request", defaultValue = "") String request,
			@RequestParam(value = "userAgent", defaultValue = "") String userAgent,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "lines", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) throws MethodArgumentNotValidException {
		Page<Log> pageLogs = service.listarPorFiltro(de, ate, ip, status, request, userAgent, page, linesPerPage, orderBy, direction);
		List<LogDTO> dtos = pageLogs.getContent().stream().map(log -> new LogDTO(log)).collect(Collectors.toList());
		Page<LogDTO> pageDtos = new Page<>(dtos, pageLogs.getNumber(), pageLogs.isFirst(), pageLogs.isLast(), pageLogs.getSize(), pageLogs.getTotalPages(), pageLogs.getTotalElements());
		return ResponseEntity.ok().body(pageDtos);
	}
	
	@RequestMapping(value = "/quant", method = RequestMethod.GET)
	public Integer buscarQuantidadeLogs() {
		return service.buscarQuantidadeLogs();
	}	
	
	@RequestMapping(value = "/quantIps", method = RequestMethod.GET)
	public Integer buscarQuantidadeIpsUnicos() {
		return service.buscarQuantidadeIpsUnicos();
	}
	
	@RequestMapping(value = "/quantUserAgents", method = RequestMethod.GET)
	public Integer buscarQuantidadeUserAgentsUnicos() {
		return service.buscarQuantidadeUserAgentsUnicos();
	}
	
	@RequestMapping(value = "/ips", method = RequestMethod.GET)
	public List<String> listarIpsUnicos() {
		return service.listarIpsUnicos();
	}
	
	@RequestMapping(value = "/userAgents", method = RequestMethod.GET)
	public List<String> listarUserAgentsUnicos() {
		return service.listarUserAgentsUnicos();
	}
	
	@RequestMapping(value = "/estatisticasPorIp", method = RequestMethod.GET)
	public ResponseEntity<List<LogEstatistica>> listarEstatisticasPorIp(
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "lines", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) throws MethodArgumentNotValidException {
		List<LogEstatistica> logs = service.listarEstatisticasPorIp(ip, page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(logs);
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<Void> adicionarPeloArquivo(@RequestParam("file") MultipartFile file) throws MethodArgumentNotValidException {
		service.adicionarLogsPeloArquivo(file, "\\|");
		return ResponseEntity.noContent().build();
	}

}
