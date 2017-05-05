package tfidf;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	@Override
	protected void reduce(Text text, Iterable<FloatWritable> values,Context context)
			throws IOException, InterruptedException {
		float total = 0;
		for (FloatWritable val:values) {
			total+=val.get();
		}
		context.write(text, new FloatWritable(total));
	}
	

}
