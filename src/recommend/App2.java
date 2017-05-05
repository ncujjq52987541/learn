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

public class App2 {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/recommend/output1";
		String outputPath= "/root/recommend/output2";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
//		job.setNumReduceTasks(3);
		job.setGroupingComparatorClass(FriendGroup.class);
		
		job.setJarByClass(App2.class);
		
		job.setMapperClass(Mapper2.class);
		job.setOutputKeyClass(Friend.class);
		job.setOutputValueClass(IntWritable.class);
		
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
		System.out.println("job:"+flag+","+(end-begin));
		
		
	}

}
