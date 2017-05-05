package recommend;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, IntWritable, Text, NullWritable> {

	@Override
	protected void reduce(Text text, Iterable<IntWritable> iterable,Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		boolean flag = true;
		for(IntWritable i:iterable){
			if(i.get()==0){
				flag = false;
				break;
			}
			sum+=i.get();
		}
		if(flag){
			context.write(new Text(text.toString()+"-"+sum), null);
		}
	}

}
