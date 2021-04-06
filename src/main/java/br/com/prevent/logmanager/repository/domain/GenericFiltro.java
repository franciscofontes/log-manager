package br.com.prevent.logmanager.repository.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericFiltro {

	private List<String> wheres = new ArrayList<>();

	public void addWhereString(String nomeCampo, String valor) {
		if (!valor.isEmpty()) {
			wheres.add(nomeCampo + "=" + valor);
		}
	}

	public void addWhereStringLike(String nomeCampo, String valor) {
		if (!valor.isEmpty()) {
			wheres.add(nomeCampo + " like '%" + valor + "%'");
		}
	}

	public void addWhereDate(String nomeCampo, String de, String ate) {
		if (!de.isEmpty()) {
			if (ate.isEmpty()) {
				ate = LocalDateTime.now().toString();
			}
			wheres.add(nomeCampo + " >= '" + de + "' and " + nomeCampo + " <= '" + ate + "'");
		}
	}

	public String getWhereCompleto() {
		String whereString = "";
		for (int i = 0; i < wheres.size(); i++) {
			if (i == 0) {
				whereString = "where ";
			}
			String item = wheres.get(i);
			whereString += item;
			if (i < wheres.size() - 1) {
				whereString += " and ";
			}
		}
		return whereString;
	}

}
