package knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kdtree.KDTreeApp;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class App {
	private static Log logger = LogFactory.getLog(App.class);
	public static KDTreeApp buildKDTree(String inputDatasetS,int dimesion) throws IOException{
//		Path path = new Path(inputDatasetS);
		BufferedReader br = new BufferedReader(new FileReader(inputDatasetS));
		
//		String sdata="100;c1;1.0,1.0\r101;c1;1.1,1.2\r102;c1;1.2,1.0\r103;c1;1.6,1.5\r104;c1;1.3,1.7\r105;c1;2.0,2.1\r106;c1;2.0,2.2\r107;c1;2.3,2.3";
//		BufferedReader br = new BufferedReader(new StringReader(sdata));
		
		String str = null;
		
		List<String[]> dataList = new ArrayList<String[]>();
		
		while((str=br.readLine())!=null){
			  String[] sTokens = str.split(";");
		      String s = sTokens[2]; // s.1, s.2, ..., s.d
		      String[] data = s.split(",");
		     
		      String[] finalData = new String[2];
		      finalData[0] = sTokens[0];
		      finalData[1] = sTokens[1].substring(1);
		      finalData = (String[])ArrayUtils.addAll(finalData, data);
		      dataList.add(finalData);
			}
			
			br.close();
			double[][] matrix = AppUtil.ListToArray(dataList, dimesion);
			logger.warn("dimesion="+dimesion);
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					logger.warn(matrix[i][j]+"#");
				}
			}
			return KDTreeApp.build(matrix);
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		int k = Integer.parseInt(args[0]);
	    int d = Integer.parseInt(args[1]);
	    String inputDatasetR = args[2];
	    String inputDatasetS = args[3];
	    String output = args[4];
	    /********************本地测试用******************************/
//	    int k = 4;
//	    int d = 2;
//	    String inputDatasetR = "/user/knnspark/test/R.txt";
//	    String inputDatasetS = "/user/knnspark/test/S.txt";
//	    String output = "/user/knnspark/test/output";
	    /********************本地测试用******************************/
	    
	    System.out.println("k="+k+",d="+d);
	    System.out.println("inputDatasetR="+inputDatasetR);
	    System.out.println("inputDatasetS="+inputDatasetS);
	    System.out.println("output="+output);
	    
	    long begin = System.currentTimeMillis();
		
		Configuration conf = new Configuration();
		conf.setInt("d", d);
		
		Job job = Job.getInstance(conf);
		job.addCacheFile(new Path(inputDatasetS).toUri());
//		AppUtil.kdtree = buildKDTree(inputDatasetS,d);
		job.setJarByClass(App.class);
		job.setMapperClass(Mapper1.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		
		job.setReducerClass(Reducer1.class);
		
		FileInputFormat.setInputPaths(job, inputDatasetR);
		
		Path outPath = new Path(output);
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(outPath)){
			fs.delete(outPath);
		}
		FileOutputFormat.setOutputPath(job, outPath);
		
		boolean flag = job.waitForCompletion(true);
		
		long end = System.currentTimeMillis();
		System.out.println("job:"+flag+","+(end-begin));
		
	}

}
