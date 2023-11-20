import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;



public class Parser {
private TokenManager tokenmanager; //token manager instance that will be used in this class

public Parser(LinkedList<Token> tokens) {
	this.tokenmanager = new TokenManager(tokens);   // constructor
}


public boolean AcceptSeperators() {
	boolean Separator = false; //sets boolean defaul to false
	while(tokenmanager.MoreTokens()) { // loops while there are still tokens in the token manager
		Optional<Token> NextToken= tokenmanager.Peek(0); //peeks at token to come
		if(NextToken.get().getTokenType()== Token.TokenType.Separator) { // checks if peeked token is separator
		tokenmanager.MatchAndRemove(NextToken.get().getTokenType());	// remove the separator
		Separator= true;	 // update boolean to true
		} 
	}
   return Separator;
}


public ProgramNode parse() {
ProgramNode programnode = new ProgramNode(null, null, null, null); // new programNode object

while(tokenmanager.MoreTokens()) {
	if(ParseFunction(programnode) || ParseAction(programnode)) { //checks if parsing occured with any issues
      //successfull parsing no issues            
	}else { throw new RuntimeException("Error while parsing");
	}
}
return programnode;
}


private boolean ParseAction(ProgramNode programnode) {
	boolean begin = false; //blocknode location  flag
	boolean end = false;
	
	if(tokenmanager.MatchAndRemove(Token.TokenType.Begin).isPresent()) {  //checks if is begin block node
		begin = true;
	}
	if(tokenmanager.MatchAndRemove(Token.TokenType.End).isPresent()) {//checks if is endblock node
		end = true;
	}
	
	if(!begin && !end) { //if neither, call methods below
		ParseOperation(); 
		ParseBlock();
	}
	
	Optional<Token> TokenPeek = tokenmanager.Peek(0); //peeek at next token
	if(TokenPeek.get().getTokenType()== Token.TokenType.Begin) { //checks if its begin token type
		BlockNode BBlock= ParseBlock(); // parse block
		programnode.addBBlock(BBlock); // add to programnode tree
		return true; 
	}else if(TokenPeek.get().getTokenType()== Token.TokenType.End) { //checks if its end token type
		BlockNode EBlock= ParseBlock(); // parse block
		programnode.addEBlock(EBlock); // add to programnode tree
		return true;
	}else {
		ParseOperation();
		ParseBlock();
	}
	
	return false; // no action 
}
  
public Optional<Node>ParseBottomLevel(){
	Optional<Token>TokenPeek = tokenmanager.Peek(0); //peeks at current token in token list
	if( TokenPeek.get().getTokenType() == Token.TokenType.Stringliteral) { //checks if token is string literal
		String Value = TokenPeek.get().getValue(); // extract value
		tokenmanager.MatchAndRemove(Token.TokenType.Stringliteral);	//remove token from token list
		return Optional.of(new ConstantNode(Value)); //create and return a new constant Node
	}else if( TokenPeek.get().getTokenType() == Token.TokenType.Number) { //checks if token is number
		String Value = TokenPeek.get().getValue();// extract value
		tokenmanager.MatchAndRemove(Token.TokenType.Number); //remove token from list
		return Optional.of(new ConstantNode(Value));// return and return constant node
	}else if( TokenPeek.get().getTokenType() == Token.TokenType.Pattern) {  //checks if token is Pattern
		String Value = TokenPeek.get().getValue();// extract Value
		tokenmanager.MatchAndRemove(Token.TokenType.Pattern); // remove token from token list
		return Optional.of(new PatternNode(Value)); // create and return new Pattern Node
	}else if(TokenPeek.get().getTokenType() == Token.TokenType.LEFT_PAREN) { // checks if token is left parenthesis
		//tokenmanager.MatchAndRemove(Token.TokenType.LEFT_PAREN); // remove token from list
		Optional<Node> ans= ParseOperation(); // parse operation
		tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_PAREN); // remove token from list
		return ans;		 // return parse op answer
	}else if(TokenPeek.get().getTokenType() == Token.TokenType.NOT) { //checks if token is not
		//tokenmanager.MatchAndRemove(Token.TokenType.NOT);// remove token from list
		Optional<Node> ans = ParseOperation(); // call method
		OperationNode result = new OperationNode(ans, OperationNode.Operation.NOT); //create new Operation node
		return Optional.of(result); // return Operation Node
	}else if(TokenPeek.get().getTokenType()== Token.TokenType.MINUS) { //check if token is minus
		//tokenmanager.MatchAndRemove(Token.TokenType.MINUS); // remove token from list
		Optional<Node> ans = ParseOperation(); // Parse Operation
		OperationNode result = new OperationNode( ans, OperationNode.Operation.UNARYNEG); // create new Operation Node
		return Optional.of(result); // return Operation Node
	}else if(TokenPeek.get().getTokenType() == Token.TokenType.PLUS){ // checks if token is plus
		//tokenmanager.MatchAndRemove(Token.TokenType.PLUS); // remove token from list
		Optional<Node> ans = ParseOperation(); // parse operation
		OperationNode result = new OperationNode(ans, OperationNode.Operation.UNARYPOS); //create new Operation  Node
		return Optional.of(result);// return Operation Node
	}else if(TokenPeek.get().getTokenType() == Token.TokenType.INCREMENT) { // check if token is Increment
		//tokenmanager.MatchAndRemove(Token.TokenType.INCREMENT); // remove token from list
		Optional<Node> ans = ParseOperation();//parse operation
		OperationNode result = new OperationNode(ans, OperationNode.Operation.PREINC); // create new Operation Node
		return Optional.of(result); // return Operation Node
	}else if(TokenPeek.get().getTokenType() == Token.TokenType.DECREMENT) { // check if token is Decrement
		//tokenmanager.MatchAndRemove(Token.TokenType.DECREMENT);// remove token from list
		Optional<Node> ans = ParseOperation(); // parse operation
		OperationNode result = new OperationNode(ans, OperationNode.Operation.PREDEC);// create noew Operation Node
		return Optional.of(result); // return operation Node
	}else {
		Optional<Node>ans =  ParseFunctionCall();
		if(ans.isPresent()) {
			return ans;
		}
		return ParseLValue(); // if nothing else, call ParseLValue method
	}
}

