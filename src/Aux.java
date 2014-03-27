
public class Aux {
	
	public static double computeWeight(double tOrigin, double tTarget, double alpha, String model){
		
		// probability is zero it tO>=tT (time flows in one direction)
		if(tTarget <= tOrigin){
			return 0D;
		}
		
		//exponential-law: P(i,j) \propto exp(-delta/alpha)
		if(model.equals("exponential-law")){
			double delta = tTarget -  tOrigin;
			return Math.exp( - delta / alpha);
		}
		//power-law: P(i,j) \propto 1 / delta^alpha
		else if(model.equals("power-law")){
			double delta = tTarget -  tOrigin;
			return Math.pow(delta, -alpha);
		}
		
		System.err.println("Error: provided metric does not exist");
		System.exit(1);
		return 0D;
	}

}
