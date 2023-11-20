
public class StringHandler { 
	
	
	 String HoldDoc; //Document holder string
	 int index; 
	
	//constructor
	public StringHandler(String HoldDoc) {
		this.HoldDoc= HoldDoc;
	}

	// peek check character at point "i"
public char Peek(int i) {
	if (index + i < HoldDoc.length()) {
		return HoldDoc.charAt(index + i );	
	}
	else {
		return 0;
	}
}
// returns String of the next specified number of characters
public String PeekString(int i) {
	if(i > 0 && index + i <= HoldDoc.length()) {
		return HoldDoc.substring(index, index + i);
	}
	else {
		return "end of file";
	}
}

// gets character at specific index and moves index forward
public char GetChar() {
	if(index < HoldDoc.length()) {
		char CurrentChar = HoldDoc.charAt(index); 
		index ++;
		return CurrentChar;
	}
	else {
		return 0;
	}
	
}

//moves index certain number of "i" ahead
public void Swallow(int i) {
	if(i >= 0 && index < HoldDoc.length() ) {
		 index += i;
	}  else {
		index = index + 0;
	}
	
}   

public boolean isDone() { // checks if at end of doc
 return index >= HoldDoc.length();
}

public String Remainder() { //returns rest of document as a string
	if(index < HoldDoc.length()) {
		return HoldDoc.substring(index);
	}
	else {
		return "end of file";
	}
}

}	

