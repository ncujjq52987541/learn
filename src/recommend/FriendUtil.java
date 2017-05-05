package recommend;

public class FriendUtil {
	public static String friend(String f1,String f2){
		if(f1.compareTo(f2)>0){
			return f2+"-"+f1;
		}
		
		return f1+"-"+f2;
	}
}
