package com.autotrack.webmanager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.Perfil;
import com.autotrack.webmanager.model.Usuario;

@Repository
public class UsuarioDao extends GenericDao implements IUsuarioDao {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Usuario obterPeloLogin(String login) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Usuario u where u.login like :login");
		query.setParameter("login", login);
		return (Usuario)query.uniqueResult();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Usuario> obterTodos() {
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery("from Usuario");
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Usuario> obterPorCPF(String cpf) {
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery("from Usuario where cpf like :cpf");
		query.setParameter("cpf", "%" + cpf + "%");
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Perfil> obterTodosPerfis() {
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery("from Perfil");
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Usuario> obterTodosDesignar() {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"select distinct p.usuario from PerfilUsuario p where p.perfil.nomePerfil like :role");
		query.setParameter("role", "ROLE_USER");
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Usuario> obterPorCPFDesignar(String cpf) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"select distinct p.usuario from PerfilUsuario p where p.perfil.nomePerfil like :role and p.usuario.cpf like :cpf");
		query.setParameter("cpf", "%" + cpf + "%");
		query.setParameter("role", "ROLE_USER");
		return query.list();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ModuloVeicular> obterModulosLivresDesignacao(){
		
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery("from ModuloVeicular where dono = null");
		return query.list();
		
	}

}
