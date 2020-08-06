import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
/**
 * AStarSeacrhAlgorithm uses A* algorithm to find the optimal path for a randomly given input and output state. 
 * @author Monesa
 * @author Chandan
 * @author Aparajitha
 */
public class AStarSearchAlgorithm {

	//Class variables
	public int rowIndexForSpace    = 0;
	public int columnIndexForSpace = 0;
	public int generatedNodesCount = 0;
	public int algorithm           = 0;
	public HashMap<Integer, State>						goalStateHashMap 	= new HashMap<Integer, State>();
	public HashMap<Integer, State> 						currentStateHashMap = new HashMap<Integer, State>();
	public HashMap<Integer, ArrayList<State>> 	   		generatedNodesMap 	= new HashMap<>();
	public ArrayList<Integer[][]> 						visitedNodes 		= new ArrayList<Integer[][]>();

	//Class Implementation
	//Logic for A* Search algorithm
	public LinkedHashMap<Integer, State> AstarCall(State state, Integer[][] goalState, LinkedHashMap<Integer,State> goalPath) {

		Integer[][] currentState = state.nodesTobeAdded;
		visitedNodes.add(currentState);
		int actualCost = state.stepCost;
		int parentKey = -1;
		int nodeNumber = state.nodeNumber;
		visitedNodes.add(currentState);

		while( Arrays.deepEquals(currentState , goalState) == false ) {
			System.out.println();
			System.out.println("\nGenerated Nodes : ");
			System.out.println("-----------------");
			generateNodes(currentState, actualCost);

			//Break after 10000 levels
			if(actualCost > 10000) {
				goalPath.clear();
				break;
			}
			goalPath.put(parentKey, new State(currentState, nodeNumber));
			parentKey = nodeNumber;

			if(generatedNodesMap.size()>0) {
				Integer key = generatedNodesMap.keySet().iterator().next();
				currentState = generatedNodesMap.get(key).get(0).nodesTobeAdded;
				actualCost = generatedNodesMap.get(key).get(0).stepCost;
				nodeNumber = generatedNodesMap.get(key).get(0).nodeNumber;
				generatedNodesMap.get(key).remove(0);
				if(generatedNodesMap.get(key).size() <= 0) {
					generatedNodesMap.remove(key);
				}
			}else {
				goalPath.clear();
				break;
			}
			System.out.println("--------------------------------------------------------");
			System.out.print("Node Chosen for Level " +actualCost +" - ");
			displayNode(currentState, true);
			visitedNodes.add(currentState);
		}
		goalPath.put(parentKey, new State(currentState, nodeNumber));
		return goalPath;
	}

	/**
	 * ************************Check whether node is previously generated**********
	 * @param localNode
	 * @param visitedNodes
	 * @return
	 */
	private boolean isVistedBefore(Integer[][] localNode, ArrayList<Integer[][]> visitedNodes) {
		// TODO Auto-generated method stub
		for(Integer[][] Node : visitedNodes) {
			if(Arrays.deepEquals(Node, localNode)){
				return true;
			}
		}
		return false;
	}


	/**
	 * Add Nodes to Generated List and parentChildRelation HashMaps*****************
	 * @param localNode
	 * @param currentNode
	 * @param heuristic
	 * @param cost
	 */
	private void addNodes(Integer[][] localNode, Integer[][] currentNode, int heuristic, int cost) {
		//Add Node to generatedNodes HashMap
		ArrayList<State> nodesToBeAdded = new ArrayList<>();
		if(generatedNodesMap.containsKey(heuristic)) {
			nodesToBeAdded = generatedNodesMap.get(heuristic);
		}
		nodesToBeAdded.add(new State(localNode, (cost+1),generatedNodesCount));
		generatedNodesMap.put(heuristic, nodesToBeAdded);
	}

	/**
	 * ***************Calculate Manhattan heuristic********************************
	 */
	private void calculateTotalHeuristic(int actualCost, Integer[][] localNode, Integer[][] currentNode) {
		int heuristicCost = 0;
		//calculating the heuristic cost for Manhattan
		if(algorithm == 1) {
			for(int iterator = 1; iterator < goalStateHashMap.size(); iterator++) {
				heuristicCost = heuristicCost 
						+ Math.abs( goalStateHashMap.get(iterator).rowValue   - currentStateHashMap.get(iterator).rowValue) 
						+ Math.abs(goalStateHashMap.get(iterator).columnValue - currentStateHashMap.get(iterator).columnValue);

			}
		}else if(algorithm == 2){
			//calculating the heuristic cost for Misplaced tiles
			for(int iterator = 1; iterator < goalStateHashMap.size(); iterator++) {
				if(goalStateHashMap.get(iterator).rowValue!= currentStateHashMap.get(iterator).rowValue 
						|| goalStateHashMap.get(iterator).columnValue != currentStateHashMap.get(iterator).columnValue){
					heuristicCost = heuristicCost+1;
				}
			}
		}
		int currentHeuristic = heuristicCost + actualCost;
		System.out.print(" - f(n) =  "+actualCost+ " + " + heuristicCost +" = "+currentHeuristic+"\n");

		//Adding nodes to Generated List
		addNodes(localNode, currentNode, currentHeuristic, actualCost);

	}

