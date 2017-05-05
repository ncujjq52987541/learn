package tfidf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class Mapper3 extends Mapper<LongWritable, Text, Text, Text> {

	private static Map<String,Integer> cf = new HashMap<String,Integer>();
	
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		if(cf.size()==0){
			URI[] uri = context.getCacheFiles();
			Path path = new Path(uri[0].getPath());
			BufferedReader br = new BufferedReader(new FileReader(path.getName()));
			String str = null;
			while((str=br.readLine())!=null){
				String[] values = str.split("\t");
				cf.put(values[0], Integer.parseInt(values[1]));
			}
		}
	}

	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		String[] values = value.toString().split("\t");
		if(values!=null&&values.length==2){
			String[] tmp = values[0].split("_");
			if(tmp!=null&&tmp.length==2){
				String word = tmp[0];
				String id = tmp[1];
				
				long totalNum = context.getConfiguration().getLong("totalNum", 1L);
				float tfidf = (float)(Float.parseFloat(values[1])*Math.log((totalNum*1.0)/cf.get(word)));
				
				context.write(new Text(id), new Text(word+":"+tfidf));
			}
		}
	}

	
}
