package dao.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import query.ParametrizedQuery;

/**
 * Proporciona una estructura básica a la capa DAO.
 * 
 * <p>
 * Obliga a implementar {@link #findById(Object)}, {@link #insert(Object)},
 * {@link #update(Object)}, {@link #delete(Object)}, {@link #getTableName()} y
 * {@link #getTableAlias()}.
 * 
 * <p>
 * Proporciona los métodos {@link #select(ParametrizedQuery)} y
 * {@link BaseDao#persist(ParametrizedQuery)} para simplificar el acceso y
 * control de excepciones.
 * 
 * <p>
 * Proporciona los métodos {@link #getTableAndAlias()} y
 * {@link #getFirstRecord(ParametrizedQuery)}.
 * 
 * @author fferezsa
 *
 * @param <T>
 *            Clase parametrizada.
 * 
 */
public abstract class BaseDao<T> {

	protected Connection connection;
	protected static final String DELETE = "DELETE FROM ";
	protected static final String SELECT_ALL = "SELECT * FROM ";
	protected static final String INSERT_INTO = "INSERT INTO ";
	protected static final String UPDATE = "UPDATE ";
	protected static final String VALUES = " VALUES ";
	protected static final String WHERE = " WHERE ";
	protected static final String SET = " SET ";
	protected static final String EQUALS_TO_PARAMETER = " = ? ";
	protected static final String GT_TO_PARAMETER = " > ? ";
	protected static final String LT_TO_PARAMETER = " < ? ";
	protected static final String GET_TO_PARAMETER = " >= ? ";
	protected static final String LET_TO_PARAMETER = " <= ? ";
	protected static final String NOT_EQUALS_TO_PARAMETER = " = ? ";
	protected static final String AND = " AND ";
	protected static final String CLOSE_PARENTHESIS = ")";
	protected static final String COMA = ", ";
	protected static final String OPEN_PARENTHESIS = "(";

	/**
	 * 
	 * @return Nombre de la tabla.
	 */
	public abstract String getTableName();

	/**
	 * 
	 * @return Alias que se desea usar generalmente para las consultas.
	 */
	public abstract String getTableAlias();

	public abstract T findById(T bean) throws DaoException;

	public abstract int insert(T bean) throws DaoException;

	public abstract int update(T bean) throws DaoException;

	public abstract int delete(T bean) throws DaoException;

	/**
	 * Ejecuta una consulta devolviendo únicamente el primero de los resultados
	 * si los hubiera, nulo en caso contrario.
	 * 
	 * @param parametrized
	 *            Objeto ParametrizedQuery que ha sido preparado para lanzar una
	 *            consulta.
	 * @return Objeto correspondiente a la primera posición del resultado de la
	 *         consulta. Nulo si no hubiera resultado.
	 * @throws DaoException
	 *             Si ocurre cualquier problema en la consulta.
	 */
	public T getFirstRecord(ParametrizedQuery<T> parametrized) throws DaoException {
		T result = null;
		List<T> list = null;
		try {
			list = parametrized.select();
		} catch (SQLException e) {
			throw new DaoException(e, "Error accesing DDBB.", parametrized);
		}
		if (list != null && list.size() >= 1) {
			result = list.get(0);
		}
		return result;
	}

	/**
	 * Proporciona una cadena de texto con el nombre de la tabla seguido de
	 * espacio y el alias que se le ha asignado.
	 * 
	 * @return "tabla alias"
	 */
	public String getTableAndAlias() {
		return getTableName().concat(" ").concat(getTableAlias());
	}

	/**
	 * Trata de ejecutar una modificación en BBDD para el objeto
	 * {@link ParametrizedQuery} proporcionado.
	 * 
	 * @param parametrized
	 *            {@link ParametrizedQuery} que ha sido declarado con las
	 *            opciones para un insert/update.
	 * @return Número de filas afectadas por el cambio.
	 * @throws DaoException
	 *             Si ocurre un problema en el acceso a base de datos.
	 */
	public int persist(final ParametrizedQuery<T> parametrized) throws DaoException {
		try {
			return parametrized.persist();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e, "Could not persist", parametrized);
		}
	}

	/**
	 * Trata de ejecutar una consulta sobre base de datos para el objeto
	 * {@link ParametrizedQuery} proporcionado.
	 * 
	 * @param parametrized
	 *            {@link ParametrizedQuery} que ha sido declarado con las
	 *            opciones necesarias para un select.
	 * @return Listado del tipo parametrizado correspondiente al resultado de la
	 *         consulta.
	 * @throws DaoException
	 *             Si ocurre un problema en el acceso a base de datos.
	 * 
	 */
	public List<T> select(final ParametrizedQuery<T> parametrized) throws DaoException {
		try {
			return parametrized.select();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e, "Could not read", parametrized);
		}
	}

}
