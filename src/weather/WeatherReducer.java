package weather;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WeatherReducer extends Reducer<Weather, FloatWritable, Text, FloatWritable> {

	@Override
	protected void reduce(Weather w, Iterable<FloatWritable> values,Context context)
			throws IOException, InterruptedException {
		int i=0;
		for(FloatWritable val:values){
			context.write(new Text(w.getYear()+"-"+w.getMonth()), val);
			i++;
			if(i>=2){
				break;
			}
		}
	}

	
}
