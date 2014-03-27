
public class Edge {
	private int vertexI;
	private int vertexJ;
	private double weight;
	private double beta;
	
	//for case where beta is constant across graph
	public Edge (double weight){
		this.weight = weight;
	}
	
	//for case where beta is learned for each edge
	public Edge (int i, int j, double weight, double beta){
		this.vertexI = i;
		this.vertexJ = j;
		this.weight = weight;
		this.beta = beta;
	}
	
	public double getWeight(){
		return this.weight;
	}
	
	public void setWeight(double w){
		this.weight = w;
	}
	
	public double getBeta(){
		return this.beta;
	}
	
	public void setBeta(double b){
		this.beta = b;
	}
	
	public int getVertexI(){
		return this.vertexI;
	}
	
	public int getVertexJ(){
		return this.vertexJ;
	}
	
	
}
