package br.com.prevent.logmanager.service;

import java.util.List;

public interface CRUDService<T, ID> {

	void adicionar(T entity);

	void editar(T entity);

	List<T> listar();

	T buscarPorId(ID id);

	void remover(ID id);
}
