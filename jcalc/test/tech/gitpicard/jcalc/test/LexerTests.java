package tech.gitpicard.jcalc.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import tech.gitpicard.jcalc.Lexer;
import tech.gitpicard.jcalc.SyntaxException;
import tech.gitpicard.jcalc.Token;
import tech.gitpicard.jcalc.TokenType;

public class LexerTests {

	// Helper method to reduce code copy-pasting. Checks if the
	// source contains the right token.
	private void testToken(String src, TokenType t, String s)
			throws SyntaxException {
		Lexer lex = new Lexer(src);
		Token tok = lex.advance();
		
		assertEquals(t, tok.getType());
		assertEquals(s, tok.getContents());
	}
	
	@Test
	public void testEmpty() throws SyntaxException {
		testToken("", TokenType.EOF, "\0");
	}
	
	@Test
	public void testInt() throws SyntaxException {
		testToken("1", TokenType.NUMBER, "1");
	}
	
	@Test
	public void testIntComplex() throws SyntaxException {
		testToken("56789", TokenType.NUMBER, "56789");
	}
	
	@Test
	public void testIntWithWhitespace() throws SyntaxException {
		testToken("\t 10\t   ", TokenType.NUMBER, "10");
	}
	
	@Test
	public void testIdent() throws SyntaxException {
		testToken("x", TokenType.IDENTIFIER, "x");
	}
	
	@Test
	public void testIdentComplex() throws SyntaxException {
		testToken("hello_world", TokenType.IDENTIFIER, "hello_world");
	}
	
	@Test
	public void testIdentWithWhitespace() throws SyntaxException {
		testToken("  \tabc \t", TokenType.IDENTIFIER, "abc");
	}
	
	@Test
	public void testTrue() throws SyntaxException {
		testToken("true", TokenType.TRUE, "true");
	}
	
	@Test
	public void testFalse() throws SyntaxException {
		testToken("false", TokenType.FALSE, "false");
	}
	
	@Test
	public void testOperators() throws SyntaxException {
		testToken("+", TokenType.PLUS, "+");
		testToken("-", TokenType.MINUS, "-");
		testToken("*", TokenType.STAR, "*");
		testToken("/", TokenType.SLASH, "/");
		testToken("%", TokenType.MOD, "%");
		testToken("^", TokenType.CARROT, "^");
		testToken("&", TokenType.AND, "&");
		testToken("|", TokenType.OR, "|");
		testToken("=", TokenType.EQLS, "=");
		testToken("<", TokenType.LESS, "<");
		testToken(">", TokenType.GREATER, ">");
		testToken("<=", TokenType.LESS_OR_EQLS, "<=");
		testToken(">=", TokenType.GREATER_OR_EQLS, ">=");
		testToken("!", TokenType.NOT, "!");
		testToken("!=", TokenType.NOT_EQLS, "!=");
		// Parenthesis are not actually operators but it makes sense
		// to put them here.
		testToken("(", TokenType.LEFT_PARENTHESIS, "(");
		testToken(")", TokenType.RIGHT_PARENTHESIS, ")");
		testToken(",", TokenType.COMMA, ",");
	}
	
	@Test
	public void testExpression() throws SyntaxException {
		Lexer lex = new Lexer("5*(3^4+7) / mod(10)");
		// Don't check the contents for each one because the tests
		// above already verify that the contents work.
		assertEquals(TokenType.NUMBER, lex.advance().getType());
		assertEquals(TokenType.STAR, lex.advance().getType());
		assertEquals(TokenType.LEFT_PARENTHESIS,
				lex.advance().getType());
		assertEquals(TokenType.NUMBER, lex.advance().getType());
		assertEquals(TokenType.CARROT, lex.advance().getType());
		assertEquals(TokenType.NUMBER, lex.advance().getType());
		assertEquals(TokenType.PLUS, lex.advance().getType());
		assertEquals(TokenType.NUMBER, lex.advance().getType());
		assertEquals(TokenType.RIGHT_PARENTHESIS,
				lex.advance().getType());
		assertEquals(TokenType.SLASH, lex.advance().getType());
		assertEquals(TokenType.IDENTIFIER, lex.advance().getType());
		assertEquals(TokenType.LEFT_PARENTHESIS,
				lex.advance().getType());
		assertEquals(TokenType.NUMBER, lex.advance().getType());
		assertEquals(TokenType.RIGHT_PARENTHESIS,
				lex.advance().getType());
		assertEquals(TokenType.EOF, lex.advance().getType());
	}
	
	@Test
	public void testIllegalArgument() {
		assertThrows(IllegalArgumentException.class,
				() -> new Lexer(null));
	}
	
	@Test
	public void testIllegalToken() {
		assertThrows(SyntaxException.class, () -> {
			Lexer lex = new Lexer("this is illegal -> #");
			while (lex.advance().getType() != TokenType.EOF);
		});
	}
}
