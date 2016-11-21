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

	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
		super.renewConnection();
	}

	// C
	@Test
	public void testCreateClima() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("INSERT INTO Accidente(fecha) values('2016-01-01')",
				Statement.RETURN_GENERATED_KEYS);

		final Clima cli = new Clima();
		cli.setCondicion_meteorologica("Nublado");
		doTransaction(emf, em -> {
			em.persist(cli);
			Accidente a = em.find(Accidente.class, id);
			a.setClima(cli);
		});

		cli.getAccidentes();

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Clima WHERE id = " + cli.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	// R
	@Test
	public void testFindClima() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')",
				Statement.RETURN_GENERATED_KEYS);
		int climaId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, clima_id) values('2016-01-01', " + climaId + ")",
				Statement.RETURN_GENERATED_KEYS);
		int accidenteId = getLastInsertedId(statement);

		// test code
		Clima cli = emf.createEntityManager().find(Clima.class, climaId);

		// assert code
		assertEquals("Nublado", cli.getCondicion_meteorologica());
		assertEquals(1, cli.getAccidentes().size());
		assertEquals(accidenteId, cli.getAccidentes().iterator().next().getId());
		assertEquals(cli, cli.getAccidentes().iterator().next().getClima());
	}

	private Clima detachedClima= null;

	@Test(expected = LazyInitializationException.class)
	public void testLazyInitializationException() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')", Statement.RETURN_GENERATED_KEYS);
		int climaId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, clima_id) values('2016-01-01', " + climaId + ")",
				Statement.RETURN_GENERATED_KEYS);
		int accidenteId = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha, clima_id) values('2016-01-01', " + climaId + ")",
				Statement.RETURN_GENERATED_KEYS);

		doTransaction(emf, em -> {
			detachedClima= em.find(Clima.class, climaId);
		});
		assertEquals(1, detachedClima.getAccidentes().size());
		assertEquals(accidenteId, detachedClima.getAccidentes().iterator().next().getId());
		assertEquals(detachedClima, detachedClima.getAccidentes().iterator().next().getId());
	}
}