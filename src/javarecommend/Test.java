package javarecommend;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Test {
	private String splitSymbol="::";
	private Map<Integer,List<UserItemRating>> adduserPre(Map<Integer,List<UserItemRating>> userItemRating,int userId,int itemId,float rating){
		List<UserItemRating> list = userItemRating.get(userId);
		UserItemRating uir = new UserItemRating();
		uir.setItemId(itemId);
		uir.setUserId(userId);
		uir.setRating(rating);
		if(list==null){
			List<UserItemRating> l = new ArrayList<UserItemRating>();
			l.add(uir);
			userItemRating.put(userId, l);
		}else{
			list.add(uir);
		}
		return userItemRating;
	}
	
	public Map<Integer,List<UserItemRating>> loadTestData(String path){
		Map<Integer,List<UserItemRating>> userItemRating = new HashMap<Integer,List<UserItemRating>>();
		FileInputStream fis =null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	BufferedInputStream bis = new BufferedInputStream(fis);
    	DataInputStream dis = new DataInputStream(bis);
    	
    	String str = null;
		try {
			while((str=dis.readLine())!=null){
				String[] datas = str.split(splitSymbol);
				int userId = Integer.parseInt(datas[0]);
				int itemId = Integer.parseInt(datas[1]);
				float rating = Float.parseFloat(datas[2]);
				adduserPre(userItemRating,userId,itemId,rating);
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
		return userItemRating;
	}
	public int intersection(List<Result> r,List<UserItemRating> t){
		Set<Integer> finalResult = new HashSet<Integer>();
		Set<Integer> rSet = new HashSet<Integer>();
		Set<Integer> tSet = new HashSet<Integer>();
		for (Iterator iterator = r.iterator(); iterator.hasNext();) {
			Result result = (Result) iterator.next();
			rSet.add(result.getItemId());
		}
		
		for (Iterator iterator = t.iterator(); iterator.hasNext();) {
			UserItemRating uir = (UserItemRating) iterator.next();
			tSet.add(uir.getItemId());
		}
		
		finalResult.addAll(rSet);
		finalResult.retainAll(tSet);
		
		return finalResult.size();
	}
	public void recommendTest(int recommendNum){
		String trainData = "D:\\tmp\\recommend\\aa_test\\part-00000";
		String testData = "D:\\tmp\\recommend\\aa_train\\part-00000";
//		String trainData = "D:\\tmp\\recommend\\ml-100k\\u1.base";
//		String testData = "D:\\tmp\\recommend\\ml-100k\\u1.test";
		Map<Integer,List<UserItemRating>> userItemRating = loadTestData(trainData);
		Recommend sm = new Recommend();
		sm.train(testData,splitSymbol);
		int numerator = 0;//分子
		int denominator = 0;//分母
		
		Set<Integer> keys = userItemRating.keySet();
		int totalUsers = keys.size();
		int i=1;
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();
			List<Result> result = sm.recommend(key,recommendNum,true);
			numerator+=intersection(result,userItemRating.get(key));
			denominator+=result.size();
			System.out.println("总共："+totalUsers+"用户，已算完："+i+++"个"+"当前准确率："+(float)numerator/denominator);
		}
		System.out.println("准确率："+(float)numerator/denominator);
	}
	public static void test1(){
//		Recommend sm = new Recommend();
//		//"D:\\tmp\\recommend\\alstrain.txt"            "D:\\tmp\\recommend\\ratings_train.dat"
//		sm.loadData("D:\\tmp\\recommend\\alstrain.txt");
//		double[][] simMatrix = sm.simItemMatrix();
//		System.out.println("同现矩阵：");
//		for (int i = 0; i < simMatrix.length; i++) {
//			for (int j = 0; j < simMatrix[i].length; j++) {
//				System.out.print(simMatrix[i][j]+",");
//			}
//			System.out.println();
//		}
//		
//		System.out.println("推荐结果：");
//		TreeSet<Result> result = sm.recommend(simMatrix,2,2);
//		sm.printlnRecommendResult(result);
	}
	
	public static void test2(){
		Recommend sm = new Recommend();
		//"D:\\tmp\\recommend\\alstrain.txt"            "D:\\tmp\\recommend\\ratings_train.dat"
		sm.train("D:\\tmp\\recommend\\alstrain.txt");
//		Double[][] simMatrix = sm.simItemMatrix();
//		System.out.println("同现矩阵：");
//		for (int i = 0; i < simMatrix.length; i++) {
//			for (int j = 0; j < simMatrix[i].length; j++) {
//				System.out.print(simMatrix[i][j]+",");
//			}
//			System.out.println();
//		}
		
		System.out.println("推荐结果：");
		//D:\\tmp\\recommend\\alstest.txt        D:\\tmp\\recommend\\ratings_train.dat
//		Map<Integer,Float> m = sm.loadUserRatingForTest("D:\\tmp\\recommend\\ratings_train.dat");
		List<Result> result = sm.recommend(2,10,true);
		sm.printlnRecommendResult(result);
	}
	public static void testSaveModel(){
		Recommend sm = new Recommend();
		sm.train("D:\\tmp\\recommend\\ratings_train.dat");
//		double[][] simMatrix = sm.simItemMatrix();
		sm.saveModel("D:\\tmp\\recommend\\recommendModel");
	}
	
	public static void testLoadModel(){
		Recommend sm = new Recommend();
		sm.loadModel("D:\\tmp\\recommend\\recommendModel");
		List<Result> result = sm.recommend(6040,10,true);
		sm.printlnRecommendResult(result);
	}
	public static void test3(){
		Recommend sm = new Recommend();
		sm.train("D:\\tmp\\recommend\\ratings_train.dat");
		long begin = System.currentTimeMillis();
		List<Result> result = sm.recommend(2,160,true);
		long end = System.currentTimeMillis();
		System.out.println("");
		System.out.println("推荐时间："+(end-begin)/1000.0);
		sm.printlnRecommendResult(result);
		
		Evaluator e = new Evaluator("D:\\tmp\\recommend\\ratings_test.dat");
		System.out.println("准确率："+e.precision(result));
	}
	public static void main(String[] args) throws IOException {
		long begin = System.currentTimeMillis();
		Test test = new Test();
		test.recommendTest(10);
//		test2();
//		test1();
//		testSaveModel();
//		testLoadModel();
//		test3();
		long end = System.currentTimeMillis();
		System.out.println("");
		System.out.println("运行时间："+(end-begin)/1000.0);
	}
}
