package com.autotrack.webmanager.util;

public class Linha<T> {
	
	T elemento;
	boolean selecionado;
	String informacao;
	
	
	public Linha(boolean selecionado, T elemento) {
		this.selecionado = selecionado;
		this.elemento = elemento;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public T getElemento() {
		return elemento;
	}

	public void setElemento(T elemento) {
		this.elemento = elemento;
	}

	public String getInformacao() {
		return informacao;
	}

	public void setInformacao(String informacao) {
		this.informacao = informacao;
	}
	
}
