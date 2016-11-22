package es.uvigo;

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

	@ManyToMany(mappedBy = "accidentes")
	private Set<Vehiculo> vehiculos = new HashSet<>();

	@ManyToMany(mappedBy = "accidentes")
	private Set<Damnificado> damnificados = new HashSet<>();

	public int getId() {
		return id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setLocalizacion(Localizacion localizacion) {
		if (this.localizacion != null) {
			this.localizacion.internalRemoveAccidente(this);
		}

		this.localizacion = localizacion;

		if (this.localizacion != null) {
			this.localizacion.internalAddAccidente(this);
		}
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setClima(Clima clima) {
		if (this.clima != null) {
			this.clima.internalRemoveAccidente(this);
		}

		this.clima = clima;

		if (this.clima != null) {
			this.clima.internalAddAccidente(this);
		}

	}

	public Clima getClima() {
		return clima;
	}

	public void setVia(Via Via) {
		if (this.via != null) {
			this.via.internalRemoveAccidente(this);
		}

		this.via = via;

		if (this.via != null) {
			this.via.internalAddAccidente(this);
		}

	}

	public Via getVia() {
		return via;
	}

	public Set<Vehiculo> getVehiculos() {
		return Collections.unmodifiableSet(this.vehiculos);
	}

	public void addVehiculo(Vehiculo v) {
		v.internalAddAccidente(this);
		this.vehiculos.add(v);
	}

	public void internalAddVehiculo(Vehiculo vehiculo) {
		this.vehiculos.add(vehiculo);
	}

	public Set<Damnificado> getDamnificados() {
		return Collections.unmodifiableSet(this.damnificados);
	}

	public void addDamnificado(Damnificado d) {
		d.internalAddAccidente(this);
		this.damnificados.add(d);
	}

	public void internalAddDamnificado(Damnificado damnificado) {
		this.damnificados.add(damnificado);
	}
}
