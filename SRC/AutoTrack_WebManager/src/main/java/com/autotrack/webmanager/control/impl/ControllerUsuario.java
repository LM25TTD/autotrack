package com.autotrack.webmanager.control.impl;

import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.constants.Messages;
import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.security.impl.AuthenticationService;

@Controller
@Scope("session")
public class ControllerUsuario {

	@Autowired
	private AuthenticationService authenticationService;

	private String userName;
	private String password;
	private String message;

	public boolean validarCampos() {
		boolean retorno = true;

		if (userName == null || userName.equals(Messages.VAZIO)
				|| userName.isEmpty())
			retorno = false;

		if (password == null || password.equals(Messages.VAZIO)
				|| password.isEmpty())
			retorno = false;

		if (!retorno) {
			message = Messages.CAMPOS_OBRIGATORIOS;
		}

		return retorno;
	}

	public String doLogin() {
		if (validarCampos()) {
			try {
				boolean success = authenticationService.login(userName,
						password);
				if (!success) {
					message = Messages.LOGIN_FALHA;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro", message));
					return URL.SEM_NAVEGACAO;
				}
				message = Messages.LOGIN_SUCESSO;

				Collection<GrantedAuthority> userAuthorities = authenticationService
						.getUsuarioLogado().getAuthorities();
				if (userAuthorities.contains(new SimpleGrantedAuthority(
						URL.ROLE_ADMIN))) {
					return URL.ADMIN_PAGINA_PRINCIPAL;
				} else {
					if (userAuthorities.contains(new SimpleGrantedAuthority(
							URL.ROLE_USER))) {
						return URL.USER_PAGINA_PRINCIPAL;
					}
				}
				return URL.SEM_NAVEGACAO;

			} catch (BadCredentialsException e) {
				message = Messages.LOGIN_FALHA;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro", message));
				return URL.SEM_NAVEGACAO;
			}
		} else {
			message = Messages.CAMPOS_OBRIGATORIOS;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro", message));
			return URL.SEM_NAVEGACAO;
		}
	}

	public String logout() {
		authenticationService.logout();
		return URL.LOGIN_PAGE;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
