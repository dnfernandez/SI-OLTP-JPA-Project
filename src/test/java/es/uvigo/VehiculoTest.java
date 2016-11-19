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

public class VehiculoTest extends SQLBasedTest {
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
	public void testCreateVehiculo() throws SQLException {
		final Vehiculo veh = new Vehiculo();

		doTransaction(emf, em -> {
			veh.setAnhos(25);
			veh.setArticulado("Doble remolque");
			veh.setCombustible("Diesel");
			veh.setImpacto("Frontal");
			veh.setManiobra("Giro");
			veh.setTipo_vehiculo("Camión");
			veh.setVolante_izq(true);
			em.persist(veh);
		});

		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Vehiculo WHERE id = " + veh.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}

	// R
	@Test
	public void testFindById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Camión', 'Doble remolque', 25, 'Giro', 'Frontal', true, 'Diesel')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		// test code
		Vehiculo veh = emf.createEntityManager().find(Vehiculo.class, id);

		// assert code
		assertEquals("Camión", veh.getTipo_vehiculo());
		assertEquals("Doble remolque", veh.getArticulado());
		assertEquals(25, veh.getAnhos());
		assertEquals("Giro", veh.getManiobra());
		assertEquals("Frontal", veh.getImpacto());
		assertEquals(true, veh.getVolante_izq());
		assertEquals("Diesel", veh.getCombustible());
		assertEquals(id, veh.getId());
	}

	// U
	@Test
	public void testUpdateVehiculo() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Camión', 'Doble remolque', 25, 'Giro', 'Frontal', true, 'Diesel')",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Vehiculo veh = em.find(Vehiculo.class, id);
			veh.setAnhos(12);
			veh.setArticulado("Sin remolque");
			veh.setCombustible("Gasolina");
			veh.setImpacto("Lateral");
			veh.setManiobra("Adelantamiento");
			veh.setTipo_vehiculo("Coche");
			veh.setVolante_izq(true);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Vehiculo WHERE id = " + id);
		rs.next();

		assertEquals(12, rs.getInt("anhos"));
		assertEquals("Sin remolque", rs.getString("articulado"));
		assertEquals("Gasolina", rs.getString("combustible"));
		assertEquals("Lateral", rs.getString("impacto"));
		assertEquals("Adelantamiento", rs.getString("maniobra"));
		assertEquals("Coche", rs.getString("tipo_vehiculo"));
		assertEquals(true, rs.getBoolean("volante_izq"));
		assertEquals(id, rs.getInt("id"));

	}

	// U
	private Vehiculo aDetachedVehiculo = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Camión', 'Doble remolque', 25, 'Giro', 'Frontal', true, 'Diesel')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedVehiculo = em.find(Vehiculo.class, id);
		});

		aDetachedVehiculo.setAnhos(12);
		aDetachedVehiculo.setArticulado("Sin remolque");
		aDetachedVehiculo.setCombustible("Gasolina");
		aDetachedVehiculo.setImpacto("Lateral");
		aDetachedVehiculo.setManiobra("Adelantamiento");
		aDetachedVehiculo.setTipo_vehiculo("Coche");
		aDetachedVehiculo.setVolante_izq(true);

		doTransaction(emf, em -> {
			em.merge(aDetachedVehiculo);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Vehiculo WHERE id = " + id);
		rs.next();

		assertEquals(12, rs.getInt("anhos"));
		assertEquals("Sin remolque", rs.getString("articulado"));
		assertEquals("Gasolina", rs.getString("combustible"));
		assertEquals("Lateral", rs.getString("impacto"));
		assertEquals("Adelantamiento", rs.getString("maniobra"));
		assertEquals("Coche", rs.getString("tipo_vehiculo"));
		assertEquals(true, rs.getBoolean("volante_izq"));
		assertEquals(id, rs.getInt("id"));
	}

	// D
	@Test
	public void testDeleteVehiculo() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Camión', 'Doble remolque', 25, 'Giro', 'Frontal', true, 'Diesel')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Vehiculo veh = em.find(Vehiculo.class, id);
			em.remove(veh);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Vehiculo WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	// L
	@Test
	public void testListVehiculo() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Camión', 'Doble remolque', 3, 'Giro', 'Frontal', true, 'Diesel')",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Furgoneta', 'Sin remolque', 8, 'Aparcando', 'Lateral', true, 'Gasolina')",
				Statement.RETURN_GENERATED_KEYS);

		List<Vehiculo> vehiculos = emf.createEntityManager()
				.createQuery("SELECT veh FROM Vehiculo veh ORDER BY veh.anhos", Vehiculo.class).getResultList();

		// check
		assertEquals(2, vehiculos.size());
		assertEquals(3, vehiculos.get(0).getAnhos());
		assertEquals(8, vehiculos.get(1).getAnhos());
	}
}
