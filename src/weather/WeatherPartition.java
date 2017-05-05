package weather;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class WeatherPartition extends HashPartitioner<Weather, FloatWritable>{

	@Override
	public int getPartition(Weather key, FloatWritable value, int numReduceTasks) {
		return (key.getYear()-1949)%numReduceTasks;
	}

	
}
