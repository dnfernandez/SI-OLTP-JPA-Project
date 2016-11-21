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

public class LocalizacionTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (emf != null && emf.isOpen())
			emf.close();
	}

	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
		super.renewConnection();
	}

	// Create
	@Test
	public void testCreateLocalizacion() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("INSERT INTO Accidente(fecha) values('2016-01-01')",
				Statement.RETURN_GENERATED_KEYS);

		final Localizacion loc = new Localizacion();
		loc.setLocalidad("Orense");
		doTransaction(emf, em -> {
			em.persist(loc);
			Accidente a = em.find(Accidente.class, id);
			a.setLocalizacion(loc);
		});

		loc.getAccidentes();

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Localizacion WHERE id = " + loc.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));

	}

	// Find
	@Test
	public void testFindLocalizacion() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Orense')",
				Statement.RETURN_GENERATED_KEYS);
		int localizacionId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, localizacion_id) values('2016-01-01', " + localizacionId + ")", Statement.RETURN_GENERATED_KEYS);
		int accidenteId = getLastInsertedId(statement);

		// test code
		Localizacion loc = emf.createEntityManager().find(Localizacion.class, localizacionId);

		// assert code
		assertEquals("Orense", loc.getLocalidad());
		assertEquals(1, loc.getAccidentes().size());
		assertEquals(accidenteId, loc.getAccidentes().iterator().next().getId());
		assertEquals(loc, loc.getAccidentes().iterator().next().getLocalizacion());
	}

	private Localizacion detachedLocalizacion = null;

	@Test(expected = LazyInitializationException.class)
	public void testLazyInitializationException() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Orense')", Statement.RETURN_GENERATED_KEYS);
		int localizacionId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, localizacion_id) values('2016-01-01', " + localizacionId + ")",
				Statement.RETURN_GENERATED_KEYS);
		int accidenteId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, localizacion_id) values('2016-01-01', " + localizacionId + ")",
				Statement.RETURN_GENERATED_KEYS);

		doTransaction(emf, em -> {
			detachedLocalizacion = em.find(Localizacion.class, localizacionId);
		});
		assertEquals(1, detachedLocalizacion.getAccidentes().size());
		assertEquals(accidenteId, detachedLocalizacion.getAccidentes().iterator().next().getId());
		assertEquals(detachedLocalizacion, detachedLocalizacion.getAccidentes().iterator().next().getId());
	}
}
