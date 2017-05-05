package knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kdtree.KDTreeApp;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {
	private Log logger = LogFactory.getLog(Mapper1.class);
	private KDTreeApp tree ;//= AppUtil.kdtree;
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		URI[] uri = context.getCacheFiles();
		Path path = new Path(uri[0].getPath());
		BufferedReader br = new BufferedReader(new FileReader(path.getName()));

		/********************本地测试用******************************/
//		String sdata="100;c1;1.0,1.0\r101;c1;1.1,1.2\r102;c1;1.2,1.0\r103;c1;1.6,1.5\r104;c1;1.3,1.7\r105;c1;2.0,2.1\r106;c1;2.0,2.2\r107;c1;2.3,2.3";
//		BufferedReader br = new BufferedReader(new StringReader(sdata));
		/********************本地测试用******************************/
		
		
		String str = null;
		
		int dimesion = context.getConfiguration().getInt("d", 2);
		List<String[]> dataList = new ArrayList<String[]>();
		
		
		
//		while((str=br.readLine())!=null){
//			  String[] sTokens = str.split(";");
//		      String s = sTokens[2]; // s.1, s.2, ..., s.d
//		      String[] data = s.split(",");
//		      dataList.add(data);
//			}
//			
//			br.close();
//			double[][] matrix = AppUtil.ListToArray(dataList, dimesion);
//			System.out.println("dimesion="+dimesion);
//			for (int i = 0; i < matrix.length; i++) {
//				for (int j = 0; j < matrix[i].length; j++) {
//					System.out.print(matrix[i][j]+"#");
//				}
//				System.out.println();
//			}
//			tree = KDTree.build(matrix);
			
			
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
		tree = KDTreeApp.build(matrix);
	}
	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		 String[] rTokens = value.toString().split(";");
		 String rRecordID = rTokens[0];
		 String r = rTokens[1]; // r.1, r.2, ..., r.d
		 double[] rData = AppUtil.stringArrToDoubleArr(r.split(","));
		 double[] sData = tree.queryKNearest(rData);
		 context.write(new Text(rRecordID), new Text(Arrays.toString(sData)));
	}

	
}
