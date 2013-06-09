package com.autotrack.webmanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;


@SuppressWarnings("deprecation")
@Entity
@Table(name = "tb_Usuario")
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3124355697409469382L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	private String cpf;

	@Column
	private String nome;

	@Column(unique = true)
	private String email;

	@Column
	private String numCelular;

	@Column(unique = true)
	private String login;

	@Column
	private String senha;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
	private List<PerfilUsuario> perfisUsuario;

	@OneToMany(mappedBy = "dono", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DETACH })
	private List<ModuloVeicular> modulosDoUsuario;

	@OneToMany(orphanRemoval = true, mappedBy = "dono", fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private List<Veiculo> veiculosDoUsuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumCelular() {
		return numCelular;
	}

	public void setNumCelular(String numCelular) {
		this.numCelular = numCelular;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<PerfilUsuario> getPerfisUsuario() {
		return perfisUsuario;
	}

	public void setPerfisUsuario(List<PerfilUsuario> perfisUsuario) {
		this.perfisUsuario = perfisUsuario;
	}
	
	@Transient
	 public Collection<GrantedAuthority> getAuthorities() {
	    List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
	    for (PerfilUsuario perfil : perfisUsuario) {
	      result.add(new GrantedAuthorityImpl(perfil.getPerfil().getNomePerfil()));
	    }
	    return result;
	  }
	  
	  @Transient
	  public boolean isEnabled() {
	    return true;
	  }

	  @Transient
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Transient
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Transient
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

}
