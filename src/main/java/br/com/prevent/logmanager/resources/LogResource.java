package br.com.prevent.logmanager.resources;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.dto.LogDTO;
import br.com.prevent.logmanager.repository.domain.Page;
import br.com.prevent.logmanager.service.LogService;

@RestController
@RequestMapping(value = "/logs")
public class LogResource implements CRUDResource<Log, LogDTO, Long> {

	@Autowired
	private LogService service;

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> adicionar(@Valid @RequestBody Log log) throws MethodArgumentNotValidException {
		log.setData(new Date());
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
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<LogDTO>> listarPorPagina(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "lines", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Log> pageLogs = service.listarPorPagina(page, linesPerPage, orderBy, direction);
		List<LogDTO> dtos = pageLogs.getContent().stream().map(log -> new LogDTO(log)).collect(Collectors.toList());
		Page<LogDTO> pageDtos = new Page<>(dtos, pageLogs.getNumber(), pageLogs.isFirst(), pageLogs.isLast(), pageLogs.getSize(), pageLogs.getTotalPages(), pageLogs.getTotalElements()); 
		return ResponseEntity.ok().body(pageDtos);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<List<LogDTO>> listarPorFiltro(@RequestParam(value = "data", defaultValue = "") String data,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "status", defaultValue = "") String status,
			@RequestParam(value = "request", defaultValue = "") String request,
			@RequestParam(value = "userAgent", defaultValue = "") String userAgent,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "lines", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		List<Log> logs = service.listarPorFiltro(data, ip, status, request, userAgent, page, linesPerPage, orderBy,
				direction);
		List<LogDTO> dtos = logs.stream().map(log -> new LogDTO(log)).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Log> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.buscarPorId(id));
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remover(@PathVariable Long id) throws MethodArgumentNotValidException {
		service.remover(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/adicionarPeloArquivo", method = RequestMethod.POST)
	public ResponseEntity<Void> adicionarPeloArquivo(@RequestBody String url)
			throws MethodArgumentNotValidException, JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> parsedMap = mapper.readValue(url, new TypeReference<HashMap<String, Object>>() {
		});
		service.adicionarLogsPeloArquivo(parsedMap.get("url").toString(), "\\|");
		return ResponseEntity.noContent().build();
	}

}
