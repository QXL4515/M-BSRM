package www.hhu.edu;

public class MBSRM_ComprehensiveQoSBean {
	String envirnmentalFactor;
	double comprehensiveQoSValue;
	
	@Override
	public String toString() {
		return "ComprehensiveQoSBean [envirnmentalFactor=" + envirnmentalFactor + ", comprehensiveQoSValue="
				+ comprehensiveQoSValue + "]";
	}
	public MBSRM_ComprehensiveQoSBean(String envirnmentalFactor, double comprehensiveQoSValue) {
		super();
		this.envirnmentalFactor = envirnmentalFactor;
		this.comprehensiveQoSValue = comprehensiveQoSValue;
	}
	public String getEnvirnmentalFactor() {
		return envirnmentalFactor;
	}
	public void setEnvirnmentalFactor(String envirnmentalFactor) {
		this.envirnmentalFactor = envirnmentalFactor;
	}
	public double getComprehensiveQoSValue() {
		return comprehensiveQoSValue;
	}
	public void setComprehensiveQoSValue(double comprehensiveQoSValue) {
		this.comprehensiveQoSValue = comprehensiveQoSValue;
	}
}
