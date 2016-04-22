package dao.impl;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import dao.base.BaseDao;
import dao.base.DaoException;
import dto.Alianza;

public class TestDao {

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
		BaseDao<Alianza> dao = new AlianzaDao(connection);
		Alianza ali = new Alianza();
		ali.setId(4L);

		try {
			System.out.println(dao.findById(ali));
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void insert() {
		BaseDao<Alianza> dao = new AlianzaDao(connection);
		Alianza ali = new Alianza();
		ali.setId(6L);
		ali.setNombre("insertDao");
		ali.setFechaInsercion(new Date());
		int count = 0;
		try {
			count = dao.insert(ali);
		} catch (DaoException e) {
			e.printStackTrace();
			fail("ERROR");
		}
		Assert.assertEquals(1, count);
	}

	@Test
	public void update() {
		BaseDao<Alianza> dao = new AlianzaDao(connection);
		Alianza ali = new Alianza();
		ali.setId(4L);
		ali.setNombre("updateDao");
		ali.setFechaInsercion(new Date());
		int count = 0;
		try {
			count = dao.update(ali);
		} catch (DaoException e) {
			e.printStackTrace();
			fail("ERROR");
		}
		Assert.assertEquals(1, count);
	}

	@Test
	public void delete() {
		BaseDao<Alianza> dao = new AlianzaDao(connection);
		Alianza ali = new Alianza();
		ali.setId(4L);
		ali.setNombre("updateDao");
		ali.setFechaInsercion(new Date());
		int count = 0;
		try {
			count = dao.update(ali);
		} catch (DaoException e) {
			e.printStackTrace();
			fail("ERROR");
		}
		Assert.assertEquals(1, count);
	}
}
