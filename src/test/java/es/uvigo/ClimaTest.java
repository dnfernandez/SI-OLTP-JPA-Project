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

public class ClimaTest extends SQLBasedTest {
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
	public void testCreateClima() throws SQLException {
		final Clima cli = new Clima();

		doTransaction(emf, em -> {
			cli.setCondicion_meteorologica("Nublado");
			em.persist(cli);
		});

		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Clima WHERE id = " + cli.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));

	}

	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')",
				Statement.RETURN_GENERATED_KEYS);

		// test code
		Clima cli= emf.createEntityManager().find(Clima.class, id);

		// assert code
		assertEquals("Nublado", cli.getCondicion_meteorologica());
		assertEquals(id, cli.getId());
	}
}