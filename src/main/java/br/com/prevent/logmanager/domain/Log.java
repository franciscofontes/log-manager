package br.com.prevent.logmanager.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

import br.com.prevent.logmanager.service.validation.LogValid;

@Entity(name = "logs")
@LogValid
public class Log implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_id_seq")
	@SequenceGenerator(name = "log_id_seq", sequenceName = "log_id_seq", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(name = "data_cadastro")
	private Date dataCadastro;
	
	@Column(name = "nome_arquivo")
	private String nomeArquivo;	
	
	@Column(nullable = false)
	private Date data;

	@Column(length = 15, nullable = false)
	private String ip;

	@NotBlank
	@Column(length = 50, nullable = false)
	private String request;

	@Column(length = 3, nullable = false)
	private String status;

	@NotBlank
	@Column(name = "user_agent", nullable = false)
	private String userAgent;

	public Log() {
	}

	public Log(String ip, String request, String status, String userAgent) {
		super();
		this.dataCadastro = new Date();
		this.data = new Date();
		this.ip = ip;
		this.request = request;
		this.status = status;
		this.userAgent = userAgent;
	}

	public Log(Date data, String ip, String request, String status, String userAgent) {
		super();
		this.data = data;
		this.ip = ip;
		this.request = request;
		this.status = status;
		this.userAgent = userAgent;
	}
	
	public Log(Long id, Date data, String ip, String request, String status) {
		super();
		this.id = id;
		this.data = data;
		this.ip = ip;
		this.request = request;
		this.status = status;
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

	public Date getData() {
		return data;
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

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Log other = (Log) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", data=" + data + ", ip=" + ip + ", request=" + request + ", status=" + status
				+ ", userAgent=" + userAgent + "]";
	}

}
