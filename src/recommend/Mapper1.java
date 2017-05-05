package recommend;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper1 extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		String[] values = value.toString().split(" ");
		
		for (int i = 0; i < values.length; i++) {
			for (int j = i+1; j < values.length; j++) {
				if(i!=0){
					context.write(new Text(FriendUtil.friend(values[i], values[j])), new IntWritable(1));
				}else{
					context.write(new Text(FriendUtil.friend(values[i], values[j])), new IntWritable(0));
				}
			}
		}
	}

}
