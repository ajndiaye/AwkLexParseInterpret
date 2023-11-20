import java.util.HashMap;
import java.util.LinkedList;

public class InterpreterArrayDataType extends InterpreterDataType {
	
	
	HashMap<String, InterpreterDataType> map;

public InterpreterArrayDataType() {
	this.map = new HashMap<>();
}

public InterpreterArrayDataType(LinkedList<InterpreterDataType> v) {
	this.map = new HashMap<>();
	for(int i =0; i < v.size(); i++ ) {
		map.put(Integer.toString(i), v.get(i));
	}
}

public int getSize() {
	return map.size();
}

public String getkeyvalue(String key) {
	InterpreterDataType value = map.get(key);
	return value.getValue();
}

public void setkeyvalue(String key, InterpreterDataType value) {
map.put(key, value);
}

}