	/**
	 ************************************Generate Nodes***********************************
	 * @param currentState
	 * @param actualCost
	 */
	private void generateNodes(Integer[][] currentState, int actualCost) {
		//Case 1: space moving up
		if( rowIndexForSpace != 0) {
			Integer[][] tmpNode = new Integer[3][3];
			//Copying current State to  tmp Node
			for(int i = 0;i < currentState.length; i++ ) {
				for(int j =0 ; j < currentState[i].length; j++) {
					tmpNode[i][j] = currentState[i][j];
				}
			}
			tmpNode[rowIndexForSpace][columnIndexForSpace]     = tmpNode[rowIndexForSpace - 1][columnIndexForSpace];
			tmpNode[rowIndexForSpace - 1][columnIndexForSpace] = 0;

			//Display the node
			displayNode(tmpNode, false);

			//Increase the node count
			generatedNodesCount++;

			//Check the node whether it is visited or not
			if(!isVistedBefore(tmpNode, visitedNodes)) {
				calculateTotalHeuristic(actualCost, tmpNode, currentState);
			}else {
				System.out.println(" - Already visited Node!");
			}
		}

		//Case 2: Space moving left
		if( columnIndexForSpace != 2) {
			//Copying current State to  tmp Node
			Integer[][] tmpNode = new Integer[3][3];
			for(int i = 0;i < currentState.length; i++ ) {
				for(int j =0 ; j < currentState[i].length; j++) {
					tmpNode[i][j] = currentState[i][j];
				}
			}
			tmpNode[rowIndexForSpace][columnIndexForSpace]     = tmpNode[rowIndexForSpace][columnIndexForSpace + 1];
			tmpNode[rowIndexForSpace][columnIndexForSpace + 1] = 0;

			//Display the node
			displayNode(tmpNode, false);

			//Increase the node count
			generatedNodesCount++;

			//Check the node whether it is visited or not
			if(!isVistedBefore(tmpNode, visitedNodes)) {
				calculateTotalHeuristic(actualCost, tmpNode, currentState);
			}else {
				System.out.println(" - Already visited Node!");
			}

		}

		//Case 3: Space moving right
		if( columnIndexForSpace != 0) {
			//Copying current State to  tmp Node
			Integer[][] tmpNode = new Integer[3][3];
			for(int i = 0;i < currentState.length; i++ ) {
				for(int j =0 ; j < currentState[i].length; j++) {
					tmpNode[i][j] = currentState[i][j];
				}
			}
			tmpNode[rowIndexForSpace][columnIndexForSpace]     = tmpNode[rowIndexForSpace][columnIndexForSpace - 1];
			tmpNode[rowIndexForSpace][columnIndexForSpace - 1] = 0;

			//Display the node
			displayNode(tmpNode, false);

			//Increase the node count
			generatedNodesCount++;

			//Check the node whether it is visited or not
			if(!isVistedBefore(tmpNode, visitedNodes)) {
				calculateTotalHeuristic(actualCost, tmpNode, currentState);
			}else {
				System.out.println(" - Already visited Node!");
			}

		}

		//Case 4: Space moving down
		if( rowIndexForSpace != 2) {
			//Copying current State to  tmp Node
			Integer[][] tmpNode = new Integer[3][3];
			for(int i = 0;i < currentState.length; i++ ) {
				for(int j =0 ; j < currentState[i].length; j++) {
					tmpNode[i][j] = currentState[i][j];
				}
			}
			tmpNode[rowIndexForSpace][columnIndexForSpace] 	= tmpNode[rowIndexForSpace + 1][columnIndexForSpace];
			tmpNode[rowIndexForSpace + 1][columnIndexForSpace]= 0;

			//Display the node
			displayNode(tmpNode, false);

			//Increase the node count
			generatedNodesCount++;

			//Check the node whether it is visited or not
			if(!isVistedBefore(tmpNode, visitedNodes)) {
				calculateTotalHeuristic(actualCost, tmpNode, currentState);
			}else {
				System.out.println(" - Already visited Node!");
			}

		}
	}


