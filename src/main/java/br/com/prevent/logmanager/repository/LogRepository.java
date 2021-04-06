package br.com.prevent.logmanager.repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.prevent.logmanager.domain.Log;
import br.com.prevent.logmanager.domain.LogEstatistica;
import br.com.prevent.logmanager.repository.domain.LogFiltro;
import br.com.prevent.logmanager.repository.domain.Page;

@Repository
public class LogRepository extends GenericJpaRepository<Log, Long> {
	
	public LogRepository() {
		super(Log.class, "logs");
	}
	
	public Integer buscarQuantidadeLogs() {
		Query query = em.createQuery("select count(id) from " + table);
		long quant = (long) query.getSingleResult();
		return Long.valueOf(quant).intValue();
	}	
	
	public Integer buscarQuantidadeIpsUnicos() {
		Query query = em.createQuery("select count(distinct(ip)) from " + table);
		long quant = (long) query.getSingleResult();
		return Long.valueOf(quant).intValue();
	}
	
	public Integer buscarQuantidadeUserAgentsUnicos() {
		Query query = em.createQuery("select count(distinct(userAgent)) from " + table);
		long quant = (long) query.getSingleResult();
		return Long.valueOf(quant).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listarIpsUnicos() {
		Query query = em.createQuery("select distinct(ip) from " + table);
		List<String> ips = query.getResultList();
		return ips;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listarUserAgentsUnicos() {
		Query query = em.createQuery("select distinct(userAgent) from " + table);		
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

		String q = "from " + table + " " + where + " order by " + orderBy + " " + direction;
		System.out.println("query:" + q);
		Query query = em.createQuery(q);
		query.setFirstResult((pageNumber) * linesPerPage);
		query.setMaxResults(linesPerPage);
		List<Log> logs = query.getResultList();

		String qCount = "select count(id) from " + table + " " + where;
		System.out.println("count:" + qCount);
		Query queryCount = em.createQuery(qCount);
		long countResult = (long) queryCount.getSingleResult();

		int totalElements = Long.valueOf(countResult).intValue();
		int totalPages = totalElements / linesPerPage;
		boolean first = pageNumber == 0;
		boolean last = pageNumber == totalPages;

		Page<Log> page = new Page<Log>(logs, pageNumber, first, last, linesPerPage, totalPages, totalElements);

		return page;
	}
	
	@SuppressWarnings("unchecked")
	public List<LogEstatistica> listarEstatisticasPorIp(String ip, int pageNumber, int linesPerPage, String orderBy, String direction) {
		String q = "select count(id) as quant, cast(min(data) as varchar) as primeira_data, cast(max(data) as varchar) as ultima_data, cast(max(data) - min(data) as varchar) as dif_data, cast((max(data) - min(data)) / (count(id)) as varchar) as media, user_agent from logs where ip = '192.168.122.135' group by user_agent";
		System.out.println("query:" + q);
		Query query = em.createNativeQuery(q);
		List<Object[]> list = query.getResultList();
		List<LogEstatistica> estatisticas = new ArrayList<>();
		for(Object[] obj : list){			
	        int quant = Integer.parseInt(obj[0].toString());
	        String primeiraData = obj[1].toString();
	        String ultimaData = obj[2].toString();
	        String difData = obj[3].toString();
	        String media = obj[4].toString();
	        String userAgent = obj[5].toString();
	        estatisticas.add(new LogEstatistica(quant, primeiraData, ultimaData, difData, media, userAgent));
		}
		return estatisticas;
	}

	public void flush() {
		em.flush();
	}

	public void clear() {
		em.clear();
	}
}
