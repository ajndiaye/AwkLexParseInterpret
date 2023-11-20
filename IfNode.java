import java.util.Optional;

public class IfNode extends StatementNode{
	
	
	Optional<Node>Condition;
	BlockNode Statements;
	Optional<StatementNode> next;
	BlockNode Elseblock;
	
	public IfNode(	Optional<Node>Conditon, BlockNode Statements){
		this.Condition = Conditon;
		this.Statements = Statements;
	}
	
	public void setNext(Optional<StatementNode> next) {
		this.next = next;
	}
	
public void setElse(BlockNode Elseblock) {
	this.Elseblock = Elseblock;
}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
