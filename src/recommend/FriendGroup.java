package recommend;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FriendGroup extends WritableComparator{
	public FriendGroup(){
		super(Friend.class,true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		Friend f1= (Friend)a;
		Friend f2 = (Friend)b;
		return f1.getFriend1().compareTo(f2.getFriend1());
	}
	
	
}
