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
public class Localizacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String localidad;

	@OneToMany(mappedBy = "localizacion")
	private Set<Accidente> accidentes = new HashSet<>();

	public int getId() {
		return id;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	public void addAccidente(Accidente accidente) {
		accidente.setLocalizacion(this);
	}

	public void removeAccidente(Accidente accidente) {
		accidente.setLocalizacion(null);
	}

	void internalRemoveAccidente(Accidente accidente) {
		this.accidentes.remove(accidente);
	}

	void internalAddAccidente(Accidente accidente) {
		this.accidentes.add(accidente);
	}
}
