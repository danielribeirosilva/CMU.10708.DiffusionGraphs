import java.util.Arrays;
import java.util.HashMap;


public class Contagion {
	 
	private HashMap<Integer,Double> infectionTimes; //fast access of infection time of a node
	private int[] infectionOrder; //important for efficiently building weights on graph
	private String name;
	
	//auxiliary class for sorting id and time arrays together
	private class NodeTime implements Comparable<NodeTime>{
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
	
	public Contagion(int[] vertices, double[] infectionTime){
		assert(vertices.length == infectionTime.length);
		int n = vertices.length;
		NodeTime[] pairArray = new NodeTime[n];
		for(int i=0; i<n; i++){
			this.infectionTimes.put(vertices[i], infectionTime[i]);
			pairArray[i] = new NodeTime(vertices[i], infectionTime[i]);
		}
		Arrays.sort(pairArray);
		this.infectionOrder = new int[n];
		for(int i=0; i<n; i++){
			this.infectionOrder[i] = pairArray[i].getVertex();
		}
	}
	
	public double getInfectonTime(int vertex){
		if(this.infectionTimes.containsKey(vertex)){
			return this.infectionTimes.get(vertex);
		}
		return Constants.INFECTION_TIME_OF_UNINFECTED_NODE;
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

}
