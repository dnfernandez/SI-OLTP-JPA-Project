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
public class Clima {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String condicion_meteorologica;

	@OneToMany(mappedBy = "clima")
	private Set<Accidente> accidentes = new HashSet<>();

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return condicion_meteorologica
	 */
	public String getCondicion_meteorologica() {
		return condicion_meteorologica;
	}

	/**
	 * @param condicion_meteorologica
	 */
	public void setCondicion_meteorologica(String condicion_meteorologica) {
		this.condicion_meteorologica = condicion_meteorologica;
	}

	/**
	 * @return Set de accidente
	 */
	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	/**
	 * Añade un clima al accidente
	 * 
	 * @param accidentes
	 */
	public void addAccidente(Accidente accidentes) {
		accidentes.setClima(this);
	}

	/**
	 * Borra el clima del accidente
	 * 
	 * @param accidentes
	 */
	public void removeAccidente(Accidente accidentes) {
		accidentes.setClima(null);
	}

	/**
	 * Borra un accidente del Set de accidente
	 * 
	 * @param accidentes
	 */
	void internalRemoveAccidente(Accidente accidentes) {
		this.accidentes.remove(accidentes);
	}

	/**
	 * Añade un accidente al Set de accidente
	 * 
	 * @param accidentes
	 */
	void internalAddAccidente(Accidente accidentes) {
		this.accidentes.add(accidentes);
	}
}
