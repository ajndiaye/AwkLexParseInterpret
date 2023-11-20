import java.util.Optional;

public class WhileNode extends StatementNode{
	
	Optional<Node>Condition;
	BlockNode rest;
	
	
	
	public WhileNode(Optional<Node>Condition,BlockNode rest) {
		this.Condition = Condition;
		this.rest = rest;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
 