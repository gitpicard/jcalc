# About

`jcalc` is a simple Java package that allows Java programs to parse and evaluate math expressions. It support custom
variables and functions meaning that you can provide data to the expressions that are evaluated.

# Usage

The full auto-generated docs are hosted [here](https://gitpicard.github.io/jcalc/). The quickest way to get up and
running is to use the `JCalculator` class. Call the `eval` method to run an expression and get the result. If you want
more control or want to change the behavior of an operator, you can implement the `ASTVisitor` class and write your
own behavior. The parser will generate an abstract syntax tree (AST) and `JCalculator` implements a visitor to evaluate
that tree. With `ASTVisitor`, you can implement your own evaluation environment.

# Example

Here is a short example that includes a custom variable and a custom function. Note, the function does not do any error
checking but it should throw `EvalException` if it is passed bad arguments.

```java
JCalculator calc = new JCalculator();
calc.setVariable("x", 15.7);
calc.setFunction("add", (args) -> args[0] + args[1]);
return calc.eval("5 * add(10, x) ^ 2");
```
