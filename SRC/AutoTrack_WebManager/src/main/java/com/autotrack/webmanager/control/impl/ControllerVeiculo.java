package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.dao.IUsuarioDao;
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
	private MapModel simpleModel;
	private List<SelectItem> veiculosRastreamento;
	private ModuloVeicular moduloAtual;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private AuthenticationService authenticationService;

	public ControllerVeiculo() {
		simpleModel = new DefaultMapModel();

		// Shared coordinates
		LatLng coord1 = new LatLng(-3.1046, -59.9845);

		Circle circle2 = new Circle(coord1, 15);
		circle2.setStrokeColor("#00ff00");
		circle2.setFillColor("#00ff00");
		circle2.setStrokeOpacity(0.5);
		circle2.setFillOpacity(0.5);

		// Basic marker
		simpleModel.addOverlay(new Marker(coord1, "Palio - JW#-74$%"));
		simpleModel.addOverlay(circle2);

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

	public MapModel getSimpleModel() {
		return simpleModel;
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

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
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

}
