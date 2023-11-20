import java.util.Optional;

public class ReturnNode extends StatementNode{
	
	Optional<Node>returnvalue;
	
	public ReturnNode(Optional<Node>returnvalue) {
		this.returnvalue = returnvalue;
		
	}

public Optional<Node> getReturn(){
	return returnvalue;
}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}}
