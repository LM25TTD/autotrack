package com.autotrack.webmanager.security.impl;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.model.Usuario;


@Component("userDetailsService")
public class UserDetailsServiceImpl  implements UserDetailsService {

	@Autowired
	private IUsuarioDao usuarioDao;
	
	public IUsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(IUsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
		return findByUsername(username);
	}
	
	private User findByUsername(String username) {
	try{
		
		Usuario usu= usuarioDao.obterPeloLogin(username);
		
		return new User(usu.getLogin(),usu.getSenha(), true, true, true, true, usu.getAuthorities()); 
	  
	}catch (NoResultException e) {
		  throw new UsernameNotFoundException("Usuario nao encontrado");
		}
	}
}
