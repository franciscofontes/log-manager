package br.com.prevent.logmanager.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.prevent.logmanager.domain.Log;

public class LogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataCadastro;
	private String nomeArquivo;
	private Date data;
	private String ip;
	private String request;
	private String status;

	public LogDTO() {
	}

	public LogDTO(Log log) {
		this.id = log.getId();
		this.dataCadastro = log.getDataCadastro();
		this.nomeArquivo = log.getNomeArquivo();
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
	
	public Date getDataCadastro() {
		return dataCadastro;
	}
	
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public boolean isExtraidoDeArquivo() {
		return getNomeArquivo() != null && !getNomeArquivo().isBlank();
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
