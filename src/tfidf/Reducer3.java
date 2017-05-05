package tfidf;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values,Context context)
			throws IOException, InterruptedException {
		StringBuffer sb = new StringBuffer();
		for (Text val:values) {
			sb.append(val.toString()).append("\t");
		}
		context.write(new Text(key),new Text(sb.toString()));
	}
	

}
