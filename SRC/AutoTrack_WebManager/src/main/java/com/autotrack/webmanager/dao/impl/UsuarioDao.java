package com.autotrack.webmanager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.model.Perfil;
import com.autotrack.webmanager.model.Usuario;

@Repository
public class UsuarioDao extends GenericDao implements IUsuarioDao {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Usuario obterPeloLogin(String login) {
		Usuario usuario = (Usuario) this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Usuario u where u.login like '%" + login + "%'")
				.uniqueResult();
		return usuario;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Usuario> obterTodos() {
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery("from Usuario");
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Usuario> obterPorCPF(String cpf) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Usuario where cpf like :cpf");
		query.setParameter("cpf", "%"+cpf+"%");
		return query.list();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Perfil> obterTodosPerfis() {
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery("from Perfil");
		return query.list();
	}
	
	

}