public Optional<Node> Factor() {
	Optional<Token>num = tokenmanager.MatchAndRemove(Token.TokenType.Number); //checks for and consumes number token
	if(num.isPresent()) {
		return Optional.of(new ConstantNode(num.get().getValue())); // returns number as constant node
	}
	if(tokenmanager.MatchAndRemove(Token.TokenType.LEFT_PAREN).isPresent()) { //check if left parenthesis is present and consume it
		Optional<Node> exp = Expression(); // call expression method to parse expression
		if(exp.isPresent()) { // checks if expressions is present
			if(tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_PAREN).isPresent()) { //checks if right parentheis is present and consume it
			return exp; // return expression
		} else { throw new RuntimeException("Mismatched Parenthesis"); // if a parenthesis is missisng throw error
		}}
			else {
				throw new RuntimeException("Unexpected error in Expression"); // if error in parsing expression throw exception
			}
	}
	return Optional.empty(); // if num wasn't present return nothing
}

public Optional<Node> Term() {
Optional<Node>left = Factor(); // check for factors by calling factor method for left hand side
do { // start of do while loop
Optional<Token> Op= tokenmanager.MatchAndRemove(Token.TokenType.TIMES); //check if token is times and consumes it
if(Op.isEmpty()) {
	Op = tokenmanager.MatchAndRemove(Token.TokenType.DIVIDE); // if there are no times tokens, check for divide
}
if(Op.isEmpty()) {
	return left; //if neither were found return left hand side
}
Optional<Node>right = Factor(); //check for factors on right hand side
left = Optional.of(new MathOpNode(left, getOperationEnum(Op.get().getTokenType()), right)); // create a return new MathOpNode
}while(true);
}

public Optional<Node>Expression(){
Optional<Node>left = Term(); //left hand side parsed using term method

do {
	Optional<Token> Op = tokenmanager.MatchAndRemove(Token.TokenType.PLUS); //check if token is plus and consume it
	if(Op.isEmpty()) {
		Op = tokenmanager.MatchAndRemove(Token.TokenType.MINUS); // if token is empty, check for and consume minus
	}
	if(Op.isEmpty()) {
		return left; // if neither are present, return left and exit the loop
	}
	Optional<Node>right = Term(); // parses right hand side using term method
	left = Optional.of(new MathOpNode(left, getOperationEnum(Op.get().getTokenType()), right)); //create and return new MathOpNode
}while(true);
}

public Optional<Node> ParseExponentiation(){
Optional<Node>left = ParseBottomLevel(); // Parses left hand side first with PARSEBOTTOMLEVEL

while(true) {
Optional<Token>TokenPeek = tokenmanager.MatchAndRemove(Token.TokenType.EXPONENT); //check to see if exponent is token and consume ut
if(TokenPeek.isPresent()) {
	Optional<Node>right = ParseExponentiation(); // if token is present, recursively parse right hand
	if(right.isPresent()) {
		left = Optional.of(new MathOpNode(left, OperationNode.Operation.EXPONENT ,right)); // if rhs is present create new MAthOpNode
	}else {
		throw new RuntimeException("Unexpected error from right hand side of exponentiation");
	}
}else {
	break; // exit loop when !tokenpeek.isPresent();
}
	
}
return left; //return statement 
}

