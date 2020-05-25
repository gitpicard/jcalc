package tech.gitpicard.jcalc;

/**
 * Represents a literal in the stream of source text. Used
 * to parse an expression into a list of tokens which are built
 * into an abstract syntax tree.
 */
public class Token {
	
	private final TokenType type;
	private final String contents;
	
	/**
	 * Create a new token and set the data it is representing.
	 * @param type The type of token encountered.
	 * @param contents The plain-text token.
	 */
	public Token(TokenType type, String contents) {
		this.type = type;
		this.contents = contents;
	}
	
	/**
	 * The type of token represented by this object.
	 * @return A enumerator representing the token type.
	 */
	public TokenType getType() {
		return type;
	}
	
	/**
	 * Get a plain-text string storing the token literal.
	 * @return The plain-text for this token.
	 */
	public String getContents() {
		return contents;
	}
}
