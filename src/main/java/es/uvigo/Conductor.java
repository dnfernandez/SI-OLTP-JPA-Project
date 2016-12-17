package es.uvigo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Conductor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(mappedBy = "conductor")
	private Vehiculo vehiculo;

	private String sexo;
	private int edad;
	private boolean ebrio;
	private String rango_edad;

	/**
	 * @return id
	 */
	public int getId() {
		return id;
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
	 * @return ebrio
	 */
	public boolean getEbrio() {
		return ebrio;
	}

	/**
	 * @param ebrio
	 */
	public void setEbrio(boolean ebrio) {
		this.ebrio = ebrio;
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
	 * @return vehiculo
	 */
	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	/**
	 * @param vehiculo
	 */
	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

}
