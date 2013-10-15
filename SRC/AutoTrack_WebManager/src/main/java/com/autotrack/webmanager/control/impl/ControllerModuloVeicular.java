package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.constants.Messages;
import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.dao.IModuloVeicularDao;
import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.dao.IVeiculoDao;
import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.PerfilUsuario;
import com.autotrack.webmanager.model.Usuario;
import com.autotrack.webmanager.model.Veiculo;
import com.autotrack.webmanager.security.impl.AuthenticationService;
import com.autotrack.webmanager.util.Crypto;
import com.autotrack.webmanager.util.Linha;
import com.autotrack.webmanager.util.Utils;
import com.autotrack.webmanager.util.ValidacaoUtil;

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
	private IVeiculoDao veiculoDao;

	@Autowired
	private AuthenticationService authenticationService;

	private List<SelectItem> veiculosAssociacao;
	
	public boolean validarDados() {
		boolean dadosValidados = true;

		if ((moduloVeicularAtual.getNumSerial() == null)
				|| (moduloVeicularAtual.getNumSerial().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:serial",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} 
		
		if ((moduloVeicularAtual.getCodAcesso() == null)
				|| (moduloVeicularAtual.getCodAcesso().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:code",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}
		
		if ((moduloVeicularAtual.getNumCelular() == null)
				|| (moduloVeicularAtual.getNumCelular().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:celular",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {
			if (!ValidacaoUtil.validaTelefone(moduloVeicularAtual.getNumCelular())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:celular",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.CELULAR_INVALIDO));
				dadosValidados = false;
			}
		}

		return dadosValidados;
	}
	
	public String salvar() {

		if (validarDados()) {

			try {

				moduloVeicularDao.saveOrUpdate(moduloVeicularAtual);

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								Messages.SUCESSO,
								Messages.SUCESSO_SALVAR_MODULO));
				pesquisarAdmin();
				return URL.ADMIN_FILTRO_MODULOS;

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								Messages.ERRO, Messages.ERRO_SALVAR_MODULO));
				e.printStackTrace();
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.ERRO_CAMPOS_INVALIDOS));
		}

		return null;
	}

	public String cancelar() {
		moduloVeicularAtual = null;
		pesquisarAdmin();
		return URL.ADMIN_FILTRO_MODULOS;
	}
	
	public String prepararInclusao() {
		moduloVeicularAtual = new ModuloVeicular();
		return URL.ADMIN_CADASTRO_MODULOS;
	}
	
	public void prepararEdicao(ActionEvent event) {

		moduloVeicularAtual = ((ModuloVeicular) ((UIComponent) event.getComponent()
				.getChildren().get(0)).getAttributes().get("value"));
		moduloVeicularDao.getRefresh(moduloVeicularAtual);
	}
	
	public String excluir() {
		try {
			for (Linha<ModuloVeicular> moduloLinha : resultadoPesquisa) {
				if (moduloLinha.isSelecionado()) {
					Veiculo veiculoInstalacao = moduloLinha.getElemento().getVeiculoHospeiro();
					
					veiculoInstalacao.setModuloAcoplado(null);					
					moduloVeicularDao.saveOrUpdate(veiculoInstalacao);
										
					usuarioDao.delete(moduloLinha.getElemento());
				}
			}
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_EXCL_MODULOS));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							Messages.ERRO, Messages.ERRO_EXCL_MODULOS));
			e.printStackTrace();
		}
		pesquisarAdmin();
		return null;
	}

	public void prepararExibicao(ActionEvent event) {

		moduloVeicularAtual = ((ModuloVeicular) ((UIComponent) event
				.getComponent().getChildren().get(0)).getAttributes().get(
				"value"));
		moduloVeicularDao.getRefresh(moduloVeicularAtual);
	}

	public void preparaAssociarVeiculo(ActionEvent event) {
		prepararExibicao(event);
		carregarListaVeiculos();
	}

	public void associarVeiculo(ActionEvent event) {
		try {

			moduloVeicularDao.saveOrUpdate(moduloVeicularAtual);
			moduloVeicularDao.getRefresh(moduloVeicularAtual);
			Veiculo veiculoAssociado = moduloVeicularAtual.getVeiculoHospeiro();
			veiculoDao.getRefresh(veiculoAssociado);
			veiculoAssociado.setModuloAcoplado(moduloVeicularAtual);
			veiculoDao.saveOrUpdate(veiculoAssociado);

			limparModuloVeicularAtual(event);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_OPERACAO));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.ERRO_OPERACAO));
			e.printStackTrace();
		}
	}

	public void carregarListaVeiculos() {
		veiculosAssociacao = new ArrayList<SelectItem>();
		if (authenticationService != null) {
			Usuario usuario = usuarioDao.obterPeloLogin(authenticationService
					.getUsuarioLogado().getUsername());
			usuario.getVeiculosDoUsuario().size();

			List<Veiculo> veiculos = veiculoDao
					.obterVeiculosLivresPorUsuario(usuario);

			for (Veiculo veiculo : veiculos) {
				veiculosAssociacao.add(new SelectItem(veiculo, veiculo
						.getLabel()));
			}
		}
	}

	public void removerVeiculo(ActionEvent event) {
		try {
			prepararExibicao(event);

			Veiculo veiculoDoModulo = moduloVeicularAtual.getVeiculoHospeiro();
			veiculoDao.getRefresh(veiculoDoModulo);
			veiculoDoModulo.setModuloAcoplado(null);
			veiculoDao.saveOrUpdate(veiculoDoModulo);

			moduloVeicularAtual.setVeiculoHospeiro(null);
			moduloVeicularDao.saveOrUpdate(moduloVeicularAtual);
			limparModuloVeicularAtual(event);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_OPERACAO));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.ERRO_OPERACAO));
			e.printStackTrace();
		}
	}

	public void limparModuloVeicularAtual(ActionEvent event) {
		moduloVeicularAtual = null;
		veiculosAssociacao = null;
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
	
	public String pesquisarAdmin(){
		if (serialPesquisa == null || serialPesquisa.isEmpty()) {
			resultadoPesquisa = Utils.mapearParaLinhas(moduloVeicularDao
					.obterTodos());
		} else {
			resultadoPesquisa = Utils.mapearParaLinhas(moduloVeicularDao
					.obterTodosPeloSerial(serialPesquisa));
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

	public void setResultadoPesquisa(
			List<Linha<ModuloVeicular>> resultadoPesquisa) {
		this.resultadoPesquisa = resultadoPesquisa;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public ModuloVeicular getModuloVeicularAtual() {
		return moduloVeicularAtual;
	}

	public void setModuloVeicularAtual(ModuloVeicular moduloVeicularAtual) {
		this.moduloVeicularAtual = moduloVeicularAtual;
	}

	public List<SelectItem> getVeiculosAssociacao() {
		return veiculosAssociacao;
	}

	public void setVeiculosAssociacao(List<SelectItem> veiculosAssociacao) {
		this.veiculosAssociacao = veiculosAssociacao;
	}

}
