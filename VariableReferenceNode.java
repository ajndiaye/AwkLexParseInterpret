import java.util.Optional;

public class VariableReferenceNode extends Node{

	String VariableName;
	Optional<Node> ExpressionIndex;
	
	
	
	public VariableReferenceNode(String VariableName, Optional<Node>ExpressionIndex) { // constructor
		this.VariableName = VariableName;
		this.ExpressionIndex = ExpressionIndex;
	}
	
	
	public VariableReferenceNode(String arrayName) { // second constructor to be able to create node
		this.VariableName = arrayName;
	}


	@Override
	public String toString() {
		return VariableName;
	}
}
