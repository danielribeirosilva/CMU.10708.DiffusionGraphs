import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


// structure is meant for a sparse graph: only store non-zero values
// But since we use epsilon-edges, strictly speaking, no edge will be zero
// so we only store values that are "non-epsilon" 
public class Graph {
	
	private int totalNodes; 
	private int totalNetworkEdges;
	private ArrayList<HashMap<Integer,Edge>> graph;
	private double epsilon;
	private double beta;
	private ArrayList<Contagion> contagions;
	private String model;
	
	//for case where beta is learned for each edge
	public Graph(int totalNodes, double epsilon, String model){
		this.model = model;
		this.totalNodes = totalNodes;
		this.totalNetworkEdges = 0;
		this.epsilon = epsilon;
		this.graph = new ArrayList<HashMap<Integer,Edge>>(totalNodes);
		for(int i=0; i<totalNodes; i++){
			graph.add(i, new HashMap<Integer,Edge>());
		}
		this.contagions = new ArrayList<Contagion>();
	}
	
	//for case where beta is constant across graph
	public Graph(int totalNodes, double epsilon, double beta, String model){
		this.model = model;
		this.totalNodes = totalNodes;
		this.totalNetworkEdges = 0;
		this.epsilon = epsilon;
		this.beta = beta;
		this.graph = new ArrayList<HashMap<Integer,Edge>>(totalNodes);
		for(int i=0; i<totalNodes; i++){
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
	
	public int getTotalNetworkEdges(){
		return this.totalNetworkEdges;
	}
	
	public void increaseTotalNetworkEdgesCounterBy(int i){
		this.totalNetworkEdges += i;
	}
	
	public void addContagion(Contagion c){
		this.contagions.add(c);
	}
	
	public double computeEdgePcPrime(int contagionIdx, int origin, int target){
		Contagion c = this.contagions.get(contagionIdx);
		
		double tOrigin = c.getInfectonTime(origin);
		double tTarget = c.getInfectonTime(target);
		
		// probability is 0 if tO>=tT (time flows in one direction)
		if(tTarget <= tOrigin){
			return 0D;
		}
		
		//if target node is not contaminated
		if(!c.containsNode(target)){
			//if is network edge
			if(this.graph.get(origin).containsKey(target)){
				return 1D - this.beta;
			}
			//else (if is epsilon-edge)
			else{
				return 1D - this.epsilon;
			}
		}
		
		//else (if target node is contaminated)
		else{
			double Pc = Aux.computePc(tOrigin, tTarget, Constants.alpha, model);
			//if is network edge
			if(this.graph.get(origin).containsKey(target)){
				return this.beta * Pc;
			}
			//else (if is epsilon-edge)
			else{
				return this.epsilon * Pc;
			}
		}
	}
	
	public double computeEdgeWc(int contagionIdx, int origin, int target){
		double PcPrime = computeEdgePcPrime(contagionIdx, origin, target);
		Contagion c = this.contagions.get(contagionIdx);
		double tOrigin = c.getInfectonTime(origin);
		double tTarget = c.getInfectonTime(target);
		double Pc = Aux.computePc(tOrigin, tTarget, Constants.alpha, this.model);
		
		return Math.log(PcPrime) - Math.log(this.epsilon*Pc);
	}
	
	//Algorithm for Maximum Weight Directed Spanning Tree of a DAG (Algorithm 1 in NetInf Paper)
	public Graph maximumSpanningTree(int contagionIndex, int[] vertices){
		Graph tree = new Graph(vertices.length, this.epsilon, this.model);
		
		for(int i=0; i<vertices.length; i++){
			double maxWeight = 0D;
			int maxWeightIndex = -1;
			for(int j=0; j<vertices.length; j++){
				if(i==j){
					continue;
				}
				double currentWeight = computeEdgePcPrime(contagionIndex,vertices[j],vertices[i],this.model); 
				if( maxWeight < currentWeight){
					maxWeight = currentWeight;
					maxWeightIndex = j;
				}
			}
			tree.addEdge(vertices[maxWeightIndex], vertices[i], maxWeight);
		}
		return tree;
	} 
	
	//NetInf algorithm
	public void buildNetInfGraph(int k){
		int cSize = contagions.size();
		Graph[] dagTree = new Graph[cSize];
		for(int i=0;i<cSize; i++){
			dagTree[i] = maximumSpanningTree(i, contagions.get(i).getInfectedNodesOrdered());
		}
		while(this.totalNetworkEdges < k){
			for(int i=0; i<totalNodes; i++){
				for(int j=0; j<totalNodes; j++){
					//we want edges (i!=j)
					if(i==j){
						continue;
					}
					// we want (j,i) not in graph
					if(graph.get(j).containsKey(i)){
						continue;
					}
					double marginalImprovementJI = 0D;
					HashSet<Integer> Mji = new HashSet<Integer>();
					
					for(int cIdx=0; cIdx<cSize; cIdx++){
						//we only want contagions that contain i and j
						Contagion currentC = this.contagions.get(cIdx);
						if(!currentC.containsNode(i) || !currentC.containsNode(j)){
							continue;
						}
						//we only want contagions such (j,i) has positive weight
						if(currentC.getInfectonTime(j) >= currentC.getInfectonTime(i)){
							continue;
						}
						
						
						
						
					}
					
				}// for j
			}//for i
		}
		
	}
	

}