public Optional<Node> ParsePostIncrementDecrement(){
	Optional<Node> value = ParseLValue(); // parse lhs
	Optional<Token>TokenPeek = tokenmanager.Peek(0); //peek at current token
	Token.TokenType token = TokenPeek.get().getTokenType();
	if(TokenPeek.isPresent() &&(token == Token.TokenType.INCREMENT|| token == Token.TokenType.DECREMENT)) { //check if current token exist and is increment or decrement
		tokenmanager.MatchAndRemove(token); // consume token
	OperationNode.Operation op;
	if(token == Token.TokenType.INCREMENT) {
		op = OperationNode.Operation.POSTINC; //if token type is increment change to appropriate operation node
	}else {
		op = OperationNode.Operation.POSTDEC; // if token type is decrement change to appropriate operation node
	}
	return Optional.of(new MathOpNode(value, op, Optional.empty())); // create and return new math Op node
	
	}
	return Optional.empty();
	
}

public Optional<Node> ParseStringConcatenation() {
	Optional<Node>left = ParseExponentiation(); //parses left hand side using method of next highest precedence
	
	while(true) {
		Optional<Token> TokenPeek = tokenmanager.Peek(0); //peek at current token
		Token.TokenType token = TokenPeek.get().getTokenType(); // get token type
		if(TokenPeek.isPresent() &&(token == Token.TokenType.Word ||token == Token.TokenType.Stringliteral)) { // checks if token is present and is word or string literal
			Optional<Node>right = ParseExponentiation();//parse right hand side 
			left = Optional.of(new MathOpNode(left, OperationNode.Operation.CONCATENATION, right)); //create new mathOpNode
		}else {
			break; //exit loop if no parameters in if loop is met
		}
		
	}
return left; //return left
}

public Optional<Node> ParseBooleanCompare(){
Optional<Node>left = Expression(); // parse lhs using expression method
Optional<Token>	TokenPeek = tokenmanager.Peek(0); // peek at current token

if(TokenPeek.isPresent()) {
	if(isComparisonOperator(TokenPeek.get().getTokenType())) { //checks if current token is comparison operator
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType());// consume comparison operator
		Optional<Node> right = Expression(); // parse rhs using expression method
		if(right.isPresent()) {
			return Optional.of(new MathOpNode(left, getCompareEnum(TokenPeek.get().getTokenType()), right));// if rhs is present return new MathOpNode
		}else {throw new RuntimeException("Error from right hand side of comparison");}// if rhs doesnt exist, throw exception
		
	}
}else { throw new RuntimeException("Unexpected end of output"); } //  if tokenpeek is not present, throw error
return Optional.empty();
}

public boolean isComparisonOperator(Token.TokenType tokentype) {  //method that returns true if tokentype is comparison Operator
	return tokentype == Token.TokenType.LESS_THAN ||
			tokentype == Token.TokenType.GREATER_EQUAL ||
			tokentype == Token.TokenType.LESS_EQUAL ||
			tokentype == Token.TokenType.GREATER_THAN ||
			tokentype == Token.TokenType.NOT_EQUAL ||
			tokentype == Token.TokenType.EQUAL;
			
}

public OperationNode.Operation getCompareEnum(Token.TokenType token){ 
	switch(token) { //case by case switch to convert Token type to corresponding OperationNode enum
	case LESS_THAN:
		return OperationNode.Operation.LT;
	case GREATER_EQUAL:
		return OperationNode.Operation.GE;	
	case LESS_EQUAL:
		return OperationNode.Operation.LE;
	case GREATER_THAN:
		return OperationNode.Operation.GT;
	case NOT_EQUAL:
		return OperationNode.Operation.NE;
	case EQUAL:
		return OperationNode.Operation.EQ;
	default:
		break;
	}
	return null;
}

public Optional<Node> ParseMatch(){
	Optional<Node>left = Expression(); // parse lhs first
	Optional<Token>TokenPeek = tokenmanager.Peek(0); // peek at current token
	if(TokenPeek.get().getTokenType() == Token.TokenType.TILDE || TokenPeek.get().getTokenType() == Token.TokenType.NO_MATCH) {//check if token type is match or not match
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); //if token is either match or notmatch consume it
		Optional<Node> right = Expression(); //parse rhs 
		if(right.isPresent()) { 
			if(TokenPeek.get().getTokenType() == Token.TokenType.TILDE){
				return Optional.of(new MathOpNode(left, OperationNode.Operation.MATCH, right)); //if right side is present and token type was match, create and return corresponding MathOpNode
			}else {
				return Optional.of(new MathOpNode(left, OperationNode.Operation.NOT, right)); //if right side is present and token type was not match, create and return corresponding MAthOpNode
			}
			}else {
				throw new RuntimeException("Error in Right Hand Matching"); //if right hand doesnt exist, throw error
			}
	} 
	return Optional.empty();
}

