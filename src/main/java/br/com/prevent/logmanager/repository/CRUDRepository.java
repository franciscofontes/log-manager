package br.com.prevent.logmanager.repository;

import java.util.List;
import java.util.Optional;

import br.com.prevent.logmanager.repository.domain.Page;

public interface CRUDRepository<T, ID> {

	void adicionar(T entity) throws RuntimeException;

	void editar(T entity) throws RuntimeException;

	List<T> listar();

	Optional<T> buscarPorId(ID id) throws RuntimeException;

	void remover(ID id) throws RuntimeException;
	
	Page<T> listarPorPagina(int pageNumber, int linesPerPage, String orderBy, String direction);
}
