package pageRank;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class App1 {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long begin = System.currentTimeMillis();
		
		String inputPath = "";
		String outputPath= "";
		Configuration conf = new Configuration();
		conf.setInt("nodeNum", 4);//节点数，这里写死，具体和数据有关，一般数据有多少行，节点数就是多少
		double d = 0.001;//收敛值
		int i=0;
		while(true){
			Job job = Job.getInstance(conf);
			if(i==0){
				inputPath = "/root/pageRank/input";
				outputPath= "/root/pageRank/output/"+i;
			}else{
				inputPath = "/root/pageRank/output/"+(i-1);
				outputPath= "/root/pageRank/output/"+i;
			}
			
	//		job.setNumReduceTasks(3);
			
			job.setJarByClass(App1.class);
			
			job.setMapperClass(Mapper1.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
	
			job.setReducerClass(Reducer1.class);
			
			FileInputFormat.setInputPaths(job, inputPath);
			
			Path outPath = new Path(outputPath);
			FileSystem fs = FileSystem.get(conf);
			if(fs.exists(outPath)){
				fs.delete(outPath);
			}
			FileOutputFormat.setOutputPath(job, outPath);
			
			boolean flag = job.waitForCompletion(true);
			
			long end = System.currentTimeMillis();
			System.out.println("job:"+flag+","+(end-begin));
			
			long sum = job.getCounters().findCounter(MyCounter.my).getValue();
			double nowD = sum/(conf.getInt("nodeNum", 4)*1000.0);
			if(nowD<=d) break;
			i++;
//			if(i==2) break;
		}
	}
}