public Optional<Node> ParseLValue() {
	Optional<Token>TokenPeek = tokenmanager.Peek(0); // peeks are current token in token list
	
	if(TokenPeek.get().getTokenType() == Token.TokenType.DOLLARSIGN) { // checks if current token is dollar sign
		tokenmanager.MatchAndRemove(Token.TokenType.DOLLARSIGN);// remove token from list
		Optional<Node> bottom = ParseBottomLevel(); //PArse bottom level
		OperationNode result = new OperationNode( bottom, OperationNode.Operation.DOLLAR); // create new opration Node
		return Optional.of(result); // return operation node
	}else if(TokenPeek.get().getTokenType() == Token.TokenType.Word) { // if token is a word...
	String ArrayName = TokenPeek.get().getValue();	 // get value of token
	tokenmanager.MatchAndRemove(Token.TokenType.Word);// remove token from token list
		if(TokenPeek.get().getTokenType() == Token.TokenType.LEFT_BRACKET) { //check if token following word is left bracket
			tokenmanager.MatchAndRemove(Token.TokenType.LEFT_BRACKET); // remove token from list
			Optional<Node> ans = ParseOperation(); // parse operation
			if(TokenPeek.get().getTokenType()== Token.TokenType.RIGHT_BRACKET) { //checks if next token is right bracket
				tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_BRACKET);// remove token from list
				VariableReferenceNode result = new VariableReferenceNode( ArrayName, ans); // make new reference variable node
				return Optional.of(result);// return new reference variable node
			} else {
			return Optional.of(new VariableReferenceNode(ArrayName)); // if no bracket after word create new variable reference node without parse operation
			}
	        } else { 
	        	return Optional.of(new VariableReferenceNode(ArrayName)); // create and return new variable reference node without parse operation
	
	}
  } 
	
	return Optional.empty(); // returns nothing if nothing else works

}

public Optional<Node> ParseArray(){
Optional<Node>left = ParseBottomLevel(); // parses left hand side prior to array
while(true) {
	Optional<Token>TokenPeek = tokenmanager.Peek(0); // peek at current token
	
	if(TokenPeek.isPresent()) {
	Token.TokenType tokentype = TokenPeek.get().getTokenType(); // if token is present, get tokentype
		if(tokentype == Token.TokenType.LEFT_BRACKET) {
			tokenmanager.MatchAndRemove(tokentype); // if token is left bracket match and remove
			if(tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_BRACKET).isPresent()) {
				left = Optional.of(new OperationNode(left, Optional.empty(), OperationNode.Operation.IN_Array)) ; //if right bracket is present then create new operationNode
			}else {
				throw new RuntimeException("Missing Right Bracket to close array");// if right bracket is missing throw exception
			}
		}
		else if(tokentype == Token.TokenType.LEFT_PAREN) { //else if token type is left parenthesis
			tokenmanager.MatchAndRemove(tokentype); // consume tokentype
			if(tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_PAREN).isPresent()) { //check if right parenthesis
				left = Optional.of(new OperationNode(left, Optional.empty(), OperationNode.Operation.IN_MultiDArray)); // there both () are present create new OperationNode
			}else {
				throw new RuntimeException("Missing Right Paranthesis to close Multidimensional Array"); // if missing right paren throw exception
			}
			
		}else { //if neither parenthesis nor bracket are present break loop
			break;
		}
	
	}else { //if token is not present at all break loop
		break;
	}
}
return left;
}



public Optional<Node> ParseLogicalAND(){
	Optional<Node>left = Expression(); //  parse lhs 
	
	while(true) {
		Optional<Token>TokenPeek = tokenmanager.Peek(0); //peek at current token
		if(TokenPeek.get().getTokenType() == Token.TokenType.LOGICAL_AND) { //check if token is logical and
			tokenmanager.MatchAndRemove(Token.TokenType.LOGICAL_AND); //consume token
			Optional<Node>right= Expression(); // parse rhs
			if(right.isPresent()) { // if rhs is present
				left = Optional.of(new MathOpNode(left, OperationNode.Operation.AND, right)); // create new MATHOPNODE
			}else {
				throw new RuntimeException("Right Hand Side error for Logical AND"); //if rhs doesnt exitst throw exceptions
			}
		} else { //if token isnt logical and, break the loop
			break;
		}
	}
return left; //return left
}

