package br.com.prevent.logmanager.service;

import java.util.List;

import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.prevent.logmanager.repository.domain.Page;

public interface CrudService<T, ID> {

	void adicionar(T entity) throws MethodArgumentNotValidException;

	void editar(T entity) throws MethodArgumentNotValidException;

	List<T> listar();

	T buscarPorId(ID id);

	void remover(ID id) throws MethodArgumentNotValidException;
	
	Page<T> listarPorPagina(int pageNumber, int linesPerPage, String orderBy, String direction);
}
