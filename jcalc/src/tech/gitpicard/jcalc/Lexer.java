package tech.gitpicard.jcalc;

import java.util.Stack;

/**
 * Used to pick out context free tokens from a source
 * expression. These tokens are then used to build an
 * abstract syntax tree that can be evaluated to get a
 * numerical result. 
 */
public final class Lexer {
	
	private Stack<Character> sourceStack;
	
	/**
	 * Create a new lexer that will consume tokens from a string
	 * containing an expression.
	 * @param source The expression to tokenize.
	 */
	public Lexer(String source) {
		if (source == null)
			throw new IllegalArgumentException("source");
		// Convert the expression to a stack that we can easily
		// move through. The string will need to be pushed on
		// backwards to put it on the stack in the correct order.
		sourceStack = new Stack<>();
		for (int i = source.length() - 1; i >= 0; i--)
			sourceStack.push(source.charAt(i));
	}
	
	private boolean isSpace() {
		if (sourceStack.empty())
			return false;
		return Character.isWhitespace(sourceStack.peek());
	}
	
	private void consumeWhitespace() {
		while (isSpace())
			sourceStack.pop();
	}
	
	private boolean isNumber() {
		if (sourceStack.empty())
			return false;
		return Character.isDigit(sourceStack.peek()) ||
				sourceStack.peek() == '.';
	}
	
	private Token consumeNumber() {
		StringBuilder num = new StringBuilder();
		boolean point = false;
		
		while (isNumber()) {
			if (sourceStack.peek() == '.') {
				// We might have already had the decimal point
				// in which case this means we hit the end of the
				// number.
				if (point)
					break;
				point = true;
			}
			
			num.append(sourceStack.pop());
		}
		
		return new Token(TokenType.NUMBER, num.toString());
	}
	
	private boolean isIdent() {
		if (sourceStack.empty())
			return false;
		char c = sourceStack.peek();
		return (Character.isAlphabetic(c) || c == '_');
	}
	
	private Token consumeIdent() {
		StringBuilder ident = new StringBuilder();
		
		while (isIdent())
			ident.append(sourceStack.pop());
		
		// It is possible that this is the true or false literal
		// instead of an identifier.
		String str = ident.toString();
		if (str.compareTo("true") == 0)
			return new Token(TokenType.TRUE, str);
		else if (str.compareTo("false") == 0)
			return new Token(TokenType.FALSE, str);
		return new Token(TokenType.IDENTIFIER, str);
	}
	
	/**
	 * Get the next token in the text and consume it, moving on to the
	 * next token.
	 * @return The token that was found in the source.
	 * @throws SyntaxException Thrown when unable to identifiy a token.
	 */
	public Token advance() throws SyntaxException {
		// Skip any whitespace that could be at the front.
		consumeWhitespace();
		
		if (isNumber())
			return consumeNumber();
		else if (isIdent())
			return consumeIdent();
		else if (!sourceStack.empty()) {
			// All the operators use only a single character.
			switch (sourceStack.pop()) {
			case '+':
				return new Token(TokenType.PLUS, "+");
			case '-':
				return new Token(TokenType.MINUS, "-");
			case '*':
				return new Token(TokenType.STAR, "*");
			case '/':
				return new Token(TokenType.SLASH, "/");
			case '%':
				return new Token(TokenType.MOD, "%");
			case '^':
				return new Token(TokenType.CARROT, "^");
			case '&':
				return new Token(TokenType.AND, "&");
			case '|':
				return new Token(TokenType.OR, "|");
			case '=':
				return new Token(TokenType.EQLS, "=");
			case '<':
				// It is possible that this is a two-character token.
				if (!sourceStack.empty() &&
						sourceStack.peek() == '=') {
					sourceStack.pop();
					return new Token(TokenType.LESS_OR_EQLS, "<=");
				}
				return new Token(TokenType.LESS, "<");
			case '>':
				// It is possible that this is a two-character token.
				if (!sourceStack.empty() &&
						sourceStack.peek() == '=') {
					sourceStack.pop();
					return new Token(TokenType.GREATER_OR_EQLS, ">=");
				}
				return new Token(TokenType.GREATER, ">");
			case '!':
				// It is possible that this is a two-character token.
				if (!sourceStack.empty() &&
						sourceStack.peek() == '=') {
					sourceStack.pop();
					return new Token(TokenType.NOT_EQLS, "!=");
				}				
				return new Token(TokenType.NOT, "!");
			case '(':
				return new Token(TokenType.LEFT_PARENTHESIS, "(");
			case ')':
				return new Token(TokenType.RIGHT_PARENTHESIS, ")");
			case ',':
				return new Token(TokenType.COMMA, ",");
			default:
				throw new SyntaxException("Unidentified token!");
			}
		}
		else if (sourceStack.empty()) {
			return new Token(TokenType.EOF, "\0");
		}
		// If we get this far then we don't know what the token is.
		throw new SyntaxException("Unidentified token!");
	}
}
