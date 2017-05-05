package weather;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class WeatherMapper extends Mapper<LongWritable, Text, Weather, FloatWritable> {

	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		String[] values = value.toString().split("\t");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;;
		try {
			date = sdf.parse(values[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		Weather w = new Weather();
		w.setYear(c.get(Calendar.YEAR));
		w.setMonth(c.get(Calendar.MONTH)+1);
		w.setTemperature(Float.parseFloat(values[1]));
		context.write(w, new FloatWritable(w.getTemperature()));
	}

	
}
