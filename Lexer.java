import java.util.LinkedList;


import java.util.HashMap;

public class Lexer {

	StringHandler stringhandler;
	int LineNumber;
	int cposition;
	char CurrentChar;
	HashMap<String, Token.TokenType> keywordMap = new HashMap<String, Token.TokenType>();
    HashMap<String, Token.TokenType> twoCharSymbol = new HashMap<String, Token.TokenType>();
    HashMap<String, Token.TokenType> oneCharSymbol = new HashMap<String, Token.TokenType>();
	LinkedList<Token> tokenList;
	private int Doclength;

	 
	//lexer constructor
	public Lexer (String Doc) {
		this.tokenList = new LinkedList<Token>();
		this.stringhandler = new StringHandler(Doc);
		this.cposition = 0;
		this.Doclength = Doc.length();
		this.LineNumber = 1;
        keywordMap.put("while", Token.TokenType.While);
        keywordMap.put("if", Token.TokenType.If);
        keywordMap.put("do", Token.TokenType.Do);
        keywordMap.put("for", Token.TokenType.For);
        keywordMap.put("break", Token.TokenType.Break);
        keywordMap.put("continue", Token.TokenType.Continue);
        keywordMap.put("else", Token.TokenType.Else);
        keywordMap.put("return", Token.TokenType.Return);
        keywordMap.put("BEGIN", Token.TokenType.Begin);
        keywordMap.put("END", Token.TokenType.End);
        keywordMap.put("print", Token.TokenType.Print);
        keywordMap.put("printf", Token.TokenType.Printf);
        keywordMap.put("next", Token.TokenType.Next);
        keywordMap.put("in", Token.TokenType.In);
        keywordMap.put("delete", Token.TokenType.Delete);
        keywordMap.put("getline", Token.TokenType.Getline);
        keywordMap.put("exit", Token.TokenType.Exit);
        keywordMap.put("nextfile", Token.TokenType.Nextfile);
        keywordMap.put("function", Token.TokenType.Function);
        twoCharSymbol.put("==", Token.TokenType.ASSIGN);
        twoCharSymbol.put("!=", Token.TokenType.NOT_EQUAL);
        twoCharSymbol.put("+=", Token.TokenType.ADD_ASSIGN);
        twoCharSymbol.put("++", Token.TokenType.INCREMENT);
        twoCharSymbol.put("--", Token.TokenType.DECREMENT);
        twoCharSymbol.put("<=", Token.TokenType.LESS_EQUAL);
        twoCharSymbol.put(">=", Token.TokenType.GREATER_EQUAL);
        twoCharSymbol.put("^=", Token.TokenType.EXPONENT_ASSIGN);
        twoCharSymbol.put("|=", Token.TokenType.BITWISE_OR);
        twoCharSymbol.put("&=", Token.TokenType.BITWISE_AND);
        twoCharSymbol.put("&&", Token.TokenType.LOGICAL_AND);
        twoCharSymbol.put("||", Token.TokenType.LOGICAL_OR);
        twoCharSymbol.put("/=", Token.TokenType.DIVIDE_ASSIGN);
        twoCharSymbol.put("%=", Token.TokenType.MODULO_ASSIGN);
        twoCharSymbol.put("~!", Token.TokenType.NO_MATCH);
        twoCharSymbol.put("*=", Token.TokenType.MULTIPLY_ASSIGN);
        twoCharSymbol.put("-=", Token.TokenType.SUBTRACT_ASSIGN);
        twoCharSymbol.put(">>", Token.TokenType.APPEND);
        oneCharSymbol.put("{", Token.TokenType.LEFT_BRACE);
        oneCharSymbol.put("}", Token.TokenType.RIGHT_BRACE);
        oneCharSymbol.put("[", Token.TokenType.LEFT_BRACKET);
        oneCharSymbol.put("]", Token.TokenType.RIGHT_BRACKET);
        oneCharSymbol.put("(", Token.TokenType.LEFT_PAREN);
        oneCharSymbol.put(")", Token.TokenType.RIGHT_PAREN);
        oneCharSymbol.put("$", Token.TokenType.DOLLARSIGN);
        oneCharSymbol.put("~", Token.TokenType.TILDE);
        oneCharSymbol.put("=", Token.TokenType.EQUAL);
        oneCharSymbol.put("<", Token.TokenType.LESS_THAN);
        oneCharSymbol.put(">", Token.TokenType.GREATER_THAN);
        oneCharSymbol.put("!", Token.TokenType.NOT);
        oneCharSymbol.put("+", Token.TokenType.PLUS);
        oneCharSymbol.put("^", Token.TokenType.EXPONENT);
        oneCharSymbol.put("-", Token.TokenType.MINUS);
        oneCharSymbol.put("?", Token.TokenType.QUESTIONMARK);
        oneCharSymbol.put(":", Token.TokenType.COLON);
        oneCharSymbol.put("*", Token.TokenType.TIMES);
        oneCharSymbol.put("/", Token.TokenType.DIVIDE);
        oneCharSymbol.put("%", Token.TokenType.PERCENT);
        oneCharSymbol.put("|", Token.TokenType.VERTICAL_BAR);
        oneCharSymbol.put(",", Token.TokenType.COMMA);
        
        
		
	}
	



	
	 public char Peek(int i) {
	        return stringhandler.Peek(i);
	    }