public Optional<Node> ParseLogicalOR(){
	Optional<Node>left = Expression(); //parse lhs
	
	while(true) {
		Optional<Token>TokenPeek = tokenmanager.Peek(0); // peek at current token
		if(TokenPeek.get().getTokenType() == Token.TokenType.LOGICAL_OR) { //check if token is logical or
			tokenmanager.MatchAndRemove(Token.TokenType.LOGICAL_OR); // consume token
			Optional<Node>right= Expression(); // parse rhs
			if(right.isPresent()) { //if rhs is present
				left = Optional.of(new MathOpNode(left, OperationNode.Operation.OR, right)); //create and return new mathopNode
			}else {
				throw new RuntimeException("Right Hand Side error for Logical OR"); //throw exception if rhs doesnt exist
			}
		} else {// if token is not logical or, break loop
			break;
		}
	}
return left;
}

public Optional<Node> ParseAssignment(){
	Optional<Node>left = ParseLValue(); // parse lhs
	
	Optional<Token>AssignmentOp = tokenmanager.Peek(0);// peek at current token
	Token.TokenType token = AssignmentOp.get().getTokenType(); //get token type
	
	if(AssignmentOp.isPresent() && isAssignmentOperator(token)) { //check if token is assignmentoperator and is present
		tokenmanager.MatchAndRemove(token); // consume token
		Optional<Node>right = ParseAssignment(); // recursively parse rhs
		OperationNode assign = new OperationNode(left, right, getAssignmentOp(token)); // create new operationNode
		return Optional.of(new AssignmentNode(left, Optional.of(assign))); //inbed operation node into newly created AssignmentNode
		
	}
	return Optional.empty();
}

public boolean isAssignmentOperator(Token.TokenType token) {
	//method that returns true is token is an Assignment Operator
	return token == Token.TokenType.ASSIGN ||
			token == Token.TokenType.DIVIDE_ASSIGN ||
			token == Token.TokenType.MULTIPLY_ASSIGN ||
			token == Token.TokenType.SUBTRACT_ASSIGN ||
			token == Token.TokenType.EXPONENT_ASSIGN ||
			token == Token.TokenType.ADD_ASSIGN ||
			token == Token.TokenType.MODULO_ASSIGN;
}

public OperationNode.Operation getAssignmentOp(Token.TokenType token){
	//method that does case by case switch of Token type to corresponding OperationNode enum
	switch(token) {
	case EXPONENT_ASSIGN:
		return OperationNode.Operation.EXPONENT;
	case ADD_ASSIGN:
		return OperationNode.Operation.ADD;
	case MULTIPLY_ASSIGN:
		return OperationNode.Operation.MULTIPLY;
	case SUBTRACT_ASSIGN:
		return OperationNode.Operation.SUBTRACT;
	case MODULO_ASSIGN:
		return OperationNode.Operation.MODULO;
	case ASSIGN:
		return OperationNode.Operation.EQ;
	case DIVIDE_ASSIGN:
		return OperationNode.Operation.DIVIDE;
	default:
		break;
	}
	return null;
}

public Optional<Node> ParseConditional(){
	Optional<Node> Condition = Expression(); //parses condition before conditional
	Optional<Token> Conditional = tokenmanager.MatchAndRemove(Token.TokenType.QUESTIONMARK); // check for conditional token type and consumes it
	
	if(Conditional.isPresent()) { 
		Optional<Node> falseEx = ParseConditional(); //recursively parse false expression
		Optional<Token> Colon = tokenmanager.MatchAndRemove(Token.TokenType.COLON); //check for and consume colon token
		if(Colon.isPresent()) {
			Optional<Node> TrueEx = ParseConditional(); //if colon is present recursively parse for true expression
			if(TrueEx.isPresent() && falseEx.isPresent()) {// check to see both expressions are present
				return Optional.of(new TernaryNode(Condition, TrueEx, falseEx)); // create and return new Ternary node
			}else { //if an expression is missing throw exception
				throw new RuntimeException("Conditional Operator error: Invalid Expression");
			}
		}else { //if colon is missing throw exception
			throw new RuntimeException("Missing ':' after '?' in conditional operator");
		}
		
	}
	return 	Optional.empty();
	
}

