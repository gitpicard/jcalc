package tech.gitpicard.jcalc;

import java.util.ArrayList;

import tech.gitpicard.jcalc.ast.BinaryNode;
import tech.gitpicard.jcalc.ast.CallNode;
import tech.gitpicard.jcalc.ast.LiteralNode;
import tech.gitpicard.jcalc.ast.Node;
import tech.gitpicard.jcalc.ast.UnaryNode;
import tech.gitpicard.jcalc.ast.VariableNode;

/**
 * Builds an abstract syntax tree from a lexer that
 * will feed the parser tokens. The abstract syntax tree
 * can be directly evaluated by the calculator.
 */
public final class Parser {
	
	private Lexer lexer;
	private int pos;
	private ArrayList<Token> tokens;
	
	/**
	 * Creates a new parser to generate an abstract syntax tree
	 * from the lexer that you provide.
	 * @param lex The lexer to take tokens from.
	 */
	public Parser(Lexer lex) {
		if (lex == null)
			throw new IllegalArgumentException("lex");
		lexer = lex;
		pos = 0;
		tokens = new ArrayList<>();
	}
	
	private Token peek() {
		return tokens.get(pos);
	}
	
	private Token next() {
		return tokens.get(pos + 1);
	}
	
	private Token pop() {
		return tokens.get(pos++);
	}
	
	private Token previous() {
		return tokens.get(pos - 1);
	}
	
	private boolean empty() {
		return pos == tokens.size();
	}
	
	private int left() {
		return tokens.size() - pos;
	}
	
	private boolean check(TokenType t) {
		if (empty())
			return false;
		return peek().getType() == t;
	}
	
	private boolean isMatch(TokenType... types) {
		for (TokenType t : types) {
			if (check(t)) {
				pop();
				return true;
			}
		}
		
		return false;
	}
	
	private Node primary() throws SyntaxException {
		// Look for the true and false constant literals.
		if (isMatch(TokenType.TRUE))
			return new LiteralNode(1);
		else if (isMatch(TokenType.FALSE))
			return new LiteralNode(0);
		// A number is the only value type currently supported.
		else if (isMatch(TokenType.NUMBER)) {
			double d = Double.parseDouble(previous().getContents());
			return new LiteralNode(d);
		}
		// An identifier which needs to be looked up at runtime.
		else if (isMatch(TokenType.IDENTIFIER)) {
			String s = previous().getContents();
			return new VariableNode(s);
		}
		
		// A left parenthesis means we have to do some grouping...
		if (isMatch(TokenType.LEFT_PARENTHESIS)) {
			Node expression = equality();
			if (pop().getType() != TokenType.RIGHT_PARENTHESIS)
				throw new SyntaxException("Missing ')' token.");
			return expression;
		}
		
		throw new SyntaxException("Bad expression structure.");
	}
	
	private Node call() throws SyntaxException {
		// Is it a function call? Functions are not first class
		// objects so their parsing is handled pretty literally.
		if (left() >= 2 && peek().getType() == TokenType.IDENTIFIER &&
				next().getType() == TokenType.LEFT_PARENTHESIS) {
			String name = peek().getContents();
			ArrayList<Node> args = new ArrayList<>();
			
			pop();
			
			while (true) {
				pop();
				if (empty())
					throw new SyntaxException("No closing ')' found.");
				args.add(comparison());
				if (empty())
					throw new SyntaxException("No closing ')' found.");
				if (peek().getType() == TokenType.RIGHT_PARENTHESIS)
					break;
				if (peek().getType() != TokenType.COMMA)
					throw new SyntaxException("No closing ')' found.");
			}
			
			pop();
			
			return new CallNode(name,
					args.toArray(new Node[args.size()]));
		}
		// If not then we keep descending in our parser.
		return primary();
	}
	
