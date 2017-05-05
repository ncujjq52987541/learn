package recommend;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Friend, IntWritable, Text, NullWritable> {

	@Override
	protected void reduce(Friend f, Iterable<IntWritable> iter,Context context)
			throws IOException, InterruptedException {
		for (IntWritable intWritable : iter) {
			String msg = f.getFriend1()+" "+f.getFriend2()+" "+intWritable.get();
			context.write(new Text(msg), null);
		}
		
	}

	

}
