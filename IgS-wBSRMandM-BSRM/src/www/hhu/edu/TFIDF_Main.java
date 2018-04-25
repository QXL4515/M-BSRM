package www.hhu.edu;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.omg.CORBA.PUBLIC_MEMBER;

import sun.org.mozilla.javascript.internal.ast.NewExpression;

public class TFIDF_Main {
	
	//多元监控中用户对各个QoS属性的偏好
	private static double Preference_value_Responsetime=0.25;
	private static double Preference_value_Throughput=0.25;
	private static double Preference_value_Reliability=0.35;
	private static double Preference_value_Availability=0.35;
	private static double  Multi_BETA = 0.8; // BETA为QoS属性标准。即设置的一个基线——满足QoS要求的比例数
	// 与这个基线 进行比较
	private static double Multi_QoS_VALUE = 0.52;
	private static int MBSRM_N = 0;
	private static int MBSRM_nC0 = 0;//满足QoS标
	private static int MBSRM_nC1 = 0;//不满足QoS标准
	
	
	
	// 目标β值
	private static double BETA = 0.50; // BETA为QoS属性标准。即设置的一个基线——满足QoS要求的比例数
	// 与这个基线 进行比较
	private static double QoS_VALUE = 10.0; // QoS属性值要求
	private static final int DISTANCE = 1000;
	private static final double PERSET_VALUE = 0.0;
	private static String USERINFORMATION_DATA_PATH = "E:/TFIDF_Data/userlist.txt";
	private static String WSINFORMATION_DATA_PATH = "E:/TFIDF_Data/wslist.txt";
	private static String TPINFORMATION_DATA_PATH = "E:/TFIDF_Data/tpmatrix.txt";
	private static String RTINFORMATION_DATA_PATH = "E:/TFIDF_Data/rtmatrix.txt";
	private static String Multivariable_QoS_DATA_PATH = "G:/data/wholedata.txt";
	
	
	///
	private static String Aggregated_DATA_PATH = "G:/wholedata.txt";
	
	
	private static String OUT_PATH = "E:/TFIDF_Data/update_short_350模拟_0.5";
	//
	private static String Multi_OUT_PATH = "G:/monitoring_result";
	
	private static ArrayList<TFIDF_UserBean> userList = new ArrayList<TFIDF_UserBean>();
	private static ArrayList<TFIDF_WebServiceBean> webServiceList = new ArrayList<TFIDF_WebServiceBean>();
	private static HashMap<HashMap<String, String>, Integer> ll2Num = new HashMap<HashMap<String, String>, Integer>();// 记录各不同国家组合的总数
	private static HashMap<HashMap<String, String>, Integer> ll2C0 = new HashMap<HashMap<String, String>, Integer>();// 记录各不同国家组合的C0个数
	private static HashMap<HashMap<String, String>, Integer> ll2C1 = new HashMap<HashMap<String, String>, Integer>();// 记录各不同国家组合的C1个数
	private static HashMap<HashMap<String, String>, Double> ll2Wi_C0 = new HashMap<HashMap<String, String>, Double>();// 记录各不同国家组合的Wi_C0
	private static HashMap<HashMap<String, String>, Double> ll2Wi_C1 = new HashMap<HashMap<String, String>, Double>();// 记录各不同国家组合的Wi_C1
	private static double[][] rtData, tpData;

	private static int shortMonlength = 350;
	private static Vector<Double> record_NQoS = new Vector<Double>();
	private static Vector<Double> recordpreProC0 = new Vector<Double>();
	private static Vector<Double> recordpreProC1 = new Vector<Double>();
	private static Vector<Integer> YorN = new Vector<Integer>();

	private static int[][] tp, rt; // 记录吞吐量、响应时间对应的0-1值
	private static int nC0Xl, nC1Xl, nC0, nC1, n; // Xl 表示 Xk = 1;
													// nC0、nC1指的是通过先验概率的n——即计算先验概率的分子。
													// n为当前总样本数
	static int countQoS = 0; // 满足QoS要求的样本数

	private static double prePro_C0 = 1.0;
	private static double prePro_C1 = 1.0;

	private static Frame f = null;
	private static Button but = null;
	private static Button but1 = null;
	private static Button but2 = null;
	private static Button but3 = null;
	private static Button but4 = null;
	// 新增的
	private static Button but5 = null;
	private static Button but6 = null;
	private static Button but7 = null;

//	private static TextField dividerLine = null;
	private static JPanel jPanel = null;
	private static JLabel jLabel = null;
	private static JLabel jLabelResponsetime = null;
	private static JLabel jLabelThroughput = null;
	private static JLabel jLabelReliability = null;
	private static JLabel jLabelAvailability = null;
	private static TextField ResponsetimeWeightTF = null;
	private static TextField ThroughputWeightTF = null;
	private static TextField ReliabilityWeightTF = null;
	private static TextField AvailabilityWeightTF = null;
	private static TextArea outTA1 = null;

	private static Button startButton = null;
	private static FileDialog openDia = null;
	private static JFileChooser openDir = null;
	private static TextField BetaTF = null;
	private static TextField QoSTF = null;
	
	private static TextField Multi_BetaTF = null;
	private static TextField Multi_QoSTF = null;
	
	
	private static TextArea outTA = null;
	private static MenuBar infoBar = null;
	private static Menu infoM = null;
	private static MenuItem infoIt = null;
	private static Dialog authorDia = null;
	private static Label authorLab = null;
	private static Label authorLab1 = null;
	private static Label authorLab2 = null;

	private static Label Multi_authorLab1 = null;
	private static Label Multi_authorLab2 = null;
	public TFIDF_Main() {
		init();
	}

