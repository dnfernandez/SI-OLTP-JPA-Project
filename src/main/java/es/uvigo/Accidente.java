package es.uvigo;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Accidente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Date fecha;

	@ManyToOne
	private Localizacion localizacion;

	@ManyToOne
	private Clima clima;

	@ManyToOne
	private Via via;

	@ManyToMany
	private Set<Vehiculo> vehiculos = new HashSet<>();

	@ManyToMany
	private Set<Damnificado> damnificados = new HashSet<>();

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Guarda la localización. Si existe la localización, se elimina el
	 * accidente y lo añade tras modificarlo.
	 * 
	 * @param localizacion
	 */
	public void setLocalizacion(Localizacion localizacion) {
		if (this.localizacion != null) {
			this.localizacion.internalRemoveAccidente(this);
		}

		this.localizacion = localizacion;

		if (this.localizacion != null) {
			this.localizacion.internalAddAccidente(this);
		}
	}

	/**
	 * @return localizacion
	 */
	public Localizacion getLocalizacion() {
		return localizacion;
	}

	/**
	 * Guarda el clima. Si existe el clima, se elimina el accidente y lo añade
	 * tras modificarlo.
	 * 
	 * @param clima
	 */
	public void setClima(Clima clima) {
		if (this.clima != null) {
			this.clima.internalRemoveAccidente(this);
		}

		this.clima = clima;

		if (this.clima != null) {
			this.clima.internalAddAccidente(this);
		}

	}

	/**
	 * @return clima
	 */
	public Clima getClima() {
		return clima;
	}

	/**
	 * Guarda la vía. Si existe la vía, se elimina el accidente y lo añade tras
	 * modificarlo.
	 * 
	 * @param via
	 */
	public void setVia(Via via) {
		if (this.via != null) {
			this.via.internalRemoveAccidente(this);
		}

		this.via = via;

		if (this.via != null) {
			this.via.internalAddAccidente(this);
		}

	}

	/**
	 * @return via
	 */
	public Via getVia() {
		return via;
	}

	/**
	 * @return Set de Vehiculo
	 */
	public Set<Vehiculo> getVehiculos() {
		return Collections.unmodifiableSet(this.vehiculos);
	}

	/**
	 * Crea una copia del Set de vehiculo que recorre para eliminar los borrados
	 * e insertar los nuevos.
	 * 
	 * @param vehiculos
	 */
	public void setVehiculos(Collection<Vehiculo> vehiculos) {

		Set<Vehiculo> myVehiculosCopy = new HashSet<>(this.vehiculos);
		for (Vehiculo veh : myVehiculosCopy) {
			if (!vehiculos.contains(veh)) {
				this.removeVehiculo(veh);
			}
		}

		for (Vehiculo vehiculo : vehiculos) {
			this.addVehiculo(vehiculo);
		}

	}

	/**
	 * Añade el vehículo a este accidente.
	 * 
	 * @param v
	 */
	public void addVehiculo(Vehiculo v) {
		v.internalAddAccidente(this);
		this.vehiculos.add(v);
	}

	/**
	 * Elimina el vehículo de este accidente.
	 * 
	 * @param v
	 */
	public void removeVehiculo(Vehiculo v) {
		v.internalRemoveAccidente(this);
		this.vehiculos.remove(v);
	}

	/**
	 * Añade un vehículo.
	 * 
	 * @param vehiculo
	 */
	public void internalAddVehiculo(Vehiculo vehiculo) {
		this.vehiculos.add(vehiculo);
	}

	/**
	 * @return Set Damnificado
	 */
	public Set<Damnificado> getDamnificados() {
		return Collections.unmodifiableSet(this.damnificados);
	}

	/**
	 * Crea una copia del Set de damnificado que recorre para eliminar los
	 * borrados e insertar los nuevos.
	 * 
	 * @param damnificados
	 */
	public void setDamnificados(Collection<Damnificado> damnificados) {

		Set<Damnificado> myDamnificadosCopy = new HashSet<>(this.damnificados);
		for (Damnificado dam : myDamnificadosCopy) {
			if (!damnificados.contains(dam)) {
				this.removeDamnificado(dam);
			}
		}

		for (Damnificado damnificado : damnificados) {
			this.addDamnificado(damnificado);
		}

	}

	/**
	 * Añade el damnificado a este accidente.
	 * 
	 * @param d
	 */
	public void addDamnificado(Damnificado d) {
		d.internalAddAccidente(this);
		this.damnificados.add(d);
	}

	/**
	 * Elimina el vehículo de este accidente.
	 * 
	 * @param d
	 */
	public void removeDamnificado(Damnificado d) {
		d.internalRemoveAccidente(this);
		this.damnificados.remove(d);
	}

	/**
	 * Añade un damnificado.
	 * 
	 * @param damnificado
	 */
	public void internalAddDamnificado(Damnificado damnificado) {
		this.damnificados.add(damnificado);
	}
}
