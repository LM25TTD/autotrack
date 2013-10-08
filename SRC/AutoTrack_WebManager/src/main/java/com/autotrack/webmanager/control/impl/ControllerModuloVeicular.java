package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.hibernate.hql.internal.ast.tree.SelectExpressionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.dao.IModuloVeicularDao;
import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.Usuario;
import com.autotrack.webmanager.model.Veiculo;
import com.autotrack.webmanager.security.impl.AuthenticationService;
import com.autotrack.webmanager.util.Linha;
import com.autotrack.webmanager.util.Utils;

@Controller
@Scope("session")
public class ControllerModuloVeicular implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7142262601329488049L;

	private String serialPesquisa;

	private List<Linha<ModuloVeicular>> resultadoPesquisa;

	private ModuloVeicular moduloVeicularAtual;
	
	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IModuloVeicularDao moduloVeicularDao;

	@Autowired
	private AuthenticationService authenticationService;
	
	
	public void prepararExibicao(ActionEvent event) {

		moduloVeicularAtual = ((ModuloVeicular) ((UIComponent) event.getComponent()
				.getChildren().get(0)).getAttributes().get("value"));
		moduloVeicularDao.getRefresh(moduloVeicularAtual);
	}
	
	public void associarVeiculo(ActionEvent event) {
		prepararExibicao(event);
	}
	
	public void removerVeiculo(ActionEvent event) {
		prepararExibicao(event);
	}
	
	

	public String pesquisar() {
		Usuario usuario = usuarioDao.obterPeloLogin(authenticationService
				.getUsuarioLogado().getUsername());

		if (serialPesquisa == null || serialPesquisa.isEmpty()) {
			resultadoPesquisa = Utils.mapearParaLinhas(moduloVeicularDao
					.obterTodosPeloUsuario(usuario));
		} else {
			resultadoPesquisa = Utils.mapearParaLinhas(moduloVeicularDao
					.obterPeloSerialUsuario(serialPesquisa, usuario));
		}
		return null;
	}

	public String getSerialPesquisa() {
		return serialPesquisa;
	}

	public void setSerialPesquisa(String serialPesquisa) {
		this.serialPesquisa = serialPesquisa;
	}

	public List<Linha<ModuloVeicular>> getResultadoPesquisa() {
		return resultadoPesquisa;
	}

	public void setResultadoPesquisa(List<Linha<ModuloVeicular>> resultadoPesquisa) {
		this.resultadoPesquisa = resultadoPesquisa;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public ModuloVeicular getModuloVeicularAtual() {
		return moduloVeicularAtual;
	}

	public void setModuloVeicularAtual(ModuloVeicular moduloVeicularAtual) {
		this.moduloVeicularAtual = moduloVeicularAtual;
	}
	
	

}
