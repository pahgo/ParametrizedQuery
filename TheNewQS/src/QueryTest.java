import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import dto.Alianza;
import query.ParametrizedQuery;

public class QueryTest {

	static Connection connection;
	final static String userName = "root";
	final static String password = "1234";

	@BeforeClass
	public static void initialize() {
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/castillos", connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Could not connect to DDBB.");
		}

	}

	@Test
	public void select() {
		String query = "select * from alianza ali";
		ParametrizedQuery<Alianza> parametrized = new ParametrizedQuery<Alianza>(Alianza.class, connection, query) {

			@Override
			protected Alianza mapper(ResultSet rs) throws SQLException {
				return Alianza.mapper(rs);
			}
		};

		List<Alianza> alianzas = new ArrayList<Alianza>();
		try {
			alianzas = parametrized.select();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to access DDBB");
		}
		for (Alianza alianza : alianzas) {
			System.out.println(alianza);
		}
	}

	@Test
	public void insert() {
		final Alianza ali = new Alianza();
		ali.setId(3L);
		ali.setNombre("Prueba inserción 2");
		ali.setFechaInsercion(new Date());

		String query = "insert into alianza (id, nombre, fecha_insercion) values (?, ? ,?)";
		ParametrizedQuery<Alianza> parametrized = new ParametrizedQuery<Alianza>(Alianza.class, connection, query) {
			@Override
			protected void addParametersToStatement(PreparedStatement statement) throws SQLException {
				ali.addParametersToStatement(statement);
			}
		};

		try {
			org.junit.Assert.assertEquals(1, parametrized.persist());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
