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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConductorTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

	/**
	 * Crear entity manager factory.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}

	/**
	 * Cerrar entity manager factory.
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (emf != null && emf.isOpen())
			emf.close();
	}

	/**
	 * Renovar la conexión.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
		super.renewConnection();
	}

	/**
	 * Inserta un conductor y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testCreateConductor() throws SQLException {
		final Conductor con = new Conductor();

		doTransaction(emf, em -> {
			con.setEbrio(false);
			con.setEdad(25);
			con.setRango_edad("25-30");
			con.setSexo("Hombre");
			em.persist(con);
		});

		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Conductor WHERE id = " + con.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	/**
	 * Realiza una búsqueda de un conductor y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Conductor(ebrio, edad, rango_edad, sexo) values(false,25,'25-30','Hombre')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		Conductor con = emf.createEntityManager().find(Conductor.class, id);

		assertEquals(25, con.getEdad());
		assertEquals(false, con.getEbrio());
		assertEquals("25-30", con.getRango_edad());
		assertEquals("Hombre", con.getSexo());
		assertEquals(id, con.getId());
	}

	/**
	 * Actualiza un conductor almacenado y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testUpdateConductor() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Conductor(ebrio, edad, rango_edad, sexo) values(false,25,'25-30','Hombre')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Conductor con = em.find(Conductor.class, id);
			con.setEbrio(true);
			con.setEdad(28);
			con.setRango_edad("25-30");
			con.setSexo("Mujer");
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Conductor WHERE id = " + id);
		rs.next();

		assertEquals(28, rs.getInt("edad"));
		assertEquals(true, rs.getBoolean("ebrio"));
		assertEquals("25-30", rs.getString("rango_edad"));
		assertEquals("Mujer", rs.getString("sexo"));
		assertEquals(id, rs.getInt("id"));

	}

	/**
	 * Actualiza un conductor almacenado mediante merge y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */

	private Conductor aDetachedConductor = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Conductor(ebrio, edad, rango_edad, sexo) values(false,25,'25-30','Hombre')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedConductor = em.find(Conductor.class, id);
		});

		aDetachedConductor.setEbrio(true);
		aDetachedConductor.setEdad(28);
		aDetachedConductor.setRango_edad("25-30");
		aDetachedConductor.setSexo("Mujer");

		doTransaction(emf, em -> {
			em.merge(aDetachedConductor);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Conductor WHERE id = " + id);
		rs.next();

		assertEquals(28, rs.getInt("edad"));
		assertEquals(true, rs.getBoolean("ebrio"));
		assertEquals("25-30", rs.getString("rango_edad"));
		assertEquals("Mujer", rs.getString("sexo"));
		assertEquals(id, rs.getInt("id"));
	}

	/**
	 * Elimina un conductor y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDeleteConductor() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Conductor(ebrio, edad, rango_edad, sexo) values(false,25,'25-30','Hombre')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Conductor con = em.find(Conductor.class, id);
			em.remove(con);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Conductor WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	/**
	 * Lista varios conductores almacenados y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testListConductor() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Conductor(ebrio, edad, rango_edad, sexo) values(true,50,'45-50','Mujer')",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate("INSERT INTO Conductor(ebrio, edad, rango_edad, sexo) values(true,42,'40-45','Hombre')",
				Statement.RETURN_GENERATED_KEYS);

		List<Conductor> conductores = emf.createEntityManager()
				.createQuery("SELECT con FROM Conductor con ORDER BY con.edad DESC", Conductor.class).getResultList();

		// check
		// assertEquals(2, climas.size());
		assertEquals(50, conductores.get(0).getEdad());
		assertEquals(42, conductores.get(1).getEdad());
	}
}
