package br.com.prevent.logmanager.repository;

import java.util.List;
import java.util.Optional;

public interface CRUDRepository<T, ID> {

	void adicionar(T entity);

	void editar(T entity);

	List<T> listar();

	Optional<T> buscarPorId(ID id);

	void remover(ID id);
}
