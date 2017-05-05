package javarecommend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Recommend {
	//用户物品喜好矩阵  userPre.put(1, new List{1,2});  用户id和物品集合id
	private Map<Integer,List<UserItemRating>> userPre = new HashMap<Integer,List<UserItemRating>>();
	/**
	 * 物品id--索引号
	 * itemIndex.put(1, 0);
	 * itemIndex.put(2, 1);
	 * itemIndex.put(3, 2);
	 * itemIndex.put(4, 3);
	 */
	private TreeMap<Integer,Integer> itemIndex  = new TreeMap<Integer,Integer>();
	/**
	 * 索引号--物品id
	 * indexItem.put(0, 1);
	 * indexItem.put(1, 2);
	 * indexItem.put(2, 3);
	 * indexItem.put(3, 4);
	 */
	private TreeMap<Integer,Integer> indexItem  = new TreeMap<Integer,Integer>();
	private Double[][] simMatrix;
	private Map<Integer,List<UserItemRating>> adduserPre(int userId,int itemId,float rating){
		List<UserItemRating> list = userPre.get(userId);
		UserItemRating uir = new UserItemRating();
		uir.setItemId(itemId);
		uir.setUserId(userId);
		uir.setRating(rating);
		if(list==null){
			List<UserItemRating> l = new ArrayList<UserItemRating>();
			l.add(uir);
			userPre.put(userId, l);
		}else{
			list.add(uir);
		}
		return userPre;
	}
	private void loadData(String path,String splitSymbol){
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
    	
		TreeSet<Integer> items = new TreeSet<Integer>();
		
		try {
			while((str=dis.readLine())!=null){
				String[] datas = str.split(splitSymbol);
				int userId = Integer.parseInt(datas[0]);
				int itemId = Integer.parseInt(datas[1]);
				float rating = Float.parseFloat(datas[2]);
				items.add(itemId);
				adduserPre(userId,itemId,rating);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int index = 0;
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			itemIndex.put(integer, index);
			indexItem.put(index, integer);
			index++;
		}
		simMatrix = new Double[itemIndex.size()][itemIndex.size()];
		for (int i = 0; i < simMatrix.length; i++) {
			for (int j = 0; j < simMatrix[i].length; j++) {
				simMatrix[i][j]=0d;
			}
		}
		try {
			dis.close();
			bis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void train(String path){
		loadData(path,"::");
		simItemMatrix();
	}
	public void train(String path,String symbol){
		loadData(path,symbol);
		simItemMatrix();
	}
	public void saveModel(String path){
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(path);
			bos = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(userPre);
			oos.writeObject(itemIndex);
			oos.writeObject(indexItem);
			oos.writeObject(simMatrix);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			oos.close();
			bos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	public void loadModel(String path){
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(path);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			userPre = (Map<Integer,List<UserItemRating>>)ois.readObject();
			itemIndex = (TreeMap<Integer,Integer>)ois.readObject();
			indexItem = (TreeMap<Integer,Integer>)ois.readObject();
			simMatrix = (Double[][])ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			ois.close();
			bis.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Double[][] simItemMatrix(){
//		double[][] simMatrix = new double[itemIndex.size()][itemIndex.size()];
		
		Collection<List<UserItemRating>> c = userPre.values();
		//计算同现次数
		for (Iterator iterator = c.iterator(); iterator.hasNext();) {
			List<UserItemRating> itemList = (List<UserItemRating>) iterator.next();
			for (int i = 0; i < itemList.size(); i++) {
				for (int j = i; j < itemList.size(); j++) {
					if(simMatrix[itemIndex.get(itemList.get(i).getItemId())][itemIndex.get(itemList.get(j).getItemId())]==0){
						simMatrix[itemIndex.get(itemList.get(i).getItemId())][itemIndex.get(itemList.get(j).getItemId())]=1D;
					}else{
						simMatrix[itemIndex.get(itemList.get(i).getItemId())][itemIndex.get(itemList.get(j).getItemId())]+=1D; 
					}
				}
			}
			
		}
	  /**
	   * 同现相似度矩阵计算.
	   * w(i,j) = N(i)∩N(j)/sqrt(N(i)*N(j))
	   * 其中simMatrix[i][j]值是N(i)∩N(j)
	   *    simMatrix[i][i]值是N(i)
	   *    simMatrix[j][j]值是N(j)
	   *
	   */
		for (int i = 0; i < simMatrix.length; i++) {
			for (int j = 0; j < simMatrix[i].length; j++) {
				if(i!=j){
					simMatrix[i][j] = simMatrix[i][j]/Math.sqrt(simMatrix[i][i]*simMatrix[j][j]);
				}
			}
		}
		for (int i = 0; i < simMatrix.length; i++) {
			for (int j = 0; j < simMatrix[i].length; j++) {
				if(i==j){
					simMatrix[i][i] = 0D;
				}
				if(i>j){
					//把上半角矩阵值赋给对应的下半角矩阵
					simMatrix[i][j]=simMatrix[j][i];
				}
			}
			
		}
		
		return simMatrix;
	}
	/**
	 * @param simMatrix  同现矩阵
	 * @param userRating   某个用户对物品的评分   Map<Integer,Float> userRating  物品id--评分
	 */
	private Map<Integer,Float> recommend( Map<Integer,Float> userRating){
		Map<Integer,Float> result = new HashMap<Integer,Float>();
		for (int i = 0; i < simMatrix.length; i++) {
			float sum = 0;
			for (int j = 0; j < simMatrix[i].length; j++) {
				int itemId = indexItem.get(j);//根据索引找到物品实际id
				float rating = userRating.get(itemId)==null?0:userRating.get(itemId);
				sum += simMatrix[i][j]*rating;
			}
			int finalItemId = indexItem.get(i);//最终计算完的物品id
			result.put(finalItemId, sum);
		}
		
		return result;
	}
	/**
	 * 推荐
	 * @param simMatrix
	 * @param userId
	 * @return
	 */
//	public Map<Integer,Float> recommend(double[][] simMatrix , int userId){
//		List<UserItemRating> list = userPre.get(userId);
//		Map<Integer,Float> m = new HashMap<Integer,Float>();
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			UserItemRating userItemRating = (UserItemRating) iterator.next();
//			m.put(userItemRating.getItemId(), userItemRating.getRating());
//		}
//		return recommend(simMatrix, m);
//	}
	/**
	 * 
	 * @param simMatrix 同现矩阵
	 * @param userId
	 * @param num 推荐列表数量
	 * @param filter  true过滤调训练集已有的itemId
	 * @return
	 */
	public List<Result> recommend(int userId,int num,boolean filter){
		List<UserItemRating> list = userPre.get(userId);
		if(list==null){
			System.out.println("用户："+userId+",不存在");
			return new ArrayList<Result>();
		}
		Map<Integer,Float> m = new HashMap<Integer,Float>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UserItemRating userItemRating = (UserItemRating) iterator.next();
			m.put(userItemRating.getItemId(), userItemRating.getRating());
		}
		
		List<Result> listResult = new ArrayList<Result>();
		
		Map<Integer,Float> result = recommend(m);
		if(filter){
			List<UserItemRating> uir = userPre.get(userId);
			for (Iterator iterator = uir.iterator(); iterator.hasNext();) {
				UserItemRating userItemRating = (UserItemRating) iterator
						.next();
				result.remove(userItemRating.getItemId());
				
			}
		}
		Set<Integer> keys = result.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();
			Result r= new Result();
			r.setItemId(key);
			r.setSimDegree(result.get(key));
			listResult.add(r);
		}
		Collections.sort(listResult);
		
		if(listResult.size()>num){
			return listResult.subList(0, num);
		}
		return listResult;
	}
	/**
	 * 
	 * @param simMatrix
	 * @param userRating
	 * @param userId
	 * @return 根据用户的Id过滤掉该用户已有的偏好
	 */
//	public Map<Integer,Float> recommend(double[][] simMatrix , Map<Integer,Float> userRating,int userId){
//		Map<Integer,Float> result = recommend(simMatrix, userRating);
//		List<UserItemRating> items = userPre.get(userId);
//		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
//			UserItemRating uir = (UserItemRating) iterator.next();
//			result.remove(uir.getItemId());
//		}
//		return result;
//	}
	/**
	 * 对推荐结果排序 出来的结果是    rating--itemId
	 * @param result
	 * @return
	 */
//	public Map<Float,Integer> recommendOrder(Map<Integer,Float> result){
//		Map<Float,Integer> orderResult = new TreeMap<Float,Integer>();
//		Set<Integer> keys = result.keySet();
//		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
//			Integer key = (Integer) iterator.next();
//			orderResult.put(result.get(key), key);
//		}
//		return orderResult;
//	}
	/**
	 * 为测试用
	 * @param path
	 * @return
	 */
//	public Map<Integer,Float> loadUserRatingForTest(String path){
//		Map<Integer,Float> m = new HashMap<Integer,Float>();
//		FileInputStream fis =null;
//		try {
//			fis = new FileInputStream(path);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	BufferedInputStream bis = new BufferedInputStream(fis);
//    	DataInputStream dis = new DataInputStream(bis);
//    	String str = null;
//    	try {
//			while((str=dis.readLine())!=null){
//				String[] datas = str.split("::");
////				int userId = Integer.parseInt(datas[0]);
//				int itemId = Integer.parseInt(datas[1]);
//				float rating = Float.parseFloat(datas[2]);
//				m.put(itemId, rating);
//			}
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	
//    	try {
//			dis.close();
//			bis.close();
//			fis.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	return m;
//	}
//	public void printlnRecommendResult(Map result){
//		Set s = result.keySet();
//		for (Iterator iterator = s.iterator(); iterator.hasNext();) {
//			Object key = (Object) iterator.next();
//			System.out.println(key+"-->"+result.get(key));
//		}
//	}
	
	public void printlnRecommendResult(List<Result> result){
		
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Result r = (Result) iterator.next();
			System.out.println(r.getItemId()+"-->"+r.getSimDegree());
		}
	}
	public Double[][] getSimMatrix() {
		return simMatrix;
	}
	
}
