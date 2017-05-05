package knn;

import java.util.List;

import kdtree.KDTreeApp;

public class AppUtil {
	public static KDTreeApp kdtree;
	public static double[][] ListToArray(List<String[]> dataList,int dimesion){
		double[][] data = new double[dataList.size()][dimesion];

		for(int i=0;i<dataList.size();i++){
			data[i]=stringArrToDoubleArr(dataList.get(i));
		}
		
		return data;
	}
	
	public static double[] stringArrToDoubleArr(String[] strArr){
		double[] data = new double[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			data[i] = Double.parseDouble(strArr[i]);
		}
		
		return data;
	}
}
