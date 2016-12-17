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

public class DamnificadoTest extends SQLBasedTest {
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
	 * Inserta un damnificado y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testCreateDamnificado() throws SQLException {
		final Damnificado dam = new Damnificado();

		doTransaction(emf, em -> {
			dam.setEdad(24);
			dam.setGravedad("Muerto");
			dam.setPasajero("Delantero-derecha");
			dam.setRango_edad("20-25");
			dam.setSexo("Mujer");
			em.persist(dam);
		});

		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement
				.executeQuery("SELECT COUNT(*) as total FROM Damnificado" + " WHERE id = " + dam.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	/**
	 * Realiza una búsqueda de un damnificado y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testFindById() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (24, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);

		Damnificado dam = emf.createEntityManager().find(Damnificado.class, id);

		assertEquals(24, dam.getEdad());
		assertEquals("Muerto", dam.getGravedad());
		assertEquals("Delantero-derecha", dam.getPasajero());
		assertEquals("20-25", dam.getRango_edad());
		assertEquals("Mujer", dam.getSexo());
		assertEquals(id, dam.getId());
	}

	/**
	 * Actualiza un damnificado almacenado y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testUpdateDamnificado() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (24, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Damnificado dam = em.find(Damnificado.class, id);
			dam.setEdad(25);
			dam.setGravedad("Lesion-leve");
			dam.setPasajero("Trasero-izquierda");
			dam.setRango_edad("25-34");
			dam.setSexo("Hombre");
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Damnificado WHERE id = " + id);
		rs.next();

		assertEquals(25, rs.getInt("Edad"));
		assertEquals("Lesion-leve", rs.getString("Gravedad"));
		assertEquals("Trasero-izquierda", rs.getString("Pasajero"));
		assertEquals("25-34", rs.getString("Rango_edad"));
		assertEquals("Hombre", rs.getString("Sexo"));
		assertEquals(id, rs.getInt("id"));

	}

	/**
	 * Actualiza un damnificado almacenado mediante merge y comprueba su
	 * correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	private Damnificado aDetachedDamnificado = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (24, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedDamnificado = em.find(Damnificado.class, id);
		});

		aDetachedDamnificado.setEdad(21);
		aDetachedDamnificado.setSexo("Hombre");

		doTransaction(emf, em -> {
			em.merge(aDetachedDamnificado);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Damnificado WHERE id = " + id);
		rs.next();

		assertEquals(21, rs.getInt("Edad"));
		assertEquals("Hombre", rs.getString("Sexo"));
		assertEquals(id, rs.getInt("id"));
	}

	/**
	 * Elimina un damnificado y comprueba su correcta funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDeleteDamnificado() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (24, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Damnificado dam = em.find(Damnificado.class, id);
			em.remove(dam);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Damnificado WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	/**
	 * Lista varios damnificados almacenados y comprueba su correcta
	 * funcionalidad.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testListDamnificado() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (34, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (31, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);

		List<Damnificado> damnificados = emf.createEntityManager()
				.createQuery("SELECT dam FROM Damnificado dam ORDER BY dam.edad DESC", Damnificado.class)
				.getResultList();

		// check
		// assertEquals(2, damnificados.size());
		assertEquals(34, damnificados.get(0).getEdad());
		assertEquals(31, damnificados.get(1).getEdad());
	}
}