private boolean ParseFunction(ProgramNode programnode) {

	
	Optional<Token> TokenPeek = tokenmanager.Peek(0); //peeek at next token
	
	
	
	
	if (TokenPeek.get().getTokenType() == Token.TokenType.Word) { //check if its word token
		String functionName = TokenPeek.get().getValue(); // get function name
		if(TokenPeek.get().getTokenType() == Token.TokenType.Getline || TokenPeek.get().getTokenType() == Token.TokenType.Print || TokenPeek.get().getTokenType() == Token.TokenType.Printf || TokenPeek.get().getTokenType() == Token.TokenType.Exit || TokenPeek.get().getTokenType() == Token.TokenType.Nextfile || TokenPeek.get().getTokenType() == Token.TokenType.Next ) {
			new CallFunctionNode(functionName, new LinkedList<Node>());
		}
		tokenmanager.MatchAndRemove(Token.TokenType.Word); // remove token
	if(tokenmanager.MatchAndRemove(Token.TokenType.LEFT_PAREN).isPresent()) { //checks if left parentheisis is present
		LinkedList<String> Parameters = new LinkedList<>();
	
	while(tokenmanager.Peek(0).isPresent() && tokenmanager.Peek(0).get().getTokenType() == Token.TokenType.Word) { // checks of parameter is word
		String Parameter=tokenmanager.Peek(0).get().getValue(); // get the word value
		Parameters.add(Parameter); // add it to parameters
		tokenmanager.MatchAndRemove(Token.TokenType.Word); 
	if(tokenmanager.MatchAndRemove(Token.TokenType.COMMA).isEmpty()) {
		break; // when token manager does not see anymore commas after word, breaks loop
	}	
	}
	
	BlockNode Blockfunction = ParseBlock(); //parse block
	FunctionDefinitionNode NewFunction = new FunctionDefinitionNode(functionName,Parameters, Blockfunction.getStatements()); //create new function definition
	programnode.addFunctionDef(NewFunction);
	
	
	}
	}
	
	return false; // returns false if there wasnt a function
}


public Optional<Node> ParseOperation() {
	Optional<Node> left = ParseBottomLevel(); // parse lhs 
	
	while(true) {
	
	
	Optional<Token>TokenPeek = tokenmanager.Peek(0); // peek at current token
	if(isOperator(TokenPeek.get().getTokenType())) { //check if token type is an operator
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); //consume and remove token
		Optional<Node>right = ParseBottomLevel(); // parse rhs or operation
		left = Optional.of(new OperationNode(left, right, getOperationEnum(TokenPeek.get().getTokenType()) )); // create and return newOperationNode
		}else {  break;} //break loop if token is not operator
				}	
		return left;}



public boolean isOperator(Token.TokenType TokenType) {
	//method that returns true if token type is an Operator
	return TokenType == Token.TokenType.PLUS ||
			TokenType == Token.TokenType.MINUS ||
			TokenType == Token.TokenType.INCREMENT ||
			TokenType == Token.TokenType.DECREMENT ||
			TokenType == Token.TokenType.NOT ||
			TokenType == Token.TokenType.LEFT_PAREN;
	
}


public OperationNode.Operation getOperationEnum(Token.TokenType Tokentype){
	//method that does case by case switch of Token type to corresponding OperationNode enum	
	switch(Tokentype) {
	case PLUS:
		return OperationNode.Operation.ADD;
	case MINUS:
		return OperationNode.Operation.SUBTRACT;
	case INCREMENT:
		return OperationNode.Operation.PREINC;
	case DECREMENT:
		return OperationNode.Operation.PREDEC;
	case NOT:
		return OperationNode.Operation.NOT;
	case TIMES:
		return OperationNode.Operation.MULTIPLY;
	case DIVIDE:
		return OperationNode.Operation.DIVIDE;
	default:
		break;
	}
	return null;	
}



public BlockNode ParseBlock() {
	// TODO Auto-generated method stub
	LinkedList<StatementNode> results = new LinkedList<>();
	Optional<StatementNode>result;
	
	
	//parses for Multi line block first
	Optional<Token>TokenPeek = tokenmanager.Peek(0);  // peek at current token
	if(TokenPeek.get().getTokenType() == Token.TokenType.LEFT_BRACE) { // check if current token is left brace
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType());// consume token
		AcceptSeperators(); //check to see if token manager Accepts tokens
		while(true) { // loops until broken
		result = ParseStatement(); // calls parsestatement method
		if(result.isPresent()) { // check if  method returned something
			results.add(result.get()); // add to linked list
		}else { // if method did no return anything
			break; //break loop
		}
		}
	
	if(tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_BRACE).isPresent()) { // check for and consume Right brace token
		return new BlockNode(results,Optional.empty()); // returns new blocknode
	}else {
		throw new RuntimeException("Missing '}' to close multiline block"); // throw exception if missing right brace
	}
	
	}else { // for single block parse
		result = ParseStatement(); // parse statement
		if(result.isPresent()) { //check if method returned successfully
			AcceptSeperators(); // check if separators are being accepted
			results.add(result.get()); // add results to linked lisr
		
		return new BlockNode(results,Optional.empty()); // return new block node
	}
}
	return null; // if nothing else catches, return nothing

}

