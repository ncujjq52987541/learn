package pk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PK {
	int num;
	int type;
	int flag=0;//0未抽到，1抽到
	
	public List<PK> init(){
		List<PK> l = new ArrayList<PK>();
		for(int i=1;i<=13;i++){
			for(int j=1;j<=4;j++){
				PK p = new PK();
				p.setNum(i);
				p.setType(j);
				l.add(p);
			}
			
		}
		return l;
	}

	public List<PK> extract(List<PK> all){
		List<PK> ret = new ArrayList<PK>();
		Random r = new Random();
		int num = r.nextInt(52);
		int successNum=0;
		for(;successNum<3;){
			PK p = all.get(num);
			if(p.flag!=1){
				ret.add(p);
				p.setFlag(1);
				successNum++;
			}
			num = r.nextInt(52);
		}
		for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
			PK pk = (PK) iterator.next();
			pk.setFlag(0);
		}
		return ret;
	}
	//是否同花
	public boolean isTonghua(List<PK> pks){
		PK pk1 = pks.get(0);
		PK pk2 = pks.get(1);
		PK pk3 = pks.get(2);
		if(pk1.getType()==pk2.getType()&&pk2.getType()==pk3.getType()){
			return true;
		}
		return false;
		
	}
	//是否拖拉机
	public boolean isTuolaji(List<PK> pks){
		int[] num = new int[3];
		num[0]=pks.get(0).getNum();
		num[1]=pks.get(1).getNum();
		num[2]=pks.get(2).getNum();
		
		Arrays.sort(num);
		
		if(num[0]+1==num[1]&&num[1]+1==num[2]){
			return true;
		}
		if(num[0]==1&&num[1]==12&&num[2]==13){
			return true;
		}
		return false;
	}
	//是否同花拖拉机
	public boolean isTongHuaTuolaji(List<PK> pks){
		if(isTonghua(pks)&&isTuolaji(pks)){
			return true;
		}
		return false;
	}
	//是否豹子
	public boolean isBaozi(List<PK> pks){
		PK pk1 = pks.get(0);
		PK pk2 = pks.get(1);
		PK pk3 = pks.get(2);
		if(pk1.getNum()==pk2.getNum()&&pk2.getNum()==pk3.getNum()){
			return true;
		}
		return false;
		
	}
	public static void main(String[] args) {
		PK pk = new PK();
		List<PK> all = pk.init();
		
		int totaltimes=10000000;
		int numTonghua=0;
		int numTuolaji=0;
		int numTonghuaTuolaji=0;
		int numBaozi=0;
		for (int i = 0; i < totaltimes; i++) {
			List extract = pk.extract(all);
			if(pk.isTonghua(extract)){
				numTonghua++;
			}
			if(pk.isTuolaji(extract)){
				numTuolaji++;
			}
			if(pk.isTongHuaTuolaji(extract)){
				numTonghuaTuolaji++;
			}
			if(pk.isBaozi(extract)){
				numBaozi++;
			}
			
		}
		System.out.println("同花概率："+(numTonghua*1.0/totaltimes));
		System.out.println("拖拉机概率："+(numTuolaji*1.0/totaltimes));
		System.out.println("同花拖拉机概率："+(numTonghuaTuolaji*1.0/totaltimes));
		System.out.println("豹子概率："+(numBaozi*1.0/totaltimes));
//		for (Iterator iterator = extract.iterator(); iterator.hasNext();) {
//			PK p = (PK) iterator.next();
//			System.out.println(p.getNum()+","+p.getType());
//			
//		}
		
//		for (Iterator iterator = all.iterator(); iterator.hasNext();) {
//			PK p = (PK) iterator.next();
//			if(p.flag==1){
//				System.out.println("有问题");
//			}
//			
//		}
	}
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
