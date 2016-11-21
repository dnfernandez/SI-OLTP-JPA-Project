package es.uvigo;

import static es.uvigo.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

	
}
