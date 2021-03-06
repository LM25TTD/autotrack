package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javassist.bytecode.analysis.Util;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.constants.Messages;
import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.model.dao.IUsuarioDao;
import com.autotrack.webmanager.model.pojos.ModuloVeicular;
import com.autotrack.webmanager.model.pojos.Perfil;
import com.autotrack.webmanager.model.pojos.PerfilUsuario;
import com.autotrack.webmanager.model.pojos.Usuario;
import com.autotrack.webmanager.security.impl.AuthenticationService;
import com.autotrack.webmanager.util.Crypto;
import com.autotrack.webmanager.util.Linha;
import com.autotrack.webmanager.util.Utils;
import com.autotrack.webmanager.util.ValidacaoUtil;

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
	private List<Linha<Usuario>> resultadoPesquisa;
	private List<Linha<Usuario>> resultadoPesquisaDesignacoes;
	private List<Linha<ModuloVeicular>> modulosDesignados;
	private Usuario usuarioAtual;
	private Perfil perfilUsuarioAtual;
	private String cpfPesquisa;
	private List<SelectItem> perfisDisponiveis;
	private String senha, csenha;

	@Autowired
	private IUsuarioDao usuarioDao;

	public void pesquisarModulosDesignados() {
		modulosDesignados = Utils.mapearParaLinhas(usuarioDao
				.obterModulosLivresDesignacao());
	}

	public boolean validarDados() {
		boolean dadosValidados = true;

		if ((usuarioAtual.getCpf() == null)
				|| (usuarioAtual.getCpf().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:cpf",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {

			if (!ValidacaoUtil.validaCPF(usuarioAtual.getCpf())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:cpf",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.CPF_INVALIDO));
				dadosValidados = false;
			}
		}

		if ((usuarioAtual.getNome() == null)
				|| (usuarioAtual.getNome().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:nome",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if ((usuarioAtual.getLogin() == null)
				|| (usuarioAtual.getLogin().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:login",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if ((usuarioAtual.getEmail() == null)
				|| (usuarioAtual.getEmail().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:email",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {
			if (!ValidacaoUtil.validaEmail(usuarioAtual.getEmail())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:email",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.EMAIL_INVALIDO));
				dadosValidados = false;
			}
		}

		if ((usuarioAtual.getNumCelular() == null)
				|| (usuarioAtual.getNumCelular().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:celular",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {
			if (!ValidacaoUtil.validaTelefone(usuarioAtual.getNumCelular())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:celular",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.CELULAR_INVALIDO));
				dadosValidados = false;
			}
		}

		if (!senha.equals(csenha)) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:csenha",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.SENHAS_NEQ));
			dadosValidados = false;
		}

		if (usuarioAtual.getPerfisUsuario() == null) {

			if ((senha == null) || (csenha == null) || (senha.isEmpty())
					|| (csenha.isEmpty())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:csenha",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
				dadosValidados = false;
			} else {
				usuarioAtual.setSenha(Crypto.textToSHA1(senha));
			}
		}

		return dadosValidados;
	}

	public boolean validarMeusDados() {
		boolean dadosValidados = true;

		if ((usuarioAtual.getCpf() == null)
				|| (usuarioAtual.getCpf().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:cpf",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {

			if (!ValidacaoUtil.validaCPF(usuarioAtual.getCpf())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:cpf",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.CPF_INVALIDO));
				dadosValidados = false;
			}
		}

		if ((usuarioAtual.getNome() == null)
				|| (usuarioAtual.getNome().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:nome",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if ((usuarioAtual.getLogin() == null)
				|| (usuarioAtual.getLogin().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:login",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if ((usuarioAtual.getEmail() == null)
				|| (usuarioAtual.getEmail().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:email",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {
			if (!ValidacaoUtil.validaEmail(usuarioAtual.getEmail())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:email",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.EMAIL_INVALIDO));
				dadosValidados = false;
			}
		}

		if ((usuarioAtual.getNumCelular() == null)
				|| (usuarioAtual.getNumCelular().isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:celular",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		} else {
			if (!ValidacaoUtil.validaTelefone(usuarioAtual.getNumCelular())) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:celular",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.CELULAR_INVALIDO));
				dadosValidados = false;
			}
		}

		if (!senha.isEmpty() || !csenha.isEmpty()) {
			if (!senha.equals(csenha)) {
				FacesContext.getCurrentInstance().addMessage(
						"formCadastro:csenha",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								Messages.ERRO, Messages.SENHAS_NEQ));
				dadosValidados = false;
			} else {
				usuarioAtual.setSenha(Crypto.textToSHA1(senha));
			}
		}

		return dadosValidados;
	}

	public void prepararEdicao(ActionEvent event) {

		usuarioAtual = ((Usuario) ((UIComponent) event.getComponent()
				.getChildren().get(0)).getAttributes().get("value"));
		usuarioDao.getRefresh(usuarioAtual);
		perfilUsuarioAtual = usuarioAtual.getPerfisUsuario().get(0).getPerfil();
	}

	public void removerModuloDesignado(ActionEvent event) {

		ModuloVeicular moduloRemovido = ((ModuloVeicular) ((UIComponent) event
				.getComponent().getChildren().get(0)).getAttributes().get(
				"value"));
		usuarioAtual.getModulosDoUsuario().remove(moduloRemovido);
		modulosDesignados.add(new Linha<ModuloVeicular>(false, moduloRemovido));
	}

	public void prepararEdicaoDesignacoes(ActionEvent event) {

		usuarioAtual = ((Usuario) ((UIComponent) event.getComponent()
				.getChildren().get(0)).getAttributes().get("value"));
		usuarioAtual = usuarioDao
				.getObjeto(Usuario.class, usuarioAtual.getId());
		if (usuarioAtual.getModulosDoUsuario() == null) {
			usuarioAtual.setModulosDoUsuario(new ArrayList<ModuloVeicular>());
		}
		pesquisarModulosDesignados();

	}

	public String salvar() {

		if (validarDados()) {

			try {

				if (usuarioAtual.getPerfisUsuario() == null) {
					List<PerfilUsuario> perfis = new ArrayList<PerfilUsuario>(1);
					perfis.add(new PerfilUsuario(perfilUsuarioAtual,
							usuarioAtual));
					usuarioAtual.setPerfisUsuario(perfis);
				} else {
					usuarioAtual.getPerfisUsuario().get(0)
							.setPerfil(perfilUsuarioAtual);
				}

				usuarioDao.saveOrUpdate(usuarioAtual);

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								Messages.SUCESSO,
								Messages.SUCESSO_SALVAR_USUARIO));
				pesquisar();
				return URL.ADMIN_FILTRO_USUARIOS;

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								Messages.ERRO, Messages.ERRO_SALVAR_USUARIO));
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

	public String prepararMeusDados() {

		usuarioAtual = usuarioDao.obterPeloLogin(authenticationService
				.getUsuarioLogado().getUsername());
		usuarioDao.getRefresh(usuarioAtual);

		if (usuarioAtual.getPerfisUsuario().get(0).getPerfil().getNomePerfil()
				.equalsIgnoreCase(URL.ROLE_ADMIN)) {
			return URL.ADMIN_MEUS_DADOS;
		} else {
			return URL.USER_MEUS_DADOS;
		}

	}

	public String salvarMeusDados() {

		if (validarMeusDados()) {

			try {

				usuarioDao.saveOrUpdate(usuarioAtual);

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								Messages.SUCESSO,
								Messages.SUCESSO_SALVAR_USUARIO));
				pesquisar();

				if (usuarioAtual.getPerfisUsuario().get(0).getPerfil()
						.getNomePerfil().equalsIgnoreCase(URL.ROLE_ADMIN)) {
					return URL.ADMIN_FILTRO_USUARIOS;
				} else {
					return URL.USER_PAGINA_RASTREAMENTO;
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								Messages.ERRO, Messages.ERRO_SALVAR_USUARIO));
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

	public String salvarDesignacoes() {

		try {

			List<ModuloVeicular> modulosUsuario = usuarioAtual
					.getModulosDoUsuario();

			for (ModuloVeicular moduloVeicular : modulosUsuario) {
				moduloVeicular.setDono(usuarioAtual);
				usuarioDao.saveOrUpdate(moduloVeicular);
			}

			for (Linha<ModuloVeicular> moduloVeicular : modulosDesignados) {
				moduloVeicular.getElemento().setDono(null);
				usuarioDao.saveOrUpdate(moduloVeicular.getElemento());
			}

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_OPERACAO));
			pesquisarDesignacoes();
			return URL.ADMIN_FILTRO_DESIGNACOES;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							Messages.ERRO, Messages.ERRO_OPERACAO));
			e.printStackTrace();
		}

		return null;
	}

	public String pesquisar() {
		if (cpfPesquisa == null || cpfPesquisa.isEmpty()) {
			resultadoPesquisa = Utils.mapearParaLinhas(usuarioDao.obterTodos());
		} else {
			resultadoPesquisa = Utils.mapearParaLinhas(usuarioDao
					.obterPorCPF(cpfPesquisa));
		}
		return null;
	}

	public String pesquisarDesignacoes() {
		if (cpfPesquisa == null || cpfPesquisa.isEmpty()) {
			resultadoPesquisaDesignacoes = Utils.mapearParaLinhas(usuarioDao
					.obterTodosDesignar());
		} else {
			resultadoPesquisaDesignacoes = Utils.mapearParaLinhas(usuarioDao
					.obterPorCPFDesignar(cpfPesquisa));
		}
		return null;
	}

	public String retornaPerfil(Perfil perfil) {
		if (perfil.getNomePerfil().equals(URL.ROLE_ADMIN))
			return URL.ADMINSITRADOR;
		if (perfil.getNomePerfil().equalsIgnoreCase(URL.ROLE_USER))
			return URL.USER_COM;
		return URL.PERF_INDEF;
	}

	public String prepararInclusao() {
		usuarioAtual = new Usuario();
		return URL.ADMIN_CADASTRO_USUARIOS;
	}

	public String cancelar() {
		usuarioAtual = null;
		pesquisar();
		return URL.ADMIN_FILTRO_USUARIOS;
	}

	public String cancelarMeusDados() {
		String retorno;

		if (usuarioAtual.getPerfisUsuario().get(0).getPerfil().getNomePerfil()
				.equalsIgnoreCase(URL.ROLE_ADMIN)) {
			retorno = URL.ADMIN_FILTRO_USUARIOS;
		} else {
			retorno = URL.USER_PAGINA_RASTREAMENTO;
		}

		usuarioAtual = null;
		return retorno;
	}

	public String cancelarDesignacoes() {
		usuarioAtual = null;
		pesquisarDesignacoes();
		return URL.ADMIN_FILTRO_DESIGNACOES;
	}

	public String excluir() {
		try {
			for (Linha<Usuario> usuario : resultadoPesquisa) {
				if (usuario.isSelecionado()) {
					usuarioDao.delete(usuario.getElemento());
				}
			}
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_EXCL_USUARIOS));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							Messages.ERRO, Messages.ERRO_EXCL_USUARIOS));
			e.printStackTrace();
		}
		pesquisar();
		return null;
	}

	public String incluirModulosDesignados() {
		try {
			List<Linha<ModuloVeicular>> elementosManipulados = new ArrayList<Linha<ModuloVeicular>>();

			for (Linha<ModuloVeicular> modulo : modulosDesignados) {
				if (modulo.isSelecionado()) {
					elementosManipulados.add(modulo);
				}
			}

			modulosDesignados.removeAll(elementosManipulados);
			usuarioAtual.getModulosDoUsuario().addAll(
					Utils.mapearParaElementos(elementosManipulados));

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_OPERACAO));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							Messages.ERRO, Messages.ERRO_OPERACAO));
			e.printStackTrace();
		}
		pesquisar();
		return null;
	}

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

	@PostConstruct
	public void carregarListaPerfis() {
		perfisDisponiveis = new ArrayList<SelectItem>();
		List<Perfil> perfis = usuarioDao.obterTodosPerfis();
		for (Perfil perfilAtual : perfis) {
			perfisDisponiveis.add(new SelectItem(perfilAtual, perfilAtual
					.getLabel()));
		}
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

	public List<Linha<Usuario>> getResultadoPesquisa() {
		return resultadoPesquisa;
	}

	public void setResultadoPesquisa(List<Linha<Usuario>> resultadoPesquisa) {
		this.resultadoPesquisa = resultadoPesquisa;
	}

	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}

	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}

	public String getCpfPesquisa() {
		return cpfPesquisa;
	}

	public void setCpfPesquisa(String cpfPesquisa) {
		this.cpfPesquisa = cpfPesquisa;
	}

	public Perfil getPerfilUsuarioAtual() {
		return perfilUsuarioAtual;
	}

	public void setPerfilUsuarioAtual(Perfil perfilUsuarioAtual) {
		this.perfilUsuarioAtual = perfilUsuarioAtual;
	}

	public List<SelectItem> getPerfisDisponiveis() {
		return perfisDisponiveis;
	}

	public void setPerfisDisponiveis(List<SelectItem> perfisDisponiveis) {
		this.perfisDisponiveis = perfisDisponiveis;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCsenha() {
		return csenha;
	}

	public void setCsenha(String csenha) {
		this.csenha = csenha;
	}

	public List<Linha<Usuario>> getResultadoPesquisaDesignacoes() {
		return resultadoPesquisaDesignacoes;
	}

	public void setResultadoPesquisaDesignacoes(
			List<Linha<Usuario>> resultadoPesquisaDesignacoes) {
		this.resultadoPesquisaDesignacoes = resultadoPesquisaDesignacoes;
	}

	public List<Linha<ModuloVeicular>> getModulosDesignados() {
		return modulosDesignados;
	}

	public void setModulosDesignados(
			List<Linha<ModuloVeicular>> modulosDesignados) {
		this.modulosDesignados = modulosDesignados;
	}

}
