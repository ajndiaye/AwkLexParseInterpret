import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class Interpreter {
	LineManager linemanager;
	HashMap<String, InterpreterDataType> map;
	HashMap<String, FunctionDefinitionNode> data;
	List<String>input;
	String FILENAME;
	String FS;
	String OFS;
	String ORS;
	String OFMT;
	
	public Interpreter(ProgramNode program, String path) throws IOException {
		if(path != null) { // check if patg is provided
		input = Files.readAllLines(Paths.get(path)); // read all lines and store them in input
		linemanager = new LineManager(input); // new linemanager to handle input
		}else {
			linemanager = new LineManager(new ArrayList<String>()); // if line manager doesn't exist make one with empty array
		}	
		// populate hashmaps with built in fucntuins
		data = new HashMap<>();
		data.put("print", new FunctionDefinitionNode("print", new LinkedList<>(),new LinkedList<>() ));
		data.put("printf", new FunctionDefinitionNode("printf", new LinkedList<>(),new LinkedList<>() ));
		data.put("getline", new FunctionDefinitionNode("getline", new LinkedList<>(),new LinkedList<>() ));
		data.put("next", new FunctionDefinitionNode("next", new LinkedList<>(),new LinkedList<>() ));
		data.put("gsub", new FunctionDefinitionNode("gsub", new LinkedList<>(),new LinkedList<>() ));
		data.put("index", new FunctionDefinitionNode("index", new LinkedList<>(),new LinkedList<>() ));
		data.put("length", new FunctionDefinitionNode("length", new LinkedList<>(),new LinkedList<>() ));
		data.put("match",new FunctionDefinitionNode("match", new LinkedList<>(),new LinkedList<>() ));
		data.put("split", new FunctionDefinitionNode("split", new LinkedList<>(),new LinkedList<>() ));
		data.put("Sprintf", new FunctionDefinitionNode("Sprintf", new LinkedList<>(),new LinkedList<>() ));
		data.put("sub",new FunctionDefinitionNode("sub", new LinkedList<>(),new LinkedList<>() ));
		data.put("substr",new FunctionDefinitionNode("substr", new LinkedList<>(),new LinkedList<>() ));
		data.put("tolower", new FunctionDefinitionNode("tolower", new LinkedList<>(),new LinkedList<>() ));
		data.put("toupper", new FunctionDefinitionNode("toupper", new LinkedList<>(),new LinkedList<>() ));
		//populate hashmap with global variables
		map = new HashMap<>();
		map.put(FILENAME, new InterpreterDataType(path));
		map.put( FS, new InterpreterDataType(" "));
		map.put( OFS ,new InterpreterDataType(" "));
		map.put( ORS,  new InterpreterDataType("\n"));
		map.put( OFMT, new InterpreterDataType("%.6g"));		
		
	}
	
	public class LineManager{
		List<String>input;
		int Lineindex;
		public LineManager(List<String>input) { // constructor to initialize line index
			this.input = input;
			this.Lineindex = 0;
		}
		
		public boolean splitAndAssign() { // method to split and assign values to global variables
			if(Lineindex >= input.size()) { // check if all lines been processed
				return false;
			}
		String currentline = input.get(Lineindex); // get current line
			 
		String Fs = map.get("FS").getValue(); //get field separator from map
		String [ ] split = currentline.split(Fs); // split current line with separator
		
		for(int i = 0; i < split.length; i++ ) {
			map.put("$"+i, new InterpreterDataType(split[i])); // split values and assign them to $0 ....
		}
		String nfvalue = String.valueOf(split.length); //calculate number of fields
		
		map.put("NF", new InterpreterDataType(nfvalue)); //set number number of fields
		map.put("NR",  new InterpreterDataType(String.valueOf(Lineindex + 1)));	//set record number
		map.put("FNR",  new InterpreterDataType(String.valueOf(Lineindex + 1))); // set file record number
		Lineindex ++; // increase line index
		return true;
	}
	}
	
	
	public BuiltinFunctionDefinitionNode ToupperFunction() {
		//create new BuiltinFunctionDefinition for toUpper
		BuiltinFunctionDefinitionNode toupper = new BuiltinFunctionDefinitionNode ("toupper", new LinkedList<>(List.of("string")), new LinkedList<>(), 
				parameters -> {	
					String string = parameters.get("string").getValue(); // extract string parameterr from list of args
					return string.toUpperCase(); // return string in upper case
				});
		toupper.setVariadic(false);
		return toupper; //return to upper
	}
	
	public BuiltinFunctionDefinitionNode PrintFunctiion() {
		// create new BuiltinFunctionDefinitionNode for print function
		BuiltinFunctionDefinitionNode print =  new BuiltinFunctionDefinitionNode("print", new LinkedList<>(List.of("args")), new LinkedList<>(), 
			parameters -> {
				StringBuilder result = new StringBuilder(); // stringbuilder to store result
				InterpreterArrayDataType arg = (InterpreterArrayDataType) parameters.get("args"); // get args parameter 
				for(int i=0; i < arg.getSize(); i ++) {//iterate through args elements
					if( i > 0) {
						result.append(" "); //append space if not 1st element
					}
					result.append(arg.getkeyvalue(Integer.toString(i))); // append value of the elements to result
				}
				System.out.print(result.toString()); //print to console
				return " "; // return empty string
			});
		
		print.setVariadic(true); //set variadic to true
		return print; // return print 
	}
	
	
	public BuiltinFunctionDefinitionNode NextFunction() {
		//create new BuiltinFunctionDefinition for next
		BuiltinFunctionDefinitionNode next = new BuiltinFunctionDefinitionNode ("next", new LinkedList<>(List.of("args")), new LinkedList<>(), 
				parameters -> {
					linemanager.splitAndAssign();// call line manager to split and assign paramters
					return " "; // return blank
				});
				next.setVariadic(false);
				return next; // return next
		
	}
	
	public BuiltinFunctionDefinitionNode MatchFunction() {
		//create new BuiltinFunctionDefinition for Match
		BuiltinFunctionDefinitionNode match = new BuiltinFunctionDefinitionNode ("Match", new LinkedList<>(List.of("regexp", "target")), new LinkedList<>(), 
				parameters ->{
					String regexp= parameters.get("regexp").getValue(); //get regexp from parameters
					String target= parameters.get("target").getValue(); // get target from parameters
					boolean result = target.matches(regexp); //check if target matches regular expression
					return result ? "1" : "0"; //if match is successful return 1 else return 0
				});
		match.setVariadic(false);
		return match; // return match
	}
	
	
	public BuiltinFunctionDefinitionNode TolowerFunction() {
		//create new BuiltinFunctionDefinition for toLower
		BuiltinFunctionDefinitionNode tolower = new BuiltinFunctionDefinitionNode ("tolower", new LinkedList<>(List.of("string")), new LinkedList<>(), 
				parameters -> {	
					String string = parameters.get("string").getValue(); // extract string parameterr from list of args
					return string.toLowerCase(); // return string in lower case
				});
		tolower.setVariadic(false);
		return tolower; //return to lower
	
	}
	
	
	public BuiltinFunctionDefinitionNode PrintfFunction() {
		// create new BuiltinFunctionDefinitionNode for printf function
		BuiltinFunctionDefinitionNode printf =  new BuiltinFunctionDefinitionNode("printf", new LinkedList<>(List.of("args")), new LinkedList<>(), 
				parameters -> {
					StringBuilder result = new StringBuilder(); // string builder to store results
					InterpreterArrayDataType arg = (InterpreterArrayDataType) parameters.get("args"); // args parameter
					for(int i=0; i < arg.getSize(); i ++) {//iterate through elements args
						if( i > 0) {
							result.append(" "); // append space
						}
						result.append(arg.getkeyvalue(Integer.toString(i))); //append value to results
					}
					System.out.print(result.toString()); // print out to console
					return " "; // return empty string
				});
			
			printf.setVariadic(true);
			return printf; // return printf
		}

	
	public BuiltinFunctionDefinitionNode GetLineFunction() {
		//create new BuiltinFunctionDefinition for getline
		BuiltinFunctionDefinitionNode getline = new BuiltinFunctionDefinitionNode ("getline", new LinkedList<>(List.of("args")), new LinkedList<>(), 
				parameters -> {
					linemanager.splitAndAssign(); // call line manager to split and assign paramters

					return " ";// return blank string
				});
				getline.setVariadic(false);
				return getline; // return getline
	}
	
	public BuiltinFunctionDefinitionNode GsubFunction() {
		//create new BuiltinFunctionDefinition for Gsub 
		BuiltinFunctionDefinitionNode gsub = new BuiltinFunctionDefinitionNode ("gsub", new LinkedList<>(List.of("regexp" , "replacement" , "target")), new LinkedList<>(), 
				parameters ->{
					String regexp= parameters.get("regexp").getValue(); // get regular expression
					String replacement= parameters.get("replacement").getValue(); // get replacement
					String target= parameters.get("target").getValue();// get target
					String result = target.replaceAll(regexp, replacement); // replacement occurs and is stored as result
					return result; // return result
				});
		gsub.setVariadic(false);
		return gsub; // return gsub
	}

	
	
	public BuiltinFunctionDefinitionNode subFunction() {
		//create new BuiltinFunctionDefinition for sub
		BuiltinFunctionDefinitionNode sub = new BuiltinFunctionDefinitionNode ("sub", new LinkedList<>(List.of("regexp" , "replacement" , "target")), new LinkedList<>(), 
				parameters ->{
					// get regexp, replacement, and target from parameters
					String regexp= parameters.get("regexp").getValue();
					String replacement= parameters.get("replacement").getValue();
					String target= parameters.get("target").getValue();
					String result = target.replaceFirst(regexp, replacement); //replace first occurence of regexp with replacement in target
					return result; // return result
				});
		sub.setVariadic(false);
		return sub; // return sub
	}
	
	public BuiltinFunctionDefinitionNode IndexFunction() {
		//create new BuiltinFunctionDefinition for index
		BuiltinFunctionDefinitionNode index = new BuiltinFunctionDefinitionNode ("Index", new LinkedList<>(List.of("substring", "string")), new LinkedList<>(), 
				parameters ->{
					String substring= parameters.get("substring").getValue();// get substring from parameters
					String string= parameters.get("string").getValue();// get string from parameters
					int i = string.indexOf(substring);//find index of substring in string 
					return Integer.toString(i); // return index as string 
				});
		index.setVariadic(false);
		return index; // return index
	}
	
	public BuiltinFunctionDefinitionNode LengthFunction() {
		//create new BuiltinFunctionDefinition for length
		BuiltinFunctionDefinitionNode length = new BuiltinFunctionDefinitionNode ("length", new LinkedList<>(List.of("string")), new LinkedList<>(), 
				parameters -> {
				String string = parameters.get("string").getValue(); // get string from parameters
				return Integer.toString(string.length()); // return string length
				});
				length.setVariadic(false);
				return length; // return length 
		
	}
	
	public BuiltinFunctionDefinitionNode SplitFunction() {
		//create new BuiltinFunctionDefinition for Split
		BuiltinFunctionDefinitionNode split = new BuiltinFunctionDefinitionNode ("split", new LinkedList<>(List.of("string", "seperator")), new LinkedList<>(), 
				parameters ->{	
				String string= ((InterpreterDataType)parameters.get("string")).getValue(); // extract string parameter from args
				String separator=  ((InterpreterDataType)parameters.get("seperator")).getValue();  // extract separator parameter from args
				String[] splits = string.split(separator) ; // split string into array using the separator
				InterpreterArrayDataType result = new InterpreterArrayDataType(); // new array data type
				for(int i=0; i < splits.length; i ++) { // populate array with split value
				 result.setkeyvalue(Integer.toString(i), new InterpreterDataType(splits[i]));
				}
				return result.toString(); // return result to string
				});
		split.setVariadic(false);
		return split; //return split
	}

	
	public BuiltinFunctionDefinitionNode  SubstrFunction() {
		//create new BuiltinFunctionDefinition for Substr
		BuiltinFunctionDefinitionNode substr = new BuiltinFunctionDefinitionNode ("substr", new LinkedList<>(List.of("string" , "start" , "length")), new LinkedList<>(), 
				parameters ->{	
					String string = parameters.get("string").getValue();// extract string parameterr from list of args
					int start = Integer.parseInt(((InterpreterDataType)parameters.get("start")).getValue());// extract start parameterr from list of args
					int length =Integer.parseInt(((InterpreterDataType)parameters.get("length")).getValue());// extract length parameterr from list of args
					if(start < 1) {
						start = 1; //check if start is in bounds
					}
					if(start < string.length()) { 
						return " "; // if start in within string length
					}
				int end = Math.min(start + length - 1, string.length()); // calculate end
				return string.substring(start -1 , end);// extract substring
	});
		substr.setVariadic(false);
	return substr; //return substr
	}	
	
	
	public InterpreterDataType GetIDT(Node node, HashMap<String, InterpreterDataType> lVariables) throws Exception{
		if(node instanceof AssignmentNode) {
			AssignmentNode Assignment = (AssignmentNode) node;
			Node target =  Assignment.getTarget().get();
			if(target instanceof VariableReferenceNode || target instanceof OperationNode) {
				InterpreterDataType result = GetIDT(Assignment.getExpression().get(),lVariables);
				String Variable = target.toString();
				map.put(Variable, result);
				return result;			
				}else {
					throw new RuntimeException("Invalid Target Error");
				}
			}else if(node instanceof ConstantNode) {
				ConstantNode Constant = (ConstantNode) node;
				 InterpreterDataType ret = new InterpreterDataType(Constant.getValue()); 
				 return ret;
				
			}else if(node instanceof CallFunctionNode) {
				CallFunctionNode CallFunction = (CallFunctionNode) node;
				return new InterpreterDataType(RunFunctionCall(CallFunction, lVariables));
			}else if(node instanceof PatternNode) {
				throw new RuntimeException("Invalid usage of Pattern Node");
			}else if(node instanceof TernaryNode) {
				TernaryNode ternary = (TernaryNode) node;
				InterpreterDataType result = GetIDT(ternary.getCondition().get(), lVariables);
				if(result.getValue().equals("1")){
					return GetIDT(ternary.getTrue().get(), lVariables);
				}else{
					return GetIDT(ternary.getFalse().get(), lVariables);
				}
			}else if(node instanceof VariableReferenceNode) {
				VariableReferenceNode VariableReference = (VariableReferenceNode) node;
				String Vname = VariableReference.toString();
				if(Vname.startsWith("$")) {
					return map.get(Vname);
				}else if(lVariables != null && lVariables.containsKey(Vname)) {
					return lVariables.get(Vname);
				}else {
					throw new RuntimeException("Could not find " + Vname + "variable");
				}
			}else if(node instanceof OperationNode) {
				OperationNode operation = (OperationNode) node;
				InterpreterDataType left = GetIDT(operation.getLeft().get(), lVariables);
				if(operation.getRight() != null) {
				InterpreterDataType right = GetIDT(operation.getRight().get(), lVariables);
				String Op = operation.getOperation().toString();
				InterpreterDataType result = perfomOperation(left, right, Op);
				return result;
				}else {
					return left;
				}
			}else {
				throw new Exception("unrecognized Node error");
			}
		
		}

	private InterpreterDataType perfomOperation(InterpreterDataType left, InterpreterDataType right, String op) {
		if(isMathOperation(op)) {
			double leftValue = left.getFloatValue();
			double rightValue = right.getFloatValue();
			InterpreterDataType result = performMathOperation(leftValue, rightValue, op);
			return new InterpreterDataType(String.valueOf(result));
		}else if(isComparisonOperation(op)) {
			boolean result = performComparisonOperation(left, right, op);
			if(result) {
				return new InterpreterDataType("1");
			}else {
				return new InterpreterDataType("0");
			}
		}else if(isBooleanOperation(op)) {
			boolean lbool= left.getFloatValue() != 0;
			boolean rbool= right.getFloatValue() !=0;
			boolean result = performBooleanOperation(lbool, rbool, op);
			if(result) {
				return new InterpreterDataType("1");
			}else {
				return new InterpreterDataType("0");
			}
		}else if(isMatchOperation(op)) {
			return performMatchOperation(left, right, op);
		}else if("$".equals(op)) {
			return performDollarOperation(left);
		}else if(isUnaryOperation(op)){
		double unary = left.getFloatValue();
		double result = performUnaryOperation(unary, op);
		return new InterpreterDataType(String.valueOf(result));
		}else if("+".equals(op)) {
			return new InterpreterDataType(left.getValue() + right.getValue());
		}else if("IN".equals(op)) {
		InterpreterDataType array = map.get(right.getValue());
		if(array instanceof InterpreterArrayDataType) {
			String key = left.getValue();
			boolean result =  ((InterpreterArrayDataType) array).map.containsKey(key);
			if(result) {
				return new InterpreterDataType("1");
			}else if(!result){
				return new InterpreterDataType("0");
			}
		}else {
			throw new RuntimeException("Right Hand is not an Array, nor a Reference Variables");
		}
		}
			throw new UnsupportedOperationException("Unsupported or Invalid Operator");
		}
	

	private double performUnaryOperation(double unary, String op) {
			double result;
			if("+".equals(op)) {
				result = unary;
			}else if("-".equals(op)) {
				result = -unary;
			}else {
				throw new UnsupportedOperationException("Unsupported or Invalid Operator");
			}
			return result;
	}

	private boolean isUnaryOperation(String op) {
	
		return "+".equals(op) || "-".equals(op);
	}

	private InterpreterDataType performDollarOperation(InterpreterDataType left) {
		return map.get("$" + left.getValue());
	}



	private InterpreterDataType performMathOperation(double left, double right, String op) {
		double result;
		
		switch(op) {
		case "ADD":
			result = left + right;
			break;
		case "SUBTRACT":
			result = left - right;
			break;
		case "MULTIPLY":
			result = left * right;
			break;
		case "DIVIDE":
			result = left/right;
			break;
		case "Modulo":
			result = left % right;
			break;
		case "EXPONENT":
			result = Math.pow(left, right);
			break;
		default:
			throw new UnsupportedOperationException("Unsupported or Invalid Operator");
		}
		return new InterpreterDataType(String.valueOf(result));
	}

	private boolean isMathOperation(String op) {
		return "+".equals(op) || "-".equals(op) || "*".equals(op) || "/".equals(op) || "%".equals(op) || "^".equals(op);
	}

	private boolean performBooleanOperation(boolean lbool, boolean rbool, String op) {
		if("&&".equals(op)) {
			return lbool && rbool;
		}else if("||".equals(op)) {
			return lbool || rbool;
		}else if( "!".equals(op)) {
			return !lbool;
		}else {
			throw new UnsupportedOperationException("Unsupported or Invalid Operator");
		}
	}

	private boolean isBooleanOperation(String op) {
		return "&&".equals(op) || "||".equals(op) || "!".equals(op);
	}

	private boolean performComparisonOperation(InterpreterDataType left, InterpreterDataType right, String op) {
		if("==".equals(op)) {
			return left.getValue().equals(right.getValue());
		}else if("!=".equals(op)) {
			return !left.getValue().equals(right.getValue());
		}else if("<".equals(op)) {
			return left.getValue().compareTo(right.getValue()) < 0;
		}else if("<=".equals(op)) {
			return  left.getValue().compareTo(right.getValue()) <= 0;
		}else if(">".equals(op)) {
			return  left.getValue().compareTo(right.getValue()) > 0;
		}else if(">=".equals(op)) {
			return  left.getValue().compareTo(right.getValue()) >= 0;
		}else {
			throw new UnsupportedOperationException("Unsupported or Invalid Operator");
		}
	}

	private boolean isComparisonOperation(String op) {
		// TODO Auto-generated method stub
		return "==".equals(op) || "!=".equals(op) || "<".equals(op) || "<=".equals(op) || ">".equals(op) || ">=".equals(op);
	}

	private InterpreterDataType performMatchOperation(InterpreterDataType leftValue, InterpreterDataType rightValue, String op) {
		String left = leftValue.getValue();
		String right = rightValue.getValue();
		boolean result;
		if("~".equals(op)) {
			result = right.matches(left);
			return new InterpreterDataType("1");
		}else if("~!".equals(op)) {
			result = !right.matches(left);
			return new InterpreterDataType("0");
		}else {
			throw new UnsupportedOperationException("Unsupported or Invalid Operator");
		}
		
		
	}

	private boolean isMatchOperation(String op) {
		// TODO Auto-generated method stub
		return "~".equals(op) || "~!".equals(op);
	}
	
	public ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode stmt) throws Exception {
		if(stmt instanceof AssignmentNode) {
		AssignmentNode Assignment = (AssignmentNode) stmt;
		InterpreterDataType rightV= GetIDT(Assignment.getExpression().get(), locals); // evaluate right side
		Node target = Assignment.getTarget().get(); // get target 
		if(target instanceof VariableReferenceNode || target instanceof OperationNode) { 
			InterpreterDataType result= GetIDT(Assignment.getExpression().get(), locals); // evaluate target with GetIDT
			String Variable = target.toString();
			locals.put(Variable, result); // add assigmnent to local variables hashmap
			return new ReturnType(ReturnType.Statustype.Normal, rightV.getValue());
		}else {
			throw new RuntimeException("Error: Invalid Target");
		}
		}else if(stmt instanceof BreakNode) {
			return new ReturnType(ReturnType.Statustype.Break);
		}else if(stmt instanceof ContinueNode) {
			return new ReturnType(ReturnType.Statustype.Continue);
		}else if(stmt instanceof DeleteNode) {
			DeleteNode delete = (DeleteNode) stmt;
			InterpreterDataType array = locals.getOrDefault(delete.getName(), map.get(delete.getName())); //get array from local variables if not found then in global variables
			if (array instanceof InterpreterArrayDataType) {
				InterpreterArrayDataType data = (InterpreterArrayDataType) array;
				if(delete.getIndex() != null && !delete.getIndex().isEmpty()) {
					for(int i = 0; i < delete.getIndex().size(); i++) {
						Node index = delete.getIndex().get(i); //extract index for deletion
						InterpreterDataType indexValue = GetIDT(index, locals); //evaluate index value
						if(indexValue != null) {
							data.map.remove(indexValue.getValue());//remove at specified index
						}
					}
				}else {
					data.map.clear(); //clears entire array if index not specified
				}
				return new ReturnType(ReturnType.Statustype.Normal);
			}else {
				throw new RuntimeException("Error: Not an array");
			}	
		}else if(stmt instanceof DoWhileNode) {
			DoWhileNode DoWhile = (DoWhileNode) stmt;
			InterpreterDataType result;
			do {
				//calling Interpret List of statements in do while loop
				ReturnType type = InterpretListofStatements(DoWhile.rest.getStatements(), locals);
			    result = GetIDT(DoWhile.Condition.orElse(null), locals); // used GETIDT to evaluate condition
				if(type != null) {
					if(type.getType() == ReturnType.Statustype.Break) {
						break; //if return type is break, break loop
					}
				}
				
			}while(result.getFloatValue()!=0); //do until condition is met
			return new ReturnType(ReturnType.Statustype.Normal);
			
		}else if(stmt instanceof ForNode) {
			ForNode For = (ForNode) stmt;
			ReturnType.Statustype type ;
			if(For.getInitial().isPresent()) {
				ProcessStatement(locals, (StatementNode) For.getInitial().get());//recursively processes initial 
			}
			while(true) {
				Node Condition = For.getCondition().get(); // extract condition
				boolean Cresult = false;
				
				if(For.getCondition().isPresent()) {
					InterpreterDataType conditionvalue = GetIDT(Condition, locals); // evaluate condition
					Cresult = conditionvalue.getFloatValue() != 0;  
					if(!Cresult) {
						break; //break loop if conditon is false
					}
				} 
				
				ReturnType result = InterpretListofStatements(For.getRest().getStatements(), locals); //get result from Interprete list of statements
				if(result.getType() == ReturnType.Statustype.Break) {
					 type = ReturnType.Statustype.Break;
					 return new ReturnType(type);
				}else if(result.getType() == ReturnType.Statustype.Continue) {
					 type = ReturnType.Statustype.Continue;
					 return new ReturnType(type);
				}
			}
		}else if(stmt instanceof ForEachNode) {
			ForEachNode forEach = (ForEachNode) stmt;
			
			Node array = forEach.getIterable().get();
			InterpreterDataType data = GetIDT(array, locals); 
			if(data instanceof InterpreterArrayDataType) { // check if data is array
				InterpreterArrayDataType ArrayData = (InterpreterArrayDataType)	data;
				Set<String> keyset = ArrayData.map.keySet(); //get keyset from map
				Iterator<String>Iterator = keyset.iterator(); //set iterator
				while(Iterator.hasNext()) { 
				String key = Iterator.next();//get current key
				locals.put(forEach.getVariable(), new InterpreterDataType(key) );//Set variables in locals to current key
				ReturnType type = InterpretListofStatements(forEach.getRest().getStatements(), locals); //Interpret statements inside ForEachNode
				if (type != null) {
					if(type.getType() == ReturnType.Statustype.Break) {
						return new ReturnType(ReturnType.Statustype.Break); //return break
					}else if(type.getType() == ReturnType.Statustype.Continue){
						return new ReturnType(ReturnType.Statustype.Continue); //return continue
					}
				}
				
				}
				return new ReturnType(ReturnType.Statustype.Normal);
			}else {
				throw new RuntimeException("Error: Not an array"); //if not instance of interpreterarray data type throw exception
			}
			
		}else if(stmt instanceof CallFunctionNode) {
			CallFunctionNode callfunction = (CallFunctionNode) stmt;
			RunFunctionCall(callfunction, locals); // call Runfunctioncall method
			return new ReturnType(ReturnType.Statustype.Normal);
		}else if(stmt instanceof IfNode) {
			IfNode ifNode = (IfNode) stmt;
			
			while(ifNode != null) {
				InterpreterDataType Ifresult = GetIDT(ifNode.Condition.get(), locals); // evaluate condition
				boolean Cresult = Ifresult.getFloatValue() != 0; 
				if(!ifNode.Condition.isPresent() || Cresult) {
					ReturnType result = InterpretListofStatements(ifNode.Statements.getStatements(), locals); // calls method if condition is empty or evaluates to true
					if(result != null && result.getType() != ReturnType.Statustype.Normal) {
						return result; // return result 
					}
				}else {
					ifNode= (IfNode)ifNode.next.orElse(null); // if condition is false check next ifnode in list
				}
			}
		}else if(stmt instanceof ReturnNode) {
			ReturnNode returnNode = (ReturnNode) stmt;
			
			if(returnNode.getReturn().isPresent()) {
				InterpreterDataType result = GetIDT(returnNode.getReturn().get(), locals); // evaluate return if it exists
				return new ReturnType(ReturnType.Statustype.Return, result.getValue());// make new return type with result and return status
			}
					
		}else if(stmt instanceof WhileNode) {
			WhileNode While = (WhileNode) stmt;
			InterpreterDataType result;
			while(true) {
				result = GetIDT(While.Condition.orElse(null), locals);// evaluate condition using GETIDT
				
				if(result.getFloatValue() == 0) {//break out of loop if condition is false
					break;
				}
				ReturnType type = InterpretListofStatements(While.rest.getStatements(), locals);// call  interpret list of statements in while loop
				
				if(type != null) {// check return values of interpret list of statements
					if(type.getType() == ReturnType.Statustype.Break) {
						break; // if it is break, break out of loop
					}
				}
			}
			return new ReturnType(ReturnType.Statustype.Normal);
		}
			throw new Exception("Error: Invalid or Unsupported StatementNode");
		

	}

	private String RunFunctionCall(CallFunctionNode callfunction, HashMap<String, InterpreterDataType> locals) throws Exception {
		FunctionDefinitionNode functionDefinition = null; // initialize function def
		for (int i = 0; i < ProgramNode.getFunctionDNode().size(); i++) { //iterate through function definition list
		    if (ProgramNode.getFunctionDNode().get(i).getFunctionName().equals(callfunction.getName())) { // look for match in function name
		        functionDefinition = ProgramNode.getFunctionDNode().get(i);
		        break; // once match is found break loop
		    }
		}

		if(functionDefinition == null) {
			throw new RuntimeException("Function does not exist"); //exception if function name not found
		}
		
		LinkedList<Node> Param = callfunction.getParam();  //get paramertes from function call
		int pCount = Param.size(); //get # of parameters
		
		if(callfunction.getParameterCount() != pCount) { 
			throw new RuntimeException("Parameters do not match"); // throw exception if parameter counts dont match
		}
		
		HashMap<String, InterpreterDataType> flocals = new HashMap<>(); // initialize local hashmap
		int i =0;
		LinkedList<String> fparam = functionDefinition.getParameters(); //initialize paramter list
		LinkedList<InterpreterDataType> variadic = new LinkedList<>(); //initialize variadic values list
		
		while(i<pCount) {
			String parameter = fparam.get(i);
			Node Currentparam =  Param.get(i);
			
			if(((BuiltinFunctionDefinitionNode) functionDefinition).isVariadic() && i == functionDefinition.getParameters().size()-1) {  // handle variadic parameters
				variadic.add(GetIDT(Currentparam, locals));
			} else {
				flocals.put(parameter, GetIDT(Currentparam, locals)); // map non variadic paramters
			}
			i++; //iterate i
		}
		
		if(((BuiltinFunctionDefinitionNode) functionDefinition).isVariadic()) { // handle varidic function
	String VariadicParamName = fparam.getLast();
	map.put(VariadicParamName, new InterpreterArrayDataType(variadic)); //map to variadic list 
		}
		
		 Object result = ((BuiltinFunctionDefinitionNode) functionDefinition).executing(map); //execute function, store result
		 
		 if(result != null) {
			 return result.toString(); //return result if it exists
		 }else {
			 return " "; // return null if doesnt exist
		 }
	}
	
	

	

	


	private ReturnType InterpretListofStatements( LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
		for(int i = 0; i < statements.size(); i++) {
			StatementNode statement = statements.get(i);
			ReturnType result = ProcessStatement(locals, statement);
			if(result != null && result.getType() != ReturnType.Statustype.Normal) { // check return type of each process statement
				return result; // return passing up same return type
			}
		}
		return new ReturnType(ReturnType.Statustype.Normal); // if eveerything is processed without Break, or continue, return normal
	}
	
	
	public void InterpretProgram(ProgramNode programNode) throws Exception {
		List<BlockNode> BeginB = programNode.getBeginBNode(); // get Begin blocks and put them into list
		for(int i = 0; i < BeginB.size(); i++) {
			BlockNode block = BeginB.get(i);   // extract block node
			InterpretBlock(block);          // interpret block          
		}
		
		
		linemanager.splitAndAssign();  //call line manager
		
		List<BlockNode> OtherB = programNode.getOtherBNode(); // get Other blocks and put them into list
		for(int i = 0; i < BeginB.size(); i++) {
		BlockNode block = OtherB.get(i);		// extract block node
		InterpretBlock(block);       //interpret block
		}
		
		List<BlockNode> EndB = programNode.getEndBNode(); // get end blocks and put them into list
		for(int i = 0; i < EndB.size(); i++) {
		BlockNode block = EndB.get(i); // extract block node
		InterpretBlock(block);   //interpret block
		}
	}

	private void InterpretBlock(BlockNode block) throws Exception {
		Optional<Node> condition = block.getCondition();  //extract condition from block
		
		if(!condition.isPresent()  ) { //check if conditon is present
			LinkedList<StatementNode> statements = block.getStatements(); 
			InterpretListofStatements(statements, map); //if conditon is get statements and interpret them 
		}
		
	}
	
	
		
	
	}

