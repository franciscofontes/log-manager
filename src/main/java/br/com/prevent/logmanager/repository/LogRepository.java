package br.com.prevent.logmanager.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.prevent.logmanager.domain.Log;

@Repository
public class LogRepository implements CRUDRepository<Log, Long> {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	@Override
	public void adicionar(Log log) {
		em.persist(log);
	}

	@Transactional
	@Override
	public void editar(Log log) {
		em.merge(log);
	}

	@Override
	public List<Log> listar() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Log> cq = cb.createQuery(Log.class);
		Root<Log> rootEntry = cq.from(Log.class);
		CriteriaQuery<Log> all = cq.select(rootEntry);
		TypedQuery<Log> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public Optional<Log> buscarPorId(Long id) {
		return Optional.of(em.find(Log.class, id));
	}

	@Transactional
	@Override
	public void remover(Long id) {
		em.remove(buscarPorId(id).get());
	}

}
