import java.util.Optional;

public class ForNode extends StatementNode{
	Optional<Node>initial;
	Optional<Node>Condition;
	Optional<Node>iteration;
	BlockNode rest;
	public ForNode(	Optional<Node>initial,Optional<Node>Condition,Optional<Node>iteration,BlockNode rest) {
		this.initial = initial;
		this.Condition = Condition;
		this.iteration=iteration;
		this.rest =rest;
	}
	
	

	public Optional<Node> getInitial() {
		return initial;
	}

	public Optional<Node> getCondition() {
		return Condition;
	}

	public Optional<Node> getIteration() {
		return iteration;
	}



	public BlockNode getRest() {
		return rest;
	}





	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
