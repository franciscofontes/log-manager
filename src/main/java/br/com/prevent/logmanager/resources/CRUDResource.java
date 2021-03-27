package br.com.prevent.logmanager.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CRUDResource<T, ID> {

	ResponseEntity<Void> adicionar(@RequestBody T entity) throws MethodArgumentNotValidException;

	ResponseEntity<Void> editar(@RequestBody T t, @PathVariable ID id) throws MethodArgumentNotValidException;

	List<T> listar();

	ResponseEntity<T> buscarPorId(@PathVariable ID id);

	ResponseEntity<Void> remover(@PathVariable ID id) throws MethodArgumentNotValidException;
}
