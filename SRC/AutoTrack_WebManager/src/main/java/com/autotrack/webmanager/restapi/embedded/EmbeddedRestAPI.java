package com.autotrack.webmanager.restapi.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.autotrack.webmanager.constants.URL;
import com.autotrack.webmanager.control.impl.EmbeddedControl;
import com.autotrack.webmanager.restapi.BaseRestAPI;

@Controller
public class EmbeddedRestAPI extends BaseRestAPI {

	@Autowired
	private EmbeddedControl embeddedControl;

	@RequestMapping(value = URL.EMBEDDED, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EmbeddedResponse addPosition(@RequestBody EmbeddedRequest request)
			throws Exception {
		String responseFromServer = embeddedControl.inserirPosicao(request);
		return EmbeddedResponse.newFrom(responseFromServer);
	}
}
