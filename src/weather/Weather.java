package weather;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Weather implements WritableComparable<Weather>{
	private int year;
	private int month;
	private float temperature;
	public void readFields(DataInput input) throws IOException {
		year = input.readInt();
		month = input.readInt();
		temperature = input.readFloat();
	}
	public void write(DataOutput output) throws IOException {
		output.writeInt(year);
		output.writeInt(month);
		output.writeFloat(temperature);
		
	}
	public int compareTo(Weather w) {
		int c1 = year-w.getYear();
		if(c1==0){
			int c2 = month - w.getMonth();
			if(c2==0){
				float a = temperature-w.getTemperature();
				if(a<0){
					return 1;
				}else if(a>0){
					return -1;
				}else{
					return 0;
				}
			}
			return c2;
		}
		
		return c1;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	
	
}
