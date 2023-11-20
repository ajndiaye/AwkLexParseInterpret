import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends StatementNode {
	
	public LinkedList<StatementNode> statements;
	private Optional<Node> condition;
	
	public BlockNode( LinkedList<StatementNode> statements,Optional<Node> condition) { //constructor
		this.statements=statements;
		this.condition = condition;
			
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Node> getCondition() {
		return condition;
	}

	public LinkedList<StatementNode> getStatements() {
		return statements;
	}


}
