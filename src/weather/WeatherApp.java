package weather;

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

public class WeatherApp {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long begin = System.currentTimeMillis();
		
		String inputPath = "/root/weather/input";
		String outputPath="/root/weather/output";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		job.setNumReduceTasks(3);
		job.setPartitionerClass(WeatherPartition.class);
//		job.setSortComparatorClass(WeatherSort.class);
//		job.setGroupingComparatorClass(WeatherGroup.class);
		
		job.setJarByClass(WeatherApp.class);
		
		job.setMapperClass(WeatherMapper.class);
		job.setOutputKeyClass(Weather.class);
		job.setOutputValueClass(FloatWritable.class);

		job.setReducerClass(WeatherReducer.class);
		
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
