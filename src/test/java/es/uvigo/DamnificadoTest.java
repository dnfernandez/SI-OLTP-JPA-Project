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

public class DamnificadoTest extends SQLBasedTest {
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
	public void testCreateDamnificado() throws SQLException{
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
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Damnificado"
				+ " WHERE id = " + dam.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
	}
	
	@Test
	public void testFindById() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
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

}
