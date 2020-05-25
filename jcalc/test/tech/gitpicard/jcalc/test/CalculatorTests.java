package tech.gitpicard.jcalc.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import tech.gitpicard.jcalc.EvalException;
import tech.gitpicard.jcalc.JCalculator;
import tech.gitpicard.jcalc.SyntaxException;

class CalculatorTests {
	
	// Helper function to go from boolean to double.
	double bool(boolean b) {
		return b ? 1.0 : 0.0;
	}
	
	@Test
	void testLiteral() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("5"), 5.0);
	}
	
	@Test
	void testIdent() throws SyntaxException, EvalException {
		JCalculator cal = new JCalculator();
		cal.setVariable("test", 5);
		assertEquals(cal.eval("test"), cal.getVariable("test"));
	}
	
	@Test
	void testBinary1() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 + 5.6"), 12.3 + 5.6);
	}
	
	@Test
	void testBinary2() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12 - 45"), 12 - 45);
	}
	
	@Test
	void testBinary3() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("1 * 5.6"), 1 * 5.6);
	}
	
	@Test
	void testBinary4() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 / 5.6"), 12.3 / 5.6);
	}
	
	@Test
	void testBinary5() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 ^ 5.6"),
				Math.pow(12.3, 5.6));
	}
	
	@Test
	void testBinary6() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12 % 5"), 12 % 5);
	}
	
	@Test
	void testBinary7() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 = 5"),
				bool(12.3 == 5));
	}
	
	@Test
	void testBinary8() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 != 5"), 
				bool(12.3 != 5));
	}
	
	@Test
	void testBinary9() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 < 5"),
				bool(12.3 < 5));
	}
	
	@Test
	void testBinary10() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 > 5"),
				bool(12.3 > 5));
	}
	
	@Test
	void testBinary11() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 <= 5"),
				bool(12.3 <= 5));
	}
	
	@Test
	void testBinary12() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12.3 >= 5"),
				bool(12.3 >= 5));
	}
	
	@Test
	void testUnary1() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("-5"), -5);
	}
	
	@Test
	void testUnary2() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("--10"), -(-10));
	}
	
	@Test
	void testUnary3() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("!5"), 0);
	}
	
	@Test
	void testUnary4() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("!!5"), 1);
	}
	
	@Test
	void testUnary5() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("!0"), 1);
	}
	
	@Test
	void testComplex1() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12 * (5 - 6)"),
				12 * (5 - 6));
	}
	
	@Test
	void testComplex2() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12 ^ (5 - 2)"),
				Math.pow(12, 5 - 2));
	}
	
	@Test
	void testComplex3() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12 * 5 - 6"),
				12 * 5 - 6);
	}
	
	@Test
	void testComplex4() throws SyntaxException, EvalException {
		assertEquals(new JCalculator().eval("12 ^ 5 - 6"),
				Math.pow(12, 5) - 6);
	}
	
	@Test
	void testFunction1() throws SyntaxException, EvalException {
		JCalculator cal = new JCalculator();
		cal.setFunction("times_two", (args) -> args[0] * 2);
		assertEquals(cal.eval("times_two(5)"), 5 * 2);
	}
	
	@Test
	void testFunction2() throws SyntaxException, EvalException {
		JCalculator cal = new JCalculator();
		cal.setFunction("add", (args) -> args[0] + args[1]);
		assertEquals(cal.eval("add(1, 2)"), 1 + 2);
	}
	
	@Test
	void testFunctionWithExpression()
			throws SyntaxException, EvalException {
		JCalculator cal = new JCalculator();
		cal.setFunction("add", (args) -> args[0] + args[1]);
		assertEquals(cal.eval("5 + add(1, 2 * 2)"), 5 + (1 + 2 * 2));
	}
	
	@Test
	void testSyntaxError1() {
		assertThrows(SyntaxException.class, () ->
			new JCalculator().eval("12 +"));
	}
	
	@Test
	void testSyntaxError2() {
		assertThrows(SyntaxException.class, () ->
			new JCalculator().eval("+ 5"));
	}
	
	@Test
	void testSyntaxError3() {
		assertThrows(SyntaxException.class, () ->
			new JCalculator().eval("**"));
	}
	
	@Test
	void testSyntaxError4() {
		assertThrows(SyntaxException.class, () ->
			new JCalculator().eval("12 ** 3"));
	}
	
	@Test
	void testSyntaxError5() {
		assertThrows(SyntaxException.class, () ->
			new JCalculator().eval("1 5"));
	}
	
	@Test
	void testSyntaxError6() {
		assertThrows(SyntaxException.class, () ->
			new JCalculator().eval("5)"));
	}
	
	@Test
	void testSyntaxError7() {
		assertThrows(SyntaxException.class, () ->
		new JCalculator().eval("function("));
	}
	
	@Test
	void testSyntaxError8() {
		assertThrows(SyntaxException.class, () ->
		new JCalculator().eval("function(5"));
	}
	
	@Test
	void testSyntaxError9() {
		assertThrows(SyntaxException.class, () ->
		new JCalculator().eval("function(5,"));
	}
	
	@Test
	void testSyntaxError10() {
		assertThrows(SyntaxException.class, () ->
		new JCalculator().eval("function(9,)"));
	}
	
	@Test
	void testEvalError() {
		assertThrows(EvalException.class, () ->
		new JCalculator().eval("no"));
	}
}
