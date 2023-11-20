import java.util.Optional;

public class DoWhileNode extends StatementNode {
	
	Optional<Node>Condition;
	BlockNode rest;
	
	
	
	public DoWhileNode(Optional<Node>Condition,BlockNode rest) {
		this.Condition = Condition;
		this.rest = rest;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
