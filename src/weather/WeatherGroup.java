package weather;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class WeatherGroup extends WritableComparator{

	public WeatherGroup(){
		super(Weather.class,true);
	}
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		Weather w1 = (Weather)a;
		Weather w2 = (Weather)b;
		int c1 = w1.getYear()-w2.getYear();
		if(c1==0){
			int c2 = w1.getMonth() - w2.getMonth();
			return c2;
		}
		
		return c1;
	}
}
