package com.autotrack.webmanager.dao;

import java.util.List;

import com.autotrack.webmanager.model.ModuloVeicular;
import com.autotrack.webmanager.model.Usuario;

public interface IModuloVeicularDao extends IGenericDao {

	public List<ModuloVeicular> obterTodosPeloUsuario(Usuario usuario);
	public List<ModuloVeicular> obterPeloSerialUsuario(String serialNum, Usuario usuario);
	public ModuloVeicular obterPeloSerial(String serialNum);

}
