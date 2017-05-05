package tfidf;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class Mapper1 extends Mapper<LongWritable, Text, Text, FloatWritable> {

	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		String[] values = value.toString().split("\t");
		if(values==null||values.length<=1){
			return;
		}
		//计算器增1，记录有效行数
		context.getCounter(LineCount.num).increment(1L);
		
		String id = values[0];
		String content = values[1];
		
		StringReader reader = new StringReader(content);
		IKSegmenter ikSegementer = new IKSegmenter(reader, true);
		Lexeme word = null;
		//注释是视频中代码，应该是有问题的
//		while((word=ikSegementer.next())!=null){
//			String str = word.getLexemeText();
//			context.write(new Text(str+"_"+id), new IntWritable(1));
//		}
		Map<String,Integer> m = new HashMap<String,Integer>();
		int totalWord=0;
		while((word=ikSegementer.next())!=null){
			totalWord++;
			String str = word.getLexemeText();
			if(m.get(str)==null){
				m.put(str, 1);
			}else{
				m.put(str, m.get(str)+1);
			}
//			context.write(new Text(str+"_"+id), new IntWritable(1));
		}
		
		Set<Map.Entry<String, Integer>> entry = m.entrySet();
		for (Iterator iterator = entry.iterator(); iterator.hasNext();) {
			Entry<String, Integer> entry2 = (Entry<String, Integer>) iterator
					.next();
			context.write(new Text(entry2.getKey()+"_"+id), new FloatWritable(entry2.getValue()*1.0f/totalWord));
			
		}
		
	}

	
}
