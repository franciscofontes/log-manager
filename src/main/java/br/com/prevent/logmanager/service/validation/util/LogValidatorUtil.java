package br.com.prevent.logmanager.service.validation.util;

import java.util.regex.Pattern;

public class LogValidatorUtil {

	private Pattern ipPattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
	private Pattern statusPattern = Pattern.compile("1\\d\\d|2\\d\\d|3\\d\\d|4\\d\\d|5\\d\\d");
	public static final String MSG_IP_INVALIDO = "Ip nao esta no formato correto";
	public static final String MSG_STATUS_INVALIDO = "Status nao esta no formato correto";

	public boolean isIpValid(String ip) {
		return ipPattern.matcher(ip).matches();
	}

	public boolean isStatusValid(String status) {
		return statusPattern.matcher(status).matches();
	}

}
