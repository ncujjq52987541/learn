package kdtree;
import java.util.Arrays;
import java.util.TreeMap;


public class KDTreeAppUtil {
	
	public static double[] getData(double[] rawData){
		return Arrays.copyOfRange(rawData, 2, rawData.length);
	}
	
	public static void main(String[] args) {
		double rawData[] = {1,2,1.1,1.2,1.3};
		double result[] = getData(rawData);
		
		System.out.println(result);
	}
}
