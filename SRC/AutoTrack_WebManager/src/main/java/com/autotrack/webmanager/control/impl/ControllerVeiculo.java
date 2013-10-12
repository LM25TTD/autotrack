package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.constants.Messages;
import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.dao.IVeiculoDao;
import com.autotrack.webmanager.model.LogPosicao;
import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.Usuario;
import com.autotrack.webmanager.model.Veiculo;
import com.autotrack.webmanager.security.impl.AuthenticationService;
import com.autotrack.webmanager.util.Linha;
import com.autotrack.webmanager.util.Utils;

@Controller
public class ControllerVeiculo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -389270324531259788L;
	private MapModel mapa;
	private Marker marcador = null;
	private Circle circulo = null;
	private List<SelectItem> veiculosRastreamento;
	private ModuloVeicular moduloAtual;
	private Veiculo veiculoAtual;
	private float last_lat, last_long;

	private List<Linha<Veiculo>> resultadoPesquisa;
	private String placaPesquisa;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IVeiculoDao veiculoDao;

	@Autowired
	private AuthenticationService authenticationService;

	public ControllerVeiculo() {
		mapa = new DefaultMapModel();
	}
	
	public String prepararInclusao() {
		veiculoAtual = new Veiculo();
		return URL.USER_CADASTRO_VEICULOS;
	}

	public String salvar() {

		if (validarDados()) {

			veiculoAtual.setDono(usuarioDao
					.obterPeloLogin(authenticationService.getUsuarioLogado()
							.getUsername()));
			try {
				veiculoDao.saveOrUpdate(veiculoAtual);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								Messages.SUCESSO,
								Messages.SUCESSO_SALVAR_VEICULO));
				pesquisar();
				return URL.USER_FILTRO_VEICULOS;

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								Messages.ERRO, Messages.ERRO_SALVAR_VEICULOS));
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

	public boolean validarDados() {
		boolean dadosValidados = true;

		/*
		 * if(veiculoDao.existePlaca(veiculoAtual.getPlaca())){
		 * FacesContext.getCurrentInstance().addMessage( "formCadastro:placa",
		 * new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.ERRO,
		 * Messages.EXISTE_PLACA)); dadosValidados = false; }
		 * 
		 * if(veiculoDao.existeChassi(veiculoAtual.getChassi())){
		 * FacesContext.getCurrentInstance().addMessage( "formCadastro:chassi",
		 * new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.ERRO,
		 * Messages.EXISTE_CHASSI)); dadosValidados = false; }
		 */

		if ((veiculoAtual.getAno() < 1900)
				|| (veiculoAtual.getAno() > (Calendar.getInstance().get(
						Calendar.YEAR) + 1))) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:ano",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.VALOR_INVALIDO));
			dadosValidados = false;
		}

		if (veiculoAtual.getChassi() == null
				|| veiculoAtual.getChassi().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:chassi",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if (veiculoAtual.getCor() == null || veiculoAtual.getCor().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:cor",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if (veiculoAtual.getMarca() == null
				|| veiculoAtual.getMarca().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:marca",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if (veiculoAtual.getModelo() == null
				|| veiculoAtual.getModelo().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:modelo",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		if (veiculoAtual.getPlaca() == null
				|| veiculoAtual.getPlaca().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(
					"formCadastro:placa",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.CAMPO_OBRIGATORIO));
			dadosValidados = false;
		}

		return dadosValidados;
	}

	public String cancelar() {
		veiculoAtual = null;
		pesquisar();
		return URL.USER_FILTRO_VEICULOS;
	}

	public void prepararEdicao(ActionEvent event) {

		veiculoAtual = ((Veiculo) ((UIComponent) event.getComponent()
				.getChildren().get(0)).getAttributes().get("value"));
		veiculoDao.getRefresh(veiculoAtual);
	}

	public String pesquisar() {
		Usuario usuario = usuarioDao.obterPeloLogin(authenticationService
				.getUsuarioLogado().getUsername());

		if (placaPesquisa == null || placaPesquisa.isEmpty()) {
			resultadoPesquisa = Utils.mapearParaLinhas(veiculoDao
					.obterVeiculosPorUsuario(usuario));
		} else {
			resultadoPesquisa = Utils.mapearParaLinhas(veiculoDao
					.obterVeiculosPorUsuarioPlaca(usuario, placaPesquisa));
		}
		return null;
	}

	public String excluir() {
		try {
			for (Linha<Veiculo> veiculo : resultadoPesquisa) {
				if (veiculo.isSelecionado()) {
					ModuloVeicular moduloDecouple = veiculo.getElemento()
							.getModuloAcoplado();
					moduloDecouple.setVeiculoHospeiro(null);
					veiculoDao.update(moduloDecouple);

					veiculo.getElemento().setModuloAcoplado(null);
					veiculoDao.update(veiculo.getElemento());

					usuarioDao.delete(veiculo.getElemento());

				}
			}
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							Messages.SUCESSO, Messages.SUCESSO_EXCL_VEICULOS));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							Messages.ERRO, Messages.ERRO_EXCL_VEICULOS));
			e.printStackTrace();
		}

		pesquisar();
		return null;
	}

	public void obterPosicaoVeiculo() {

		usuarioDao.getRefresh(moduloAtual);
		int totalPosicoes = moduloAtual.getPosicoes().size();

		if (totalPosicoes > 0) {
			LogPosicao posicaoAtual = moduloAtual.getPosicoes().get(
					totalPosicoes - 1);

			LatLng coord = new LatLng(posicaoAtual.getLatitude(),
					posicaoAtual.getLongitude());
			last_lat = posicaoAtual.getLatitude();
			last_long = posicaoAtual.getLongitude();

			if (circulo == null) {
				circulo = new Circle(coord, 15);
				circulo.setStrokeColor("#00ff00");
				circulo.setFillColor("#00ff00");
				circulo.setStrokeOpacity(0.5);
				circulo.setFillOpacity(0.5);
				mapa.addOverlay(circulo);
			} else {
				circulo.setCenter(coord);
			}

			if (marcador == null) {
				marcador = new Marker(coord, veiculoAtual.getLabel());
				mapa.addOverlay(marcador);
			} else {
				marcador.setLatlng(coord);
			}

		} else {
			moduloAtual = null;
		}
	}

	public void carregarModulo() {
		//mapa = new DefaultMapModel();
		marcador = null;
		circulo = null;
		try {
			if (veiculoAtual != null)
				moduloAtual = veiculoAtual.getModuloAcoplado();
			else
				moduloAtual = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bloquearVeiculo() {
		moduloAtual.setBloqueado(true);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.SUCESSO,
						Messages.SUCESSO_BLOQU_VEICULO));
		try {
			usuarioDao.update(moduloAtual);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.ERRO_BLOQ_VEICULO));
		}

	}

	public void desbloquearVeiculo() {
		moduloAtual.setBloqueado(false);
		try {
			usuarioDao.update(moduloAtual);
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									Messages.SUCESSO,
									Messages.SUCESSO_DESBLOQU_VEICULO));

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							Messages.ERRO, Messages.ERRO_DESBLOQ_VEICULO));
		}

	}

		
	public void carregarListaVeiculos() {
		veiculosRastreamento = new ArrayList<SelectItem>();
		if (authenticationService != null) {
			Usuario usuario = usuarioDao.obterPeloLogin(authenticationService
					.getUsuarioLogado().getUsername());
			usuario.getVeiculosDoUsuario().size();

			List<Veiculo> veiculos = veiculoDao
					.obterVeiculosRastreaveisPorUsuario(usuario);

			for (Veiculo veiculo : veiculos) {
				veiculosRastreamento.add(new SelectItem(veiculo, veiculo
						.getLabel()));
			}
		}
	}

	public MapModel getMapa() {
		return mapa;
	}

	public void setMapa(MapModel mapa) {
		this.mapa = mapa;
	}

	public List<SelectItem> getVeiculosRastreamento() {
		return veiculosRastreamento;
	}

	public void setVeiculosRastreamento(List<SelectItem> veiculosRastreamento) {
		this.veiculosRastreamento = veiculosRastreamento;
	}

	public IUsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(IUsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public ModuloVeicular getModuloAtual() {
		return moduloAtual;
	}

	public void setModuloAtual(ModuloVeicular moduloAtual) {
		this.moduloAtual = moduloAtual;
	}

	public Veiculo getVeiculoAtual() {
		return veiculoAtual;
	}

	public void setVeiculoAtual(Veiculo veiculoAtual) {
		this.veiculoAtual = veiculoAtual;
	}

	public float getLast_lat() {
		return last_lat;
	}

	public void setLast_lat(float last_lat) {
		this.last_lat = last_lat;
	}

	public float getLast_long() {
		return last_long;
	}

	public void setLast_long(float last_long) {
		this.last_long = last_long;
	}

	public List<Linha<Veiculo>> getResultadoPesquisa() {
		return resultadoPesquisa;
	}

	public void setResultadoPesquisa(List<Linha<Veiculo>> resultadoPesquisa) {
		this.resultadoPesquisa = resultadoPesquisa;
	}

	public String getPlacaPesquisa() {
		return placaPesquisa;
	}

	public void setPlacaPesquisa(String placaPesquisa) {
		this.placaPesquisa = placaPesquisa;
	}
}
