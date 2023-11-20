import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BuiltinFunctionDefinitionNode extends FunctionDefinitionNode{

	boolean isVariadic;
	Function<HashMap<String,InterpreterDataType>,String > execute;
	

	public BuiltinFunctionDefinitionNode(String functionName, LinkedList<String> Parameters,
			LinkedList<StatementNode> statements, Function<HashMap<String,InterpreterDataType>,String > execute) {
		super(functionName, Parameters, statements);
		this.execute = execute;
		
	}
	

	public String executing(HashMap<String,InterpreterDataType> parameters) {
		return execute.apply(parameters);
		
	}
	
	public boolean isVariadic() {
		return isVariadic;
	}


	public void setVariadic(boolean isVariadic) {
		this.isVariadic = isVariadic;
	}
	
}
