import java.util.Optional;

public class ForEachNode extends StatementNode {

	String  variable;
	Optional<Node> iterable;
	BlockNode rest;
	
	public ForEachNode(String  variable,Optional<Node> iterable,BlockNode rest) {
		this.variable = variable;
		this.iterable = iterable;
		this.rest = rest;
		
	}
	
	public String getVariable() {
		return variable;
	}
	
	public Optional<Node> getIterable() {
		return iterable;
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
