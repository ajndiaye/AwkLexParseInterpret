
public class InterpreterDataType {

	String Value;
	
	public InterpreterDataType(String Value) {
		this.Value = Value;
	}
	
	
	
	public InterpreterDataType() {
		this.Value = null;
	}

public double getFloatValue() {
	if(Value != null) {
		try {
			return Double.parseDouble(Value);
		}catch(NumberFormatException e) {
			throw new UnsupportedOperationException("Invalid Float Value");
		}
		
	}else {
		return 0;
	}
}

	public String getValue() {
		// TODO Auto-generated method stub
		return Value;
	}
}
