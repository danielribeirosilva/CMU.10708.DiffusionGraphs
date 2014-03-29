
public class Run {
	
	public static void main(String[] args) {
		
		//dummy data 
		//int totalNodes = 11;
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
		cA.setName("A");
		Contagion cB= new Contagion(verticesB,timesB);
		cB.setName("B");
		Contagion cC= new Contagion(verticesC,timesC);
		cC.setName("C");
		
		cA.printInfectionOrder();
		cB.printInfectionOrder();
		cC.printInfectionOrder();
		System.out.println("--------------");
		Graph g = new Graph(11, epsilon, beta, model);
		g.addContagion(cA);
		g.addContagion(cB);
		g.addContagion(cC);
		System.out.println("max spanning tree");
		cA.printInfectionOrder();
		g.maximumSpanningTree(0, new int[]{2,3,4,5,6}).printEdges();
		System.out.println("--------------");
		cB.printInfectionOrder();
		g.maximumSpanningTree(1, new int[]{3,5,6,7,8}).printEdges();
		System.out.println("--------------");
		cC.printInfectionOrder();
		g.maximumSpanningTree(2, new int[]{2,3,5,6,7}).printEdges();
		System.out.println("--------------");
		
		int from = 3;
		int to = 10;
		
		for(int k=from; k<=to; k++){
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
			
			G.buildNetInfGraph(k);
			System.out.println("k="+String.valueOf(k));
			G.printEdges();
			System.out.println("--------------");
		}
		

	}

}
