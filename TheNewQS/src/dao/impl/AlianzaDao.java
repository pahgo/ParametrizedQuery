package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.base.BaseDao;
import dao.base.DaoException;
import dto.Alianza;
import query.ParametrizedQuery;

public class AlianzaDao extends BaseDao<Alianza> {

	public AlianzaDao(final Connection connection) {
		this.connection = connection;
	}

	@Override
	public Alianza findById(final Alianza bean) throws DaoException {
		final StringBuilder sb = new StringBuilder(SELECT_ALL);
		sb.append(getTableAndAlias()).append(WHERE);
		sb.append(Alianza.ID).append(EQUALS_TO_PARAMETER);
		final ParametrizedQuery<Alianza> parametrized = new ParametrizedQuery<Alianza>(Alianza.class, connection,
				sb.toString(), bean.getId()) {
			@Override
			protected Alianza mapper(ResultSet rs) throws SQLException {
				return Alianza.mapper(rs);
			}
		};

		return getFirstRecord(parametrized);
	}

	@Override
	public int insert(final Alianza bean) throws DaoException {
		final StringBuilder sb = new StringBuilder(INSERT_INTO);
		sb.append(getTableName());
		sb.append(OPEN_PARENTHESIS);
		sb.append(Alianza.ID).append(COMA);
		sb.append(Alianza.NOMBRE).append(COMA);
		sb.append(Alianza.FECHA_INSERCION).append(CLOSE_PARENTHESIS);
		sb.append(VALUES).append(OPEN_PARENTHESIS);
		sb.append("?, ?, ?").append(CLOSE_PARENTHESIS);
		final ParametrizedQuery<Alianza> parametrized = new ParametrizedQuery<Alianza>(Alianza.class, connection,
				sb.toString()) {

			@Override
			protected void addParametersToStatement(PreparedStatement statement) throws SQLException {
				bean.addParametersToStatement(statement);
			}
		};

		return persist(parametrized);
	}

	@Override
	public int update(final Alianza bean) throws DaoException {
		final StringBuilder sb = new StringBuilder(UPDATE);
		sb.append(getTableName());
		sb.append(SET);
		sb.append(Alianza.NOMBRE).append(EQUALS_TO_PARAMETER).append(COMA);
		sb.append(Alianza.FECHA_INSERCION).append(EQUALS_TO_PARAMETER);
		sb.append(WHERE);
		sb.append(Alianza.ID).append(EQUALS_TO_PARAMETER);
		final ParametrizedQuery<Alianza> parametrized = new ParametrizedQuery<Alianza>(Alianza.class, connection,
				sb.toString()) {

			@Override
			protected void addParametersToStatement(PreparedStatement statement) throws SQLException {
				int cont = 1;
				statement.setString(cont++, bean.getNombre());
				statement.setDate(cont++, new java.sql.Date(bean.getFechaInsercion().getTime()));
				statement.setLong(cont++, bean.getId());
			}
		};
		return persist(parametrized);
	}

	@Override
	public int delete(final Alianza bean) throws DaoException {
		final StringBuilder sb = new StringBuilder(DELETE);
		sb.append(getTableName());
		sb.append(WHERE);
		sb.append(Alianza.ID).append(EQUALS_TO_PARAMETER);
		final ParametrizedQuery<Alianza> parametrized = new ParametrizedQuery<Alianza>(Alianza.class, connection, sb.toString(), bean.getId());
		return persist(parametrized);
	}
	
	@Override
	public String getTableName() {
		return "ALIANZA";
	}
	
	@Override
	public String getTableAlias() {
		return "ali";
	}
}
