package com.autotrack.webmanager.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.dao.IModuloVeicularDao;
import com.autotrack.webmanager.model.ModuloVeicular;

@Repository
public class ModuloVeicularDao extends GenericDao implements IModuloVeicularDao {

	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public ModuloVeicular obterPeloSerial(String serialNum) {
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery("from ModuloVeicular where numSerial like :serialNum");
		query.setParameter("serialNum", serialNum);
		return (ModuloVeicular) query.uniqueResult();
	}

}
