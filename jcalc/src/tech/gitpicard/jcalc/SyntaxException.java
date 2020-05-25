package tech.gitpicard.jcalc;

/**
 * An exception caused by illegal expression syntax.
 */
public class SyntaxException extends Exception {
	/**
	 * Create a new exception resulting from an illegal expression
	 * being passed in.
	 * @param s A short error message.
	 */
	public SyntaxException(String s) {
		super(s);
	}
}
