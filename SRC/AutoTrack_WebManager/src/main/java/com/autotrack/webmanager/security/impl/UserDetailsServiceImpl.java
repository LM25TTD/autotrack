package com.autotrack.webmanager.security.impl;

import javax.persistence.NoResultException;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.autotrack.webmanager.model.Usuario;


@Component("userDetailsService")
public class UserDetailsServiceImpl  implements UserDetailsService {

//	@Autowired
//	@Qualifier("genericDao")
//	private GenericDao genericDao;
//	
//	public GenericDao getGenericDao() {
//		return genericDao;
//	}
//
//	public void setGenericDao(GenericDao genericDao) {
//		this.genericDao = genericDao;
//	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
		return findByUsername(username);
	}
	
	private User findByUsername(String username) {
	try{
		
		//Usuario usu=(Pessoa)genericDao.getSessionFactory().getCurrentSession().createQuery("from Pessoa p where p.login like '%"+username+"%'").uniqueResult();
		Usuario usu = new Usuario();
		
		return new User("leandro","1234", true, true, true, true, usu.getAuthorities()); 
	  
	}catch (NoResultException e) {
		  throw new UsernameNotFoundException("Usuario nao encontrado");
		}
	}
}
