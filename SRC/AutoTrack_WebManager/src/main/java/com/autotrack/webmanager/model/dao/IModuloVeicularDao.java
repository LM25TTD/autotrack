package com.autotrack.webmanager.model.dao;

import java.util.List;

import com.autotrack.webmanager.model.pojos.ModuloVeicular;
import com.autotrack.webmanager.model.pojos.Usuario;

public interface IModuloVeicularDao extends IGenericDao {

	public List<ModuloVeicular> obterTodosPeloUsuario(Usuario usuario);
	public List<ModuloVeicular> obterPeloSerialUsuario(String serialNum, Usuario usuario);
	public ModuloVeicular obterPeloSerial(String serialNum);
	public List<ModuloVeicular> obterTodos();
	public List<ModuloVeicular> obterTodosPeloSerial(String serialNum);
	
}
