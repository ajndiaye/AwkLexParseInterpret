
import java.util.List;
import java.util.Optional;


public class DeleteNode extends StatementNode{
	
	
	String name;
	List<Node> index;

	
public DeleteNode(String name, List<Node> indices)	{
	this.name=name;
	this.index=indices;
}

public DeleteNode(String name) {
	this.name=name;
}


	public String getName() {
	return name;
}


public List<Node> getIndex() {
	return index;
}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