	private Node unary() throws SyntaxException {
		if (isMatch(TokenType.MINUS, TokenType.NOT)) {
			Operation op = Operation.NEGATE;
			if (previous().getType() == TokenType.NOT)
				op = Operation.NOT;
			
			// Allows for chaining with unary expressions.
			Node right = unary();
			return new UnaryNode(op, right);
		}
		
		return call();
	}
	
	private Node pow() throws SyntaxException {
		Node expression = unary();
		
		while (isMatch(TokenType.CARROT)) {
			Operation op = Operation.POW;
			Node rightSide = unary();
			expression = new BinaryNode(expression, op, rightSide);
		}
		
		return expression;
	}
	
	private Node multDiv() throws SyntaxException {
		Node expression = pow();
		
		while (isMatch(TokenType.STAR, TokenType.SLASH,
				TokenType.MOD)) {
			// Translate the token to the correct operation code.
			Operation op = Operation.MULT;
			if (previous().getType() == TokenType.SLASH)
				op = Operation.DIV;
			else if (previous().getType() == TokenType.MOD)
				op = Operation.MOD;
			
			Node rightSide = pow();
			expression = new BinaryNode(expression, op, rightSide);
		}
		
		return expression;
	}
	
	private Node addSub() throws SyntaxException {
		Node expression = multDiv();
		
		while (isMatch(TokenType.PLUS, TokenType.MINUS)) {
			// Translate the token to the correct operation code.
			Operation op = Operation.ADD;
			if (previous().getType() == TokenType.MINUS)
				op = Operation.SUB;
			
			Node rightSide = multDiv();
			expression = new BinaryNode(expression, op, rightSide);
		}
		
		return expression;
	}
	
	private Node comparison() throws SyntaxException {
		Node expression = addSub();
		
		while (isMatch(TokenType.LESS, TokenType.GREATER,
				TokenType.LESS_OR_EQLS, TokenType.GREATER_OR_EQLS)) {
			// Translate token to operation. We don't need to do any
			// error checking because we are guaranteed to have the
			// right tokens because of the isMatch call.
			Operation op = Operation.LESS;
			switch (previous().getType()) {
			case LESS:
				op = Operation.LESS;
				break;
			case GREATER:
				op = Operation.GREATER;
				break;
			case LESS_OR_EQLS:
				op = Operation.LESS_OR_EQLS;
				break;
			case GREATER_OR_EQLS:
				op = Operation.GREATER_OR_EQLS;
				break;
			default:
				break;
			}
			
			Node rightSide = addSub();
			// Chain together the nodes.
			expression = new BinaryNode(expression, op, rightSide);
		}
		
		return expression;
	}
	
	private Node equality() throws SyntaxException {
		Node expression = comparison();
		
		while (isMatch(TokenType.EQLS, TokenType.NOT_EQLS)) {
			// Translate the token to the correct operation code.
			Operation op = Operation.EQLS;
			if (previous().getType() == TokenType.NOT_EQLS)
				op = Operation.NOT_EQLS;
			
			Node rightSide = comparison();
			
			// The loop chains together the expressions so that if there
			// are multiple equals operations in a row, the previous one
			// will be on the left side of the next one.
			expression = new BinaryNode(expression, op, rightSide);
		}
		
		// In the case that there is no equality, the while loop never runs
		// which means we have only the left side causing there to be no
		// equality node.
		return expression;
	}
	
	/**
	 * Parses the lexer's output to create an abstract syntax
	 * tree.
	 * @return The root of the tree with all the elements as children.
	 * @throws SyntaxException Thrown when the syntax is illegal.
	 */
	public Node parseTree() throws SyntaxException {
		// Get a list of all the tokens.
		Token tok = lexer.advance();
		while (tok.getType() != TokenType.EOF) {
			tokens.add(tok);
			tok = lexer.advance();
		}
		
		// We don't allow empty expressions.
		if (empty())
			throw new SyntaxException("Empty expression!");
		
		Node expresssion = equality();
		// If we didn't use all the tokens, something went wrong...
		if (!empty())
			throw new SyntaxException("Bad expression structure.");
		return expresssion;
	}
}
