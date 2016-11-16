package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
		int id = statement.executeUpdate("INSERT INTO Localizacion(localidad) values('Orense')",
				Statement.RETURN_GENERATED_KEYS);

		// test code
		Localizacion loc = emf.createEntityManager().find(Localizacion.class, id);

		// assert code
		assertEquals("Orense", loc.getLocalidad());
		assertEquals(id, loc.getId());
	}
}
