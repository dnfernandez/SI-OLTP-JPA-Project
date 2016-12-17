package es.uvigo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

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

	@OneToOne
	private Conductor conductor;

	@ManyToMany(mappedBy = "vehiculos")
	private Set<Accidente> accidentes = new HashSet<>();

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return tipo_vehiculo
	 */
	public String getTipo_vehiculo() {
		return tipo_vehiculo;
	}

	/**
	 * @param tipo_vehiculo
	 */
	public void setTipo_vehiculo(String tipo_vehiculo) {
		this.tipo_vehiculo = tipo_vehiculo;
	}

	/**
	 * @return articulado
	 */
	public String getArticulado() {
		return articulado;
	}

	/**
	 * @param articulado
	 */
	public void setArticulado(String articulado) {
		this.articulado = articulado;
	}

	/**
	 * @return anhos
	 */
	public int getAnhos() {
		return anhos;
	}

	/**
	 * @param anhos
	 */
	public void setAnhos(int anhos) {
		this.anhos = anhos;
	}

	/**
	 * @return maniobra
	 */
	public String getManiobra() {
		return maniobra;
	}

	/**
	 * @param maniobra
	 */
	public void setManiobra(String maniobra) {
		this.maniobra = maniobra;
	}

	/**
	 * @return impacto
	 */
	public String getImpacto() {
		return impacto;
	}

	/**
	 * @param impacto
	 */
	public void setImpacto(String impacto) {
		this.impacto = impacto;
	}

	/**
	 * @return volante_izq
	 */
	public boolean getVolante_izq() {
		return volante_izq;
	}

	/**
	 * @param volante_izq
	 */
	public void setVolante_izq(boolean volante_izq) {
		this.volante_izq = volante_izq;
	}

	/**
	 * @return combustible
	 */
	public String getCombustible() {
		return combustible;
	}

	/**
	 * @param combustible
	 */
	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}

	/**
	 * @return Set de accidente
	 */
	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	/**
	 * @return conductor
	 */
	public Conductor getConductor() {
		return conductor;
	}

	/**
	 * @param conductor
	 */
	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	/**
	 * Añade vehiculo al accidente
	 * 
	 * @param a
	 */
	public void addAccidente(Accidente a) {
		a.internalAddVehiculo(this);
		this.accidentes.add(a);
	}

	/**
	 * Añade un accidente al Set de accidente
	 * 
	 * @param accidente
	 */
	public void internalAddAccidente(Accidente accidente) {
		this.accidentes.add(accidente);
	}

	/**
	 * Elimina un accidente del Set de accidente
	 * 
	 * @param accidente
	 */
	public void internalRemoveAccidente(Accidente accidente) {
		this.accidentes.remove(accidente);
	}
}
