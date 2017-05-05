package wc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordApp {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/wc/input";
		String outputPath="/root/wc/output";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
//		job.setNumReduceTasks(2);
		
		
		job.setJarByClass(WordApp.class);
		
		job.setMapperClass(WordMapper.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setReducerClass(WordReducer.class);
		
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
		
		
	}

}
