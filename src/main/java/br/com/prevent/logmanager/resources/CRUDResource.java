package br.com.prevent.logmanager.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.prevent.logmanager.repository.domain.Page;

public interface CrudResource<T, DTO, ID> {

	ResponseEntity<Void> adicionar(@Valid @RequestBody T t) throws MethodArgumentNotValidException;

	ResponseEntity<Void> editar(@Valid @RequestBody T t, @PathVariable ID id) throws MethodArgumentNotValidException;

	List<DTO> listar();
	
	public ResponseEntity<Page<DTO>> listarPorPagina(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "lines", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction);

	ResponseEntity<T> buscarPorId(@PathVariable ID id);

	ResponseEntity<Void> remover(@PathVariable ID id) throws MethodArgumentNotValidException;	
	
}
