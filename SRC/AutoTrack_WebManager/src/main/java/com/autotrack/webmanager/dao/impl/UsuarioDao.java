package com.autotrack.webmanager.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.dao.IUsuarioDao;
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

}
