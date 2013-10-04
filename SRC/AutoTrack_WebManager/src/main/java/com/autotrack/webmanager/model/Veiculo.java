package com.autotrack.webmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.autotrack.webmanager.service.Menuable;

@Entity
@Table(name = "tb_Veiculo")
public class Veiculo implements Serializable, Menuable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8116025921337456710L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	private String placa;

	@Column(unique = true)
	private String chassi;

	@Column
	private String marca;

	@Column
	private String modelo;

	@Column
	private String cor;

	@Column
	private int ano;

	@ManyToOne
	private Usuario dono;

	@OneToOne
	private ModuloVeicular moduloAcoplado;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public Usuario getDono() {
		return dono;
	}

	public void setDono(Usuario dono) {
		this.dono = dono;
	}

	public ModuloVeicular getModuloAcoplado() {
		return moduloAcoplado;
	}

	public void setModuloAcoplado(ModuloVeicular moduloAcoplado) {
		this.moduloAcoplado = moduloAcoplado;
	}

	public String getLabel() {
		return marca+" "+modelo+" "+placa;
	}

	public Integer getIdentifier() {
		return new Integer(this.id);
	}

}
