package com.autotrack.webmanager.model.dao;

import java.util.List;

import com.autotrack.webmanager.model.pojos.ModuloVeicular;
import com.autotrack.webmanager.model.pojos.Perfil;
import com.autotrack.webmanager.model.pojos.Usuario;

public interface IUsuarioDao extends IGenericDao {
	
	public Usuario obterPeloLogin(String login);
	public List<Usuario> obterTodos();
	public List<Usuario> obterPorCPF(String cpf);
	public List<Perfil> obterTodosPerfis();
	public List<Usuario> obterTodosDesignar();
	public List<Usuario> obterPorCPFDesignar(String cpf);
	public List<ModuloVeicular> obterModulosLivresDesignacao();
	
	

}
