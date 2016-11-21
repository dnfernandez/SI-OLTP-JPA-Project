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

public class ViaTest extends SQLBasedTest {
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
	public void testCreateVia() throws SQLException {
		final Via via = new Via();

		doTransaction(emf, em -> {
			via.setEstado_via("Mojado");
			via.setN_carriles(2);
			via.setPeligros_calzada("Ninguno");
			via.setTipo_via("Nacional");
			via.setUrbano(true);
			via.setVelocidad_autorizada(80);
			em.persist(via);
		});

		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Via" + " WHERE id = " + via.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	@Test
	public void testFindById() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
						Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		Via via = emf.createEntityManager().find(Via.class, id);

		assertEquals("Mojado", via.getEstado_via());
		assertEquals(2, via.getN_carriles());
		assertEquals("Ninguno", via.getPeligros_calzada());
		assertEquals("Nacional", via.getTipo_via());
		assertEquals(true, via.isUrbano());
		assertEquals(80, via.getVelocidad_autorizada());
		assertEquals(id, via.getId());
	}

	// Update
	@Test
	public void testUpdateVia() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
						Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Via via = em.find(Via.class, id);
			via.setEstado_via("Seco");
			via.setN_carriles(5);
			via.setUrbano(false);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Via WHERE id = " + id);
		rs.next();

		assertEquals("Seco", rs.getString("Estado_via"));
		assertEquals(5, rs.getInt("N_carriles"));
		assertEquals(false, rs.getBoolean("Urbano"));
		assertEquals(id, rs.getInt("id"));

	}

	// Update by merge
	private Via aDetachedVia = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
						Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedVia = em.find(Via.class, id);
		});
		// e is detached, because the entitymanager em is closed (see
		// doTransaction)

		aDetachedVia.setEstado_via("Seco");
		aDetachedVia.setN_carriles(5);
		aDetachedVia.setUrbano(false);

		doTransaction(emf, em -> {
			em.merge(aDetachedVia);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Via WHERE id = " + id);
		rs.next();

		assertEquals("Seco", rs.getString("Estado_via"));
		assertEquals(5, rs.getInt("N_carriles"));
		assertEquals(false, rs.getBoolean("Urbano"));
		assertEquals(id, rs.getInt("id"));
	}

	// Delete
	@Test
	public void testDeleteVia() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
						Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Via con = em.find(Via.class, id);
			em.remove(con);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Via WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	// List
	@Test
	public void testListVia() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 6, 'Ninguno', 'Nacional', true, 90)",
						Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement
				.executeUpdate(
						"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
								+ "values ('Mojado', 7, 'Ninguno', 'Comarcal', true, 85)",
						Statement.RETURN_GENERATED_KEYS);

		List<Via> vias = emf.createEntityManager()
				.createQuery("SELECT v FROM Via v ORDER BY v.n_carriles DESC", Via.class)
				.getResultList();

		// check
		//assertEquals(2, vias.size());
		assertEquals(7, vias.get(0).getN_carriles());
		assertEquals(6, vias.get(1).getN_carriles());
	}

}
