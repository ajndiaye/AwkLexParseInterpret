import java.util.LinkedList;

public class CallFunctionNode extends StatementNode{
	
	
	String Name;
	LinkedList<Node>Param;
	
	public CallFunctionNode(String Name,LinkedList<Node> linkedList) {
		this.Name = Name;
		this.Param = linkedList;
	}
	
	public String getName() {
		return Name;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public LinkedList<Node> getParam() {
		return Param;
	}
	
	public int getParameterCount() {
		return Param.size();
		
	}
	

}
