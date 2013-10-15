package com.autotrack.webmanager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.dao.IModuloVeicularDao;
import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.Usuario;

@Repository
public class ModuloVeicularDao extends GenericDao implements IModuloVeicularDao {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ModuloVeicular> obterTodosPeloUsuario(Usuario usuario) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from ModuloVeicular where dono.id=:usuarioId");
		query.setParameter("usuarioId", usuario.getId());
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ModuloVeicular> obterPeloSerialUsuario(String serialNum,
			Usuario usuario) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from ModuloVeicular where numSerial like :serialNum and dono.id=:usuarioId");
		query.setParameter("serialNum", "%"+serialNum+"%");
		query.setParameter("usuarioId", usuario.getId());
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ModuloVeicular obterPeloSerial(String serialNum) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from ModuloVeicular where numSerial like :serialNum");
		query.setParameter("serialNum", serialNum);
		return (ModuloVeicular) query.uniqueResult();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ModuloVeicular> obterTodos() {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from ModuloVeicular");
		return query.list();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ModuloVeicular> obterTodosPeloSerial(String serialNum) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from ModuloVeicular where numSerial like :serialNum");
		query.setParameter("serialNum", "%"+serialNum+"%");
		return query.list();
	}

}
