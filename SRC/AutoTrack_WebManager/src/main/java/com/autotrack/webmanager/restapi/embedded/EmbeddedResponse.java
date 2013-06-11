package com.autotrack.webmanager.restapi.embedded;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EmbeddedResponse extends ArrayList<Map<String, String>> {

	/**
	 * 
	 */
	public EmbeddedResponse() {
		super();
	}
	
	public EmbeddedResponse(int startSize) {
		super(startSize);
	}
	
	private static final long serialVersionUID = -5007177567240287252L;

	public static EmbeddedResponse newFrom(String responseFromServer) {

		EmbeddedResponse response = new EmbeddedResponse(1);

		if (!(responseFromServer == null) && !responseFromServer.isEmpty()) {
			Map<String, String> entry = new LinkedHashMap<String, String>(1);
			entry.put("blockStatus", responseFromServer);
			response.add(entry);
		}
		return response;
	}

}
