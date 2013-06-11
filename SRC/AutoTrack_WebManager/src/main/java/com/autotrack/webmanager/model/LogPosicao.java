package com.autotrack.webmanager.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_LogPosicao")
public class LogPosicao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729898518120433025L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private float latitude;

	@Column
	private float longitude;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date instante;

	@ManyToOne
	private ModuloVeicular moduloDeOrigem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public ModuloVeicular getModuloDeOrigem() {
		return moduloDeOrigem;
	}

	public void setModuloDeOrigem(ModuloVeicular moduloDeOrigem) {
		this.moduloDeOrigem = moduloDeOrigem;
	}

	public Date getInstante() {
		return instante;
	}

	public void setInstante(Date instante) {
		this.instante = instante;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
