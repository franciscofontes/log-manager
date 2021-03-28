package br.com.prevent.logmanager.resources;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.service.LogService;

@RestController
@RequestMapping(value = "/logs")
public class LogResource implements CRUDResource<Log, Long> {

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
	public ResponseEntity<Void> editar(@Valid @RequestBody Log log, @PathVariable Long id) throws MethodArgumentNotValidException {
		log.setId(id);
		service.editar(log);
		return ResponseEntity.noContent().build();
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<Log> listar() {
		return service.listar();
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

}
