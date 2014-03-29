
public class Aux {
	
	//Compute the weight of a directed edge based on the contamination times
	public static double computePc(Contagion c, int origin, int target, double alpha, String model){
		
		double tOrigin = c.getInfectionTime(origin);
		double tTarget = c.getInfectionTime(target);
		
		double delta = tTarget -  tOrigin;
		
		if(delta <= 0){
			return 0D;
		}
		
		//exponential-law: P(i,j) \propto exp(-delta/alpha)
		if(model.equals("exponential-law")){
			return Math.exp( - delta / alpha);
		}
		//power-law: P(i,j) \propto 1 / delta^alpha
		else if(model.equals("power-law")){
			return Math.pow(delta, -alpha);
		}
		
		System.err.println("Error: provided metric does not exist");
		System.exit(1);
		return 0D;
	}

}
