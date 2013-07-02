package com.autotrack.webmanager.security.impl;

import javax.persistence.NoResultException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.autotrack.webmanager.dao.IGenericDao;
import com.autotrack.webmanager.model.Usuario;


@Component("userDetailsService")
public class UserDetailsServiceImpl  implements UserDetailsService {

	@Autowired
	private IGenericDao genericDao;
	
	public IGenericDao getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(IGenericDao genericDao) {
		this.genericDao = genericDao;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
		return findByUsername(username);
	}
	
	private User findByUsername(String username) {
	try{
		
		Usuario usu=(Usuario)genericDao.getSessionFactory().getCurrentSession().createQuery("from Usuario u where u.login like '%"+username+"%'").uniqueResult();
		
		return new User(usu.getLogin(),usu.getSenha(), true, true, true, true, usu.getAuthorities()); 
	  
	}catch (NoResultException e) {
		  throw new UsernameNotFoundException("Usuario nao encontrado");
		}
	}
}
