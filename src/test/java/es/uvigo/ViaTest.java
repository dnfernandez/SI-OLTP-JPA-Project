package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ViaTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

	/**
	 * Crear entity manager factory.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}

	/**
	 * Cerrar entity manager factory.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (emf != null && emf.isOpen())
			emf.close();
	}

	/**
	 * Renovar la conexión.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
		super.renewConnection();
	}

	/**
	 * Inserta una vía y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testCreateVia() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("INSERT INTO Accidente(fecha) values('2016-01-01')",
				Statement.RETURN_GENERATED_KEYS);

		final Via via = new Via();
		via.setEstado_via("Mojado");
		via.setN_carriles(2);
		via.setPeligros_calzada("Ninguno");
		via.setTipo_via("Nacional");
		via.setUrbano(true);
		via.setVelocidad_autorizada(80);
		doTransaction(emf, em -> {
			em.persist(via);
			Accidente a = em.find(Accidente.class, id);
			a.setVia(via);
		});

		via.getAccidentes();

		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Via WHERE id = " + via.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	/**
	 * Realiza una búsqueda de una vía y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testFindVia() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
						Statement.RETURN_GENERATED_KEYS);
		int viaId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, via_id) values('2016-01-01', " + viaId + ")",
				Statement.RETURN_GENERATED_KEYS);
		int accidenteId = getLastInsertedId(statement);

		Via via = emf.createEntityManager().find(Via.class, viaId);

		assertEquals("Mojado", via.getEstado_via());
		assertEquals(2, via.getN_carriles());
		assertEquals("Ninguno", via.getPeligros_calzada());
		assertEquals("Nacional", via.getTipo_via());
		assertEquals(true, via.isUrbano());
		assertEquals(80, via.getVelocidad_autorizada());
		assertEquals(1, via.getAccidentes().size());
		assertEquals(accidenteId, via.getAccidentes().iterator().next().getId());
		assertEquals(via, via.getAccidentes().iterator().next().getVia());
	}

	/**
	 * Inserta una vía a varios accidentes y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */
	private Via detachedVia = null;

	@Test(expected = LazyInitializationException.class)
	public void testLazyInitializationException() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
						Statement.RETURN_GENERATED_KEYS);
		int viaId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, via_id) values('2016-01-01', " + viaId + ")",
				Statement.RETURN_GENERATED_KEYS);
		int accidenteId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, via_id) values('2016-01-01', " + viaId + ")",
				Statement.RETURN_GENERATED_KEYS);

		doTransaction(emf, em -> {
			detachedVia = em.find(Via.class, viaId);
		});
		assertEquals(1, detachedVia.getAccidentes().size());
		assertEquals(accidenteId, detachedVia.getAccidentes().iterator().next().getId());
		assertEquals(detachedVia, detachedVia.getAccidentes().iterator().next().getId());
	}

}
