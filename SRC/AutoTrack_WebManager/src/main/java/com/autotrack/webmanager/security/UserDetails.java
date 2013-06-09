package com.autotrack.webmanager.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface UserDetails extends Serializable {
	 Collection<GrantedAuthority> getAuthorities();
	  String getPassword();
	  String getUsername();
	  boolean isAccountNonExpired();
	  boolean isAccountNonLocked();
	  boolean isCredentialsNonExpired();
	  boolean isEnabled();
}
