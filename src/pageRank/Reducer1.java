package pageRank;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, NullWritable> {

	
	@Override
	protected void reduce(Text t, Iterable<Text> iterable,Context context)
			throws IOException, InterruptedException {
		float sum = 0;
		Node outNode = null;
		for(Text iter:iterable){
			Node n = Node.formatFromString(iter.toString());
			if(n.getLinkNodes()==null){
				sum+=n.getPr();
			}else{
				outNode = n;
			}
		}
		//计算PageRank
		int nodeNum = context.getConfiguration().getInt("nodeNum", 1);
		float pr = (float)((1-0.85)/nodeNum+0.85*sum);
//		float pr = sum;
		
		//计算差值
		float d = outNode.getPr()-pr;
		int a = Math.abs((int)(d*1000));//1000为精度，这里先写死
		context.getCounter(MyCounter.my).increment(a);
		
		outNode.setPr(pr);
		
		context.write(new Text(t+"\t"+outNode.toString()), null);
	}
}
