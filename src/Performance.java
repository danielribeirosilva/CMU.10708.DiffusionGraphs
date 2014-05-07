import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;


public class Performance {

	
	public static void main(String[] args) throws Exception {
		
		//input files
		String pathNetinf = args[0];
		String pathGroundTruth = args[1];
		
		//nodes
		HashSet<Integer> usedNodes = new HashSet<Integer>(); 
		HashSet<String> predictedEdges = new HashSet<String>();
		HashSet<String> trueEdges = new HashSet<String>();
		
		//read netinf result
		BufferedReader br = new BufferedReader(new FileReader(pathNetinf));
		
		String line;
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\\t+");
			usedNodes.add(Integer.parseInt(lineSplit[0]));
			usedNodes.add(Integer.parseInt(lineSplit[1]));
			predictedEdges.add(line);
		}
		br.close();
		
		//ground truth nodes
		br = new BufferedReader(new FileReader(pathGroundTruth));
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\\t+");
			int A = Integer.parseInt(lineSplit[0]);
			int B = Integer.parseInt(lineSplit[1]);
			
			if(usedNodes.contains(A) && usedNodes.contains(B) && A!=B){
				trueEdges.add(line);
				
				//trueEdges.add(String.valueOf(B)+"\t"+String.valueOf(A));
			}
		}
		br.close();
		
		
		//stats
		int totalTrue = trueEdges.size();
		int totalPredicted = predictedEdges.size();
		
		int totalRight = 0;
		for( String p : predictedEdges){
			if(trueEdges.contains(p)){
				totalRight++;
			}
		}
		
		//performance
		double precision = (double)totalRight / (double)totalPredicted;
		double recall = (double)totalRight / (double)totalTrue;
		
		System.out.println("totalTrue: " + String.valueOf(totalTrue));
		System.out.println("totalPredicted: " + String.valueOf(totalPredicted));
		System.out.println("totalRight: " + String.valueOf(totalRight));
		System.out.println("precision: " + String.valueOf(precision));
		System.out.println("recall: " + String.valueOf(recall));

	}

}
