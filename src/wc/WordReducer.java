package wc;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text text, Iterable<IntWritable> values,Context context)
			throws IOException, InterruptedException {
		int total = 0;
		for (IntWritable val:values) {
			total+=val.get();
		}
		context.write(text, new IntWritable(total));
	}
	

}
