package br.com.prevent.logmanager.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.prevent.logmanager.domain.Log;

public class LogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date data;
	private String ip;
	private String request;
	private String status;

	public LogDTO() {
	}

	public LogDTO(Log log) {
		this.id = log.getId();
		this.data = log.getData();
		this.ip = log.getIp();
		this.request = log.getRequest();
		this.status = log.getStatus();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
