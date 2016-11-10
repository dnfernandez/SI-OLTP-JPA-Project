package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeTest extends SQLBasedTest {
	private static EntityManagerFactory emf;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	@Test
	public void testCreateEmployee() throws SQLException {
		final Employee emp = new Employee();		
		
		doTransaction(emf, em -> {
				emp.setName("Daniel");
				em.persist(emp);
			});
		
	
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Employee WHERE id = "+emp.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		
	}
	
	@Test
	public void testFindById() throws SQLException{
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("INSERT INTO Employee(name) values('Daniel')", Statement.RETURN_GENERATED_KEYS);
				
		//test code
		Employee e = emf.createEntityManager().find(Employee.class, id);

		//assert code
		assertEquals("Daniel", e.getName());
		assertEquals(id, e.getId());
	}
}