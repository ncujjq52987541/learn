package tfidf;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class App {
	public static long job1() throws IOException, ClassNotFoundException, InterruptedException{
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/tfidf/input";
		String outputPath="/root/tfidf/output1";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		job.setNumReduceTasks(4);
		
		
		job.setJarByClass(App.class);
		
		job.setMapperClass(Mapper1.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FloatWritable.class);

		job.setCombinerClass(Reducer1.class);
		
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
		System.out.println("job1:"+flag+","+(end-begin));
		
		return job.getCounters().findCounter(LineCount.num).getValue();
	}
	
	public static void job2() throws IOException, ClassNotFoundException, InterruptedException{
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/tfidf/output1";
		String outputPath="/root/tfidf/output2";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(App.class);
		
		job.setMapperClass(Mapper2.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setCombinerClass(Reducer2.class);
		
		job.setReducerClass(Reducer2.class);
		
		FileInputFormat.setInputPaths(job, inputPath);
		
		Path outPath = new Path(outputPath);
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(outPath)){
			fs.delete(outPath);
		}
		FileOutputFormat.setOutputPath(job, outPath);
		
		boolean flag = job.waitForCompletion(true);
		
		long end = System.currentTimeMillis();
		System.out.println("job2:"+flag+","+(end-begin));
		
	}
	
	public static void job3(long totalNum) throws IOException, ClassNotFoundException, InterruptedException{
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/tfidf/output1";
		String outputPath="/root/tfidf/output3";
		Configuration conf = new Configuration();
		conf.setLong("totalNum", totalNum);
		
		Job job = Job.getInstance(conf);
		job.addCacheFile(new Path("/root/tfidf/output2/part-r-00000").toUri());
		
		job.setJarByClass(App.class);
		job.setMapperClass(Mapper3.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		
		job.setReducerClass(Reducer3.class);
		
		FileInputFormat.setInputPaths(job, inputPath);
		
		Path outPath = new Path(outputPath);
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(outPath)){
			fs.delete(outPath);
		}
		FileOutputFormat.setOutputPath(job, outPath);
		
		boolean flag = job.waitForCompletion(true);
		
		long end = System.currentTimeMillis();
		System.out.println("job3:"+flag+","+(end-begin));
		
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long num = 47647L;
//		num = job1();
//		System.out.println("num:"+num);
		job3(num);
		
	}

}
