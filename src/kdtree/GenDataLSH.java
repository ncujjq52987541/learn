package kdtree;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;



public class GenDataLSH {
	public static int gen0Or1(){
		if(Math.random()>=0.5)
			return 1;
		else return 0;
	}
	public static void main(String[] args) throws IOException {
		int d = 10;//维度
		int num = 1000000;//数据行数
		String rFileName = "d:/tmp/R_"+d+"_"+num+".txt";
		String sFileName = "d:/tmp/S_"+d+"_"+num+".txt";
		
		FileOutputStream rfos = new FileOutputStream(rFileName,true);
		BufferedOutputStream rbos = new BufferedOutputStream(rfos);
		
		FileOutputStream sfos = new FileOutputStream(sFileName,true);
		BufferedOutputStream sbos = new BufferedOutputStream(sfos);
		for(int i=1;i<=num;i++){
			StringBuffer r = new StringBuffer();
			r.append(gen0Or1()).append(";");
			
			for(int j=0;j<d;j++){
				r.append(gen0Or1()).append(",");
			}
			r.deleteCharAt(r.length()-1);
			r.append("\r");
//			FileUtils.writeStringToFile(new File(rFileName), r.toString(), true);
			rbos.write(r.toString().getBytes());

			
			StringBuffer s = new StringBuffer();
			s.append(gen0Or1()).append(";");
			for(int j=0;j<d;j++){
				s.append(gen0Or1()).append(",");
			}
			s.deleteCharAt(s.length()-1);
			s.append("\r");
//			FileUtils.writeStringToFile(new File(sFileName), s.toString(), true);
			sbos.write(s.toString().getBytes());
		}
		rbos.close();
		sbos.close();

	}

}