	    public String PeekString(int i) {
	        return stringhandler.PeekString(i);
	    }

	    public char GetChar() {
	        char nextChar = stringhandler.GetChar(); //calls Stringhandler to get next character
	        if (nextChar == '\n') {
	            LineNumber++; // new line gets counted
	            cposition = 0; //resets  character position on new line
	        } else if (nextChar == '\r') {
	            // Ignore carriage return
	        } else {
	        	cposition++; //update character position
	        }
	        return nextChar;
	    }

	    public void Swallow(int i) {
	        stringhandler.Swallow(i);
	        cposition += i; // sums character position and current index
	    }


	    public String Remainder() {
	        return stringhandler.Remainder();
	    }
	    
	    public boolean IsDone() {
	    	return cposition >= Doclength;
	    }
	    
	    public void lex() {
	    	while(!IsDone()) {
	    	 CurrentChar = Peek(0);	
	    	 if(CurrentChar == ' ' || CurrentChar == '\t') { //checks if current character is space or tab
	    		 GetChar();
	    		 cposition++;
	    	 }else if (CurrentChar == '\n') { // checks if character is new line
	    		  Token Septoken = new Token(Token.TokenType.Separator, " " , LineNumber, cposition); // create separator token
	    		  tokenList.add(Septoken); // add separator token to list
	              GetChar();
	              LineNumber++; // new live
	              cposition = 0; // reset character position for new line
	    	 }  else if (CurrentChar == '\r') { //ignore carriage return
	             GetChar();
	             cposition++;
	         } else if (Character.isLetter(CurrentChar) || CurrentChar == '_') { //checks of next character is letter or underscore
	             Token wordtoken = ProcessWord(); // process the word
	             cposition++;
	             tokenList.add(wordtoken); // add word to list
	             GetChar();
	         } else if (Character.isDigit(CurrentChar) || CurrentChar == '.') { // checks if character is number or decimal point
	             Token numberToken = ProcessNumber();// process number
	             cposition++;
	             tokenList.add(numberToken);// add it to the linked list
	             GetChar();
	         }else if(CurrentChar== '#') {
	        	 
	        	 while(CurrentChar == '#' && Peek(cposition+1) != '\n') {
	        	GetChar();
	        	Token patterntoken = HandlePattern();
	        	tokenList.add(patterntoken);
	       	   //put a loop here or something 
	        		 
	        	 }
	         }else if(CurrentChar == '"') {
	        	 Token StringLiteral = HandleStringLiteral();
	        	 tokenList.add(StringLiteral);
	        	 GetChar();
	         }else if(CurrentChar == '>' || CurrentChar == '=' ||CurrentChar == '+' ||CurrentChar == '-' ||CurrentChar == '<' ||CurrentChar == '!' ||CurrentChar == '^' ||CurrentChar == '%' ||CurrentChar == '*' ||CurrentChar == '/' ||CurrentChar == '!' ||CurrentChar == '~' ||CurrentChar == '&' ||CurrentChar == '|' ||CurrentChar == '{' ||CurrentChar == '}' ||CurrentChar == '[' ||CurrentChar == ']' ||CurrentChar == '(' ||CurrentChar == ')' ||CurrentChar == '$' ||CurrentChar == '?' ||CurrentChar == ':' ||CurrentChar == ',' ) {
	        	Token symboltoken = ProcessSymbol(); 
	        	tokenList.add(symboltoken);
	        	GetChar();
	         }         
	         else {
	             // Throw an exception for unrecognized characters
	             throw new RuntimeException("Unrecognized character at line " + LineNumber + ", position " + cposition);
	         }
	    	}	 
	    }
	public Token HandlePattern() {
		StringBuffer backtick = new StringBuffer();
		int positionindex = 1;
		CurrentChar = Peek(positionindex);
		
	
		while(CurrentChar != '`') {
			backtick.append(CurrentChar);
			positionindex++;
			CurrentChar=Peek(positionindex);			
		}
		
		if(CurrentChar == '`') {
			positionindex++;
			Token pattern =  new Token(Token.TokenType.Pattern, backtick.toString(), LineNumber, cposition);
			return pattern;
		}else {
			return null;
		}
	}
	
   
	public Token HandleStringLiteral() {
		StringBuffer quote = new StringBuffer();
		int positionindex = 1;
		CurrentChar = Peek(positionindex);
		
		while(CurrentChar != '"') {
			quote.append(CurrentChar);
			positionindex++;
			CurrentChar = Peek(positionindex);
		}
		if(CurrentChar == '"') {
			positionindex++;
			CurrentChar= Peek(positionindex);
			Token stringliteral = new Token(Token.TokenType.Stringliteral, quote.toString(), LineNumber, cposition);
	        return stringliteral;
		}else {
			return null;
		}
	}

