package es.uvigo;

import javax.persistence.EntityManager;

public interface Transaction {
	public void run (EntityManager em);
}
