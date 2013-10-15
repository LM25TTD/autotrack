package com.autotrack.webmanager.dao;

import java.util.List;

import com.autotrack.webmanager.model.Perfil;
import com.autotrack.webmanager.model.Usuario;

public interface IUsuarioDao extends IGenericDao {
	
	public Usuario obterPeloLogin(String login);
	public List<Usuario> obterTodos();
	public List<Usuario> obterPorCPF(String cpf);
	public List<Perfil> obterTodosPerfis();
	

}
