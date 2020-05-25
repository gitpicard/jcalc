package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;
import tech.gitpicard.jcalc.Operation;

/**
 * A node that performs an operation on a single value.
 */
public final class UnaryNode extends Node {
	
	private Operation operation;
	private Node rightSide;
	
	/**
	 * Create a new unary node with a singular child tree.
	 * @param op The operation to perform on the expression.
	 * @param right The expression under this node.
	 */
	public UnaryNode(Operation op, Node right) {
		if (op != Operation.NOT && op != Operation.NEGATE)
			throw new IllegalArgumentException("op");
		if (right == null)
			throw new IllegalArgumentException("right");
		operation = op;
		rightSide = right;
	}
	
	/**
	 * Get the operation that this node performs.
	 * @return The operation enumeration.
	 */
	public Operation getOperation() {
		return operation;
	}
	
	/**
	 * Get the expression/tree that is underneath this node.
	 * @return The expression's root node.
	 */
	public Node getRight() {
		return rightSide;
	}
	
	public double accept(ASTVisitor vistior) throws EvalException {
		return vistior.visit(this);
	}
}
