package dto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Alianza {

	public static final String FECHA_INSERCION = "fecha_insercion";
	public static final String NOMBRE = "nombre";
	public static final String ID = "ID";

	private Long id;
	private String nombre;
	private Date fechaInsercion;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the fechaInsercion
	 */
	public Date getFechaInsercion() {
		return fechaInsercion;
	}

	/**
	 * @param fechaInsercion
	 *            the fechaInsercion to set
	 */
	public void setFechaInsercion(Date fechaInsercion) {
		this.fechaInsercion = fechaInsercion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Alianza [id=" + id + ", nombre=" + nombre + ", fechaInsercion=" + fechaInsercion + "]";
	}

	public static Alianza mapper(ResultSet rs) throws SQLException {
		final Alianza alianza = new Alianza();
		alianza.setId(rs.getLong(ID));
		alianza.setNombre(rs.getString(NOMBRE));
		alianza.setFechaInsercion(rs.getDate(FECHA_INSERCION));
		return alianza;
	}

	public void addParametersToStatement(PreparedStatement statement) throws SQLException {
		int cont = 1;
		statement.setLong(cont++, id);
		statement.setString(cont++, nombre);
		statement.setDate(cont++, new java.sql.Date(fechaInsercion.getTime()));
	}

}
