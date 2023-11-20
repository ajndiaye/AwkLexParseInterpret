import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testParseIncrementDecrement() {
	LinkedList<Token> token = new LinkedList<>();
	token.add(new Token(Token.TokenType.Word, "a", 0, 0));
	token.add(new Token(Token.TokenType.INCREMENT, "--", 0, 0));
	Parser parser = new Parser(token);
	Optional<Node>result = parser.ParsePostIncrementDecrement();
	assertTrue(result.isPresent());
	assertTrue(result.get() instanceof MathOpNode);
	}


	@Test
	public void testParseExponentiation() {
	LinkedList<Token> token = new LinkedList<>();
	token.add(new Token(Token.TokenType.Number, "3", 0, 0));
	token.add(new Token(Token.TokenType.EXPONENT, "^", 0, 0));
	token.add(new Token(Token.TokenType.Number, "3", 0, 0));
	Parser parser = new Parser(token);
	Optional<Node>result = parser.ParseExponentiation();
	assertTrue(result.isPresent());
	assertTrue(result.get() instanceof MathOpNode);
	MathOpNode op = (MathOpNode) result.get();
	assertEquals(OperationNode.Operation.EXPONENT, op.getOperation());
	}

	@Test
	public void testFactor() {
	LinkedList<Token> token = new LinkedList<>();
	token.add(new Token(Token.TokenType.Number, "39", 0, 0));
	Parser parser = new Parser(token);
	Optional<Node>result = parser.Factor();
	assertTrue(result.isPresent());
	assertTrue(result.get() instanceof ConstantNode);
	assertEquals("39", ((ConstantNode) result.get()).getValue());
	}
	
	@Test
	public void testTerm() {
	LinkedList<Token> token = new LinkedList<>();
	token.add(new Token(Token.TokenType.Number, "3", 0, 0));
	token.add(new Token(Token.TokenType.TIMES, "*", 0, 0));
	token.add(new Token(Token.TokenType.Number, "3", 0, 0));
	Parser parser = new Parser(token);
	Optional<Node>result = parser.Term();
	assertTrue(result.isPresent());
	assertTrue(result.get() instanceof MathOpNode);
	MathOpNode op = (MathOpNode) result.get();
	assertEquals(OperationNode.Operation.MULTIPLY, op.getOperation());	
	
	LinkedList<Token> token1 = new LinkedList<>();
	token1.add(new Token(Token.TokenType.Number, "4", 0, 0));
	Parser parser1 = new Parser(token1);
	Optional<Node>result1 = parser1.Term();
	assertTrue(result1.isPresent());
	assertTrue(result1.get() instanceof ConstantNode);
	ConstantNode constant = (ConstantNode) result.get();
	assertEquals("4", constant.getValue());
	}
	
	@Test
	public void testExpression() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.Number, "3", 0, 0));
		token.add(new Token(Token.TokenType.PLUS, "+", 0, 0));
		token.add(new Token(Token.TokenType.Number, "3", 0, 0));
		Parser parser = new Parser(token);
		Optional<Node>result = parser.Expression();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MathOpNode);
		MathOpNode op = (MathOpNode) result.get();
		assertEquals(OperationNode.Operation.ADD, op.getOperation());		
	}
	
	@Test
	public void testParseBooleanCompare(){
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.Number, "9", 0, 0));
		token.add(new Token(Token.TokenType.GREATER_THAN, ">", 0, 0));
		token.add(new Token(Token.TokenType.Number, "3", 0, 0));	
		Parser parser = new Parser(token);
		Optional<Node>result = parser.ParseBooleanCompare();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MathOpNode);
		MathOpNode op = (MathOpNode) result.get();
		assertEquals(OperationNode.Operation.GT, op.getOperation());
	}
	
	@Test
	public void testParseMatch() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.Word, "c", 0, 0));
		token.add(new Token(Token.TokenType.TILDE, "~", 0, 0));
		token.add(new Token(Token.TokenType.Number, "33", 0, 0));	
		Parser parser = new Parser(token);
		Optional<Node>result = parser.ParseMatch();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MathOpNode);
		MathOpNode op = (MathOpNode) result.get();
		assertEquals(OperationNode.Operation.MATCH, op.getOperation());
		
	}
	
	@Test
	public void testParseArray() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.Number, "8", 0, 0));
		token.add(new Token(Token.TokenType.LEFT_BRACKET, "[", 0, 0));
		token.add(new Token(Token.TokenType.Number, "33", 0, 0));
		token.add(new Token(Token.TokenType.TILDE, "]", 0, 0));
		Parser parser = new Parser(token);
		Optional<Node>result = parser.ParseArray();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof OperationNode);
		OperationNode op = (OperationNode) result.get();
		assertEquals(OperationNode.Operation.IN_Array, op.getOperation());
		
		LinkedList<Token> token1 = new LinkedList<>();
		token1.add(new Token(Token.TokenType.Word, "8", 0, 0));
		token1.add(new Token(Token.TokenType.LEFT_PAREN, "[", 0, 0));
		token1.add(new Token(Token.TokenType.RIGHT_PAREN, "33", 0, 0));
		Parser parser1 = new Parser(token);
		Optional<Node>result1 = parser1.ParseArray();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof OperationNode);
		OperationNode op1 = (OperationNode) result1.get();
		assertEquals(OperationNode.Operation.IN_MultiDArray, op1.getOperation());
		
	}
	
	@Test
	public void testLogicalAND() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.Word, "c", 0, 0));
		token.add(new Token(Token.TokenType.LOGICAL_AND, "&&", 0, 0));
		token.add(new Token(Token.TokenType.Number, "d", 0, 0));	
		Parser parser = new Parser(token);
		Optional<Node>result = parser.ParseLogicalAND();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MathOpNode);
		MathOpNode op = (MathOpNode) result.get();
		assertEquals(OperationNode.Operation.AND, op.getOperation());		
	}


	@Test
	public void testParseif() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.If, " ", 0, 0));
		token.add(new Token(Token.TokenType.LEFT_PAREN, "(", 0, 0));
		token.add(new Token(Token.TokenType.Word, "True", 0, 0));	
		token.add(new Token(Token.TokenType.RIGHT_PAREN, ")", 0, 0));	
		Parser parser = new Parser(token);
		Optional<StatementNode>result = parser.ParseIf();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof IfNode);	
	}

	@Test
	public void testParseWhile() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.While, " ", 0, 0));
		token.add(new Token(Token.TokenType.LEFT_PAREN, "(", 0, 0));
		token.add(new Token(Token.TokenType.Word, "True", 0, 0));	
		token.add(new Token(Token.TokenType.RIGHT_PAREN, ")", 0, 0));	
		token.add(new Token(Token.TokenType.LEFT_BRACE, "{", 0, 0));	
		token.add(new Token(Token.TokenType.RIGHT_BRACE, "}", 0, 0));		
		Parser parser = new Parser(token);
		Optional<StatementNode>result = parser.ParseWhile();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof WhileNode);
	}
	
	@Test
	public void testParseFor() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.For, " ", 0, 0));
		token.add(new Token(Token.TokenType.LEFT_PAREN, "(", 0, 0));
		token.add(new Token(Token.TokenType.Word, "int", 0, 0));
		token.add(new Token(Token.TokenType.Word, "i", 0, 0));	
		token.add(new Token(Token.TokenType.EQUAL, "=", 0, 0));	
		token.add(new Token(Token.TokenType.Number, "0", 0, 0));	
		token.add(new Token(Token.TokenType.Separator, " ", 0, 0));
		token.add(new Token(Token.TokenType.Word, "i", 0, 0));	
		token.add(new Token(Token.TokenType.LESS_THAN, "<", 0, 0));
		token.add(new Token(Token.TokenType.Number, "10", 0, 0));	
		token.add(new Token(Token.TokenType.Separator, " ", 0, 0));
		token.add(new Token(Token.TokenType.Word, "i", 0, 0));
		token.add(new Token(Token.TokenType.INCREMENT, "++", 0, 0));
		token.add(new Token(Token.TokenType.RIGHT_PAREN, ")", 0, 0));	
		Parser parser = new Parser(token);
		Optional<StatementNode>result = parser.ParseFor();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof ForNode);
		
	}

	
	@Test
	public void testParseContinueandBreak() {
		LinkedList<Token> token = new LinkedList<>();	
		token.add(new Token(Token.TokenType.Continue, " ", 0, 0));
		Parser parser = new Parser(token);
		Optional<StatementNode>result = parser.ParseContinueandBreak();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof ContinueNode);
		LinkedList<Token> token1 = new LinkedList<>();	
		token1.add(new Token(Token.TokenType.Break, " ", 0, 0));
		Parser parser1 = new Parser(token1);
		Optional<StatementNode>result1 = parser1.ParseContinueandBreak();
		assertTrue(result1.isPresent());
		assertTrue(result1.get() instanceof BreakNode);
	}

	
	@Test
	public void testParseFunctionCalls() {
		LinkedList<Token> token = new LinkedList<>();	
		token.add(new Token(Token.TokenType.Word, "Encouragement", 0, 0));
		token.add(new Token(Token.TokenType.LEFT_PAREN, "(", 0, 0));
		token.add(new Token(Token.TokenType.Word, "Nice", 0, 0));
		token.add(new Token(Token.TokenType.COMMA, ",", 0, 0));	
		token.add(new Token(Token.TokenType.Word, "Code", 0, 0));
		token.add(new Token(Token.TokenType.COMMA, ",", 0, 0));	
		token.add(new Token(Token.TokenType.Word, "Bud", 0, 0));
		token.add(new Token(Token.TokenType.RIGHT_PAREN, ")", 0, 0));	
		Parser parser = new Parser(token);
		Optional<Node>result = parser.ParseFunctionCall();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof CallFunctionNode);
		CallFunctionNode node = (CallFunctionNode) result.get();
		assertEquals("Encouragement", node.getName());
	}


	@Test
	public void testParseReturn() {
		LinkedList<Token> token = new LinkedList<>();
		token.add(new Token(Token.TokenType.Return, 0, 0));
		token.add(new Token(Token.TokenType.Number, "10", 0, 0));	
		Parser parser = new Parser(token);
		Optional<StatementNode>result = parser.ParseReturn();
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof ReturnNode);
		ReturnNode Value = (ReturnNode) result.get();
		assertTrue(Value.getReturn().isPresent());
	}













}
