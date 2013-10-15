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

import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.service.Menuable;

@Entity
@Table(name = "tb_Perfil")
public class Perfil implements Serializable, Menuable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5305940341439320663L;
	
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ADMINSITRADOR = "Administrador";
	private static final String USER_COM = "Usuário Comum";
	private static final String PERF_INDEF = "Perfil Indefinido";

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
	
	public String retornaPerfil(Perfil perfil){
		if (perfil.getNomePerfil().equals(ROLE_ADMIN))
			return ADMINSITRADOR;
		if (perfil.getNomePerfil().equalsIgnoreCase(ROLE_USER))
			return USER_COM;
		return PERF_INDEF;
	}

	@Override
	public String getLabel() {
		return this.retornaPerfil(this);
	}

	@Override
	public Integer getIdentifier() {
		return this.idPerfil;
	}
	
}
