package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

	// C
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

	// R
	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		Clima cli = emf.createEntityManager().find(Clima.class, id);

		// assert code
		assertEquals("Nublado", cli.getCondicion_meteorologica());
		assertEquals(id, cli.getId());

	}

	// U
	@Test
	public void testUpdateClima() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Clima cli = em.find(Clima.class, id);
			cli.setCondicion_meteorologica("Soleado");
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Clima WHERE id = " + id);
		rs.next();

		assertEquals("Soleado", rs.getString("condicion_meteorologica"));
		assertEquals(id, rs.getInt("id"));

	}

	// U
	private Clima aDetachedClima = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedClima = em.find(Clima.class, id);
		});
		// e is detached, because the entitymanager em is closed (see
		// doTransaction)

		aDetachedClima.setCondicion_meteorologica("Soleado");

		doTransaction(emf, em -> {
			em.merge(aDetachedClima);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Clima WHERE id = " + id);
		rs.next();

		assertEquals("Soleado", rs.getString("condicion_meteorologica"));
		assertEquals(id, rs.getInt("id"));
	}

	// D
	@Test
	public void testDeleteClima() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('Nublado')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Clima cli = em.find(Clima.class, id);
			em.remove(cli);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Clima WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	// L
	@Test
	public void testListClima() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('aLluvioso')",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate("INSERT INTO Clima(condicion_meteorologica) values('aVentoso')",
				Statement.RETURN_GENERATED_KEYS);

		List<Clima> climas = emf.createEntityManager()
				.createQuery("SELECT cli FROM Clima cli ORDER BY cli.condicion_meteorologica", Clima.class)
				.getResultList();

		// check
		//assertEquals(2, climas.size());
		assertEquals("aLluvioso", climas.get(0).getCondicion_meteorologica());
		assertEquals("aVentoso", climas.get(1).getCondicion_meteorologica());
	}

}