import java.util.Optional;

public class AssignmentNode extends StatementNode {
	Optional<Node> target;
	Optional<Node> expression;
	
	
	public  AssignmentNode(Optional<Node> target, Optional<Node> expression) {
		this.target = target;
		this.expression = expression;
		
	}
	
	

	public Optional<Node> getTarget() {
		return target;
	}
	

	public Optional<Node> getExpression() {
		return expression;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
