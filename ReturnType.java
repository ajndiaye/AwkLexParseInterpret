
public class ReturnType {
public enum Statustype{
	Normal,
	Break,
	Continue,
	Return
}

Statustype type;
String Value;

public ReturnType(Statustype type, String Value){
	this.type = type;
	this.Value = Value;
}

public ReturnType(Statustype type) {
	this.type = type;
}

public String getValue(String Value) {
return Value;
}

public Statustype getType() {
	return type;
}

@Override
public String toString() {
	if(Value != null) {
	return type.toString() + " : " + Value;
	}else {
	 return type.toString();
	}
}

}