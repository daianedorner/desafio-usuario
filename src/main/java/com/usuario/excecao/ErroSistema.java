package com.usuario.excecao;

public class ErroSistema {

	private String mensagem;

	public ErroSistema() {
		super();
	}

	public ErroSistema(String mensagem) {
		super();
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
