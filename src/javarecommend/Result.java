package javarecommend;

public class Result implements Comparable<Result>{
	private int itemId;
	private float simDegree;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public float getSimDegree() {
		return simDegree;
	}
	public void setSimDegree(float simDegree) {
		this.simDegree = simDegree;
	}
	public int compareTo(Result o) {
		if(simDegree>o.getSimDegree()){
			return -1;
		}else if(simDegree<o.getSimDegree()){
			return 1;
		}else{
			return 0;
		}
	}
	
}
