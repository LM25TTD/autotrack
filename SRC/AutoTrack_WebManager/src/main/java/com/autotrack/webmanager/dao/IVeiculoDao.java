package com.autotrack.webmanager.dao;

import java.util.List;

import com.autotrack.webmanager.model.Usuario;
import com.autotrack.webmanager.model.Veiculo;

public interface IVeiculoDao extends IGenericDao {
	public List<Veiculo> obterVeiculosPorUsuario(Usuario usuario);
	public List<Veiculo> obterVeiculosPorUsuarioPlaca(Usuario usuario, String placa);
	public boolean existePlaca(String placa);
	public boolean existeChassi(String chassi);	
}
