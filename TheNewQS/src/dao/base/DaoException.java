package dao.base;

import java.util.Arrays;

public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7851241418894620356L;
	private Exception encapsulatedException;
	private String errorCode;
	private Object[] motive;
	private StackTraceElement[] stack;

	public DaoException(Exception encapsulatedException, String errorCode, Object... motive) {
		super();
		this.encapsulatedException = encapsulatedException;
		this.errorCode = errorCode;
		this.motive = motive;
		this.setStack(Thread.currentThread().getStackTrace());
	}

	/**
	 * @return the encapsulatedException
	 */
	public Exception getEncapsulatedException() {
		return encapsulatedException;
	}

	/**
	 * @param encapsulatedException
	 *            the encapsulatedException to set
	 */
	public void setEncapsulatedException(Exception encapsulatedException) {
		this.encapsulatedException = encapsulatedException;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the motive
	 */
	public Object[] getMotive() {
		return motive;
	}

	/**
	 * @param motive
	 *            the motive to set
	 */
	public void setMotive(Object[] motive) {
		this.motive = motive;
	}

	/**
	 * @return the stack
	 */
	public StackTraceElement[] getStack() {
		return stack;
	}

	/**
	 * @param stack
	 *            the stack to set
	 */
	public void setStack(StackTraceElement[] stack) {
		this.stack = stack;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DaoException [encapsulatedException=" + encapsulatedException + ", errorCode=" + errorCode + ", motive="
				+ Arrays.toString(motive) + ", stack=" + Arrays.toString(stack) + "]";
	}

}
