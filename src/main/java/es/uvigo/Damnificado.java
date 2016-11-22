package es.uvigo;

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

	@ManyToMany
	private Set<Accidente> accidentes = new HashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getRango_edad() {
		return rango_edad;
	}

	public void setRango_edad(String rango_edad) {
		this.rango_edad = rango_edad;
	}

	public String getGravedad() {
		return gravedad;
	}

	public void setGravedad(String gravedad) {
		this.gravedad = gravedad;
	}

	public String getPasajero() {
		return pasajero;
	}

	public void setPasajero(String pasajero) {
		this.pasajero = pasajero;
	}

	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	public void addAccidente(Accidente a) {
		a.internalAddDamnificado(this);
		this.accidentes.add(a);
	}

	void internalAddAccidente(Accidente accidente) {
		this.accidentes.add(accidente);
	}

}
