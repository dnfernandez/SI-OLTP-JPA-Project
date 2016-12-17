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

@Entity
public class Damnificado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String sexo;
	private int edad;
	private String rango_edad;
	private String gravedad;
	private String pasajero;

	@ManyToMany(mappedBy = "damnificados")
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
	 * @return sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return edad
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * @param edad
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}

	/**
	 * @return rango_edad
	 */
	public String getRango_edad() {
		return rango_edad;
	}

	/**
	 * @param rango_edad
	 */
	public void setRango_edad(String rango_edad) {
		this.rango_edad = rango_edad;
	}

	/**
	 * @return gravedad
	 */
	public String getGravedad() {
		return gravedad;
	}

	/**
	 * @param gravedad
	 */
	public void setGravedad(String gravedad) {
		this.gravedad = gravedad;
	}

	/**
	 * @return pasajero
	 */
	public String getPasajero() {
		return pasajero;
	}

	/**
	 * @param pasajero
	 */
	public void setPasajero(String pasajero) {
		this.pasajero = pasajero;
	}

	/**
	 * @return Set de accidente
	 */
	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	/**
	 * Añade damnificado al accidente
	 * 
	 * @param a
	 */
	public void addAccidente(Accidente a) {
		a.internalAddDamnificado(this);
		this.accidentes.add(a);
	}

	/**
	 * Añade un accidente al Set de accidente
	 * 
	 * @param accidente
	 */
	void internalAddAccidente(Accidente accidente) {
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
