package br.com.prevent.logmanager.repository;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.repository.domain.LogFiltro;
import br.com.prevent.logmanager.repository.domain.Page;

@Repository
public class LogRepository extends GenericJpaRepository<Log, Long> {

	@PersistenceContext
	private EntityManager em;		
	
	public LogRepository() {
		super(Log.class, "logs");
	}
	
	public Integer buscarQuantidadeLogs() {
		Query query = em.createQuery("select count(log.id) from logs log");
		long quant = (long) query.getSingleResult();
		return Long.valueOf(quant).intValue();
	}	
	
	public Integer buscarQuantidadeIpsUnicos() {
		Query query = em.createQuery("select count(distinct(ip)) from logs");
		long quant = (long) query.getSingleResult();
		return Long.valueOf(quant).intValue();
	}
	
	public Integer buscarQuantidadeUserAgentsUnicos() {
		Query query = em.createQuery("select count(distinct(userAgent)) from logs");
		long quant = (long) query.getSingleResult();
		return Long.valueOf(quant).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listarIpsUnicos() {
		Query query = em.createQuery("select distinct(ip) from logs");		
		List<String> ips = query.getResultList();
		return ips;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listarUserAgentsUnicos() {
		Query query = em.createQuery("select distinct(userAgent) from logs");		
		List<String> userAgents = query.getResultList();
		return userAgents;
	}

	@SuppressWarnings("unchecked")
	public Page<Log> listarPorFiltro(String de, String ate, String ip, String status, String request, String userAgent,
			int pageNumber, int linesPerPage, String orderBy, String direction) throws ParseException {
		
		LogFiltro filtro = new LogFiltro();
		filtro.addWhereDate("data", de, ate);
		filtro.addWhereStringLike("ip", ip);
		filtro.addWhereString("status", status);
		filtro.addWhereStringLike("request", request);
		filtro.addWhereStringLike("user_agent", userAgent);
		String where = filtro.getWhereCompleto();

		String q = "from logs " + where + " order by " + orderBy + " " + direction;
		System.out.println("query:" + q);
		Query query = em.createQuery(q);
		query.setFirstResult((pageNumber - 1) * linesPerPage);
		query.setMaxResults(linesPerPage);
		List<Log> logs = query.getResultList();

		String qCount = "select count(id) from logs " + where;
		System.out.println("count:" + qCount);
		Query queryCount = em.createQuery(qCount);
		long countResult = (long) queryCount.getSingleResult();

		int totalElements = Long.valueOf(countResult).intValue();
		int totalPages = totalElements / linesPerPage;
		boolean first = pageNumber == 1;
		boolean last = pageNumber == totalPages;

		Page<Log> page = new Page<Log>(logs, pageNumber, first, last, linesPerPage, totalPages, totalElements);

		return page;
	}

	public void flush() {
		em.flush();
	}

	public void clear() {
		em.clear();
	}
}
