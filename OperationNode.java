import java.util.Optional;

public class OperationNode extends Node{

	Optional<Node> left;
	Optional<Node>right;
	Operation operation;
	
	
	public OperationNode(Optional<Node> left, Optional<Node>right, Operation operation) { // constructor
	    this.left = left;
		this.right = right;
		this.operation = operation;
	}	
	
	public OperationNode(Optional<Node> bottom, Operation operation) { // second constructor to be able to create Operation Node
		this.right = bottom;
		this.operation = operation;
	}
	
	public enum Operation { // enum tokens for different awk operations
		EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR, PREINC, POSTINC, 
		PREDEC, POSTDEC, UNARYNEG, IN_Array, EXPONENT, ADD, SUBTRACT, MULTIPLY,DIVIDE, MODULO, 
		CONCATENATION, UNARYPOS, IN_MultiDArray
	}
	
	public String toString() {
		return null;
	}

	public Operation getOperation() {
		return operation;
	}

	public Optional<Node> getLeft() {
		// TODO Auto-generated method stub
		return left;
	}
	
	public Optional<Node> getRight() {
		// TODO Auto-generated method stub
		return right;
	}
	
	

}