	public Token ProcessWord() {
		StringBuilder worditem = new StringBuilder(); // new string builder object
		int positionindex=1;
		CurrentChar = Peek(0); // looks at current character
		
		while((Character.isLetter(CurrentChar) || CurrentChar == '_')){ // checks for letters and underscore
			worditem.append(CurrentChar);// adds character to string being built
			CurrentChar= Peek(positionindex);
			GetChar();
		} 
		
		    Token.TokenType hashmapToken =  keywordMap.get(worditem.toString());
		    
		    if(hashmapToken != null) {
		    
		    return new Token(hashmapToken, " ", LineNumber, cposition );
		    } else {
		   Token wordtoken = new Token(Token.TokenType.Word, worditem.toString(), LineNumber, cposition); // creates new word token
		 
			return wordtoken; //return new word token
	}
	}
	
	public Token ProcessSymbol() {

		String PeekedString = PeekString(2);
		Token.TokenType SymbolToken = twoCharSymbol.get(PeekedString);
		if(SymbolToken != null) {
			cposition++;
			Token symboltoken = new Token(SymbolToken, LineNumber, cposition);
			cposition++;
			GetChar();
			return symboltoken;
		}else {
		PeekedString = PeekString(1);
		SymbolToken = oneCharSymbol.get(PeekedString);
		if(SymbolToken != null) {
			Token symboltoken = new Token(SymbolToken, " ", LineNumber, cposition);
			GetChar();
			return symboltoken;
		}else {
			return null;
		}
		}
	}
	
	public Token ProcessNumber() {
		StringBuilder numberitem = new StringBuilder(); // new stringbuilder		
	   int positionindex=1;
		CurrentChar = Peek(0); // look at current character
		
		while(Character.isDigit(CurrentChar)) { // checks if character is number
			numberitem.append(CurrentChar); // adds character to String being built		
			CurrentChar= Peek(positionindex);
			GetChar();
		    if(CurrentChar == '.') { //checks to see if character is decimal
			numberitem.append(CurrentChar);
			GetChar();		
			}	
		}
		
		
		return new Token(Token.TokenType.Number,numberitem.toString(), LineNumber, cposition);
		}

	public LinkedList<Token> getTokenList() {
		
		return tokenList;
	}
}
