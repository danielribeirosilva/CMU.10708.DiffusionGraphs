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
	
	
	//--------------------------------------------------------------------------------
	// CONSTRUCTORS
	//--------------------------------------------------------------------------------
	
	//Constructor 1 - for case where beta is constant across graph
	public Graph(int totalNodes, double epsilon, double beta, String model){
		this.model = model;
		this.totalNodes = totalNodes;
		this.totalNetworkEdges = 0;
		this.epsilon = epsilon;
		this.beta = beta;
		this.graph = new ArrayList<HashMap<Integer,Edge>>(totalNodes);
		for(int i=0; i<totalNodes; i++){
			graph.add(i, new HashMap<Integer,Edge>());
		}
	}
	
	//Constructor 2 - for case where beta is learned for each edge
	/*
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
	*/
	
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
	
	public int getTotalNetworkEdges(){
		return this.totalNetworkEdges;
	}
	
	public String getGraphModel(){
		return this.model;
	}
	
	public Contagion getContagion(int cIdx){
		return this.contagions.get(cIdx);
	}
	
	//--------------------------------------------------------------------------------
	// STRUCTURE MODIFIERS
	//--------------------------------------------------------------------------------
	
	public void addEdge(int i, int j){
		Edge e = new Edge(i, j);
		if(!this.graph.get(i).containsKey(j)){
			this.totalNetworkEdges += 1;
		}
		this.graph.get(i).put(j, e);
	}
	
	public void removeEdge(int i, int j){
		if(this.graph.get(i).containsKey(j)){
			this.graph.get(i).remove(j);
			this.totalNetworkEdges -= 1;
		}
	}
	
	public void addContagion(Contagion c){
		this.contagions.add(c);
	}
	
	//--------------------------------------------------------------------------------
	// NETINF-RELATED COMPUTATIONS
	//--------------------------------------------------------------------------------
	
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
			double Pc = Aux.computePc(c, origin, target, Constants.alpha, this.model);
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
		double Pc = Aux.computePc(c, origin, target, Constants.alpha, this.model);
		
		return Math.log(PcPrime) - Math.log(this.epsilon*Pc);
	}
	
	
	//--------------------------------------------------------------------------------
	// ALGORITHMS
	//--------------------------------------------------------------------------------
	
	//Algorithm for Maximum Weight Directed Spanning Tree of a DAG (Algorithm 1 in NetInf Paper)
	public Tree maximumSpanningTree(int contagionIndex, int[] vertices){
		Tree maxSpanTree = new Tree(this.contagions.get(contagionIndex), vertices.length, this.epsilon, this.beta, this.model);
		Contagion c = this.contagions.get(contagionIndex);
		
		for(int i=0; i<vertices.length; i++){
			double maxWeight = 0D;
			int maxWeightIndex = -1;
			for(int j=0; j<vertices.length; j++){
				if(i==j){
					continue;
				}
				
				double currentWeight = Aux.computePc(c, vertices[j], vertices[i], Constants.alpha, this.model);
				if( maxWeight < currentWeight){
					maxWeight = currentWeight;
					maxWeightIndex = j;
				}
			}
			int originVertex = vertices[maxWeightIndex];
			int targetVertex = vertices[i]; 
			maxSpanTree.addEdge(originVertex, targetVertex, maxWeight);
		}
		return maxSpanTree;
	} 
	
	//NetInf algorithm
	public void buildNetInfGraph(int k){
		int cSize = contagions.size();
		Tree[] dagTree = new Tree[cSize];
		for(int i=0;i<cSize; i++){
			dagTree[i] = maximumSpanningTree(i, contagions.get(i).getInfectedNodesOrdered());
		}
		while(this.totalNetworkEdges < k){
			
			//variables for keeping the max
			double maxDeltaJI = Double.MIN_VALUE;
			HashSet<Integer> maxMji = new HashSet<Integer>();
			int iStar=-1, jStar=-1;
			
			//for all (j,i) not in G
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
					double currentDeltaJI = 0D;
					HashSet<Integer> currentMji = new HashSet<Integer>();
					
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
						
						//momentarily add (j,i) to the graph
						this.addEdge(j, i);
						
						double wji = computeEdgeWc(cIdx, j, i);
						double wjiTree = computeEdgeWc(cIdx, dagTree[cIdx].getParentFromNode(i), i);
						
						if(wji >= wjiTree){
							currentDeltaJI += wji - wjiTree;
							currentMji.add(cIdx);
						}
						
						//remove the momentary (j,i) from G
						this.removeEdge(j, i);
						
					}// for c
					
					//keep best edge
					if(maxDeltaJI < currentDeltaJI){
						maxDeltaJI = currentDeltaJI;
						maxMji = currentMji;
						iStar = i;
						jStar = j;
					}
					
				}// for j
			}//for i
			
			//permanently add best edge to the graph
			this.addEdge(jStar, iStar);
			
			//update trees
			for(int c : maxMji){
				double newWeight = Aux.computePc(this.getContagion(c), jStar, iStar, Constants.alpha, this.model);
				dagTree[c].changeParent(iStar, jStar, newWeight);
			}
			
		}//while |G|<k
	}//NetInf end
	

}//Graph class end
