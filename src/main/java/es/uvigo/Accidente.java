package es.uvigo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Accidente {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private Date fecha;
	
	@OneToMany
	private Localizacion localizacion;
	
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
}
