package com.autotrack.webmanager.dao;

import com.autotrack.webmanager.model.Usuario;

public interface IUsuarioDao extends IGenericDao {
	
	Usuario obterPeloLogin(String login);

}
