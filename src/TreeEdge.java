
public class TreeEdge extends Edge{
	
	private double weight;


	//--------------------------------------------------------------------------------
	// CONSTRUCTORS
	//--------------------------------------------------------------------------------
	
	//Constructor 1 - for case where beta is constant
	public TreeEdge(int i, int j, double weight) {
		super(i, j);
		this.weight = weight;
	}
	
	//Constructor 2 - for case where beta is learned for each edge
	/*
	public TreeEdge(int i, int j, double weight, double beta) {
		super(i, j, beta);
		this.weight = weight;
	}
	*/
	
	//--------------------------------------------------------------------------------
	// GETTERS AND SETTERS
	//--------------------------------------------------------------------------------
	
	public double getWeight(){
		return this.weight;
	}
	
	public void setWeight(double w){
		this.weight = w;
	}

}
