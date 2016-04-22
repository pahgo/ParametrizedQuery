package query;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Crea un objeto que lanzará una o varias consultas a base de datos a través de
 * la conexión proporcionada.
 * {@link #ParametrizedQuery(Class, Connection, String, Object...)}.
 * 
 * <p>
 * Debe ser parametrizado en la declaración con el tipo de dato que se desea
 * obtener tras la consulta {@link #select()} o modificación {@link #persist()}.
 * 
 * <p>
 * En caso de que el tipo parametrizado sea propio de negocio, se deben
 * sobreescribir los métodos {@link #mapper(ResultSet)} y
 * {@link #addParametersToStatement(PreparedStatement)} con el fin de proveer
 * una traducción Bean-BBDD. Un consejo es preparar este método en la clase Bean
 * y acceder a él estáticamente.
 * 
 * @author fferezsa
 *
 * @param <T>
 *            Tipo parametrizado.
 * 
 */
public class ParametrizedQuery<T> {

	private final Class<T> parametrizedClass;
	transient private final Connection connection;
	private String query;
	private Object[] params;
	private int fetchSize;

	/**
	 * Crea una ParametrizedQuery con una clase parametrizada.
	 * 
	 * <p>
	 * Si la clase es propia del negocio debe declararse anonimamente y
	 * sobreescribir los métodos {@link #mapper(ResultSet)} y
	 * {@link #addParametersToStatement(PreparedStatement)}.
	 * 
	 * <p>
	 * Se pueden usar los métodos {@link #select()} y
	 * {@link #select(String, Object...)} para lanzar una consulta y obtener una
	 * lista List<T> con el tipo parametrizado.
	 * 
	 * 
	 * @param clazz
	 *            Clase con la que se parametriza el método, pe: Integer.class
	 * @param connection
	 *            Conexión con la BBDD.
	 * @param query
	 *            Consulta que debe lanzarse.
	 * @param params
	 *            Listado de valores para variables bind que deben incorporarse
	 *            a la consulta.
	 */
	public ParametrizedQuery(final Class<T> clazz, final Connection connection, final String query,
			final Object... params) {
		this.parametrizedClass = clazz;
		this.connection = connection;
		this.query = query;
		this.params = params;
	}

	/**
	 * Crea una ParametrizedQuery con una clase parametrizada. Si la clase es
	 * propia del negocio debe declararse anonimamente y sobreescribir el método
	 * {@link #mapper(ResultSet)}.
	 * 
	 * <p>
	 * Se pueden usar los métodos {@link #select()} y
	 * {@link #select(String, Object...)} para lanzar la ejecución de la quey y
	 * obtener una lista List<T> con el tipo parametrizado.
	 * 
	 * @param clazz
	 *            Clase con la que se parametriza el método, pe: Integer.class
	 * @param connection
	 *            Conexión con la BBDD.
	 */
	public ParametrizedQuery(final Class<T> clazz, final Connection connection) {
		this.parametrizedClass = clazz;
		this.connection = connection;
	}

	/**
	 * Implementa un mapper genérico de las clases típicas. En caso de que la
	 * consulta devuelva más de una columna se <b>debe sobreescribir</b> en la
	 * declaración.
	 * 
	 * @param rs
	 *            Solamente para recuperar valores, no debe alterarse de ninguna
	 *            manera.
	 * @return Un objeto del tipo parametrizado con el valor recuperado en cada
	 *         una de las filas del resultado de la consulta.
	 * @throws SQLException
	 *             Si el ResultSet devuelve un error.
	 * @throws UnsupportedOperationException
	 *             Si la clase parametrizada no es válida y debe sobreescribirse
	 *             el método.
	 */
	@SuppressWarnings("unchecked")
	protected T mapper(ResultSet rs) throws SQLException {
		if (parametrizedClass.equals(Integer.class)) {
			return (T) (Integer) rs.getInt(1);
		} else if (parametrizedClass.equals(Long.class)) {
			return (T) (Long) rs.getLong(1);
		} else if (parametrizedClass.equals(String.class)) {
			return (T) (String) rs.getString(1);
		} else if (parametrizedClass.equals(java.sql.Date.class)) {
			return (T) (java.sql.Date) rs.getDate(1);
		} else if (parametrizedClass.equals(java.util.Date.class)) {
			return (T) (java.util.Date) rs.getDate(1);
		}
		throw new UnsupportedOperationException(String.format("Not yet implemented for class: %s", parametrizedClass));
	}

	/**
	 * Ejecuta una consulta <b>query</b> utilizando los parámetros <b>param</b>
	 * que pueden estar vacíos o ser null.
	 * 
	 * <p>
	 * El número de variables en la consulta y el número de parámetros debe ser
	 * el mismo.
	 * 
	 * @param query
	 *            Consulta sql, admite variables bind con el formato '?'.
	 * @param params
	 *            Lista de valores que tendrán las variables bind en el orden
	 *            que aparecen. La forma de unir estos valores a las variables
	 *            se puede sobreescribir en
	 *            {@link #addParametersToStatement(PreparedStatement)}.
	 * @return Lista de resultados, puede tener longitud 0, del tipo que se ha
	 *         parametrizado al crear el objeto ParametrizedQuery.
	 * @throws SQLException
	 *             Si ocurre algún error con la BBDD.
	 * @throws IllegalArgumentException
	 *             Si la consulta es nula o está vacía.
	 */
	public List<T> select(String query, Object... params) throws SQLException {
		this.query = query;
		this.params = params;
		return select();
	}

	/**
	 * Ejecuta la consulta que se ha pasado en el constructor o en el método
	 * {@link #setQuery()} con los parámetros que existan.
	 * 
	 * @see {@link #select(String, Object...)}
	 * @return Lista de resultados, puede tener longitud 0, del tipo que se ha
	 *         parametrizado al crear el objeto ParametrizedQuery.
	 * @throws SQLException
	 *             Si ocurre algún error con la BBDD.
	 * @throws IllegalArgumentException
	 *             Si la consulta es nula o está vacía, típicamente haber usado
	 *             el constructor {@link #ParametrizedQuery(Class, Connection)} sin
	 *             hacer posteriormente uso del método {@link #setQuery()}.
	 */
	public List<T> select() throws SQLException {
		checkArguments();
		final ResultSet resultSet = doExecute();
		return getResult(resultSet);
	}

	/**
	 * Ejecuta una modificación <b>query</b> utilizando los parámetros <b>param</b>
	 * que pueden estar vacíos o ser null.
	 * 
	 * <p>
	 * El número de variables en la consulta y el número de parámetros debe ser
	 * el mismo.
	 * 
	 * @param query
	 *            Consulta sql, admite variables bind con el formato '?'.
	 * @param params
	 *            Lista de valores que tendrán las variables bind en el orden
	 *            que aparecen. La forma de unir estos valores a las variables
	 *            se puede sobreescribir en
	 *            {@link #addParametersToStatement(PreparedStatement)}.
	 * @return Número de filas modificadas.
	 * @throws SQLException
	 *             Si ocurre algún error con la BBDD.
	 * @throws IllegalArgumentException
	 *             Si la consulta es nula o está vacía.
	 */
	public int persist(String query, Object... params) throws SQLException {
		this.query = query;
		this.params = params;
		return persist();
	}
	/**
	 * Ejecuta la modificación que se ha pasado en el constructor o en el método
	 * {@link #setQuery()} con los parámetros que existan.
	 * 
	 * @see {@link #persist(String, Object...)}
	 * @return Número de filas modificadas.
	 * @throws SQLException
	 *             Si ocurre algún error con la BBDD.
	 * @throws IllegalArgumentException
	 *             Si la consulta es nula o está vacía, típicamente haber usado
	 *             el constructor {@link #ParametrizedQuery(Class, Connection)} sin
	 *             hacer posteriormente uso del método {@link #setQuery()}.
	 */
	public int persist() throws SQLException {
		checkArguments();
		final PreparedStatement statement = connection.prepareStatement(query);
		addParametersToStatement(statement);
		return statement.executeUpdate();
	}

	/**
	 * Debe sobreescribirse en caso de que el objeto que parametriza la clase
	 * sea propio de negocio.
	 * 
	 * @param statement
	 *            Objeto al que se le deben añadir los parámetros uno a uno que
	 *            vengan de la query.
	 * @throws SQLException
	 *             Si ocurre algún error con BBDD.
	 */
	protected void addParametersToStatement(final PreparedStatement statement) throws SQLException {
		int i = 1;
		if (params != null) {
			for (Object o : params) {
				if (o instanceof String) {
					statement.setString(i++, (String) o);
				} else if (o instanceof Integer) {
					statement.setInt(i++, (Integer) o);
				} else if (o instanceof Long) {
					statement.setLong(i++, (Long) o);
				} else if (o instanceof java.sql.Date) {
					statement.setDate(i++, (Date) o);
				} else {
					throw new UnsupportedOperationException(
							String.format("Not yet implemented for class: %s", o.getClass()));
				}
			}
		}
	}

	private ResultSet doExecute() throws SQLException {
		final PreparedStatement statement = connection.prepareStatement(query);
		addParametersToStatement(statement);
		return statement.executeQuery();
	}

	private List<T> getResult(final ResultSet resultSet) throws SQLException {
		final List<T> result = new ArrayList<T>();
		while (resultSet.next()) {
			result.add(mapper(resultSet));
		}
		return result;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	/**
	 * @return the fetchSize
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * 
	 * @param fetchSize
	 *            the fetchSize to set
	 */
	public void setFetchSize(int fetchSize) {
		if (fetchSize <= 0) {
			throw new IllegalArgumentException("Fetch size must be 1 or more");
		}
		this.fetchSize = fetchSize;
	}

	/**
	 * 
	 */
	private void checkArguments() {
		if (query == null || query.isEmpty())
			throw new IllegalArgumentException("A query has not been specified");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParametrizedQuery [parametrizedClass=" + parametrizedClass + ", query=" + query + ", params="
				+ Arrays.toString(params) + ", fetchSize=" + fetchSize + "]";
	}

}
