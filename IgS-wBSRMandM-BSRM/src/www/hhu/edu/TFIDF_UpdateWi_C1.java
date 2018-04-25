package www.hhu.edu;

import java.util.HashMap;

public class TFIDF_UpdateWi_C1 implements TFIDF_UpdateWi {

	@Override
	public double computeWi(
			HashMap<String, String> ll,
			HashMap<HashMap<String, String>, Integer> ll2Num,
			HashMap<HashMap<String, String>, Integer> ll2C0,
			HashMap<HashMap<String, String>, Integer> ll2C1,
			double preset_value, int nC00,int nC11, int nn) {
//		HashMap<HashMap<String, String>, Double> ll2W1Temp = new HashMap<HashMap<String, String>, Double>();
		double nC0Ri;
		double nC0 =nC00;
		double nC1 = nC11;
		double n = nn;
		if(ll2C0.containsKey(ll)){
			nC0Ri = ll2C0.get(ll);
		}else{
			nC0Ri = 0;
		}
		if (ll2C1.containsKey(ll)) {
//			System.out.println("wi_c1 haha");
			double nC1Ri = ll2C1.get(ll);
			double nRi = ll2Num.get(ll);
			double igic1=		(
					-nC0/n*(Math.log(nC0/n)/Math.log(2.0))-nC1/n*(Math.log(nC1/n)/Math.log(2.0))
					+nRi/n*
					((nC1Ri+1)/(nRi+1)*(Math.log((nC1Ri+1)/(nRi+1))/Math.log(2.0))+(nC0Ri+1)/(nRi+1)*(Math.log((nC0Ri+1)/(nRi+1))/Math.log(2.0)))
					+(n-nRi)/n*
					((nC1-nC1Ri+1)/(n-nRi+1)*(Math.log((nC1-nC1Ri+1)/(n-nRi+1))/Math.log(2.0))+(nC0-nC0Ri+1)/(n-nRi+1)*(Math.log((nC0-nC0Ri+1)/(n-nRi+1))/Math.log(2.0)))
				);	
//			System.out.println("igic1= "+igic1);
			double wi = nC1Ri * 1.0 / nC1 * Math.log(n / nRi)*
					(
							-nC0/n*(Math.log(nC0/n)/Math.log(2.0))-nC1/n*(Math.log(nC1/n)/Math.log(2.0))
							+nRi/n*
							((nC1Ri+1)/(nRi+1)*(Math.log((nC1Ri+1)/(nRi+1))/Math.log(2.0))+(nC0Ri+1)/(nRi+1)*(Math.log((nC0Ri+1)/(nRi+1))/Math.log(2.0)))
							+(n-nRi)/n*
							((nC1-nC1Ri+1)/(n-nRi+1)*(Math.log((nC1-nC1Ri+1)/(n-nRi+1))/Math.log(2.0))+(nC0-nC0Ri+1)/(n-nRi+1)*(Math.log((nC0-nC0Ri+1)/(n-nRi+1))/Math.log(2.0)))
						);								//�˴�+1��������˹ƽ������																				
//			System.out.println("wi C1= "+wi+"|| nC0Ri= "+nC0Ri+" nRi= "+nRi+" n= "+n+" nC0= "+nC0+" nC1= "+nC1);
//			System.out.println("(nC1-nC1Ri)= "+(nC1-nC1Ri)+" (n-nRi+1)= "+(n-nRi+1)+"|| (nC0-nC0Ri)= "+(nC0-nC0Ri)+" (n-nRi+1) "+(n-nRi+1));
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return wi;
		} else {
			return preset_value;
		}
	}

}
