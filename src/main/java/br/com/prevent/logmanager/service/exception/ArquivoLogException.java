package br.com.prevent.logmanager.service.exception;

public class ArquivoLogException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ArquivoLogException(String msg, int nrLinha, String campo) {
		super(msg + ". Linha: " + nrLinha + ". Campo: " + campo);
	}
	
	public ArquivoLogException(String msg, int nrLinha) {
		super(msg + ". Linha: " + nrLinha);
	}	

	public ArquivoLogException(String msg) {
		super(msg);
	}

	public ArquivoLogException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
