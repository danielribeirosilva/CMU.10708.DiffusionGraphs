import java.util.ArrayList;
import java.util.HashMap;


public class Tree{
	
	private int totalNodes;
	private int[] nodeLabels; //label is the index of the node on the original graph G
	private HashMap<Integer,Integer> labelIndex;
	private ArrayList<HashMap<Integer,TreeEdge>> tree;
	private double epsilon;
	private double beta;
	private Contagion c;
	private String model;
	private HashMap<Integer,Integer> edgesIndexedByTarget;
	
	//--------------------------------------------------------------------------------
	// CONSTRUCTORS
	//--------------------------------------------------------------------------------
	
	public Tree(Contagion c, int[] nodeLabels, double epsilon, double beta, String model) {
		this.c = c;
		this.model = model;
		this.nodeLabels = nodeLabels;
		this.totalNodes = nodeLabels.length;
		this.labelIndex = new HashMap<Integer,Integer>();
		for(int i=0; i<nodeLabels.length; i++){
			this.labelIndex.put(this.nodeLabels[i], i);
		}
		this.epsilon = epsilon;
		this.tree = new ArrayList<HashMap<Integer,TreeEdge>>(totalNodes);
		for(int i=0; i<totalNodes; i++){
			tree.add(i, new HashMap<Integer,TreeEdge>());
		}
		this.edgesIndexedByTarget = new HashMap<Integer,Integer>();
	}
	
	//--------------------------------------------------------------------------------
	// GETTERS
	//--------------------------------------------------------------------------------
	public double getGraphEpsilon(){
		return this.epsilon;
	}
	
	public double getGraphBeta(){
		return this.beta;
	}
	
	public int getTotalNodes(){
		return this.totalNodes;
	}
	
	public String getGraphModel(){
		return this.model;
	}
	
	public Contagion getContagion(){
		return this.c;
	}
	
	public int getParentFromNodeByIndex(int sonIdx){
		return this.edgesIndexedByTarget.get(sonIdx);
	}
	
	public int getParentFromNodeByOriginalIndex(int son){
		int sonIdx = getNodeIndex(son);
		return getParentFromNodeByIndex(sonIdx);
	}
	
	public int getNodeIndex(int nodeLabel){
		return this.labelIndex.get(nodeLabel);
	}

		
	//--------------------------------------------------------------------------------
	// STRUCTURE MODIFIERS
	//--------------------------------------------------------------------------------
	
	public void addEdgeByIndex(int i, int j, double weight){
		TreeEdge e = new TreeEdge(i, j, weight);
		this.tree.get(i).put(j, e);
		this.edgesIndexedByTarget.put(j, i);
	}
	
	public void addEdgeByOriginalIndex(int origin, int target, double weight){
		int i = getNodeIndex(origin);
		int j = getNodeIndex(target);
		addEdgeByIndex(i, j, weight);
	}
	
	public void removeEdgeByIndex(int i, int j){
		if(this.tree.get(i).containsKey(j)){
			this.tree.get(i).remove(j);
			this.edgesIndexedByTarget.remove(j);
		}
	}
	
	public void removeEdgeByOriginalIndex(int origin, int target){
		int i = getNodeIndex(origin);
		int j = getNodeIndex(target);
		removeEdgeByIndex(i, j);
	}
	
	public void changeParentByIndex(int sonIdx, int newParentIdx, double newWeight){
		int oldParentIdx = getParentFromNodeByIndex(sonIdx);
		this.removeEdgeByIndex(oldParentIdx, sonIdx);
		this.addEdgeByIndex(newParentIdx, sonIdx, newWeight);
	}
	
	public void changeParentByOriginalIndex(int son, int newParent, double newWeight){
		int sonIdx = getNodeIndex(son);
		int newParentIdx = getNodeIndex(newParent);
		changeParentByIndex(sonIdx, newParentIdx, newWeight);
	}
	
	//--------------------------------------------------------------------------------
	// AUX METHODS
	//--------------------------------------------------------------------------------
	
	public int findRoot(){
		
		
		return 0;
	}
	
	
	//--------------------------------------------------------------------------------
	// DEBUG
	//--------------------------------------------------------------------------------
	
	public void printEdges() {
        for(int target : this.edgesIndexedByTarget.keySet()){
        	String originLabel = String.valueOf(this.nodeLabels[this.edgesIndexedByTarget.get(target)]);
        	String targetLabel = String.valueOf(this.nodeLabels[target]);
        	System.out.println(originLabel+"->"+targetLabel);
        }
    }

}
