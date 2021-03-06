
public class Edge {
	private int vertexI;
	private int vertexJ;
	private double beta;
	
	
	//--------------------------------------------------------------------------------
	// CONSTRUCTORS
	//--------------------------------------------------------------------------------
	
	//Constructor 1 - for case where beta is constant
	public Edge (int i, int j){
		this.vertexI = i;
		this.vertexJ = j;
	}
	
	//Constructor 2 - for case where beta is learned for each edge
	/*
	public Edge (int i, int j, double beta){
		this.vertexI = i;
		this.vertexJ = j;
		this.beta = beta;
	}
	*/
	
	
	
	//--------------------------------------------------------------------------------
	// GETTERS AND SETTERS
	//--------------------------------------------------------------------------------
	
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
