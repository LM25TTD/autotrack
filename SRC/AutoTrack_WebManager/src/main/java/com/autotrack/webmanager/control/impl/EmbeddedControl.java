package com.autotrack.webmanager.control.impl;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.autotrack.webmanager.model.dao.IModuloVeicularDao;
import com.autotrack.webmanager.model.pojos.LogPosicao;
import com.autotrack.webmanager.model.pojos.ModuloVeicular;
import com.autotrack.webmanager.restapi.BlockException;
import com.autotrack.webmanager.restapi.embedded.EmbeddedRequest;

@Controller
public class EmbeddedControl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7436361159861319506L;
	private static final String UNBLOCK = "UNBLOCK";

	@Autowired
	private IModuloVeicularDao moduloVeicularDao;

	public String inserirPosicao(EmbeddedRequest moduleRequest)
			throws Exception {
		ModuloVeicular modulo = moduloVeicularDao.obterPeloSerial(moduleRequest
				.getIdModule());
		LogPosicao logPosicao = new LogPosicao();
		logPosicao.setInstante(new Date());
		logPosicao.setLatitude(moduleRequest.getLatitude());
		logPosicao.setLongitude(moduleRequest.getLongitude());
		logPosicao.setModuloDeOrigem(modulo);
		moduloVeicularDao.save(logPosicao);

		if (modulo.isBloqueado())
			throw new BlockException();
		else
			return UNBLOCK;

	}

}
