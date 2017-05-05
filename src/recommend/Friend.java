package recommend;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Friend implements WritableComparable<Friend>{
	private String friend1;
	private String friend2;
	private int hot;
	
	public String getFriend1() {
		return friend1;
	}

	public void setFriend1(String friend1) {
		this.friend1 = friend1;
	}

	public String getFriend2() {
		return friend2;
	}

	public void setFriend2(String friend2) {
		this.friend2 = friend2;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public void readFields(DataInput input) throws IOException {
		this.friend1 = input.readUTF();
		this.friend2 = input.readUTF();
		this.hot = input.readInt();
	}

	public void write(DataOutput output) throws IOException {
		output.writeUTF(this.friend1);
		output.writeUTF(this.friend2);
		output.writeInt(this.hot);
	}

	public int compareTo(Friend o) {
		int c = this.friend1.compareTo(o.getFriend1());
		if(c==0){
			return -Integer.compare(this.hot, o.getHot());
		}
		return c;
	}

}
