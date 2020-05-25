package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;

/**
 * The root type for all nodes in the abstract syntax tree. The
 * calculator must be able to visit each type.
 */
public abstract class Node {
	/**
	 * Evaluate this node using the environment you pass in.
	 * @param visitor The environment with which to evaluate.
	 * @return The result of the operation.
	 * @throws EvalException When there is a runtime error.
	 */
	public abstract double accept(ASTVisitor visitor) throws EvalException;
}
