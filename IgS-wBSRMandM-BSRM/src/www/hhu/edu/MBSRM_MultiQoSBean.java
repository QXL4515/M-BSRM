package www.hhu.edu;

public class MBSRM_MultiQoSBean {
	String ip;
	String wsId;
	double responsetime;
	double throughput;
	double reliability;
	double  availability;
	
	public MBSRM_MultiQoSBean(String ip, String wsId, double responsetime, double throughput, double reliability2,
			double availability2) {
		super();
		this.ip = ip;
		this.wsId = wsId;
		this.responsetime = responsetime;
		this.throughput = throughput;
		this.reliability = reliability2;
		this.availability = availability2;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getWsId() {
		return wsId;
	}
	public void setWsId(String wsId) {
		this.wsId = wsId;
	}
	public double getResponsetime() {
		return responsetime;
	}
	public void setResponsetime(double responsetime) {
		this.responsetime = responsetime;
	}
	public double getThroughput() {
		return throughput;
	}
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	public double getReliability() {
		return reliability;
	}
	public void setReliability(int reliability) {
		this.reliability = reliability;
	}
	public double getAvailability() {
		return availability;
	}
	public void setAvailability(int availability) {
		this.availability = availability;
	}
	@Override
	public String toString() {
		return "MultiQoSBean [ip=" + ip + ", wsId=" + wsId + ", responsetime=" + responsetime + ", throughput="
				+ throughput + ", reliability=" + reliability + ", availability=" + availability + "]";
	}
	
	
	
	
}
