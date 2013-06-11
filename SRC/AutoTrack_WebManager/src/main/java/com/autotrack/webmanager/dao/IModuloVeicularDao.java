package com.autotrack.webmanager.dao;

import com.autotrack.webmanager.model.ModuloVeicular;

public interface IModuloVeicularDao extends IGenericDao {
	
	public ModuloVeicular obterPeloSerial (String serialNum);

}
