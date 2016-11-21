package es.uvigo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
}
