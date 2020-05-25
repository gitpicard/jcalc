package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;

/**
 * Represents a literal value in the expression tree.
 */
public final class LiteralNode extends Node {

	private double value;
	
	/**
	 * Create a new literal that holds a constant value.
	 * @param d The constant value to hold.
	 */
	public LiteralNode(double d) {
		value = d;
	}
	
	/**
	 * Get the constant being held by this node.
	 * @return A constant value in the expression.
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public double accept(ASTVisitor visitor) throws EvalException {
		return visitor.visit(this);
	}

}
