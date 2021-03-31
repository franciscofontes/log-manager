package br.com.prevent.logmanager.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.domain.Page;

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
	public Page<Log> listarPorPagina(int pageNumber, int linesPerPage, String orderBy, String direction) {
		Query query = em.createQuery("from logs order by " + orderBy + " " + direction);
		query.setFirstResult((pageNumber - 1) * linesPerPage);
		query.setMaxResults(linesPerPage);
		List<Log> logs = query.getResultList();

		Query queryCount = em.createQuery("select count(log.id) from logs log");
		long countResult = (long) queryCount.getSingleResult();

		int totalElements = Long.valueOf(countResult).intValue();
		int totalPages = totalElements / linesPerPage;	
		boolean first = pageNumber == 1;
		boolean last = pageNumber == totalPages;

		Page<Log> page = new Page<Log>(logs, pageNumber, first, last, linesPerPage, totalPages, totalElements);

		return page;
	}

	@SuppressWarnings("unchecked")
	public List<Log> listarPorFiltro(String data, String ip, String status, String request, String userAgent,
			int pageNumber, int linesPerPage, String orderBy, String direction) {
		String where = "";
		List<String> filtros = new ArrayList<>();
		if (!data.isEmpty()) {
			filtros.add("data='" + data + "'");
		}
		if (!ip.isEmpty()) {
			filtros.add("ip like '%" + ip + "%'");
		}
		if (!status.isEmpty()) {
			filtros.add("status='" + status + "'");
		}
		if (!request.isEmpty()) {
			filtros.add("request like '%" + request + "%'");
		}
		if (!userAgent.isEmpty()) {
			filtros.add("user_agent like '%" + userAgent + "%'");
		}
		for (int i = 0; i < filtros.size(); i++) {
			String item = filtros.get(i);
			where += item;
			if (i < filtros.size() - 1) {
				where += " and ";
			}
		}
		Query query = em.createQuery("from logs where " + where + " order by " + orderBy + " " + direction);
		query.setFirstResult((pageNumber - 1) * linesPerPage);
		query.setMaxResults(linesPerPage);
		List<Log> logs = query.getResultList();
		return logs;
	}

}
