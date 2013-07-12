package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.constants.Messages;
import com.autotrack.webmanager.dao.IUsuarioDao;
import com.autotrack.webmanager.model.LogPosicao;
import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.Usuario;
import com.autotrack.webmanager.model.Veiculo;
import com.autotrack.webmanager.security.impl.AuthenticationService;

@Controller
public class ControllerVeiculo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -389270324531259788L;
	private MapModel mapa;
	private Marker marcador=null;
	private Circle circulo=null;
	private List<SelectItem> veiculosRastreamento;
	private ModuloVeicular moduloAtual;
	private Veiculo veiculoAtual;
	private float last_lat, last_long;
	

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private AuthenticationService authenticationService;

	public ControllerVeiculo() {
		mapa = new DefaultMapModel();
	}

	public void obterPosicaoVeiculo() {

		usuarioDao.getRefresh(moduloAtual);		
		int totalPosicoes = moduloAtual.getPosicoes().size();

		if (totalPosicoes > 0) {
			LogPosicao posicaoAtual = moduloAtual.getPosicoes().get(
					totalPosicoes - 1);

			LatLng coord = new LatLng(posicaoAtual.getLatitude(),
					posicaoAtual.getLongitude());
			last_lat =posicaoAtual.getLatitude();
			last_long=posicaoAtual.getLongitude();
			
			if(circulo==null){
				circulo = new Circle(coord, 15);
				circulo.setStrokeColor("#00ff00");
				circulo.setFillColor("#00ff00");
				circulo.setStrokeOpacity(0.5);
				circulo.setFillOpacity(0.5);			
				mapa.addOverlay(circulo);
			}else{
				circulo.setCenter(coord);
			}
			
			if (marcador==null){
				marcador = new Marker(coord, veiculoAtual.getLabel());
				mapa.addOverlay(marcador);
			}else{
				marcador.setLatlng(coord);
			}
				
			
		}else {
			moduloAtual=null;
		}
	}

	public void carregarModulo() {
		mapa = new DefaultMapModel();
		marcador=null;
		circulo=null;
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

			List<Veiculo> veiculos = usuario.getVeiculosDoUsuario();

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
	
	

}
