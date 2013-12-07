package com.autotrack.webmanager.model.dao;

import java.util.List;

import com.autotrack.webmanager.model.pojos.Usuario;
import com.autotrack.webmanager.model.pojos.Veiculo;

public interface IVeiculoDao extends IGenericDao {
	public List<Veiculo> obterVeiculosPorUsuario(Usuario usuario);
	public List<Veiculo> obterVeiculosLivresPorUsuario(Usuario usuario);
	public List<Veiculo> obterVeiculosRastreaveisPorUsuario(Usuario usuario);
	public List<Veiculo> obterVeiculosPorUsuarioPlaca(Usuario usuario, String placa);
	public boolean existePlaca(String placa);
	public boolean existeChassi(String chassi);	
}
