package kdtree;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;



public class GenData1 {

	public static void main(String[] args) throws IOException {
		int d = 2;//维度
		int num = 100000;//数据行数
		String rFileName = "d:/tmp/R_"+d+"_"+num+".txt";
		String sFileName = "d:/tmp/S_"+d+"_"+num+".txt";
		
		FileOutputStream rfos = new FileOutputStream(rFileName,true);
		BufferedOutputStream rbos = new BufferedOutputStream(rfos);
		
		FileOutputStream sfos = new FileOutputStream(sFileName,true);
		BufferedOutputStream sbos = new BufferedOutputStream(sfos);
		for(int i=1;i<=num;i++){
			StringBuffer r = new StringBuffer();
			r.append(i).append(";");
			
			for(int j=0;j<d;j++){
				Random ran = new Random();
				r.append(ran.nextFloat()).append(",");
			}
			r.deleteCharAt(r.length()-1);
			r.append("\r");
//			FileUtils.writeStringToFile(new File(rFileName), r.toString(), true);
			rbos.write(r.toString().getBytes());

			
			StringBuffer s = new StringBuffer();
			s.append(i).append(";");
			Random classify = new Random();
			s.append("c").append(classify.nextInt(10)).append(";");
			for(int j=0;j<d;j++){
				Random ran = new Random();
				s.append(ran.nextFloat()).append(",");
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
