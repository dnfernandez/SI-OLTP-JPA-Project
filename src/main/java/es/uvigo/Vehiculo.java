package es.uvigo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Vehiculo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String tipo_vehiculo;

	private String articulado;

	private int anhos;

	private String maniobra;

	private String impacto;

	private boolean volante_izq;

	private String combustible;

	public int getId() {
		return id;
	}

	public String getTipo_vehiculo() {
		return tipo_vehiculo;
	}

	public void setTipo_vehiculo(String tipo_vehiculo) {
		this.tipo_vehiculo = tipo_vehiculo;
	}

	public String getArticulado() {
		return articulado;
	}

	public void setArticulado(String articulado) {
		this.articulado = articulado;
	}

	public int getAnhos() {
		return anhos;
	}

	public void setAnhos(int anhos) {
		this.anhos = anhos;
	}

	public String getManiobra() {
		return maniobra;
	}

	public void setManiobra(String maniobra) {
		this.maniobra = maniobra;
	}

	public String getImpacto() {
		return impacto;
	}

	public void setImpacto(String impacto) {
		this.impacto = impacto;
	}

	public boolean getVolante_izq() {
		return volante_izq;
	}

	public void setVolante_izq(boolean volante_izq) {
		this.volante_izq = volante_izq;
	}

	public String getCombustible() {
		return combustible;
	}

	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}

}
