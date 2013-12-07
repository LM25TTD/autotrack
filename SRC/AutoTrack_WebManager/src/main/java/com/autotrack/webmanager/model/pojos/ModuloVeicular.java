package com.autotrack.webmanager.model.pojos;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.autotrack.webmanager.service.Menuable;

@Entity
@Table(name = "tb_ModuloVeicular")
public class ModuloVeicular implements Serializable, Menuable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4996027488645148366L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true, nullable = false)
	private String numSerial;

	@Column(nullable = false)
	private String codAcesso;

	@Column(unique = true)
	private String numCelular;

	@Column(nullable = false)
	private boolean bloqueado=false;

	@OneToMany(orphanRemoval = true, mappedBy = "moduloDeOrigem", fetch = FetchType.LAZY)
	private List<LogPosicao> posicoes;

	@ManyToOne
	@Cascade(CascadeType.ALL)
	private Usuario dono;

	@OneToOne
	private Veiculo veiculoHospeiro;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumSerial() {
		return numSerial;
	}

	public void setNumSerial(String numSerial) {
		this.numSerial = numSerial;
	}

	public String getCodAcesso() {
		return codAcesso;
	}

	public void setCodAcesso(String codAcesso) {
		this.codAcesso = codAcesso;
	}

	public String getNumCelular() {
		return numCelular;
	}

	public void setNumCelular(String numCelular) {
		this.numCelular = numCelular;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getLabel() {
		return this.numSerial;
	}

	public Integer getIdentifier() {
		return this.id;
	}

	public List<LogPosicao> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<LogPosicao> posicoes) {
		this.posicoes = posicoes;
	}

	public Usuario getDono() {
		return dono;
	}

	public void setDono(Usuario dono) {
		this.dono = dono;
	}

	public Veiculo getVeiculoHospeiro() {
		return veiculoHospeiro;
	}

	public void setVeiculoHospeiro(Veiculo veiculoHospeiro) {
		this.veiculoHospeiro = veiculoHospeiro;
	}
	
	

}
