package tech.gitpicard.jcalc;

import java.util.HashMap;

import tech.gitpicard.jcalc.ast.ASTVisitor;
import tech.gitpicard.jcalc.ast.BinaryNode;
import tech.gitpicard.jcalc.ast.CallNode;
import tech.gitpicard.jcalc.ast.LiteralNode;
import tech.gitpicard.jcalc.ast.UnaryNode;
import tech.gitpicard.jcalc.ast.VariableNode;

/**
 * Evaluates expressions from plain-text source using an
 * abstract syntax tree to support advanced expression syntax
 * such as function calls.
 */
public final class JCalculator implements ASTVisitor {
	
	private HashMap<String, Double> variables;
	private HashMap<String, Function> functions;
	
	/**
	 * Create a new expression interpreter.
	 */
	public JCalculator() {
		variables = new HashMap<>();
		functions = new HashMap<>();
	}
	
	/**
	 * Define or change a variable and set the value it contains. Variable
	 * names can only be letters and underscores.
	 * @param name The name of the variable.
	 * @param value The value to set the variable to.
	 */
	public void setVariable(String name, double value) {
		if (name == null)
			throw new IllegalArgumentException("name");
		if (functions.containsKey(name))
			throw new IllegalArgumentException("name");
		// Make sure that the name is legal.
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			boolean alpha = Character.isAlphabetic(c);
			if (!alpha && c != '_')
				throw new IllegalArgumentException("name");
		}
		
		variables.put(name, value);
	}
	
	/**
	 * Get the value defined by a variable.
	 * @param name The name of the variable.
	 * @return The value held by the variable.
	 */
	public double getVariable(String name) {
		if (name == null)
			throw new IllegalArgumentException("name");
		if (!variables.containsKey(name))
			throw new IllegalArgumentException("name");
		return variables.get(name);
	}
	
	/**
	 * Checks to see if the name is already bound to
	 * a variable. Does not check if the name is legal.
	 * @param name The name to lookup.
	 * @return True if it is a variable.
	 */
	public boolean isVariable(String name) {
		return variables.containsKey(name);
	}
	
	/**
	 * Define a function that can be called by the expression.
	 * @param name The name of the function.
	 * @param func Interface to the code to execute.
	 */
	public void setFunction(String name, Function func) {
		if (name == null)
			throw new IllegalArgumentException("name");
		if (func == null)
			throw new IllegalArgumentException("func");
		if (variables.containsKey(name))
			throw new IllegalArgumentException("name");
		// Make sure that the name is legal.
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			boolean alpha = Character.isAlphabetic(c);
			if (!alpha && c != '_')
				throw new IllegalArgumentException("name");
		}
		
		functions.put(name, func);
	}
	
	/**
	 * Get the function with the desired name.
	 * @param name The name of the function to lookup.
	 * @return The function that uses that name.
	 */
	public Function getFunction(String name) {
		if (name == null)
			throw new IllegalArgumentException("name");
		if (!functions.containsKey(name))
			throw new IllegalArgumentException("name");
		return functions.get(name);
	}
	
	/**
	 * Checks to see if the name is already bound to
	 * a function. Does not check if the name is legal.
	 * @param name The name to lookup.
	 * @return True if it is a function.
	 */
	public boolean isFunction(String name) {
		return functions.containsKey(name);
	}
	
	/**
	 * Evaluates the expression and immediately returns the result.
	 * @param source The string to parse and evaluate.
	 * @return The resulting value.
	 * @throws SyntaxException When there is a syntax error.
	 * @throws EvalException When there is a runtime evaluation error.
	 */
	public double eval(String source)
			throws SyntaxException, EvalException {
		return new Parser(new Lexer(source)).parseTree().accept(this);
	}

	@Override
	public double visit(BinaryNode node) throws EvalException {
		Operation op = node.getOperation();
		double left = node.getLeft().accept(this);
		double right = node.getRight().accept(this);
		
		switch (op) {
		case ADD:
			return left + right;
		case SUB:
			return left - right;
		case MULT:
			return left * right;
		case DIV:
			return left / right;
		case POW:
			return Math.pow(left, right);
		case MOD:
			return left % right;
		case EQLS:
			return left == right ? 1.0 : 0.0;
		case NOT_EQLS:
			return left != right ? 1.0 : 0.0;
		case LESS:
			return left < right ? 1.0 : 0.0;
		case GREATER:
			return left > right ? 1.0 : 0.0;
		case LESS_OR_EQLS:
			return left <= right ? 1.0 : 0.0;
		case GREATER_OR_EQLS:
			return left >= right ? 1.0 : 0.0;
		default:
			throw new EvalException("Illegal binary operation.");
		}
	}

	@Override
	public double visit(UnaryNode node) throws EvalException {
		if (node.getOperation() == Operation.NEGATE)
			return -node.getRight().accept(this);
		else if (node.getOperation() == Operation.NOT) {
			double val = node.getRight().accept(this);
			// In our calculator, only the value 0 is considered
			// false and everything else is true.
			if (val == 0.0)
				return 1.0;
			return 0.0;
		}
		
		throw new EvalException("Illegal unary operation.");	
	}

	@Override
	public double visit(LiteralNode node) throws EvalException {
		return node.getValue();
	}

	@Override
	public double visit(VariableNode node) throws EvalException {
		String s = node.getIdentifer();
		if (!variables.containsKey(s))
			throw new EvalException("No variable '" + s + "' found.");
		return variables.get(s);
	}

	@Override
	public double visit(CallNode node) throws EvalException {
		String s = node.getFunction();
		if (!functions.containsKey(s))
			throw new EvalException("No function '" + s + "' found.");
		
		// Evaluate all the argument expressions.
		double[] args = new double[node.getArguments().length];
		for (int i = 0; i < args.length; i++)
			args[i] = node.getArguments()[i].accept(this);
		
		return functions.get(s).call(args);
	}
}
