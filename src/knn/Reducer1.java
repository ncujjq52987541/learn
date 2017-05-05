package knn;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text text, Iterable<Text> values,Context context)
			throws IOException, InterruptedException {
		String result = "";
		for (Text val:values) {
			result+=val;
			result+="#";
		}
		context.write(text, new Text(result));
	}
	

}
