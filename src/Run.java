
public class Run {
	
	public static void main(String[] args) {
		
		//dummy data 
		int totalNodes = 11;
		double epsilon = 0.9;
		double beta = 0.99;
		String model = "exponential-law";
		
		int[] verticesA = new int[]{0,1,2,3,4,5,6,7};
		double[] timesA = new double[]{1,2,4,6,7,10,23,25};
		
		int[] verticesB = new int[]{2,5,3,6,7,8,10,1};
		double[] timesB = new double[]{1,4,5,6,7,7,9,10};
		
		int[] verticesC = new int[]{2,5,3,6,7,8,10,1};
		double[] timesC = new double[]{7,2,4,9,1,5,12,9};

		
		Contagion cA = new Contagion(verticesA,timesA);
		Contagion cB= new Contagion(verticesB,timesB);
		Contagion cC= new Contagion(verticesC,timesC);
		
		Tree t = new Tree(cA, new int[]{0,1,2,3,4,5,6,7,8,9,10}, epsilon, beta, model);
		t.addEdgeByIndex(1, 3, 0.54);
		t.addEdgeByIndex(3, 4, 0.1);
		t.addEdgeByIndex(3, 5, 0.65);
		t.addEdgeByIndex(5, 7, 0.65);
		t.printEdges();
		System.out.println("--------------");
		
		Graph g = new Graph(totalNodes, epsilon, beta, model);
		g.addContagion(cA);
		g.addContagion(cB);
		g.addContagion(cC);
		
		
		int[] mstVertices = new int[]{2,3,5,6};
		Tree mst = g.maximumSpanningTree(2, mstVertices);
		cC.printInfectionOrder();
		mst.printEdges();
		System.out.println("--------------");
		
		Graph G = new Graph(epsilon, beta, model);
		G.addNode("A");
		G.addNode("B");
		G.addNode("C");
		G.addNode("D");
		G.addNode("E");
		G.addNode("F");
		G.addNode("G");
		G.addNode("H");
		G.addNode("I");
		G.addNode("J");
		G.addNode("K");
		G.addNode("L");
		G.addNode("M");
		G.addContagion(cA);
		G.addContagion(cB);
		G.addContagion(cC);
		G.printNodes();
		System.out.println("--------------");
		
		G.buildNetInfGraph(3);
		G.printEdges();
		

	}

}
