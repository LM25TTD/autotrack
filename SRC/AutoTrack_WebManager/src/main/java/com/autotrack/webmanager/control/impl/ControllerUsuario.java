package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.constants.Messages;
import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.security.impl.AuthenticationService;
import com.autotrack.webmanager.util.Crypto;

@Controller
@Scope("session")
public class ControllerUsuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5106445862037517518L;

	@Autowired
	private AuthenticationService authenticationService;

	private String userName;
	private String password;
	private String message;
	private String realName;

	@Autowired
	private IUsuarioDao usuarioDao;

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
						Crypto.textToSHA1(password));
				if (!success) {
					message = Messages.LOGIN_FALHA;
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									Messages.ERRO, message));
					return URL.SEM_NAVEGACAO;
				}
				message = Messages.LOGIN_SUCESSO;
				realName = usuarioDao.obterPeloLogin(
						authenticationService.getUsuarioLogado().getUsername())
						.getNome();
				Collection<GrantedAuthority> userAuthorities = authenticationService
						.getUsuarioLogado().getAuthorities();
				if (userAuthorities.contains(new SimpleGrantedAuthority(
						URL.ROLE_ADMIN))) {
					return URL.ADMIN_PAGINA_PRINCIPAL;
				} else {
					if (userAuthorities.contains(new SimpleGrantedAuthority(
							URL.ROLE_USER))) {
						return URL.USER_PAGINA_RASTREAMENTO;
					}
				}
				return URL.SEM_NAVEGACAO;

			} catch (BadCredentialsException | AuthenticationServiceException e) {
				message = Messages.LOGIN_FALHA;
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, message));
				return URL.SEM_NAVEGACAO;
			}
		} else {
			message = Messages.CAMPOS_OBRIGATORIOS;
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, message));
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public IUsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(IUsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

}
