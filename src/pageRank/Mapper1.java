package pageRank;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {

	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		String[] values = value.toString().split("\t");
		
		Node n = new Node();
		n.setPr(Float.parseFloat(values[1]));
		
		String[] linkNodes = new String[values.length-2];
		
		for (int i = 2; i < values.length; i++) {
			Node n1 = new Node();
			n1.setPr(n.getPr()/(values.length-2));
			linkNodes[i-2]=values[i];
			context.write(new Text(values[i]), new Text(""+n1.getPr()));
		}
		
		n.setLinkNodes(linkNodes);
		context.write(new Text(values[0]), new Text(n.toString()));
	}

}
