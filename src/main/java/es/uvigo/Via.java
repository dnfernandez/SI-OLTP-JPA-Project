package es.uvigo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Via {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	String tipo_via;
	int n_carriles;
	int velocidad_autorizada;
	String estado_via;
	String peligros_calzada;
	boolean urbano;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo_via() {
		return tipo_via;
	}

	public void setTipo_via(String tipo_via) {
		this.tipo_via = tipo_via;
	}

	public int getN_carriles() {
		return n_carriles;
	}

	public void setN_carriles(int n_carriles) {
		this.n_carriles = n_carriles;
	}

	public int getVelocidad_autorizada() {
		return velocidad_autorizada;
	}

	public void setVelocidad_autorizada(int velocidad_autorizada) {
		this.velocidad_autorizada = velocidad_autorizada;
	}

	public String getEstado_via() {
		return estado_via;
	}

	public void setEstado_via(String estado_via) {
		this.estado_via = estado_via;
	}

	public String getPeligros_calzada() {
		return peligros_calzada;
	}

	public void setPeligros_calzada(String peligros_calzada) {
		this.peligros_calzada = peligros_calzada;
	}

	public boolean isUrbano() {
		return urbano;
	}

	public void setUrbano(boolean urbano) {
		this.urbano = urbano;
	}

}
