package br.com.prevent.logmanager.service.exception;

public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static final String MSG_ADD = "Nao foi possivel adicionar";
	public static final String MSG_EDITAR = "Nao foi possivel editar";
	public static final String MSG_REMOVER = "Nao foi possivel remover";

	public DataIntegrityException(String msg, String id, Class<?> tipo) {
		super(msg + (id == null ? "" : ". ID: " + id.toString()) + ". Tipo: " + tipo.getName());
	}

	public DataIntegrityException(String msg) {
		super(msg);
	}

	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
