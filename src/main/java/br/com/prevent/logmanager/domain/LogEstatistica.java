package br.com.prevent.logmanager.domain;

import java.io.Serializable;

public class LogEstatistica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer quant;
	private String primeiraData;
	private String ultimaData;
	private String difData;
	private String media;
	private String userAgent;
	
	public LogEstatistica() {
	}
	
	public LogEstatistica(Integer quant, String primeiraData, String ultimaData, String difData, String media, String userAgent) {
		super();
		this.quant = quant;
		this.primeiraData = primeiraData;
		this.ultimaData = ultimaData;
		this.difData = difData;
		this.media = media;
		this.userAgent = userAgent;
	}

	public Integer getQuant() {
		return quant;
	}

	public void setQuant(Integer quant) {
		this.quant = quant;
	}

	public String getPrimeiraData() {
		return primeiraData;
	}

	public void setPrimeiraData(String primeiraData) {
		this.primeiraData = primeiraData;
	}

	public String getUltimaData() {
		return ultimaData;
	}

	public void setUltimaData(String ultimaData) {
		this.ultimaData = ultimaData;
	}

	public String getDifData() {
		return difData;
	}

	public void setDifData(String difData) {
		this.difData = difData;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
