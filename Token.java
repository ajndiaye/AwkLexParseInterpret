

public class Token {
	

	 int LineNumber;

	 int cposition;
	
	 String index;
    TokenType tokentype;
    String TokenValue;


	
	
	public enum TokenType { //enum for token types
		Word, Number,Separator,While, If, Do, For, Break, Continue, Pattern, Else, Return, Begin, End, Print, Printf, Next, In, Delete, Getline, Stringliteral, Exit, Nextfile, Function, ADD_ASSIGN,MODULO_ASSIGN,
		ASSIGN, NOT_EQUAL, INCREMENT, DECREMENT, LESS_EQUAL, GREATER_EQUAL, EXPONENT_ASSIGN, BITWISE_OR, BITWISE_AND, NOT, LOGICAL_AND, LOGICAL_OR, APPEND, SUBTRACT_ASSIGN, MULTIPLY_ASSIGN, DIVIDE_ASSIGN, NO_MATCH, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, LEFT_PAREN, RIGHT_PAREN, DOLLARSIGN, TILDE, EQUAL, LESS_THAN, GREATER_THAN, PLUS, EXPONENT, QUESTIONMARK, MINUS, PERCENT, VERTICAL_BAR, COMMA, COLON, DIVIDE, TIMES,
	}
	//token constructor
	public Token(TokenType tokentype, int LineNumber, int cposition){
		this.tokentype = tokentype;
		this.LineNumber = LineNumber;
		this.cposition = cposition;	
	}
	
	//second token constructor
	public Token(TokenType tokentype,String TokenValue, int LineNumber, int cposition){
		this(tokentype, LineNumber,cposition);
		this.TokenValue= TokenValue;	
	}
	
	//getter for token value
	public String getValue() {
		return TokenValue;
	}
	
	
	//getter for token value
	public TokenType getTokenType() {
		return tokentype;
	}
	
	//method to display token type and value
	public String ToString() {
		return TokenValue +" (" + tokentype + ") " ;
		
	}
	

}
