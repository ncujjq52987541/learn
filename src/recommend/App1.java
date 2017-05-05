package recommend;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class App1 {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/recommend/input";
		String outputPath= "/root/recommend/output1";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
//		job.setNumReduceTasks(3);
		
		job.setJarByClass(App1.class);
		
		job.setMapperClass(Mapper1.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

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
		
		
	}

}
