package com.twitter.springboot.web.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tweets")
public class Tweet implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private String usuario;
	private String texto;
	private String localizacion;
	private boolean validacion;

	
	// id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// usuario
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	// texto
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	// localizacion
	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	// validacion
	public boolean isValidacion() {
		return validacion;
	}

	public void setValidacion(boolean validacion) {
		this.validacion = validacion;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
