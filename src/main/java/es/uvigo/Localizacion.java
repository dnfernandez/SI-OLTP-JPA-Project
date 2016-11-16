package es.uvigo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Localizacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String localidad;

	public int getId() {
		return id;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
}
