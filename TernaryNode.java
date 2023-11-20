import java.util.Optional;

public class TernaryNode extends Node{
	
	static Optional<Node> Condition;
	Optional<Node> True;
	Optional<Node> False;
	
	@SuppressWarnings("static-access")
	public TernaryNode(Optional<Node> Condition, Optional<Node> True, Optional<Node> False) {
		this.Condition=Condition;
		this.True = True;
		this.False = False;			
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Node> getCondition() {
		return Condition;
	}

	public Optional<Node> getTrue() {
		return True;
	}

	public Optional<Node> getFalse() {
		// TODO Auto-generated method stub
		return False;
	}

}
