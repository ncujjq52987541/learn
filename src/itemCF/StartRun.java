package itemCF;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;

public class StartRun {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		// config.set("fs.defaultFS", "hdfs://HadoopMaster:9000");
		// config.set("yarn.resourcemanager.hostname", "HadoopMaster");
		// 所有mr的输入和输出目录定义在map集合中
		Map<String, String> paths = new HashMap<String, String>();

		paths.put("Step1Input","/root/itemCF/input/sam_tianchi_2014002_rec_tmall_log.csv");
		paths.put("Step1Output", "/root/itemCF/step1out/");
		
		paths.put("Step2Input", paths.get("Step1Output"));
		paths.put("Step2Output", "/root/itemCF/step2out/");
		
		paths.put("Step3Input", paths.get("Step2Output"));
		paths.put("Step3Output", "/root/itemCF/step3out");
		
		paths.put("Step4Input1", paths.get("Step2Output"));
		paths.put("Step4Input2", paths.get("Step3Output"));
		paths.put("Step4Output", "/root/itemCF/step4out");
		
		paths.put("Step5Input", paths.get("Step4Output"));
		paths.put("Step5Output", "/root/itemCF/step5out");
		
		paths.put("Step6Input", paths.get("Step5Output"));
		paths.put("Step6Output", "/root/itemCF/step6out");

//		 Step1.run(config, paths);
//		 Step2.run(config, paths);
		 Step3.run(config, paths);
		// Step4.run(config, paths);
		// Step5.run(config, paths);
		// Step6.run(config, paths);
	}

	public static Map<String, Integer> R = new HashMap<String, Integer>();
	static {
		R.put("click", 1);
		R.put("collect", 2);
		R.put("cart", 3);
		R.put("alipay", 4);
	}
}
