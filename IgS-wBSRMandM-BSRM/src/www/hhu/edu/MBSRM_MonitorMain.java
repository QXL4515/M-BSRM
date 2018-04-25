package www.hhu.edu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MBSRM_MonitorMain {
	private static int MBSRM_N = 0;
	private static int MBSRM_nC0 = 0;//����QoS��
	private static int MBSRM_nC1 = 0;//������QoS��׼
	public static void main(String[] args) {
		double QoS_Value = 0.52;
		double beta = 0.8;
		long begin_n = 0;
		long end_n=0;
		List<MBSRM_MultiQoSBean> multiQoSDataList = new ArrayList<MBSRM_MultiQoSBean>();// ���ԭʼ��Ԫ���ݵ�list
		String dataPath = "G:\\wholedata.txt";
		File file = new File(dataPath);
		MBSRM_ReadData(multiQoSDataList, file);
		System.out.println("multiQoSDataList Initialization is complete");
		// ����Ϊ��Ӧʱ�������Ӧʱ����С�������������������С
		double[] maxAndMinValues = MBSRM_MaxAndMinValue(multiQoSDataList);// �����Ӧʱ�估���������������С������
		System.out.println("we get the maxandminValue!!!");
		
		
		List<MBSRM_ComprehensiveQoSBean> comprehensiveQoSBeanList = new ArrayList<>();
		/**
		 * �ۺ�QoSֵlist ComprehensiveQoSBean(��������,�ۺ�QoSֵ)
		 */
		MBSRM_getComprehensiveQoSBeanList(multiQoSDataList, maxAndMinValues, comprehensiveQoSBeanList);
		System.out.println("getComprehensiveQoSValue");
		System.out.println(comprehensiveQoSBeanList.size() + "���ۺ������������");
		// ѵ��Ȩֵ�����������
		Map<String, Integer> llNum = new HashMap<>();// ���Ӱ��������϶�ӦӰ��������ϳ��ֵĴ���
		Map<String, Integer> llNumC0 = new HashMap<>();// ���Ӱ��������϶�ӦӰ�����������������CO��Ĵ���
		Map<String, Integer> llNumC1 = new HashMap<>();// ���Ӱ��������϶�ӦӰ�����������������C1��Ĵ���
		Map<String, Double> llWi_C0 = new HashMap<>();
		Map<String, Double> llWi_C1 = new HashMap<>();
	
		// ѵ���׶�(ѵ��Ȩֵ)
		begin_n=System.currentTimeMillis();
		for (MBSRM_ComprehensiveQoSBean comprehensiveQoSBean : comprehensiveQoSBeanList) {
			MBSRM_N++;
			if (!llNum.containsKey(comprehensiveQoSBean.getEnvirnmentalFactor())) {
				llNum.put(comprehensiveQoSBean.getEnvirnmentalFactor(), 1);
			} else {
				llNum.put(comprehensiveQoSBean.getEnvirnmentalFactor(),
						llNum.get(comprehensiveQoSBean.getEnvirnmentalFactor()) + 1);
			}
			// ����ֵ
			if (!llNumC0.containsKey(comprehensiveQoSBean.getEnvirnmentalFactor())) {
				llNumC0.put(comprehensiveQoSBean.getEnvirnmentalFactor(), 0);
			}
			// ����ֵ�������PMIʱ���ֿ�ָ���쳣
			if (!llNumC1.containsKey(comprehensiveQoSBean.getEnvirnmentalFactor())) {
				llNumC1.put(comprehensiveQoSBean.getEnvirnmentalFactor(), 0);
			}
			if (comprehensiveQoSBean.getComprehensiveQoSValue() >= QoS_Value) {
				MBSRM_nC0++;
				llNumC0.put(comprehensiveQoSBean.getEnvirnmentalFactor(),
						llNumC0.get(comprehensiveQoSBean.getEnvirnmentalFactor()) + 1);
			} else {
				MBSRM_nC1++;
				llNumC1.put(comprehensiveQoSBean.getEnvirnmentalFactor(),
						llNumC1.get(comprehensiveQoSBean.getEnvirnmentalFactor()) + 1);
			}
		}
		System.out.println("�ﵽ�ۺ�QoSֵ����������"+MBSRM_nC0);
		System.out.println("û�дﵽ�ۺ�QoSֵ����������"+MBSRM_nC1);
		for (String envirnmentalFactorString : llNum.keySet()) {
			if (!llWi_C0.containsKey(envirnmentalFactorString)) {
				double P_PMIC0 = (llNumC0.get(envirnmentalFactorString) + 1) * 1.0 / (MBSRM_nC0+2)
						/  (MBSRM_nC0 * 1.0 / MBSRM_N);
				
				double wi_C0=Math.log(1+P_PMIC0);
				//����Ϊ�˼�¼TF-IDFȨֵ
				double TF_IDFW0= (llNumC0.get(envirnmentalFactorString) + 1) * 1.0 / (MBSRM_nC0+2)*Math.log(MBSRM_N/llNum.get(envirnmentalFactorString));
				   try {

						FileWriter fw = new FileWriter("G:/TestResult/TF_IDFC0.txt", true);
						BufferedWriter bw = new BufferedWriter(fw);

						String G = "TF_IDFW0 = " +TF_IDFW0;
						bw.write(G);
						bw.newLine();
						bw.flush();
						fw.close();
						bw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				try {

					FileWriter fw = new FileWriter("G:/TestResult/P_PMIC0.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);

					String G = "P_PMIC0 = " +P_PMIC0;
					bw.write(G);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {

					FileWriter fw = new FileWriter("G:/TestResult/wi_C0.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);

					String G = "wi_C0 = " +wi_C0;
					bw.write(G);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				llWi_C0.put(envirnmentalFactorString, wi_C0);
				llWi_C0.put(envirnmentalFactorString, P_PMIC0);
			}
			if (!llWi_C1.containsKey(envirnmentalFactorString)) {
				double P_PMIC1=(llNumC1.get(envirnmentalFactorString)+1)* 1.0 / (MBSRM_nC1+2)
				 / (MBSRM_nC1 * 1.0 / MBSRM_N);
				
                double TF_IDFW1=(llNumC1.get(envirnmentalFactorString)+1)* 1.0 / (MBSRM_nC1+2)*Math.log(MBSRM_N/llNum.get(envirnmentalFactorString));
                try {

					FileWriter fw = new FileWriter("G:/TestResult/TF_IDFC1.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);

					String G = "TF_IDFW1 = " +TF_IDFW1;
					bw.write(G);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
                try {

					FileWriter fw = new FileWriter("G:/TestResult/P_PMIC1.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);

					String G = "P_PMIC1 = " +P_PMIC1;
					bw.write(G);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				double wi_C1 =Math.log(1+P_PMIC1);
				
				try {

					FileWriter fw = new FileWriter("G:/TestResult/wi_C1.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);

					String G = "wi_C1 = " +wi_C1;
					bw.write(G);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				llWi_C1.put(envirnmentalFactorString, wi_C1);
				llWi_C1.put(envirnmentalFactorString, P_PMIC1);
			}
		}
		end_n=System.currentTimeMillis();
		end_n=System.currentTimeMillis();
		try {
			FileWriter fw = new FileWriter("G:/TestResult/trainingtime.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(""+(end_n-begin_n));
			bw.newLine();
			bw.flush();
			fw.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(MBSRM_nC0);
		System.out.println(MBSRM_nC1);
		// �����������
		double pC0 = MBSRM_nC0 * 1.0 / MBSRM_N;
		double pC1 = MBSRM_nC1 * 1.0 / MBSRM_N;
		int sizeSlidingWindow = 200;// �������ڴ�С
		List<Double> record_QoS = new ArrayList<>();
		List<Integer> YorN = new ArrayList<Integer>();// ��¼�ɰܼ��飬Ϊ����������׼��
		int x = 0;// ͳ������QoS��ֵ��������
		int countX = 0;// ��¼������ʵ�ֻ�������
		int countC0 = 0;
		int countC1 = 0;
		double prePro_C0 = 1.0;
		double prePro_C1 = 1.0;
		List<Double> recordpreProC0 = new ArrayList<Double>();
		List<Double> recordpreProC1 = new ArrayList<Double>();
		double K;
		
		begin_n = System.currentTimeMillis();
		// ��ؽ׶�
		for (MBSRM_ComprehensiveQoSBean comprehensiveQoSBean : comprehensiveQoSBeanList) {
			countX++;
			double comprehensiveQoSValuetemp=comprehensiveQoSBean.getComprehensiveQoSValue();
			/*if((countX>1200&&countX<2000)){
				comprehensiveQoSValuetemp=0.5;
			}else {
				comprehensiveQoSValuetemp=0.6;
			}*/
			record_QoS.add(comprehensiveQoSValuetemp);
			if (comprehensiveQoSValuetemp >= QoS_Value) {
				x++;
			}
			double aftPro_C0;
			double aftPro_C1;

			if (countX > sizeSlidingWindow) {
				double abandon = record_QoS.get(countX - sizeSlidingWindow);
				if (abandon <= QoS_Value) {
					x--;
				}
				double c = x * 1.0 / sizeSlidingWindow;// ��׼
				if (c >= beta) {
					YorN.add(1);
					countC0++;
				} else {
					YorN.add(0);
					countC1++;
				}
				double pro_c0 = MBSRM_computePro_c0(sizeSlidingWindow, YorN, countX, countC0);
				double pro_c1 = MBSRM_computePro_c1(sizeSlidingWindow, YorN, countX, countC1);
//				prePro_C0 = computePrePro_C0(llWi_C0, pC0, sizeSlidingWindow, countX, prePro_C0, recordpreProC0,
//						comprehensiveQoSBean);
				prePro_C0=countC0*1.0/countX;

				aftPro_C0 =llWi_C0.get(comprehensiveQoSBean.getEnvirnmentalFactor())* (pro_c0 + prePro_C0);

				
				prePro_C1=countC1/countX;
//				prePro_C1 = computePrePro_C1(llWi_C1, pC1, sizeSlidingWindow, countX, prePro_C1, recordpreProC1,
//						comprehensiveQoSBean);

				aftPro_C1 = llWi_C1.get(comprehensiveQoSBean.getEnvirnmentalFactor())*(pro_c1 + prePro_C1);
				K = Math.pow(Math.abs(aftPro_C0), beta) / Math.pow(Math.abs(aftPro_C1), beta);

				if (countX == 5000) {
					end_n=System.currentTimeMillis();
					end_n=System.currentTimeMillis();
					try {
						FileWriter fw = new FileWriter("G:/TestResult/monitoringtime.txt", true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(""+(end_n-begin_n));
						bw.newLine();
						bw.flush();
						fw.close();
						bw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("��ؽ׶���ʱ��"+(end_n-begin_n)+"ms");
					System.out.println("��������");
					return;
				}

			} else {
				double c = x * 1.0 / countX;
				if (c >= beta) {
					YorN.add(1);
					countC0++;
				} else {
					YorN.add(0);
					countC1++;
				}
				double pro_c0 = MBSRM_computePro_c0(sizeSlidingWindow, YorN, countX, countC0);
				prePro_C0=countC0*1.0/countX;
//				prePro_C0 = computePrePro_C0(llWi_C0, pC0, sizeSlidingWindow, countX, prePro_C0, recordpreProC0,
//						comprehensiveQoSBean);
				aftPro_C0 = (pro_c0 + prePro_C0)*llWi_C0.get(comprehensiveQoSBean.getEnvirnmentalFactor());

				double pro_c1 = MBSRM_computePro_c1(sizeSlidingWindow, YorN, countX, countC1);
				prePro_C1=countC1/countX;
//				prePro_C1 = computePrePro_C1(llWi_C1, pC1, sizeSlidingWindow, countX, prePro_C1, recordpreProC1,
//						comprehensiveQoSBean);
				aftPro_C1 = (pro_c1 + prePro_C1)*llWi_C1.get(comprehensiveQoSBean.getEnvirnmentalFactor());

				K = Math.pow(Math.abs(aftPro_C0), beta) / Math.pow(Math.abs(aftPro_C1), beta);
				if (countX == 5000) {
					
					return;
				}
			}
			try {
				FileWriter fw = new FileWriter("G:/TestResult/aftPro_Ci.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("aftPro_C0=   "+aftPro_C0);
				bw.newLine();
				bw.write("aftPro_C1=   "+aftPro_C1);
				bw.newLine();
				bw.flush();
				fw.close();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {

				FileWriter fw = new FileWriter("G:/TestResult/test_K.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);

				String G = "K = " + K;
				bw.write(G);
				bw.newLine();
				bw.flush();
				fw.close();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (K > 1) {
				try {
					FileWriter fw = new FileWriter("G:/TestResult/test_YorN.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					String s = "1";
					bw.write(s);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (K < 1) {
				try {
					FileWriter fw = new FileWriter("G:/TestResult/test_YorN.txt", true);
					BufferedWriter bw = new BufferedWriter(fw);
					String s = "-1";
					bw.write(s);
					bw.newLine();
					bw.flush();
					fw.close();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	
	}

/*	private static double computePrePro_C1(Map<String, Double> llWi_C1, double pC1, int sizeSlidingWindow, int countX,
			double prePro_C1, List<Double> recordpreProC1, MBSRM_ComprehensiveQoSBean comprehensiveQoSBean) {
		double pXC1 =  pC1;
		
		recordpreProC1.add(pXC1);
		if (countX > sizeSlidingWindow) {
			prePro_C1 = prePro_C1 + pXC1 - recordpreProC1.get(countX - sizeSlidingWindow - 1);
		} else {
			prePro_C1 = prePro_C1 + pXC1;
		}
		return prePro_C1;
	}*/

	private static double MBSRM_computePro_c1(int sizeSlidingWindow, List<Integer> YorN, int countX, int countC1) {
		double pro_C1;
		if (countX > sizeSlidingWindow) {
			if (YorN.get(countX - sizeSlidingWindow - 1) == 0) {
				countC1--;
			}
			pro_C1 = (countC1 * 1.0 + 1) / sizeSlidingWindow;
		} else {
			pro_C1 = (countC1 * 1.0 + 1) / (countX + 2);
		}
		return pro_C1;
	}

/*	private static double computePrePro_C0(Map<String, Double> llWi_C0, double pC0, int sizeSlidingWindow, int countX,
			double prePro_C0, List<Double> recordpreProC0, MBSRM_ComprehensiveQoSBean comprehensiveQoSBean) {
		double pXC0 =  pC0;
		recordpreProC0.add(pXC0);
		if (countX > sizeSlidingWindow) {
			prePro_C0 = prePro_C0 + pXC0 - recordpreProC0.get(countX - sizeSlidingWindow - 1);
		} else {
			prePro_C0 = prePro_C0 + pXC0;
		}
		return prePro_C0;
	}*/

	private static double MBSRM_computePro_c0(int sizeSlidingWindow, List<Integer> YorN, int countX, int countC0) {
		double pro_C0;
		if (countX > sizeSlidingWindow) {
			if (YorN.get(countX - sizeSlidingWindow - 1) == 1) {
				countC0--;
			}
			pro_C0 = (countC0 * 1.0 + 1) / sizeSlidingWindow;
		} else {
			pro_C0 = (countC0 * 1.0 + 1) / (countX + 2);
		}
		return pro_C0;
	}
	/**
	 * �����QoSֵ��һ�����ں�Ϊһ��QoSֵ���ٷ�װ���������ڶ��ڴ�������˴�ȡ��ǰ5000������ѵ��
	 * 
	 * @author ����
	 * @param multiQoSDataList
	 * @param maxAndMinValues
	 * @param comprehensiveQoSBeanList
	 */
	private static void MBSRM_getComprehensiveQoSBeanList(List<MBSRM_MultiQoSBean> multiQoSDataList, double[] maxAndMinValues,
			List<MBSRM_ComprehensiveQoSBean> comprehensiveQoSBeanList) {
		for (MBSRM_MultiQoSBean multiQoSBean : multiQoSDataList) {
			String envirnmentalFactor = multiQoSBean.getIp() + "," + multiQoSBean.getWsId();
			double normalizedResponseTime = (maxAndMinValues[1] - multiQoSBean.getResponsetime())
					/ (maxAndMinValues[1] - maxAndMinValues[2]);
			double normalizedThroughput = (multiQoSBean.getThroughput() - maxAndMinValues[4])
					/ (maxAndMinValues[3] - maxAndMinValues[4]);
			double normalizedReliability = (multiQoSBean.getReliability() - maxAndMinValues[6])
					/ (maxAndMinValues[5] - maxAndMinValues[6]);
			double normalizedAvailability= (multiQoSBean.getAvailability() - maxAndMinValues[8])
					/ (maxAndMinValues[7] - maxAndMinValues[8]);
			double comprehensiveQoSValue = (normalizedResponseTime + normalizedThroughput
					+ normalizedReliability + normalizedAvailability) / 4;
//			System.out.println(comprehensiveQoSValue);
		/*	try {

				FileWriter fw = new FileWriter("G:/TestResult/comprehensiveQoSValue.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);

				String G = "comprehensiveQoSValue = " +comprehensiveQoSValue;
				bw.write(G);
				bw.newLine();
				bw.flush();
				fw.close();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
//			System.out.println("comprehensiveQoSValue write over");
			comprehensiveQoSBeanList.add(new MBSRM_ComprehensiveQoSBean(envirnmentalFactor, comprehensiveQoSValue));
			/*
			 * if(comprehensiveQoSBeanList.size()==10258){ return; }
			 */
		}
	}

	/**
	 * ����QoSֵ�ҵ���Ӧʱ������ֵ�Լ���Сֵ�������������ֵ�Լ���Сֵ��Ϊ���ݹ�һ����׼��
	 * 
	 * @author ����
	 * @param multiQoSDataList
	 * @return
	 */
	private static double[] MBSRM_MaxAndMinValue(List<MBSRM_MultiQoSBean> multiQoSDataList) {
		double maxResponseTime = Double.MIN_VALUE;// ��Ӧʱ������ֵ
		double minResponseTime = Double.MAX_VALUE;// ��Ӧʱ����С��ֵ
		double maxThroughput = Double.MIN_VALUE;// ��Ӧʱ������ֵ
		double minThroughput = Double.MAX_VALUE;// ��Ӧʱ����С��ֵ
		double maxReliability = Double.MIN_VALUE;// �ɿ�������ֵ
		double minReliability = Double.MAX_VALUE;// �ɿ�����С��ֵ
		double maxAvailability = Double.MIN_VALUE;// ����������ֵ
		double minAvailability = Double.MAX_VALUE;// ��������С��ֵ
		for (MBSRM_MultiQoSBean multiQoSBean : multiQoSDataList) {
			if (multiQoSBean.getResponsetime() > maxResponseTime) {
				maxResponseTime = multiQoSBean.getResponsetime();
			}
			if (multiQoSBean.getResponsetime() < minResponseTime) {
				minResponseTime = multiQoSBean.getResponsetime();
			}
			if (multiQoSBean.getThroughput() > maxThroughput) {
				maxThroughput = multiQoSBean.getThroughput();
			}
			if (multiQoSBean.getThroughput() < minThroughput) {
				minThroughput = multiQoSBean.getThroughput();
			}
			if (multiQoSBean.getReliability() > maxReliability) {
				maxReliability = multiQoSBean.getReliability();
			}
			if (multiQoSBean.getReliability() < minReliability) {
				minReliability = multiQoSBean.getReliability();
			}
			if (multiQoSBean.getAvailability() > maxAvailability) {
				maxAvailability = multiQoSBean.getAvailability();
			}
			if (multiQoSBean.getAvailability() < minAvailability) {
				minAvailability = multiQoSBean.getAvailability();
			}
			
			
		}
		double res[] = {0.0,maxResponseTime, minResponseTime, maxThroughput, minThroughput,maxReliability,minReliability,maxAvailability,minAvailability};
		return res;
	}

	/**
	 * ���ļ��е����ݲ���װ����
	 * 
	 * @author ����
	 * @param multiQoSDataList
	 * @param file
	 */
	private static void MBSRM_ReadData(List<MBSRM_MultiQoSBean> multiQoSDataList, File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String temp;
			try {
				int window=100;//�ɿ��Ե�ͳ����Ŀ
				int n=0;//��������
				int reliableSample=0;//��¼�ɿ���������
				int availableSample=0;
				List<Integer> recordReliableSample=new ArrayList<>();
				while ((temp = br.readLine()) != null) {
					temp = temp.replaceAll("\\s{1,}", " ");
					String[] paras = temp.split(" ");
					String ip = paras[0];
					String wsId = paras[1];
				
					double responsetime = Double.parseDouble(paras[2]);
					if(responsetime<=0){
						responsetime=1;
					}
					double throughput = Double.parseDouble(paras[3])/responsetime*1000;
					double reliability;
					n++;
					if (paras[4].equals("200")) {
						reliableSample++;
						recordReliableSample.add(1);
//						reliability = 1;
					} else {
						recordReliableSample.add(0);
//						reliability = 0;
					}
					if(n<=window){
						reliability=reliableSample*1.0F/n;
					}else{
						if(recordReliableSample.get(n-window-1)==1){
							reliableSample--;
						}
						reliability=reliableSample*1.0F/window;
						
					}
					
					double availability;
					if (paras[5].equals("OK")) {
						availableSample++;
					} 
					availability = availableSample*1.0/n;
//					System.out.println("reliability=="+reliability);
//					System.out.println("availability=="+availability);
					if(ip.equals("12.108.127.136")&&wsId.equals("13977")){
						try {

							FileWriter fw = new FileWriter("G:/reliability12.108.127.136and13977.txt", true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(reliability+"");
							bw.newLine();
							bw.flush();
							fw.close();
							bw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(ip.equals("128.83.122.179")&&wsId.equals("10324")){
						try {

							FileWriter fw = new FileWriter("G:/reliability128.83.122.179and10324.txt", true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(reliability+"");
							bw.newLine();
							bw.flush();
							fw.close();
							bw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(ip.equals("12.108.127.136")&&wsId.equals("13977")){
						try {

							FileWriter fw = new FileWriter("G:/availability12.108.127.136and13977.txt", true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(availability+"");
							bw.newLine();
							bw.flush();
							fw.close();
							bw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(ip.equals("128.83.122.179")&&wsId.equals("10324")){
						try {

							FileWriter fw = new FileWriter("G:/availability128.83.122.179and10324.txt", true);
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(availability+"");
							bw.newLine();
							bw.flush();
							fw.close();
							bw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					multiQoSDataList
							.add(new MBSRM_MultiQoSBean(ip, wsId, responsetime, throughput, reliability, availability));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
