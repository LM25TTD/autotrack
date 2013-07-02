package com.autotrack.webmanager.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_Perfil")
public class Perfil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5305940341439320663L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPerfil;

	@Column(unique = true, nullable = false)
	private String nomePerfil;

	@OneToMany(mappedBy = "perfil", fetch = FetchType.EAGER)
	private List<PerfilUsuario> usuariosComPerfil;

	public int getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getNomePerfil() {
		return nomePerfil;
	}

	public void setNomePerfil(String nomePerfil) {
		this.nomePerfil = nomePerfil;
	}

	public List<PerfilUsuario> getUsuariosComPerfil() {
		return usuariosComPerfil;
	}

	public void setUsuariosComPerfil(List<PerfilUsuario> usuariosComPerfil) {
		this.usuariosComPerfil = usuariosComPerfil;
	}

}
