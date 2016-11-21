package es.uvigo;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Clima {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String condicion_meteorologica;

	public int getId() {
		return id;
	}

	public String getCondicion_meteorologica() {
		return condicion_meteorologica;
	}
	
	public void setCondicion_meteorologica(String condicion_meteorologica) {
		this.condicion_meteorologica = condicion_meteorologica;
	}
}

