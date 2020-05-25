package tech.gitpicard.jcalc;

/**
 * A enumeration of the possible mathematical operations that
 * can be performed with operators and not function calls.
 */
public enum Operation {
	/** Addition of two values. */
	ADD,
	/** Subtraction of two values. */
	SUB,
	/** Multiplication of two values. */
	MULT,
	/** Division of two values. */
	DIV,
	/** Raise the left value to the power of the right value. */
	POW,
	/** Modulus of two values. */
	MOD,
	/** Are two values equal? */
	EQLS,
	/** Is the left side less than the right side? */
	LESS,
	/** Is the left side greater than the right side? */
	GREATER,
	/** Is the left side less than or equal to the right side? */
	LESS_OR_EQLS,
	/** Is the left side greater than or equal to the right side? */
	GREATER_OR_EQLS,
	/** Negate the value. Flips the sign of the value. */
	NEGATE,
	/** Inverts the truth value. */
	NOT,
	/** Is the left side not equal to the right? */
	NOT_EQLS
}
