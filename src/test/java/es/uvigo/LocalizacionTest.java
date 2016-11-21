package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

	@Test
	public void testCreateLocalizacion() throws SQLException {
		final Localizacion loc = new Localizacion();

		doTransaction(emf, em -> {
			loc.setLocalidad("Orense");
			em.persist(loc);
		});

		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Localizacion WHERE id = " + loc.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));

	}

	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Orense')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		Localizacion loc = emf.createEntityManager().find(Localizacion.class, id);

		// assert code
		assertEquals("Orense", loc.getLocalidad());
		assertEquals(id, loc.getId());
	}

	// U
	@Test
	public void testUpdateLocalizacion() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Vigo')", Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Localizacion e = em.find(Localizacion.class, id);
			e.setLocalidad("Santiago");
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Localizacion WHERE id = " + id);
		rs.next();

		assertEquals("Santiago", rs.getString("localidad"));
		assertEquals(id, rs.getInt("id"));

	}

	// U
	private Localizacion aDetachedLocalizacion = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Vigo')", Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedLocalizacion = em.find(Localizacion.class, id);
		});
		// e is detached, because the entitymanager em is closed (see
		// doTransaction)

		aDetachedLocalizacion.setLocalidad("Santiago");

		doTransaction(emf, em -> {
			em.merge(aDetachedLocalizacion);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Localizacion WHERE id = " + id);
		rs.next();

		assertEquals("Santiago", rs.getString("localidad"));
		assertEquals(id, rs.getInt("id"));
	}

	// D
	@Test
	public void testDeleteLocalizacion() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Vigo')", Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Localizacion e = em.find(Localizacion.class, id);
			em.remove(e);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Localizacion WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	// L
	@Test
	public void testListLocalizacion() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Ávila')", Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Barcelona')", Statement.RETURN_GENERATED_KEYS);

		List<Localizacion> localizaciones = emf.createEntityManager()
				.createQuery("SELECT loc FROM Localizacion loc ORDER BY loc.localidad", Localizacion.class).getResultList();

		// check
		//assertEquals(2, localizaciones.size());
		assertEquals("Ávila", localizaciones.get(0).getLocalidad());
		assertEquals("Barcelona", localizaciones.get(1).getLocalidad());
	}
}