public Optional<StatementNode> ParseStatement() {
	Optional<StatementNode> result;
	//calls parsing method, checks for existing result and return it 
	
	result = ParseIf(); // parses if statement
	if(result.isPresent()) {
		return result;
	}
	result = ParseWhile(); // parses while loop
	if(result.isPresent()) {
		return result;
	}
	result = ParseFor();// parses For loops
	if(result.isPresent()) {
		return result;
	}
	result= ParseContinueandBreak(); // Parses Continue and Break
	if(result.isPresent()) {
		return result;
	}
	
	result = ParseReturn(); // Parses return
	if(result.isPresent()) {
		return result;
	}
	result = ParseDoWhile(); // PArses DO while Loop
	if(result.isPresent()) {
		return result;
	}
	
	result = ParseDelete(); // Parse Delete
	if(result.isPresent()) {
		return result;
	}
	return Optional.empty(); //returns empty if result is not present
	
}



public Optional<StatementNode> ParseDelete() {
	Optional<Token> Delete = tokenmanager.MatchAndRemove(Token.TokenType.Delete); //check for delete token
	String name = null;
	List<Node> indices = new ArrayList<Node>();

	if(Delete.isPresent()) { // if delete token is present, also consumed
		Optional<Token>TokenPeek = tokenmanager.Peek(0);// peek at next token
		if(TokenPeek.isPresent() && TokenPeek.get().getTokenType() == Token.TokenType.Word) { 
			 name = TokenPeek.get().getValue(); // if token is word, extract its value
			tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); // consume word token
			return Optional.of(new DeleteNode(name)); // return new delete node
		}else if(TokenPeek.isPresent() && TokenPeek.get().getTokenType() == Token.TokenType.LEFT_BRACKET) {
			tokenmanager.MatchAndRemove(Token.TokenType.LEFT_BRACKET); // if next token isnt word and is left bracket, consume token
			do {
				Optional<Node> index = ParseOperation(); // parse array indice
				indices.add(index.orElseThrow(()-> new RuntimeException("Error Parsing Delete Array"))); // add indinces to array list, throw exception if there is a failure
			}while(tokenmanager.MatchAndRemove(Token.TokenType.COMMA).isPresent());	// continue until comma tokens are done
		}
	
	if(tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_BRACKET).isPresent()) { 
		return Optional.of(new DeleteNode(name, indices)); // if right bracket is present create and return new DeleteNode
	}else {
		throw new RuntimeException("Missing Right Bracket to close Array Indices"); // throw exception if missing right bracket
	}
	}else {
		throw new RuntimeException("Syntax error from Delete Parsing"); // throw exception if missing delete token
	}
}



public Optional<StatementNode> ParseDoWhile() {
Optional<Token>Do = tokenmanager.MatchAndRemove(Token.TokenType.Do); // check for Do token
if(!Do.isPresent()){ 
	return Optional.empty(); //if do token is not present return empty
}

BlockNode rest = ParseBlock(); // parse block after do
if(tokenmanager.MatchAndRemove(Token.TokenType.While).isPresent()) {
	Optional<Node>Condition = ParseOperation(); //check if while token is present, if it is parse condition
	if(Condition.isPresent()) {
		if(tokenmanager.MatchAndRemove(Token.TokenType.Separator).isPresent()) {
			return Optional.of(new DoWhileNode(Condition, rest)); //if condition and separator are present, return new DowhileNode
		}else {
			throw new RuntimeException("Separator Missing"); // throw exception if separator is missing
		}
	}else {
		throw new RuntimeException("Error with Do-While Condition"); // throw exception if condition parsing went awry
	}
}else {
	throw new RuntimeException("Missing While in Do-While Loop");// if missing while token throw exception
}

}



public Optional<StatementNode> ParseReturn() {
Optional<Token>TokenPeek = tokenmanager.Peek(0);
if(TokenPeek.isPresent()&& TokenPeek.get().getTokenType() == Token.TokenType.Return) { //check if return token is present and consume it
	tokenmanager.MatchAndRemove(Token.TokenType.Return);
	Optional<Node>returnV = ParseOperation(); //parse return value
	if(AcceptSeperators()) {
		return Optional.of(new ReturnNode(returnV));//if seperators are accepted, create new return node
	}else {
		throw new RuntimeException("Missing Separator in Return Statement");//if missing separator throw exception
	}
}
return Optional.empty(); //else return nothing
}



public Optional<Node> ParseFunctionCall() {
Optional<Token> name = tokenmanager.MatchAndRemove(Token.TokenType.Word); //check for word token
if(name.isPresent()) {
	String FunctionName = name.get().getValue(); // if word is present, cosume token and extract value
	LinkedList<Node>Param = new LinkedList<>();
	if(tokenmanager.MatchAndRemove(Token.TokenType.LEFT_PAREN).isPresent()) {
		do {
			Optional<Node>Parameter = ParseOperation(); // if left parenthesis is present parse parameters
			if(Parameter.isPresent()) {
				Param.add(Parameter.get()); // if parameters exist, add them to list
			}
			
		}while(tokenmanager.MatchAndRemove(Token.TokenType.COMMA).isPresent()) ; // continue loop until there are no more commas
	
if(tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_PAREN).isPresent()) { 
	return Optional.of(new CallFunctionNode(FunctionName, Param)); // if right parenthesis exists, create new CallfunctionNode
}else {
	throw new RuntimeException("Missing Right Parenthesis to Close Function Call"); // throw exception if right parenthesis is missing
}
	}else {
		throw new RuntimeException("Missing Left Parenthesis to Open Function Call"); // throw exception if left parenthesis is missing
	}
}
return Optional.empty();
}



