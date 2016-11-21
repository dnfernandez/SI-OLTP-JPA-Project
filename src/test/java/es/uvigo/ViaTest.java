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

	@Test
	public void testCreateDamnificado() throws SQLException {
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
		ResultSet rs = statement
				.executeQuery("SELECT COUNT(*) as total FROM Via" + " WHERE id = " + via.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	@Test
	public void testFindById() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate(
				"INSERT INTO Via(Estado_via, N_carriles, Peligros_calzada, Tipo_via, Urbano, Velocidad_autorizada)"
						+ "values ('Mojado', 2, 'Ninguno', 'Nacional', true, 80)",
				Statement.RETURN_GENERATED_KEYS);

		Via via = emf.createEntityManager().find(Via.class, id);

		assertEquals("Mojado", via.getEstado_via());
		assertEquals(2, via.getN_carriles());
		assertEquals("Ninguno", via.getPeligros_calzada());
		assertEquals("Nacional", via.getTipo_via());
		assertEquals(true, via.isUrbano());
		assertEquals(80, via.getVelocidad_autorizada());
		assertEquals(id, via.getId());
	}

}
