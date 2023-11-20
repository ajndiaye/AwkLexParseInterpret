import java .util.LinkedList;
import java.util.Optional;




public class TokenManager {
	
	LinkedList<Token> tokenlist;
	public TokenManager(LinkedList<Token> tokenlist) {  //constructor
		this.tokenlist = tokenlist;	
	}
	

public Optional<Token> Peek(int i) {  //Method peeks "i" positions a head and returns it "optionally"
	if(i>=0&& i < tokenlist.size()) {
		return Optional.of(tokenlist.get(i));
	} else {
	
	return Optional.empty();	
}
}	

public boolean MoreTokens()  { //check to see if token list is empty
	return !tokenlist.isEmpty();
}
	
public Optional<Token>MatchAndRemove( Token.TokenType t){ //Matches token type specified and removes it from linked list.
if(MoreTokens() && tokenlist.getFirst().getTokenType() == t) {
	return Optional.of(tokenlist.removeFirst());
}else {
	return Optional.empty();
}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
