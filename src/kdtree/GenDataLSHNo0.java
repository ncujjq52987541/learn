package kdtree;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;



public class GenDataLSHNo0 {
	public static int gen0Or1(){
		if(Math.random()>=0.5)
			return 1;
		else return 0;
	}
	public static void main(String[] args) throws IOException {
		int d = 200;//维度
		int num = 5000;//数据行数
		String sFileName = "d:/tmp/S_"+d+"_"+num+".txt";
		
		FileOutputStream sfos = new FileOutputStream(sFileName,true);
		BufferedOutputStream sbos = new BufferedOutputStream(sfos);
		int oneNum = 0;
		for(int i=1;i<=num;i++){

			StringBuffer s = new StringBuffer();
			s.append(gen0Or1()).append(";");
			for(int j=0;j<d;j++){
				int genNum = gen0Or1();
				if(genNum==1) {
					oneNum++;
				}
				s.append(genNum).append(",");
			}
			if(oneNum>=1){
				s.deleteCharAt(s.length()-1);
				s.append("\r");
				sbos.write(s.toString().getBytes());
			}else{
				i--;
			}
			oneNum=0;
		}
		sbos.close();

	}

}