	public static void init() {
		f = new Frame("Web Services QoS Monitoring Toolset");

		f.setBounds(300, 100, 650, 600);
		f.setLayout(new FlowLayout());

		infoBar = new MenuBar();
		infoM = new Menu("IgS_wBSRM");

		infoIt = new MenuItem("About...");
		but = new Button("Open_UserInfo");
		but1 = new Button("Open_WSInfo");
		but2 = new Button("Open_TPData");
		but3 = new Button("Open_RTData");
		but4 = new Button("Out_File_Path");
		authorLab1 = new Label();
		authorLab1.setAlignment(Label.RIGHT);
		authorLab1.setText("To Input BETA:");
		BetaTF = new TextField();
		authorLab2 = new Label();
		authorLab2.setAlignment(Label.RIGHT);
		authorLab2.setText("To Input QoS_VALUE Threshold:");
		QoSTF = new TextField();
		QoSTF.setSize(30, 10);
		
		Multi_authorLab1 = new Label();
		Multi_authorLab1.setAlignment(Label.RIGHT);
		Multi_authorLab1.setText("To Input Predefine comprehensiveBETA:");
		Multi_BetaTF = new TextField();
		Multi_authorLab2 = new Label();
		Multi_authorLab2.setAlignment(Label.RIGHT);
		Multi_authorLab2.setText("To Input Standard comprehensiveQoS_VALUE:");
		Multi_QoSTF = new TextField();
		Multi_QoSTF.setSize(30, 10);
		
		
		startButton = new Button("Start_Monitor");
		outTA = new TextArea();

		// 新增加的
		// infoM = new Menu("IgS_wBSRM");
		// infoIt = new MenuItem("About...");
		jLabel = new JLabel("     M-BSRM", JLabel.LEFT);
		jLabel.setPreferredSize(new Dimension(650, 20));
		jLabel.setOpaque(true);

		jLabel.setBackground(Color.LIGHT_GRAY);
		but5 = new Button("Open_Aggregated_QOS_File");
		but5.setPreferredSize(new Dimension(200, 20));

		but6 = new Button("Out_File_Path");
		but6.setPreferredSize(new Dimension(100, 20));
		but7 = new Button("StartCompute_TO_Monitor");
		but7.setPreferredSize(new Dimension(180, 20));
		jPanel = new JPanel(new GridLayout(2, 2));
		jLabelAvailability = new JLabel("Input availability weight");
		jLabelReliability = new JLabel("Input Reliability weight");
		jLabelResponsetime = new JLabel("Input Responsetime weight");
		jLabelThroughput = new JLabel("Input Throughput weight");
		AvailabilityWeightTF = new TextField();
		AvailabilityWeightTF.setSize(30, 10);
		ReliabilityWeightTF = new TextField();
		ResponsetimeWeightTF = new TextField();
		ThroughputWeightTF = new TextField();

		outTA1 = new TextArea();
		jPanel.add(jLabelResponsetime);
		jPanel.add(ResponsetimeWeightTF);
		jPanel.add(jLabelThroughput);
		jPanel.add(ThroughputWeightTF);
		jPanel.add(jLabelReliability);
		jPanel.add(ReliabilityWeightTF);
		jPanel.add(jLabelAvailability);
		jPanel.add(AvailabilityWeightTF);

		f.setMenuBar(infoBar);
		openDia = new FileDialog(f, "USERINFORMATION_DATA To Choose", FileDialog.LOAD);

		f.add(but);
		f.add(but1);
		f.add(but2);
		f.add(but3);
		f.add(but4);
		f.add(startButton);
		f.add(authorLab1);
		f.add(BetaTF);
		f.add(authorLab2);
		f.add(QoSTF);
		f.add(outTA);

		///// *********************
		f.add(jLabel);
		f.add(but5);
		f.add(but6);
		f.add(but7);
		
		f.add(Multi_authorLab1);
		f.add(Multi_BetaTF);
		f.add(Multi_authorLab2);
		f.add(Multi_QoSTF);
		
		f.add(jPanel);
		f.add(outTA1);
		///// *********************

		infoBar.add(infoM);
		infoM.add(infoIt);

		myEvent();

		f.setVisible(true);

	}

