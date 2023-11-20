import java.util.LinkedList;

public class ProgramNode extends Node{
LinkedList<BlockNode> EndBNode= new LinkedList<>(); // declare Block Node Linked lists
LinkedList<BlockNode>OtherBNode= new LinkedList<>();
LinkedList<BlockNode> BeginBNode = new LinkedList<>();

static LinkedList<FunctionDefinitionNode> FunctionDNode= new LinkedList<>(); // declare function definition


public ProgramNode( LinkedList<BlockNode> BeginBNode , LinkedList<BlockNode> EndBNode , LinkedList<BlockNode> OtherBNode , LinkedList<FunctionDefinitionNode> FunctionDNode ) {
this.OtherBNode=OtherBNode; // constructor
this.BeginBNode=BeginBNode;
this.EndBNode=EndBNode;
ProgramNode.FunctionDNode=FunctionDNode;
}

@Override
public String toString() { //toString method

return null;
}

public void addFunctionDef(FunctionDefinitionNode Block) { //Method to add to functiondefinition node
FunctionDNode.add(Block);
}

public void addBBlock(BlockNode Block) { //method to add begin block to list
BeginBNode.add(Block);
}

public LinkedList<BlockNode> getEndBNode() {
	return EndBNode;
}

public LinkedList<BlockNode> getOtherBNode() {
	return OtherBNode;
}

public LinkedList<BlockNode> getBeginBNode() {
	return BeginBNode;
}

public static LinkedList<FunctionDefinitionNode> getFunctionDNode() {
	return FunctionDNode;
}

public void addOBlock(BlockNode Block) { // method to add other block node to list
OtherBNode.add(Block);
}
public void addEBlock(BlockNode Block) { // method to add ending block to list
EndBNode.add(Block);
}

}

