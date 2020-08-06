/**
 * State.java file maintains the attribute values for each generated node or state.
 * @author Monesa
 * @author Chandan
 * @author Aparajitha
 */

public class State {
	int rowValue, columnValue;
	Integer[][] nodesTobeAdded = new Integer[3][3];
	Integer[][] currentNode = new Integer[3][3];
	int stepCost;
	int nodeNumber;
	int parentKey;
	
	public State(int rowValue, int columnValue) {
		this.rowValue = rowValue;
		this.columnValue = columnValue;
	}
	
	public State(Integer[][] nodesTobeAdded, int stepCost, int nodeNumber) {
		this.nodesTobeAdded = nodesTobeAdded;
		this.stepCost = stepCost;
		this.nodeNumber = nodeNumber;
	}
	
	public State(Integer[][] currentNode, int parentKey) {
		this.currentNode = currentNode;
		this.parentKey = parentKey;
	}

	public State() {

	}
}
