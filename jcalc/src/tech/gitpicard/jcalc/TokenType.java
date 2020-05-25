package tech.gitpicard.jcalc;

/**
 * An enumeration of all the token types that
 * can be encountered in an expression.
 */
public enum TokenType {
	/** End of file, means there are no more tokens. */
	EOF,
	/** A real number literal. */
	NUMBER,
	/** An identifier for a variable or function call. */
	IDENTIFIER,
	/** The true literal. */
	TRUE,
	/** The false literal. */
	FALSE,
	/** The addition operator. */
	PLUS,
	/** The subtraction or negation operator. */
	MINUS,
	/** The multiplication operator. */
	STAR,
	/** The division operator. */
	SLASH,
	/** The modulus operator. */
	MOD,
	/** The power of operator. */
	CARROT,
	/** The or operator. */
	OR,
	/** The and operator. */
	AND,
	/** The equals operator. */
	EQLS,
	/** The less than operator. */
	LESS,
	/** The greater than operator. */
	GREATER,
	/** The less than or equals to operator. */
	LESS_OR_EQLS,
	/** The greater than or equals to operator. */
	GREATER_OR_EQLS,
	/** The not operator. */
	NOT,
	/** The not equals operator. */
	NOT_EQLS,
	/** The opening parenthesis. */
	LEFT_PARENTHESIS,
	/** The closing parenthesis. */
	RIGHT_PARENTHESIS,
	/** The comma operator. */
	COMMA
}
