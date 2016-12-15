package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccidenteTest extends SQLBasedTest {
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
	public void testFindDamnificadoAccidente() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Damnificado(Edad, Gravedad, Pasajero, Rango_edad, Sexo)"
						+ "values (24, 'Muerto', 'Delantero-derecha', '20-25', 'Mujer')",
				Statement.RETURN_GENERATED_KEYS);

		int idDam = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha) " + "values('2016/11/24')",
				Statement.RETURN_GENERATED_KEYS);

		int idAc = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente_Damnificado(damnificados_id, accidentes_id) " + "values(" + idDam
				+ "," + idAc + ")", Statement.RETURN_GENERATED_KEYS);

		EntityManager em = emf.createEntityManager();
		Damnificado dam = em.find(Damnificado.class, idDam);
		Accidente ac = em.find(Accidente.class, idAc);

		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Accidente_Damnificado WHERE damnificados_id = " + idDam + " and accidentes_id= " + idAc);
		rs.next();

		// check
		assertEquals(idDam, rs.getInt("damnificados_id"));
		assertEquals(idAc, rs.getInt("accidentes_id"));
	}

	@Test
	public void testFindVehiculoAccidente() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Vehiculo(tipo_vehiculo, articulado, anhos, maniobra, impacto, volante_izq, combustible) values('Camión', 'Doble remolque', 25, 'Giro', 'Frontal', true, 'Diesel')",
				Statement.RETURN_GENERATED_KEYS);

		int idVeh = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente(fecha) " + "values('2016/11/24')",
				Statement.RETURN_GENERATED_KEYS);

		int idAc = getLastInsertedId(statement);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO Accidente_Vehiculo(vehiculos_id, accidentes_id) " + "values(" + idVeh
				+ "," + idAc + ")", Statement.RETURN_GENERATED_KEYS);

		EntityManager em = emf.createEntityManager();
		Vehiculo veh = em.find(Vehiculo.class, idVeh);
		Accidente ac = em.find(Accidente.class, idAc);

		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Accidente_Vehiculo WHERE vehiculos_id = " + idVeh + " and accidentes_id= " + idAc);
		rs.next();

		// check
		assertEquals(idVeh, rs.getInt("vehiculos_id"));
		assertEquals(idAc, rs.getInt("accidentes_id"));
	}
}
