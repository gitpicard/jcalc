package tech.gitpicard.jcalc;

/**
 * An exception occurring at runtime due to an illegal
 * operation.
 */
public final class EvalException extends Exception {
	/**
	 * Create a new exception when evaluation must stop
	 * due to a runtime error.
	 * @param s A descriptive but short error message.
	 */
	public EvalException(String s) {
		super(s);
	}
}
