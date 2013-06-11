package com.autotrack.webmanager.restapi.embedded;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddedRequest {

	private String idModule;
	private String codAccess;
	private float latitude;
	private float longitude;
	private int alarm;

	public EmbeddedRequest() {
		super();
	}

	public String getIdModule() {
		return idModule;
	}

	public void setIdModule(String idModule) {
		this.idModule = idModule;
	}

	public String getCodAccess() {
		return codAccess;
	}

	public void setCodAccess(String codAccess) {
		this.codAccess = codAccess;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

}
