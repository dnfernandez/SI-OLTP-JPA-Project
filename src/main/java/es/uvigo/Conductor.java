package es.uvigo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Conductor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(mappedBy = "conductor")
	private Vehiculo vehiculo;
	
	private String sexo;
	
	private int edad;
	
	private boolean ebrio;
	
	private String rango_edad;
	
	public int getId() {
		return id;
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

	public boolean getEbrio() {
		return ebrio;
	}

	public void setEbrio(boolean ebrio) {
		this.ebrio = ebrio;
	}

	public String getRango_edad() {
		return rango_edad;
	}

	public void setRango_edad(String rango_edad) {
		this.rango_edad = rango_edad;
	}
	
	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}
	
}