	/**
	 **********************************Display Node*******************************
	 * @param displayNode
	 * @param isCurrentNode
	 */
	private void displayNode(Integer[][] displayNode, boolean isCurrentNode) {
		System.out.print("[");
		for(int i = 0; i < 3; i++) {
			System.out.print("[");
			for(int j = 0; j < 3; j++) {
				if(j == 2) {
					System.out.print(displayNode[i][j]);
				}else {
					System.out.print(displayNode[i][j]+",");
				}
				currentStateHashMap.put(displayNode[i][j], new State(i,j));
				if(displayNode[i][j] == 0 && isCurrentNode) {
					columnIndexForSpace = j;
					rowIndexForSpace = i;
				}
			}
			System.out.print("]");
		}
		System.out.print("]");
	}

	/**
	 ******************Display Goal Path****************************************
	 * @param goalPath
	 */
	private void displayGoalPath(LinkedHashMap<Integer, State> goalPath) {
		System.out.println("---------------------------");
		int i = -1;
		while(goalPath.get(i)!=null) {
			State stateGoal = goalPath.get(i);
			displayNode(stateGoal.currentNode, false);
			System.out.println();
			i = stateGoal.parentKey;
		};
		System.out.println("---------------------------");
	}

	/**
	 * *****************************Main Function*********************************
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("8-Puzzle problem using A* search Algorithm");

		//This is for the user entering current state
		System.out.println("Enter the Current State");
		Integer[][] currentState = new Integer[3][3];
		LinkedHashMap<Integer, State> goalPath = new LinkedHashMap<>();
		AStarSearchAlgorithm lo = new AStarSearchAlgorithm();

		for(int i = 0; i < 3; i++){
			for(int j = 0; j <3; j++) {
				currentState[i][j] = sc.nextInt();
				if(currentState[i][j] > 8) {
					System.out.println("Please enter a valid number from 0 - 8. Please re-run the algorithm");
					System.exit(0);
				}
				lo.currentStateHashMap.put(currentState[i][j], new State(i,j));
				if(currentState[i][j]==0) {
					lo.rowIndexForSpace = i;
					lo.columnIndexForSpace = j;
				}
			}
		}

		//This is for the user entering goal state
		System.out.println("Enter the Goal State");
		Integer[][] goalState = new Integer[3][3];

		for(int i = 0; i < 3; i++){
			for(int j = 0; j <3; j++) {
				goalState[i][j] = sc.nextInt();
				if(goalState[i][j] > 8) {
					System.out.println("Please enter a valid number from 0 - 8. Please re-run the algorithm");
					System.exit(0);
				}
				lo.goalStateHashMap.put(goalState[i][j], new State(i,j));
			}
		}

		System.out.println("Enter the heuristic number that you want to proceed with");

		System.out.println("1. Manhattan");
		System.out.println("2. Misplaced Tiles");

		lo.algorithm = sc.nextInt();

		System.out.println("--------------------------------------------------------");
		State state = new State();
		state.nodesTobeAdded = currentState;
		state.stepCost = 1;
		long startTime = 0;
		long endTime = 0;

		//Calling A* Algorithm
		if(Arrays.deepEquals(currentState, goalState )){
			//Initial check to find whether current state is goal state 
			System.out.print("Entered State is a goal State");
			lo.displayNode(currentState, true);
		}
		else {
			//Run the A*call and calculate the time taken to find the goal
			startTime = System.currentTimeMillis();
			System.out.print("Level 0 - ");
			lo.displayNode(state.nodesTobeAdded, true);
			System.out.print("\n--------------------------------------------------------");
			System.out.print("\nNode Chosen for Level 1 - ");
			lo.displayNode(state.nodesTobeAdded, true);
			goalPath 	   = lo.AstarCall(state, goalState, goalPath);
			endTime   = System.currentTimeMillis();
		}

		//Display the output after running A* Algorithm
		if(!goalPath.isEmpty()){  
			System.out.println("\nThe goal path found...");
			lo.displayGoalPath(goalPath);
			System.out.println("Time Taken : "+ (endTime - startTime) + " milliseconds");
			System.out.println("The number of nodes that are generated are : "+lo.generatedNodesCount);
			System.out.println("The number of nodes that are expanded are : "+goalPath.size());
		}else {
			System.out.print("The algorithm reached 10000 steps so, no solution can be found using A* search algorithm");
		}

	}


}
