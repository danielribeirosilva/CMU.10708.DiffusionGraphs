import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;


public class RunExperimentOne {

	
	
	
	public static void main(String[] args) throws Exception {
		
		//input files
		String pathContagion = args[0];
		String pathNodesName = args[1];
		
		
		//structures needed
		HashMap<Integer,Contagion> contagions = new HashMap<Integer,Contagion>();
		HashMap<Integer,HashMap<Integer, Integer>> contagionMap = new HashMap<Integer,HashMap<Integer, Integer>>();
		
		//graph parameters
		double epsilon = 0.9;
		double beta = 0.99;
		String model = "exponential-law";
		
		
		//READ CONTAGION/CASCADES FILE
		BufferedReader br = new BufferedReader(new FileReader(pathContagion));
		String line;
		int x=0;
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\\t+");
			int c = Integer.parseInt(lineSplit[0]);
			int n = Integer.parseInt(lineSplit[1]);
			int t = Integer.parseInt(lineSplit[2]);
			
			//if contagion is new create lists for this contagion
			if( !contagionMap.containsKey(c) ){
				contagionMap.put(c, new HashMap<Integer, Integer>());
			}
			
			//add infection to cascade
			contagionMap.get(c).put(n, t);
			
			if(x++%1000000 == 0 ){System.out.println(x);}
		}
		System.out.println("contagion read!");
		
		//Generate contagion
		int realC = 0;
		Iterator<Integer> it = contagionMap.keySet().iterator();
		while(it.hasNext()){
			
			if(contagionMap.size()%1000000 == 0){ System.out.println("c left: " + String.valueOf(contagionMap.size())); }
			
			int c = it.next();
			
			int[] nodesArray = new int[contagionMap.get(c).size()];
			double[] timesArray = new double[contagionMap.get(c).size()];
			
			int j=0;
			for(int n : contagionMap.get(c).keySet() ){
				nodesArray[j] = n;
				timesArray[j] = contagionMap.get(c).get(n);
				j++;
			}
			
			it.remove();
			
			//contagions.put(c, new Contagion(nodesArray,timesArray));
			//only add cascades with at least 3 nodes
			if(nodesArray.length > 2){
				contagions.put(realC, new Contagion(nodesArray,timesArray));
				realC++;
				System.out.println("c size: " + String.valueOf(nodesArray.length));
			}
		}
		
		System.out.println("contagion generated!");
		contagionMap.clear();
		br.close();
		
		
		
		//READ NODES' NAME FILE
		//count total nodes
		br = new BufferedReader(new FileReader(pathNodesName));
		int totalNodes = 0;
		while ((line = br.readLine()) != null) {
			totalNodes++;
		}
		System.out.println("nodes counted!");
		br.close();
		
		//initialize graph
		Graph G = new Graph(totalNodes, epsilon, beta, model);
		
		//add nodes to graph
		br = new BufferedReader(new FileReader(pathNodesName));
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\\t+");
			String n_name = lineSplit[0];
			int n_idx = Integer.parseInt(lineSplit[1]);
			G.addNode(n_idx, n_name);
		}
		System.out.println("nodes added to graph!");
		
		//add contagion to graph
		for( int c : contagions.keySet() ){
			G.addContagion(contagions.get(c));
		}
		System.out.println("contagion added to graph!");
		
		int k = 2*totalNodes;
		G.buildNetInfGraph(k);
		
		System.out.println("FINAL EDGES:");
		G.printEdges();
		G.outputGraph("NETINFgraphLOCAL.txt", "NETINFgraphTRUE.txt");
		
	}
	
	
	
	

}