public Optional<StatementNode> ParseContinueandBreak() {
	Optional<Token>TokenPeek = tokenmanager.Peek(0);
	
	if(TokenPeek.get().getTokenType() == Token.TokenType.Continue && TokenPeek.isPresent()) {
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); // if token type is continue and exists, return new ContinueNode
		return Optional.of(new ContinueNode());
	}
	else if(TokenPeek.get().getTokenType() == Token.TokenType.Break && TokenPeek.isPresent()){
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); // if token type is break and present, return new Breaknode
		return Optional.of(new BreakNode());
		
	}
	return Optional.empty();// otherwise return nothing
}



public Optional<StatementNode> ParseFor() {
Optional<Token>ForToken = tokenmanager.MatchAndRemove(Token.TokenType.For);
if(!ForToken.isPresent()) {
	return Optional.empty(); // if for token is not present return nothing
}
Optional<Token>LeftParen = tokenmanager.MatchAndRemove(Token.TokenType.LEFT_PAREN);
if(LeftParen.isPresent()) { // check for and consume left token
	Optional<Node>Initial = ParseAssignment(); //parse initialization
	tokenmanager.MatchAndRemove(Token.TokenType.Separator); //remove semicolon
	Optional<Node>Condition = ParseAssignment();// parse Conditional
	tokenmanager.MatchAndRemove(Token.TokenType.Separator); //remove semicolon
	Optional<Node>Iterable = ParseAssignment(); //Parse Iteration
	Optional<Token>RParen = tokenmanager.MatchAndRemove(Token.TokenType.RIGHT_PAREN);
	if(RParen.isPresent()) { // if right parenthesis is there
		BlockNode rest = ParseBlock(); // parse rest of block
		return Optional.of(new ForNode(Initial, Condition, Iterable, rest)); // create and return new ForNode
	}else { 
		throw new RuntimeException("Missing Right Parenthesis to close loop"); // if missing right parenthesis throw exception
	}
}else { //parses for Each loop
	Optional<Token>Var = tokenmanager.MatchAndRemove(Token.TokenType.Word); //check for and consume Word Token
	String Variable= Var.get().getValue();// extract word value
	tokenmanager.MatchAndRemove(Token.TokenType.In); // remove in token
	Optional<Node>Iterable = Expression(); // parse iterable
	BlockNode rest = ParseBlock();//parse rest of block
	return Optional.of(new ForEachNode(Variable, Iterable, rest));//create and return new ForEachode
}
}



public Optional<StatementNode> ParseWhile() {
if(tokenmanager.MatchAndRemove(Token.TokenType.While).isPresent()) {//check if while token is present and consumed
	Optional<Node>Condition = ParseOperation();// parse condition
	if(Condition.isPresent()) { // check if conditon is present
		BlockNode rest = ParseBlock(); // parse rest of block
		if(rest != null) {
			return Optional.of(new WhileNode(Condition, rest)); // if parse block successfull, create new While node
		}else {
			throw new RuntimeException("Error with while block"); // throw except if block parse went awry
		}
	}else {
		throw new RuntimeException("Error with while condition"); // throw exception if while conditon parsing goes awry
	}
}
return Optional.empty();
}


public Optional<StatementNode> ParseIf() {
	Optional<Node>Condition = ParseOperation(); // parse condition
	BlockNode Statements = ParseBlock(); // parse statement
	IfNode ifNode = new IfNode(Condition, Statements); // create new ifNode
	Optional<Token>TokenPeek = tokenmanager.Peek(0); // peek at current token
	
	if(TokenPeek.isPresent() && TokenPeek.get().getTokenType() == Token.TokenType.Else) {
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); //check if there is else statement
		TokenPeek = tokenmanager.Peek(0);
		if(TokenPeek.isPresent() && TokenPeek.get().getTokenType() == Token.TokenType.If) {
		tokenmanager.MatchAndRemove(TokenPeek.get().getTokenType()); //check if is if else statement
		ifNode.setNext(ParseIf());// recursively calls method again
		}else {
			 ifNode.setElse(ParseBlock()); // parse else block
		}
	}
	return Optional.of(ifNode); // return ifNode
	
}




}
