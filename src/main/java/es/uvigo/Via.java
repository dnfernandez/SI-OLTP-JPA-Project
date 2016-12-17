package es.uvigo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

	@OneToMany(mappedBy = "via")
	private Set<Accidente> accidentes = new HashSet<>();

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return tipo_via
	 */
	public String getTipo_via() {
		return tipo_via;
	}

	/**
	 * @param tipo_via
	 */
	public void setTipo_via(String tipo_via) {
		this.tipo_via = tipo_via;
	}

	/**
	 * @return n_carriles
	 */
	public int getN_carriles() {
		return n_carriles;
	}

	/**
	 * @param n_carriles
	 */
	public void setN_carriles(int n_carriles) {
		this.n_carriles = n_carriles;
	}

	/**
	 * @return velocidad_autorizada
	 */
	public int getVelocidad_autorizada() {
		return velocidad_autorizada;
	}

	/**
	 * @param velocidad_autorizada
	 */
	public void setVelocidad_autorizada(int velocidad_autorizada) {
		this.velocidad_autorizada = velocidad_autorizada;
	}

	/**
	 * @return estado_via
	 */
	public String getEstado_via() {
		return estado_via;
	}

	/**
	 * @param estado_via
	 */
	public void setEstado_via(String estado_via) {
		this.estado_via = estado_via;
	}

	/**
	 * @return peligros_calzada
	 */
	public String getPeligros_calzada() {
		return peligros_calzada;
	}

	/**
	 * @param peligros_calzada
	 */
	public void setPeligros_calzada(String peligros_calzada) {
		this.peligros_calzada = peligros_calzada;
	}

	/**
	 * @return urbano
	 */
	public boolean isUrbano() {
		return urbano;
	}

	/**
	 * @param urbano
	 */
	public void setUrbano(boolean urbano) {
		this.urbano = urbano;
	}

	/**
	 * @return Set de accidente
	 */
	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	/**
	 * Añade via al accidente
	 * 
	 * @param accidente
	 */
	public void addAccidente(Accidente accidente) {
		accidente.setVia(this);
	}

	/**
	 * Elimina via de un accidente
	 * 
	 * @param accidente
	 */
	public void removeAccidente(Accidente accidente) {
		accidente.setVia(null);
	}

	/**
	 * Elimina un accidente del Set de accidente
	 * 
	 * @param accidente
	 */
	void internalRemoveAccidente(Accidente accidente) {
		this.accidentes.remove(accidente);
	}

	/**
	 * Añade un accidente al Set de accidente
	 * 
	 * @param accidente
	 */
	void internalAddAccidente(Accidente accidente) {
		this.accidentes.add(accidente);
	}

}
