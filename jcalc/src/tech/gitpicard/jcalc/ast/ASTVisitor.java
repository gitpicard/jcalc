package tech.gitpicard.jcalc.ast;

import tech.gitpicard.jcalc.EvalException;

/**
 * Implement this interface to create an environment that can evaluate
 * an expression by visiting the nodes of the abstract syntax tree.
 */
public interface ASTVisitor {
	/**
	 * Visit and evaluate both sides of the expression.
	 * @param node The binary node to visit.
	 * @return Returns the result of the operation.
	 * @throws EvalException When unable to evaluate the result.
	 */
	double visit(BinaryNode node) throws EvalException;
	/**
	 * Visit and evaluate an operation that only has a single expression
	 * as a child.
	 * @param node The unary node to visit.
	 * @return Returns the result of the operation.
	 * @throws EvalException When unable to evaluate the result.
	 */
	double visit(UnaryNode node) throws EvalException;
	/**
	 * Visit and evaluate a literal value. These are values such as literal
	 * numbers, literal true, and literal false.
	 * @param node The literal to visit.
	 * @return The value represented by the node.
	 * @throws EvalException When unable to evaluate the result.
	 */
	double visit(LiteralNode node) throws EvalException;
	/**
	 * Visit and evaluate a variable. The value of the variable will
	 * be looked up at runtime.
	 * @param node The node with the variable reference.
	 * @return The value being held by the variable.
	 * @throws EvalException When unable to evaluate the result.
	 */
	double visit(VariableNode node) throws EvalException;
	/**
	 * Visit and evaluate a function. This will call a function
	 * with the passed-in arguments and then return the result.
	 * @param node The function call node.
	 * @return The return value from the function.
	 * @throws EvalException When the function fails.
	 */
	double visit(CallNode node) throws EvalException;
}
