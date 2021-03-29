package br.com.prevent.logmanager.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.prevent.logmanager.domain.Log;

@Repository
public class LogRepository implements CRUDRepository<Log, Long> {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	@Override
	public void adicionar(Log log) throws RuntimeException {
		em.persist(log);
	}

	@Transactional
	@Override
	public void editar(Log log) throws RuntimeException {
		em.merge(log);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Log> listar() {
		Query query = em.createQuery("from logs");
		List<Log> logs = query.getResultList();
		return logs;
	}

	@Override
	public Optional<Log> buscarPorId(Long id) throws RuntimeException {
		return Optional.of(em.find(Log.class, id));
	}

	@Transactional
	@Override
	public void remover(Long id) throws RuntimeException {
		em.remove(buscarPorId(id).get());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Log> listarPorPagina(int pageNumber, int linesPerPage, String orderBy, String direction) {
		Query query = em.createQuery("from logs order by " + orderBy + " " + direction);
		query.setFirstResult((pageNumber - 1) * linesPerPage);
		query.setMaxResults(linesPerPage);		
		List<Log> logs = query.getResultList();
		return logs;
	}

}
