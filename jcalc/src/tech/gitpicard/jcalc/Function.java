package tech.gitpicard.jcalc;

/**
 * Implement a call to a function from an expression. This
 * will allow a math expression to call a Java function.
 */
public interface Function {
	/**
	 * Called by the calculator and provided with
	 * the arguments passed by the expression.
	 * @param args The arguments passed by the expression.
	 * @return The result from the function call.
	 * @throws EvalException When the function has a runtime error.
	 */
	double call(double[] args) throws EvalException;
}
