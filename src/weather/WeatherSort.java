package weather;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class WeatherSort extends WritableComparator{

	public WeatherSort(){
		super(Weather.class,true);
	}
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		Weather w1 = (Weather)a;
		Weather w2 = (Weather)b;
		int c1 = w1.getYear()-w2.getYear();
		if(c1==0){
			int c2 = w1.getMonth() - w2.getMonth();
			if(c2==0){
				float t = w1.getTemperature()-w2.getTemperature();
				if(t<0){
					return 1;
				}else if(t>0){
					return -1;
				}else{
					return 0;
				}
			}
			return c2;
		}
		
		return c1;
	}
}
