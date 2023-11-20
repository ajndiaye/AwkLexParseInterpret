import java.util.LinkedList;


public class FunctionDefinitionNode extends Node {
	
	String functionName;
	LinkedList<String> Parameters;
	LinkedList<StatementNode> statements;

	public FunctionDefinitionNode(String functionName, LinkedList<String> Parameters,LinkedList<StatementNode> statements ) {
		this.Parameters= Parameters;  //constructor
		this.functionName= functionName;
		this.statements = statements;

	}
	
	public String getFunctionName() {
		return functionName;
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Function: " + functionName + "\n");  //appends function name
		for(int i =0; i < Parameters.size(); i++) {
		info.append("Parameters: "+ Parameters.get(i) +" "); i++; } // appends Parameter linked list
		info.append("Statements: ");
		for(int i =0; i < statements.size(); i++) {
			info.append(" "+ statements.get(i));  // appends statement linked list
			i++;
		}
		return info.toString();	
	}



public LinkedList<StatementNode> getStatements(){  //linked list getter
	return statements;
}

public LinkedList<String> getParameters(){ //linked list getter
	return Parameters;
}






}
 