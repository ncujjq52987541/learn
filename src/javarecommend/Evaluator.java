package javarecommend;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Evaluator {
	private Set<Integer> items = new HashSet<Integer>();
	public Evaluator(String path){
		FileInputStream fis =null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	BufferedInputStream bis = new BufferedInputStream(fis);
    	DataInputStream dis = new DataInputStream(bis);
    	String str = null;
    	try {
			while((str=dis.readLine())!=null){
				String[] datas = str.split("::");
//				int userId = Integer.parseInt(datas[0]);
				int itemId = Integer.parseInt(datas[1]);
//				float rating = Float.parseFloat(datas[2]);
//				m.put(itemId, rating);
				items.add(itemId);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			dis.close();
			bis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public  float precision(List<Result> predict){
		Set<Integer> r = new HashSet<Integer>();
		for (Iterator iterator = predict.iterator(); iterator.hasNext();) {
			Result result = (Result) iterator.next();
			r.add(result.getItemId());
		}
		Set<Integer> intersection = new HashSet<Integer>();
		intersection.addAll(r);
		intersection.retainAll(items);
		return (float)intersection.size()/r.size();
	}
}
