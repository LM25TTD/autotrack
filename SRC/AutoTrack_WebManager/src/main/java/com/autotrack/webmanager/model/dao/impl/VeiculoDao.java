package com.autotrack.webmanager.model.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.model.dao.IVeiculoDao;
import com.autotrack.webmanager.model.pojos.Usuario;
import com.autotrack.webmanager.model.pojos.Veiculo;

@Repository
public class VeiculoDao extends GenericDao implements IVeiculoDao {

	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Veiculo> obterVeiculosPorUsuario(Usuario usuario) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Veiculo where dono.id=:donoId");
		query.setParameter("donoId", usuario.getId());
		return query.list();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Veiculo> obterVeiculosLivresPorUsuario(Usuario usuario) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Veiculo where dono.id=:donoId and moduloAcoplado=null");
		query.setParameter("donoId", usuario.getId());
		return query.list();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Veiculo> obterVeiculosRastreaveisPorUsuario(Usuario usuario) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Veiculo where dono.id=:donoId and moduloAcoplado<>null");
		query.setParameter("donoId", usuario.getId());
		return query.list();
	}
	
	
	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Veiculo> obterVeiculosPorUsuarioPlaca(Usuario usuario,
			String placa) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Veiculo where dono.id=:donoId and placa like :placa");
		query.setParameter("donoId", usuario.getId());
		query.setParameter("placa", placa);	
		return query.list();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existePlaca(String placa) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Veiculo where placa like :placa");
		query.setParameter("placa", placa);	
		return !query.list().isEmpty();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existeChassi(String chassi) {
		Query query = this
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from Veiculo where chassi like :chassi");
		query.setParameter("chassi", chassi);	
		return !query.list().isEmpty();
	}
	
	

	
}