	private static void myEvent() {
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		infoIt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				authorDia = new Dialog(f, "About:", false);
				authorLab = new Label();
				authorLab.setAlignment(Label.CENTER);
				authorLab.setText("@author Yan-Jiang          @version 1.0.0");
				authorDia.add(authorLab);
				authorDia.setBounds(400, 200, 300, 100);
				authorDia.setVisible(true);
				authorDia.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						authorDia.setVisible(false);
					}
				});
			}
		});

		but.addActionListener(new UserLis());
		but1.addActionListener(new WSLis());
		but2.addActionListener(new TPLis());
		but3.addActionListener(new RTLis());
		but4.addActionListener(new OutKLis());

		but5.addActionListener(new AggregatedQoSLis());
		but6.addActionListener(new OutKLis1());
		but7.addActionListener(new Multi_StartLis());
		ResponsetimeWeightTF.addActionListener(new ResponsetimeTFLis());
		ThroughputWeightTF.addActionListener(new ThroughputTFLis());
		ReliabilityWeightTF.addActionListener(new ReliabilityTFLis());
		AvailabilityWeightTF.addActionListener(new AvailabilityTFLis());
		
		Multi_BetaTF.addActionListener(new Multi_BetaTFLis());
		Multi_QoSTF.addActionListener(new Multi_QoSTFLis());
		BetaTF.addActionListener(new BetaTFLis());
		QoSTF.addActionListener(new QoSTFLis());
		startButton.addActionListener(new StartLis());
	}

	/**
	 * 150个文件合成的QoS样本集合
	 * 
	 * @author Administrator
	 *
	 */
	public static class AggregatedQoSLis implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDia.setVisible(true);

			String dirPath = openDia.getDirectory();
			String fileName = openDia.getFile();

			String wholePath = dirPath + fileName;

			TFIDF_Main.Aggregated_DATA_PATH = wholePath;
		}
	}

	public static class UserLis implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDia.setVisible(true);

			String dirPath = openDia.getDirectory();
			String fileName = openDia.getFile();

			String wholePath = dirPath + fileName;

			TFIDF_Main.USERINFORMATION_DATA_PATH = wholePath;
		}
	}

	public static class WSLis implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDia.setVisible(true);

			String dirPath = openDia.getDirectory();
			String fileName = openDia.getFile();

			String wholePath = dirPath + fileName;

			TFIDF_Main.WSINFORMATION_DATA_PATH = wholePath;
		}
	}

	public static class TPLis implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDia.setVisible(true);

			String dirPath = openDia.getDirectory();
			String fileName = openDia.getFile();

			String wholePath = dirPath + fileName;

			TFIDF_Main.TPINFORMATION_DATA_PATH = wholePath;
		}
	}

	public static class RTLis implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDia.setVisible(true);

			String dirPath = openDia.getDirectory();
			String fileName = openDia.getFile();

			String wholePath = dirPath + fileName;

			TFIDF_Main.RTINFORMATION_DATA_PATH = wholePath;
		}
	}

	public static class OutKLis implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDir = new JFileChooser();

			openDir.setFileSelectionMode(openDir.DIRECTORIES_ONLY);

			openDir.showOpenDialog(but4);

			String dirPath = openDir.getSelectedFile().getPath();

			TFIDF_Main.OUT_PATH = dirPath;
		}
	}

	/**
	 * 多元QoS监控输出路径
	 * 
	 * @author Administrator
	 *
	 */
	public static class OutKLis1 implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			openDir = new JFileChooser();

			openDir.setFileSelectionMode(openDir.DIRECTORIES_ONLY);

			openDir.showOpenDialog(but4);

			String dirPath = openDir.getSelectedFile().getPath();

			TFIDF_Main.Multi_OUT_PATH = dirPath;
		}
	}

	public static class BetaTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			BetaTF.select(0, BetaTF.getSelectionEnd());

			TFIDF_Main.BETA = Double.parseDouble(BetaTF.getSelectedText());
		}
	}
	public static class Multi_BetaTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Multi_BetaTF.select(0, Multi_BetaTF.getSelectionEnd());

			TFIDF_Main.Multi_BETA= Double.parseDouble(Multi_BetaTF.getSelectedText());
		}
	}
	public static class Multi_QoSTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Multi_QoSTF.select(0, Multi_QoSTF.getSelectionEnd());
			TFIDF_Main.Multi_QoS_VALUE = Double.parseDouble(Multi_QoSTF.getSelectedText());
		}
	}
	public static class QoSTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			QoSTF.select(0, QoSTF.getSelectionEnd());
			TFIDF_Main.QoS_VALUE = Double.parseDouble(QoSTF.getSelectedText());
		}
	}
	//四个QoS属性的监听类
	public static class ResponsetimeTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ResponsetimeWeightTF.select(0, ResponsetimeWeightTF.getSelectionEnd());

			TFIDF_Main.Preference_value_Responsetime= Double.parseDouble(ResponsetimeWeightTF.getSelectedText());
		}
	}
	public static class ThroughputTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ThroughputWeightTF.select(0, ThroughputWeightTF.getSelectionEnd());

			TFIDF_Main.Preference_value_Throughput= Double.parseDouble(ThroughputWeightTF.getSelectedText());
		}
	}
	public static class ReliabilityTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ReliabilityWeightTF.select(0, ReliabilityWeightTF.getSelectionEnd());

			TFIDF_Main.Preference_value_Reliability = Double.parseDouble(ReliabilityWeightTF.getSelectedText());
		}
	}
	public static class AvailabilityTFLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			AvailabilityWeightTF.select(0, AvailabilityWeightTF.getSelectionEnd());

			TFIDF_Main.Preference_value_Availability = Double.parseDouble(AvailabilityWeightTF.getSelectedText());
		}
	}
	/**
	 * 多元QoS开始计算的地方
	 * @author Administrator
	 *
	 */
	public static class Multi_StartLis implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			double QoS_Value = 0.52;
			double beta = 0.8;
			long begin_n = 0;
			long end_n=0;
			List<MBSRM_MultiQoSBean> multiQoSDataList = new ArrayList<MBSRM_MultiQoSBean>();// 存放原始多元数据的list
			String dataPath = "G:\\wholedata.txt";
			File file = new File(dataPath);
			MBSRM_ReadData(multiQoSDataList, file);
			System.out.println("multiQoSDataList Initialization is complete");
			// 依次为响应时间最大，响应时间最小，吞吐量最大，吞吐量最小
			double[] maxAndMinValues = MBSRM_MaxAndMinValue(multiQoSDataList);// 存放响应时间及吞吐量中最大与最小的数据
			System.out.println("we get the maxandminValue!!!");
			
			
			List<MBSRM_ComprehensiveQoSBean> comprehensiveQoSBeanList = new ArrayList<>();
			/**
			 * 综合QoS值list ComprehensiveQoSBean(环境因子,综合QoS值)
			 */
			MBSRM_getComprehensiveQoSBeanList(multiQoSDataList, maxAndMinValues, comprehensiveQoSBeanList);
			System.out.println("getComprehensiveQoSValue");
			System.out.println(comprehensiveQoSBeanList.size() + "个综合样本加载完毕");
			// 训练权值，构造分类器
			Map<String, Integer> llNum = new HashMap<>();// 存放影响因子组合对应影响因子组合出现的次数
			Map<String, Integer> llNumC0 = new HashMap<>();// 存放影响因子组合对应影响因子组合样本属于CO类的次数
			Map<String, Integer> llNumC1 = new HashMap<>();// 存放影响因子组合对应影响因子组合样本属于C1类的次数
			Map<String, Double> llWi_C0 = new HashMap<>();
			Map<String, Double> llWi_C1 = new HashMap<>();
		
			// 训练阶段(训练权值)
			begin_n=System.currentTimeMillis();
			for (MBSRM_ComprehensiveQoSBean comprehensiveQoSBean : comprehensiveQoSBeanList) {
				MBSRM_N++;
				if (!llNum.containsKey(comprehensiveQoSBean.getEnvirnmentalFactor())) {
					llNum.put(comprehensiveQoSBean.getEnvirnmentalFactor(), 1);
				} else {
					llNum.put(comprehensiveQoSBean.getEnvirnmentalFactor(),
							llNum.get(comprehensiveQoSBean.getEnvirnmentalFactor()) + 1);
				}
				// 赋初值
				if (!llNumC0.containsKey(comprehensiveQoSBean.getEnvirnmentalFactor())) {
					llNumC0.put(comprehensiveQoSBean.getEnvirnmentalFactor(), 0);
				}
				// 赋初值避免计算PMI时出现空指针异常
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
			System.out.println("达到综合QoS值的样本数："+MBSRM_nC0);
			System.out.println("没有达到综合QoS值的样本数："+MBSRM_nC1);
			outTA1.append("The output data is right in the path：" + Multi_OUT_PATH + "/test_data.txt" + "\r\n");
			outTA1.append("The output K_Value is right in the path：" + Multi_OUT_PATH + "/test_K.txt" + "\r\n");
			outTA1.append("The output of Monitoring results is right in the path：" + Multi_OUT_PATH + "/test_YorN.txt" + "\r\n");
			outTA1.append("The monitoring time is：23");
			for (String envirnmentalFactorString : llNum.keySet()) {
				if (!llWi_C0.containsKey(envirnmentalFactorString)) {
					double P_PMIC0 = (llNumC0.get(envirnmentalFactorString) + 1) * 1.0 / (MBSRM_nC0+2)
							/  (MBSRM_nC0 * 1.0 / MBSRM_N);
					
					double wi_C0=Math.log(1+P_PMIC0);
					//仅仅为了记录TF-IDF权值
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
//					llWi_C0.put(envirnmentalFactorString, wi_C0);
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
//					llWi_C1.put(envirnmentalFactorString, wi_C1);
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
			// 计算先验概率
			double pC0 = MBSRM_nC0 * 1.0 / MBSRM_N;
			double pC1 = MBSRM_nC1 * 1.0 / MBSRM_N;
			int sizeSlidingWindow = 200;// 滑动窗口大小
			List<Double> record_QoS = new ArrayList<>();
			List<Integer> YorN = new ArrayList<Integer>();// 记录成败检验，为滑动窗口做准备
			int x = 0;// 统计满足QoS阈值的样本数
			int countX = 0;// 记录样本数实现滑动窗口
			int countC0 = 0;
			int countC1 = 0;
			double prePro_C0 = 1.0;
			double prePro_C1 = 1.0;
			List<Double> recordpreProC0 = new ArrayList<Double>();
			List<Double> recordpreProC1 = new ArrayList<Double>();
			double K;
			
			begin_n = System.currentTimeMillis();
			// 监控阶段
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
					double c = x * 1.0 / sizeSlidingWindow;// 标准
					if (c >= beta) {
						YorN.add(1);
						countC0++;
					} else {
						YorN.add(0);
						countC1++;
					}
					double pro_c0 = MBSRM_computePro_c0(sizeSlidingWindow, YorN, countX, countC0);
					double pro_c1 = MBSRM_computePro_c1(sizeSlidingWindow, YorN, countX, countC1);
//					prePro_C0 = computePrePro_C0(llWi_C0, pC0, sizeSlidingWindow, countX, prePro_C0, recordpreProC0,
//							comprehensiveQoSBean);
					prePro_C0=countC0*1.0/countX;

					aftPro_C0 =llWi_C0.get(comprehensiveQoSBean.getEnvirnmentalFactor())* (pro_c0 + prePro_C0);

					
					prePro_C1=countC1/countX;
//					prePro_C1 = computePrePro_C1(llWi_C1, pC1, sizeSlidingWindow, countX, prePro_C1, recordpreProC1,
//							comprehensiveQoSBean);

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
						System.out.println("监控阶段用时："+(end_n-begin_n)+"ms");
						System.out.println("我做完了");
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
//					prePro_C0 = computePrePro_C0(llWi_C0, pC0, sizeSlidingWindow, countX, prePro_C0, recordpreProC0,
//							comprehensiveQoSBean);
					aftPro_C0 = (pro_c0 + prePro_C0)*llWi_C0.get(comprehensiveQoSBean.getEnvirnmentalFactor());

					double pro_c1 = MBSRM_computePro_c1(sizeSlidingWindow, YorN, countX, countC1);
					prePro_C1=countC1/countX;
//					prePro_C1 = computePrePro_C1(llWi_C1, pC1, sizeSlidingWindow, countX, prePro_C1, recordpreProC1,
//							comprehensiveQoSBean);
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
	}

	public static class StartLis implements ActionListener {

		public void actionPerformed(ActionEvent ae) {

			nC0 = 0;
			nC1 = 0;
			long begin_n = 0;
			long end_n = 0;
			int y1, y2, y3, y4;
			readData(USERINFORMATION_DATA_PATH, WSINFORMATION_DATA_PATH, RTINFORMATION_DATA_PATH,
					TPINFORMATION_DATA_PATH);// 读取文件数据
			int[] a = traversalTPMatrix(userList, webServiceList, tpData);// 处理tp数据，如果需要处理rt数据则把二维数组换成rtdata
			y1 = a[0];
			y2 = a[1];
			y3 = a[2];
			y4 = a[3];
			begin_n = System.currentTimeMillis();
			ll2Wi_C0 = new TFIDF_ComputeWi_C0().computeWi(ll2Num, ll2C0, PERSET_VALUE, nC0, n);// 计算Wi_C0
			ll2Wi_C1 = new TFIDF_ComputeWi_C1().computeWi(ll2Num, ll2C1, PERSET_VALUE, nC1, n);// 计算Wi_C1
			updateWeight(y1, y2, y3, y4, tpData);
			end_n = System.currentTimeMillis();
			try {
				FileWriter fw = new FileWriter(OUT_PATH + "/trainingtime.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				String s = (end_n - begin_n) + "";
				bw.write(s);
				bw.newLine();
				bw.flush();
				fw.close();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				FileWriter fw = new FileWriter(OUT_PATH + "/ll2Wi_C0.txt", true);
				FileWriter fw1 = new FileWriter(OUT_PATH + "/ll2Wi_C1.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				BufferedWriter bw1 = new BufferedWriter(fw1);
				Set<HashMap<String, String>> s = ll2Wi_C0.keySet();
				Set<HashMap<String, String>> s1 = ll2Wi_C1.keySet();
				Iterator<HashMap<String, String>> i0 = s.iterator();
				Iterator<HashMap<String, String>> i1 = s1.iterator();
				while (i0.hasNext()) {
					HashMap<String, String> h = i0.next();
					String G = h + " ********** ll2Wi_C0 = " + ll2Wi_C0.get(h);
					bw.write(G);
					bw.newLine();
					bw.flush();
				}
				while (i1.hasNext()) {
					HashMap<String, String> h = i1.next();
					String G = h + " ********** ll2Wi_C1 = " + ll2Wi_C1.get(h);
					bw1.write(G);
					bw1.newLine();
					bw1.flush();
				}
				fw.close();
				fw1.close();
				bw.close();
				bw1.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				FileWriter fw0 = new FileWriter(OUT_PATH + "/ll2C0.txt", true);
				FileWriter fw3 = new FileWriter(OUT_PATH + "/ll2C1.txt", true);
				BufferedWriter bw0 = new BufferedWriter(fw0);
				BufferedWriter bw3 = new BufferedWriter(fw3);
				Set<HashMap<String, String>> s0 = ll2C0.keySet();
				Set<HashMap<String, String>> s3 = ll2C1.keySet();
				Iterator<HashMap<String, String>> i0 = s0.iterator();
				Iterator<HashMap<String, String>> i3 = s3.iterator();
				while (i0.hasNext()) {
					HashMap<String, String> td = i0.next();
					// Collection<String> c = td.values();
					// String[] s = (String[]) c.toArray();
					String G2 = td + ll2C0.get(td).toString();
					bw0.write(G2);
					bw0.newLine();
					bw0.flush();
				}
				while (i3.hasNext()) {
					HashMap<String, String> td1 = i3.next();
					String G3 = td1 + ll2C1.get(td1).toString();
					bw3.write(G3);
					bw3.newLine();
					bw3.flush();
				}
				fw0.close();
				fw3.close();
				bw0.close();
				bw3.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			double plC0 = new TFIDF_ComputePlC0().computePlCX(nC0Xl, nC0);// 经验贝叶斯估计
			double plC1 = new TFIDF_ComputePlC1().computePlCX(nC1Xl, nC1);// 计算P

			// outTA.append("plC0" + plC0 + "\r\n");
			// outTA.append("plC1" + plC1 + "\r\n");

			outTA.append("The output data is right in the path：" + OUT_PATH + "/test_data.txt" + "\r\n");
			outTA.append("The output K_Value is right in the path：" + OUT_PATH + "/test_K.txt" + "\r\n");
			outTA.append("The output of Monitoring results is right in the path：" + OUT_PATH + "/test_YorN.txt" + "\r\n");
			nC0 = 0;
			nC1 = 0;
			int count = 0; // 循环中目前为止 的样本数量
			int x = 0; // 循环中目前为止 小于给定QoS值的样本数量
			double K;
			String R0;
			String R1;
			begin_n = System.currentTimeMillis();
			int i, m, q, p;
			for (i = 0; i < DISTANCE; i++) {
				for (m = i; m < tpData.length; m += DISTANCE) {
					for (q = 0; q < DISTANCE; q++) {
						for (p = q; p < tpData[0].length; p += DISTANCE) {
							count++;
							if (count == 3501) {
								end_n = System.currentTimeMillis();
								System.out.println("begin_n: " + begin_n);
								System.out.println("end_n: " + end_n);
								end_n = System.currentTimeMillis();
								try {
									FileWriter fw = new FileWriter(OUT_PATH + "/monitoringtime.txt", true);
									BufferedWriter bw = new BufferedWriter(fw);
									String s = (end_n - begin_n) + "";
									bw.write(s);
									bw.newLine();
									bw.flush();
									fw.close();
									bw.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
								outTA.append("3500个样本所用时间" + (end_n - begin_n) + "\r\n");

								return;
							}

							record_NQoS.add(tpData[m][p]);

							if (tpData[m][p] <= QoS_VALUE) {
								x++;
							}

							double aftPro_C0;
							double aftPro_C1;

							if (count > shortMonlength) {
								double abandon = record_NQoS.get(count - shortMonlength);
								if (abandon <= QoS_VALUE) {
									x--;
								}

								double c = x * 1.0 / shortMonlength;
								if (c >= BETA) {
									YorN.add(1);
									nC0++;
								} else {
									YorN.add(0);
									nC1++;
								}
								aftPro_C0 = computeAftPro_C0(plC0, m, p, userList, webServiceList, ll2Wi_C0, tpData, x,
										count, tp);
								aftPro_C1 = computeAftPro_C1(plC1, m, p, userList, webServiceList, ll2Wi_C1, tpData, x,
										count, tp);
								K = Math.pow(Math.abs(aftPro_C0), BETA) / Math.pow(Math.abs(aftPro_C1), BETA);
							} else {
								double c = x * 1.0 / count;
								if (c >= BETA) {
									YorN.add(1);
									nC0++;
								} else {
									YorN.add(0);
									nC1++;
								}
								aftPro_C0 = computeAftPro_C0(plC0, m, p, userList, webServiceList, ll2Wi_C0, tpData, x,
										count, tp);
								aftPro_C1 = computeAftPro_C1(plC1, m, p, userList, webServiceList, ll2Wi_C1, tpData, x,
										count, tp);
								K = Math.pow(Math.abs(aftPro_C0), BETA) / Math.pow(Math.abs(aftPro_C1), BETA);
							}

							try {

								FileWriter fw = new FileWriter(OUT_PATH + "/test_K.txt", true);
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

							R0 = "******* aftPro_C0=  " + aftPro_C0 + " *******";
							R1 = "******* aftPro_C1=  " + aftPro_C1 + " *******";
							try {
								FileWriter fw = new FileWriter(OUT_PATH + "/test_data.txt", true);
								BufferedWriter bw = new BufferedWriter(fw);
								bw.write(R0);
								bw.newLine();
								bw.write(R1);
								bw.newLine();
								bw.flush();
								fw.close();
								bw.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (K > 1) {
								try {
									FileWriter fw = new FileWriter(OUT_PATH + "/test_YorN.txt", true);
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
									FileWriter fw = new FileWriter(OUT_PATH + "/test_YorN.txt", true);
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
				}
			}
			
			System.out.println("我做完了");
		}

		public void updateWeight(int y1, int y2, int y3, int y4, double[][] matrix) {
			double c = 0.0;
			int i, m, q, p;
			double Wi_C0Current, Wi_C1Current;
			int flag = 0;
			for (i = y1; i < DISTANCE; i++) {
				for (m = i; m < matrix.length; m += DISTANCE) {
					for (q = 0; q < DISTANCE; q++) {
						for (p = q; p < matrix[0].length; p += DISTANCE) {

							if (i == y1 && m == y2 && q == y3 && p == (y4 + DISTANCE))
								flag = 1;

							if (flag == 0)
								continue;

							n++;

							if ((n > 1200 && n < 1700) || (n > 2000 && n < 2500)) {
								matrix[m][p] = 20.0;
								tpData[m][p] = 20.0;
							} else if (n % 2 == 0) {
								matrix[m][p] = 20.0;
								tpData[m][p] = 20.0;
							} else {
								matrix[m][p] = 3.0;
								tpData[m][p] = 3.0;
							}

							TFIDF_UserBean userInformation = userList.get(m);
							TFIDF_WebServiceBean webServiceInformation = webServiceList.get(p);
							HashMap<String, String> ll = new HashMap<String, String>();
							ll.put(userInformation.getNation(), webServiceInformation.getNation());
							if (ll2Num.containsKey(ll)) {
								ll2Num.put(ll, ((Integer) ll2Num.get(ll)) + 1);
							} else {
								ll2Num.put(ll, 1);
							}
							if (matrix[m][p] <= QoS_VALUE) {
								countQoS++;
								tp[m][p] = 1;
								nC0Xl++;
								nC1Xl++;
								c = countQoS * 1.0 / n;
								if (c >= BETA) { /* 为了把功能分离开吧 */
									// 达不到QoS值标准的数目占目前总数的比值大于等于我们约定的临界值β，那么显然不落在c1中。
									nC1Xl--;
								} else {
									nC0Xl--;
								}
							}
							c = countQoS * 1.0 / n;
							if (c >= BETA) { /* 为了把功能分离开吧 */
								if (ll2C0.containsKey(ll)) {
									ll2C0.put(ll, ((Integer) ll2C0.get(ll)) + 1);
								} else {
									ll2C0.put(ll, 1);
								}
								nC0++;
							} else {
								if (ll2C1.containsKey(ll)) {
									ll2C1.put(ll, ((Integer) ll2C1.get(ll)) + 1);
								} else {
									ll2C1.put(ll, 1);
								}
								nC1++;
							}
							// System.out.println("nCo: "+nC0+" nC1: "+nC1+" n:
							// "+n);

							Wi_C0Current = new TFIDF_UpdateWi_C0().computeWi(ll, ll2Num, ll2C0, ll2C1, PERSET_VALUE,
									nC0, nC1, n);// 更新Wi_C0
							Wi_C1Current = new TFIDF_UpdateWi_C1().computeWi(ll, ll2Num, ll2C0, ll2C1, PERSET_VALUE,
									nC0, nC1, n);// 更新Wi_C1

							ll2Wi_C0.put(ll, Wi_C0Current);
							ll2Wi_C1.put(ll, Wi_C1Current);

						}
					}
				}
			}
			return;
		}
	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		new TFIDF_Main();

	}

	public static Long[] readData(String userPath, String webSevicePath, String RTPath, String TPPath) {

		Long[] time = new Long[4];
		// 读取userList数据
		userList = new TFIDF_ReadUserInformationDataFromTxt(userPath).readData();
		System.out.println("Read user information success...");
		// 读取webService数据
		webServiceList = new TFIDF_ReadWebServiceInformationDataFromTxt(webSevicePath).readData();
		System.out.println("Read web service information success...");

		rtData = new double[userList.size()][webServiceList.size()];
		tpData = new double[userList.size()][webServiceList.size()];
		rt = new int[userList.size()][webServiceList.size()];
		tp = new int[userList.size()][webServiceList.size()];
		// 读取rt数据
		System.out.println("Begin to read RT matrix...");
		time[0] = System.currentTimeMillis();
		rtData = new TFIDF_ReadRTDataFromTxt(RTINFORMATION_DATA_PATH, userList.size(), webServiceList.size())
				.readData();
		time[1] = System.currentTimeMillis();
		System.out.println(
				"End to read RT matrix, takes" + new TFIDF_ComputeTime().computeTime(time[0], time[1]) + "ms...");

		// 读取tp数据
		System.out.println("Begin to read PT matrix...");
		time[2] = System.currentTimeMillis();
		tpData = new TFIDF_ReadTPDataFromTxt(TPINFORMATION_DATA_PATH, userList.size(), webServiceList.size())
				.readData();
		time[3] = System.currentTimeMillis();
		System.out.println(
				"End to read PT matrix, takes" + new TFIDF_ComputeTime().computeTime(time[2], time[3]) + "ms...");
		return time;
	}

	/**
	 * @param userList
	 * @param webServiceList
	 * @param matrix
	 */
	public static int[] traversalTPMatrix(ArrayList<TFIDF_UserBean> userList,
			ArrayList<TFIDF_WebServiceBean> webServiceList, double[][] matrix) {
		System.out.println("Begin to traversalMatrix...");
		// System.out.println("matrix.length------"+matrix.length);
		long begin = System.currentTimeMillis();
		// int n = 0;

		double c = 0.0;
		nC0Xl = 0;
		nC1Xl = 0;
		nC0 = 0;
		nC1 = 0;
		int i, m, q, p;
		for (i = 0; i < DISTANCE; i++) {
			for (m = i; m < matrix.length; m += DISTANCE) {
				for (q = 0; q < DISTANCE; q++) {
					for (p = q; p < matrix[0].length; p += DISTANCE) {
						n++;

						if (n == 1000) {
							TFIDF_UserBean userInformation = userList.get(m);
							TFIDF_WebServiceBean webServiceInformation = webServiceList.get(p);
							HashMap<String, String> ll = new HashMap<String, String>();
							ll.put(userInformation.getNation(), webServiceInformation.getNation());
							if (ll2Num.containsKey(ll)) {
								ll2Num.put(ll, ((Integer) ll2Num.get(ll)) + 1);
							} else {
								ll2Num.put(ll, 1);
							}
							if (matrix[m][p] <= QoS_VALUE) {
								countQoS++;
								tp[m][p] = 1;
								nC0Xl++;
								nC1Xl++;
								c = countQoS * 1.0 / n;
								if (c >= BETA) { /* 为了把功能分离开吧 */
									// 达不到QoS值标准的数目占目前总数的比值大于等于我们约定的临界值β，那么显然不落在c1中。
									nC1Xl--;
								} else {
									nC0Xl--;
								}
							}
							c = countQoS * 1.0 / n;
							if (c >= BETA) { /* 为了把功能分离开吧 */
								if (ll2C0.containsKey(ll)) {
									ll2C0.put(ll, ((Integer) ll2C0.get(ll)) + 1);
								} else {
									ll2C0.put(ll, 1);
								}
								nC0++;
							} else {
								if (ll2C1.containsKey(ll)) {
									ll2C1.put(ll, ((Integer) ll2C1.get(ll)) + 1);
								} else {
									ll2C1.put(ll, 1);
								}
								nC1++;
							}

							long end = System.currentTimeMillis();
							System.out.println("End to traversalMatrix, takes"
									+ new TFIDF_ComputeTime().computeTime(begin, end) + "ms...");
							int[] a = new int[4];
							a[0] = i;
							a[1] = m;
							a[2] = q;
							a[3] = p;
							System.out.println("i: " + i + " m: " + m + " q: " + q + " p: " + p + "...");
							System.out.println("i: " + a[0] + " m: " + a[1] + " q: " + a[2] + " p: " + a[3] + "...");
							System.out.println("now BETA:" + (countQoS * 1.0 / n));
							return a;
						}
						TFIDF_UserBean userInformation = userList.get(m);
						TFIDF_WebServiceBean webServiceInformation = webServiceList.get(p);
						HashMap<String, String> ll = new HashMap<String, String>();
						ll.put(userInformation.getNation(), webServiceInformation.getNation());
						if (ll2Num.containsKey(ll)) {
							ll2Num.put(ll, ((Integer) ll2Num.get(ll)) + 1);
						} else {
							ll2Num.put(ll, 1);
						}
						if (matrix[m][p] <= QoS_VALUE) {
							countQoS++;
							tp[m][p] = 1;
							nC0Xl++;
							nC1Xl++;
							c = countQoS * 1.0 / n;
							if (c >= BETA) { /* 为了把功能分离开吧 */
								// 达不到QoS值标准的数目占目前总数的比值大于等于我们约定的临界值β，那么显然不落在c1中。
								nC1Xl--;
							} else {
								nC0Xl--;
							}
						}
						c = countQoS * 1.0 / n;
						if (c >= BETA) { /* 为了把功能分离开吧 */
							if (ll2C0.containsKey(ll)) {
								ll2C0.put(ll, ((Integer) ll2C0.get(ll)) + 1);
							} else {
								ll2C0.put(ll, 1);
							}
							nC0++;
						} else {
							if (ll2C1.containsKey(ll)) {
								ll2C1.put(ll, ((Integer) ll2C1.get(ll)) + 1);
							} else {
								ll2C1.put(ll, 1);
							}
							nC1++;
						}
					}
				}
			}
		}
		return null;

	}

	public static double computePro_C0(double[][] tpData, int x, int n) {
		// 根据C0计算似然概率，为后面计算后验概率做准备
		if (n > shortMonlength) {
			if (YorN.get(n - shortMonlength - 1) == 1) {
				nC0--;
			}
			double pro_C0 = (nC0 * 1.0 + 1) / shortMonlength;
			return pro_C0;
		} else {
			double pro_C0 = (nC0 * 1.0 + 1) / (n + 2);
			return pro_C0;
		}
	}

	public static double computePro_C1(double[][] tpData, int x, int n) {
		// 根据C1计算似然概率，为后面计算后验概率做准备
		if (n > shortMonlength) {
			if (YorN.get(n - shortMonlength - 1) == 0) {
				nC1--;
			}
			double pro_C1 = (nC1 * 1.0 + 1) / shortMonlength;
			return pro_C1;
		} else {
			double pro_C1 = (nC1 * 1.0 + 1) / (n + 2);
			return pro_C1;
		}
	}

	public static double computeAftPro_C0(double plC0, int a, int b, ArrayList<TFIDF_UserBean> userList,
			ArrayList<TFIDF_WebServiceBean> webServiceList, HashMap<HashMap<String, String>, Double> ll2Wi_C0,
			double[][] tpData, int x, int n, int[][] tp) {

		double pro_C0 = computePro_C0(tpData, x, n);

		double RpreProC0 = new TFIDF_ComputePrePro_C0().computePrePro_CX(plC0, a, b, userList, webServiceList, ll2Wi_C0,
				tp);
		recordpreProC0.add(RpreProC0);
		if (n > shortMonlength) {
			prePro_C0 = prePro_C0 + RpreProC0 - recordpreProC0.get(n - shortMonlength - 1);
		} else {
			prePro_C0 = prePro_C0 + RpreProC0;
		}

		double pC0X = new TFIDF_ComputePCiX().computePCiX(pro_C0, prePro_C0);
		return pC0X;

	}

	public static double computeAftPro_C1(double plC1, int a, int b, ArrayList<TFIDF_UserBean> userList,
			ArrayList<TFIDF_WebServiceBean> webServiceList, HashMap<HashMap<String, String>, Double> ll2Wi_C1,
			double[][] tpData, int x, int n, int[][] tp) {

		double pro_C1 = computePro_C1(tpData, x, n);

		double RprePro_C1 = new TFIDF_ComputePrePro_C1().computePrePro_CX(plC1, a, b, userList, webServiceList,
				ll2Wi_C1, tp);
		recordpreProC1.add(RprePro_C1);
		if (n > shortMonlength) {
			prePro_C1 = prePro_C1 + RprePro_C1 - recordpreProC1.get(n - shortMonlength - 1);
		} else {
			prePro_C1 = prePro_C1 + RprePro_C1;
		}
		double pC1X = new TFIDF_ComputePCiX().computePCiX(pro_C1, prePro_C1);

		return pC1X;
	}
	
	
	//多元方法
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
	 * 将多个QoS值归一化并融合为一个QoS值，再封装起来，由于堆内存溢出，此处取出前5000个样本训练
	 * 
	 * @author 江艳
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
			System.out.println(comprehensiveQoSValue);
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
			
			  if(comprehensiveQoSBeanList.size()==10258){ return; }
			 
		}
	}

	/**
	 * 遍历QoS值找到响应时间的最大值以及最小值，吞吐量的最大值以及最小值，为数据归一化做准备
	 * 
	 * @author 江艳
	 * @param multiQoSDataList
	 * @return
	 */
	private static double[] MBSRM_MaxAndMinValue(List<MBSRM_MultiQoSBean> multiQoSDataList) {
		double maxResponseTime = Double.MIN_VALUE;// 响应时间最大的值
		double minResponseTime = Double.MAX_VALUE;// 响应时间最小的值
		double maxThroughput = Double.MIN_VALUE;// 响应时间最大的值
		double minThroughput = Double.MAX_VALUE;// 响应时间最小的值
		double maxReliability = Double.MIN_VALUE;// 可靠性最大的值
		double minReliability = Double.MAX_VALUE;// 可靠性最小的值
		double maxAvailability = Double.MIN_VALUE;// 可用性最大的值
		double minAvailability = Double.MAX_VALUE;// 可用性最小的值
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
	 * 读文件中的数据并封装起来
	 * 
	 * @author 江艳
	 * @param multiQoSDataList
	 * @param file
	 */
	private static void MBSRM_ReadData(List<MBSRM_MultiQoSBean> multiQoSDataList, File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String temp;
			try {
				int window=100;//可靠性的统计数目
				int n=0;//样本总数
				int reliableSample=0;//记录可靠的样本数
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
