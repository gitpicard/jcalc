package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;

/**
 * Represents a variable in the expression tree. The value is looked
 * up at runtime using the current environment.
 */
public final class VariableNode extends Node {

	private String ident;
	
	/**
	 * Create a new variable look-up. This is only to
	 * look-up existing variables. Variables must be created
	 * by the calculator.
	 * @param s The name of the variable.
	 */
	public VariableNode(String s) {
		if (s == null)
			throw new IllegalArgumentException("s");
		// Make sure that the name is legal.
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			boolean alpha = Character.isAlphabetic(c);
			if (!alpha && c != '_')
				throw new IllegalArgumentException("s");
		}
		ident = s;
	}
	
	public String getIdentifer() {
		return ident;
	}
	
	@Override
	public double accept(ASTVisitor visitor) throws EvalException {
		return visitor.visit(this);
	}

}
