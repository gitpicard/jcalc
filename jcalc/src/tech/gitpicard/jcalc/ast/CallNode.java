package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;

/**
 * Represents a call to a function. The node can have
 * an arbitrary number of arguments.
 */
public final class CallNode extends Node {
	
	private String ident;
	private Node[] args;
	
	/**
	 * Create a new call to a function.
	 * @param function The name of the function to call.
	 * @param arguments The arguments to use for the call.
	 */
	public CallNode(String function, Node[] arguments) {
		ident = function;
		args = arguments;
	}
	
	/**
	 * Get the name of the function that will be called.
	 * @return Function name.
	 */
	public String getFunction() {
		return ident;
	}
	
	/**
	 * Get a list of all the arguments that will be passed to
	 * the function.
	 * @return Array of arguments.
	 */
	public Node[] getArguments() {
		return args;
	}

	@Override
	public double accept(ASTVisitor visitor) throws EvalException {
		return visitor.visit(this);
	}

}
