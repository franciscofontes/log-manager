package br.com.prevent.logmanager.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.com.prevent.logmanager.repository.domain.Page;

public abstract class GenericJpaRepository<T, ID> implements CRUDRepository<T, ID> {

	@PersistenceContext
	private EntityManager em;

	private final Class<T> entityClass;
	private String table;

	public GenericJpaRepository(Class<T> entityClass, String table) {
		this.entityClass = entityClass;
		this.table = table;
	}

	@Transactional
	@Override
	public void adicionar(T t) throws RuntimeException {
		em.persist(t);
	}

	@Transactional
	@Override
	public void editar(T t) throws RuntimeException {
		em.merge(t);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> listar() {
		Query query = em.createQuery("from " + table);
		List<T> list = query.getResultList();
		return list;
	}

	@Override
	public Optional<T> buscarPorId(ID id) throws RuntimeException {
		return Optional.of(em.find(entityClass.getClass(), id));
	}

	@Transactional
	@Override
	public void remover(ID id) throws RuntimeException {
		em.remove(buscarPorId(id).get());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Page<T> listarPorPagina(int pageNumber, int linesPerPage, String orderBy, String direction) {
		Query query = em.createQuery("from " + table + " order by " + orderBy + " " + direction);
		query.setFirstResult((pageNumber - 1) * linesPerPage);
		query.setMaxResults(linesPerPage);
		List<T> list = query.getResultList();

		Query queryCount = em.createQuery("select count(id) from " + table);
		long countResult = (long) queryCount.getSingleResult();

		int totalElements = Long.valueOf(countResult).intValue();
		int totalPages = totalElements / linesPerPage;
		boolean first = pageNumber == 0;
		boolean last = pageNumber == totalPages;

		Page<T> page = new Page<T>(list, pageNumber, first, last, linesPerPage, totalPages, totalElements);

		return page;
	}
}
