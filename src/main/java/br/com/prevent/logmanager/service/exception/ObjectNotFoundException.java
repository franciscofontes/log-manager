package br.com.prevent.logmanager.service.exception;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String id, Class<?> tipo) {
		super("Objeto nao encontrado. ID: " + id + ". Tipo: " + tipo.getName());
	}

	public ObjectNotFoundException(String msg) {
		super(msg);
	}

	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
