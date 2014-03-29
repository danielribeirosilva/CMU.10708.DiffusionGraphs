import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Contagion {
	 
	private HashMap<Integer,Double> infectionTimes; //fast access of infection time of a node
	private int[] infectionOrder; //important for efficiently building weights on graph
	private String name;
	
	//auxiliary class for sorting id and time arrays together
	public class NodeTime implements Comparable<NodeTime>{
		private int vertex;
		private double time;
		
		public NodeTime(int vertex, double time){
			this.time = time;
			this.vertex = vertex;
		}
		public int compareTo(NodeTime nt) {
			return Double.compare(this.time, nt.time);
		}
		public int getVertex(){
			return this.vertex;
		}
	}
	
	//--------------------------------------------------------------------------------
	// CONSTRUCTORS
	//--------------------------------------------------------------------------------
	
	public Contagion(int[] vertices, double[] infectionTime){
		assert(vertices.length == infectionTime.length);
		
		int n = vertices.length;
		this.infectionTimes = new HashMap<Integer,Double>();
		this.name = "";
		
		ArrayList<NodeTime> pairList = new ArrayList<NodeTime>();
		for(int i=0; i<n; i++){
			this.infectionTimes.put(vertices[i], infectionTime[i]);
			NodeTime nt = new NodeTime(vertices[i], infectionTime[i]); 
			pairList.add(nt);
		}
		Collections.sort(pairList);
		this.infectionOrder = new int[n];
		for(int i=0; i<n; i++){
			this.infectionOrder[i] = pairList.get(i).getVertex();
		}
	}
	
	//--------------------------------------------------------------------------------
	// GETTERS & SETTERS
	//--------------------------------------------------------------------------------
	
	public double getInfectionTime(int vertex){
		if(this.infectionTimes.containsKey(vertex)){
			return this.infectionTimes.get(vertex);
		}
		return Constants.INFECTION_TIME_OF_UNINFECTED_NODE;
	}
	
	public int[] getInfectedNodesOrdered(){
		return this.infectionOrder;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean containsNode(int i){
		return this.infectionTimes.containsKey(i);
	}
	
	
	
	
	
	
	//--------------------------------------------------------------------------------
	// DEBUG
	//--------------------------------------------------------------------------------
	public void printInfectionOrder(){
		System.out.println("Contagion "+this.name);
		for(int i=0; i<this.infectionOrder.length; i++){
			System.out.print(String.valueOf(this.infectionOrder[i])+" ");
		}
		System.out.println();
	}

}
