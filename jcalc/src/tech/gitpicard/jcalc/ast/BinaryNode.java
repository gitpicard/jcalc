package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;
import tech.gitpicard.jcalc.Operation;

/**
 * A node that branches with an expression on the left and
 * an expression on the right. An operation will be applied
 * that requires two values.
 */
public final class BinaryNode extends Node {
	
	private Node leftSide;
	private Operation operation;
	private Node rightSide;
	
	/**
	 * Create a new binary node that has a left and a right side.
	 * @param left The left side to evaluate.
	 * @param op The operation that will use both sides.
	 * @param right The right side to evaluate.
	 */
	public BinaryNode(Node left, Operation op, Node right) {
		if (left == null)
			throw new IllegalArgumentException("left");
		// There are only two unary operations so make sure we did not
		// get passed those.
		if (op == Operation.NOT || op == Operation.NEGATE)
			throw new IllegalArgumentException("op");
		if (right == null)
			throw new IllegalArgumentException("right");
		leftSide = left;
		operation = op;
		rightSide = right;
	}
	
	/**
	 * The left side of the expression.
	 * @return The sub-expression on the left side.
	 */
	public Node getLeft() {
		return leftSide;
	}
	
	/**
	 * The operation to perform with the two child nodes.
	 * @return The operation enumerator.
	 */
	public Operation getOperation() {
		return operation;
	}
	
	/**
	 * The right side of the expression.
	 * @return The sub-expression on the right side.
	 */
	public Node getRight() {
		return rightSide;
	}
	
	public double accept(ASTVisitor vistior) throws EvalException {
		return vistior.visit(this);
	}
}
