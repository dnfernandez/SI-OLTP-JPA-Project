package es.uvigo;

import javax.persistence.EntityManager;

public interface Transaction {
	/**
	 * @param em
	 */
	public void run(EntityManager em);
}
