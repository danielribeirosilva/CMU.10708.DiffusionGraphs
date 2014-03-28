import java.util.ArrayList;
import java.util.HashMap;


public class Tree{
	
	private int totalNodes;
	private ArrayList<HashMap<Integer,TreeEdge>> tree;
	private double epsilon;
	private double beta;
	private Contagion c;
	private String model;
	private HashMap<Integer,Integer> edgesIndexedByTarget;
	
	//--------------------------------------------------------------------------------
	// CONSTRUCTORS
	//--------------------------------------------------------------------------------
	
	public Tree(Contagion c, int totalNodes, double epsilon, double beta, String model) {
		this.c = c;
		this.model = model;
		this.totalNodes = totalNodes;
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
	
	public int getParentFromNode(int son){
		return this.edgesIndexedByTarget.get(son);
	}

		
	//--------------------------------------------------------------------------------
	// STRUCTURE MODIFIERS
	//--------------------------------------------------------------------------------
	
	public void addEdge(int i, int j, double weight){
		TreeEdge e = new TreeEdge(i, j, weight);
		this.tree.get(i).put(j, e);
		this.edgesIndexedByTarget.put(j, i);
	}
	
	public void removeEdge(int i, int j){
		if(this.tree.get(i).containsKey(j)){
			this.tree.get(i).remove(j);
			this.edgesIndexedByTarget.remove(j);
		}
	}
	
	public void changeParent(int son, int newParent, double newWeight){
		int oldParent = this.getParentFromNode(son);
		this.removeEdge(oldParent, son);
		this.addEdge(newParent, son, newWeight);
	}
	
	

}
