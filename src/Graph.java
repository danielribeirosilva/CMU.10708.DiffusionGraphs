import java.util.ArrayList;
import java.util.HashMap;


// structure is meant for a sparse graph: only store non-zero values
// But since we use epsilon-edges, strictly speaking, no edge will be zero
// so we only store values that are "non-epsilon" 
public class Graph {
	
	private int totalNodes; 
	private ArrayList<HashMap<Integer,Edge>> graph;
	private double epsilon;
	private double beta;
	private ArrayList<Contagion> contagions;
	
	//for case where beta is learned for each edge
	public Graph(int totalNodes, double epsilon){
		this.totalNodes = totalNodes;
		this.epsilon = epsilon;
		this.graph = new ArrayList<HashMap<Integer,Edge>>(totalNodes);
		for(int i=0; i<totalNodes; i++){
			graph.add(i, new HashMap<Integer,Edge>());
		}
		this.contagions = new ArrayList<Contagion>();
	}
	
	//for case where beta is constant across graph
	public Graph(int n, double epsilon, double beta){
		this.epsilon = epsilon;
		this.beta = beta;
		this.graph = new ArrayList<HashMap<Integer,Edge>>(n);
		for(int i=0; i<n; i++){
			graph.add(i, new HashMap<Integer,Edge>());
			//graph.get(i).put(i, new Edge(1D-beta));
		}
	}
	
	public double getGraphEpsilon(){
		return this.epsilon;
	}
	
	public double getGraphBeta(){
		return this.beta;
	}
	
	public int getTotalNodes(){
		return this.totalNodes;
	}
	
	public void addEdge(int i, int j, double weight){
		Edge e = new Edge(weight);
		this.graph.get(i).put(j, e);
	}
	
	public void removeEdge(int i, int j){
		if(this.graph.get(i).containsKey(j)){
			this.graph.get(i).remove(j);
		}
	}
	
	public void addContagion(Contagion c){
		this.contagions.add(c);
	}
	
	public double getEdgeWeight(int contagionIdx, int origin, int target, String model){
		Contagion c = this.contagions.get(contagionIdx);
		
		//if either node is not contaminated
		if(!c.containsNode(origin) || !c.containsNode(target)){
			return 0D;
		}
		
		double tOrigin = c.getInfectonTime(origin);
		double tTarget = c.getInfectonTime(target);	
		return Aux.computeWeight(tOrigin, tTarget, Constants.alpha, model);
	}
	
	//Algorithm for Maximum Weight Directed Spanning Tree of a DAG (Algorithm 1 in NetInf Paper)
	public Graph maximumSpanningTree(int contagionIndex, int[] vertices, String model){
		Graph tree = new Graph(vertices.length, this.epsilon);
		
		for(int i=0; i<vertices.length; i++){
			double maxWeight = 0D;
			int maxWeightIndex = -1;
			for(int j=0; j<vertices.length; j++){
				if(i==j){
					continue;
				}
				double currentWeight = getEdgeWeight(contagionIndex,vertices[i],vertices[j],model); 
				if( maxWeight < currentWeight){
					maxWeight = currentWeight;
					maxWeightIndex = j;
				}
			}
			tree.addEdge(vertices[i], vertices[maxWeightIndex], maxWeight);
		}
		return tree;
	} 
	

}
