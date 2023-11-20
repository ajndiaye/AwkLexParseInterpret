
public class ConstantNode extends Node {

	 static String Value;
	
	@SuppressWarnings("static-access")
	public ConstantNode(String Value) {
		this.Value = Value;
	}
	
	
	@Override
	public String toString() {
		return Value;
	}


	public String getValue() {
		return Value;
	}

}
