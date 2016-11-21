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

	public int getId() {
		return id;
	}

	public String getCondicion_meteorologica() {
		return condicion_meteorologica;
	}
	
	public void setCondicion_meteorologica(String condicion_meteorologica) {
		this.condicion_meteorologica = condicion_meteorologica;
	}
	
	public Set<Accidente> getAccidentes() {
		return Collections.unmodifiableSet(accidentes);
	}

	public void addAccidente(Accidente accidentes) {
		accidentes.setClima(this);
	}

	public void removeAccidente(Accidente accidentes) {
		accidentes.setClima(null);
	}

	void internalRemoveAccidente(Accidente accidentes) {
		this.accidentes.remove(accidentes);
	}

	void internalAddAccidente(Accidente accidentes) {
		this.accidentes.add(accidentes);
	}
}

