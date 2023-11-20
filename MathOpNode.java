import java.util.Optional;

public class MathOpNode extends Node{
Optional<Node> left;
Optional <Node> right;
OperationNode.Operation Op;

public MathOpNode(Optional<Node> left, OperationNode.Operation Op, Optional <Node> right) {
	this.left = left;
	this.right = right;
	this.Op = Op;
}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getOperation() {
		return Op;
	}

}
