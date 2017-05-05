package testKdTree;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class KDTree {
    
    private Node kdtree;
    
    private class Node implements Cloneable {
        //分割的维度
        int partitionDimention;
        //分割的值
        double partitionValue;
        //如果为非叶子节点，该属性为空
        //否则为数据
        double[] value;
        //是否为叶子
        boolean isLeaf=false;
        //左树
        Node left;
        //右树
        Node right;
        //每个维度的最小值
        double[] min;
        //每个维度的最大值
        double[] max;
		@Override
		protected Object clone(){
			Node node = null;
			try {
				node = (Node)super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 
			if(left!=null)
			node.left=(Node)left.clone(); 
			if(right!=null)
			node.right=(Node)right.clone(); 
			return node;
		}
        
        
    }
    
    private static class UtilZ{
        /**
         * 计算给定维度的方差
         * @param data 数据
         * @param dimention 维度
         * @return 方差
         */
        static double variance(ArrayList<double[]> data,int dimention){
            double vsum = 0;
            double sum = 0;
            for(double[] d:data){
                sum+=d[dimention];
                vsum+=d[dimention]*d[dimention];
            }
            int n = data.size();
            return vsum/n-Math.pow(sum/n, 2);
        }
        /**
         * 取排序后的中间位置数值
         * @param data 数据
         * @param dimention 维度
         * @return
         */
        static double median(ArrayList<double[]> data,int dimention){
            double[] d =new double[data.size()];
            int i=0;
            for(double[] k:data){
                d[i++]=k[dimention];
            }
            return findPos(d, 0, d.length-1, d.length/2);
        }
        
        static double[][] maxmin(ArrayList<double[]> data,int dimentions){
            double[][] mm = new double[2][dimentions];
            //初始化 第一行为min，第二行为max
            for(int i=0;i<dimentions;i++){
                mm[0][i]=mm[1][i]=data.get(0)[i];
                for(int j=1;j<data.size();j++){
                    double[] d = data.get(j);
                    if(d[i]<mm[0][i]){
                        mm[0][i]=d[i];
                    }else if(d[i]>mm[1][i]){
                        mm[1][i]=d[i];
                    }
                }
            }
            return mm;
        }
        
        static double distance(double[] a,double[] b){
            double sum = 0;
            for(int i=0;i<a.length;i++){
                sum+=Math.pow(a[i]-b[i], 2);
            }
            return sum;
        }
        /*自己测试用*/
        static double distanceTest(String aStr,String bStr){
//        	String aStr ="1.9872,2.38746";
//        	String bStr = "1.4533,1.893";
        	String[] asplit = aStr.split(",");
        	String[] bsplit = bStr.split(",");
        	
        	double[] a = strToDouble(asplit);
        	double[] b = strToDouble(bsplit);
            double sum = 0;
            for(int i=0;i<a.length;i++){
                sum+=Math.pow(a[i]-b[i], 2);
            }
            return sum;
        }
        static double[] strToDouble(String[] a){
        	double[] d = new double[a.length];
        	for (int i = 0; i < a.length; i++) {
        		d[i] = Double.parseDouble(a[i]);
			}		
        	return d;
        }
        /**
         * 在max和min表示的超矩形中的点和点a的最小距离
         * @param a 点a
         * @param max 超矩形各个维度的最大值
         * @param min 超矩形各个维度的最小值
         * @return 超矩形中的点和点a的最小距离
         */
        static double mindistance(double[] a,double[] max,double[] min){
            double sum = 0;
            for(int i=0;i<a.length;i++){
                if(a[i]>max[i])
                    sum += Math.pow(a[i]-max[i], 2);
                else if (a[i]<min[i]) {
                    sum += Math.pow(min[i]-a[i], 2);
                }
            }
            
            return sum;
        }
        
        /**
         * 使用快速排序，查找排序后位置在point处的值
         * 比Array.sort()后去对应位置值，大约快30%
         * @param data 数据
         * @param low 参加排序的最低点
         * @param high 参加排序的最高点
         * @param point 位置
         * @return
         */
        private static double findPos(double[] data,int low,int high,int point){
            int lowt=low;
            int hight=high;
            double v = data[low];
            ArrayList<Integer> same = new ArrayList<Integer>((int)((high-low)*0.25));
            while(low<high){
                while(low<high&&data[high]>=v){
                    if(data[high]==v){
                        same.add(high);
                    }
                    high--;
                }
                data[low]=data[high];
                while(low<high&&data[low]<v)
                    low++;
                data[high]=data[low];
            }
            data[low]=v;
            int upper = low+same.size();
            if (low<=point&&upper>=point) {
                return v;
            }
            
            if(low>point){
                return findPos(data, lowt, low-1, point);
            }
            
            int i=low+1;
            for(int j:same){
                if(j<=low+same.size())
                    continue;
                while(data[i]==v)
                    i++;
                data[j]=data[i];
                data[i]=v;
                i++;
            }
            
            return findPos(data, low+same.size()+1, hight, point);
        }
    }
    
    private KDTree() {}
    /**
     * 构建树
     * @param input 输入
     * @return KDTree树
     */
    public static KDTree build(double[][] input){
        int n = input.length;
        int m = input[0].length;
        
        ArrayList<double[]> data =new ArrayList<double[]>(n);
        for(int i=0;i<n;i++){
            double[] d = new double[m];
            for(int j=0;j<m;j++)
                d[j]=input[i][j];
            data.add(d);
        }
        
        KDTree tree = new KDTree();
        tree.kdtree = tree.new Node();
        tree.buildDetail(tree.kdtree, data, m);
        
        return tree;
    }
    /**
     * 循环构建树
     * @param node 节点
     * @param data 数据
     * @param dimentions 数据的维度
     */
    private void buildDetail(Node node,ArrayList<double[]> data,int dimentions){
        if(data.size()==1){
            node.isLeaf=true;
            node.value=data.get(0);
            return;
        }
        
        //选择方差最大的维度
        node.partitionDimention=-1;
        double var = -1;
        double tmpvar;
        for(int i=0;i<dimentions;i++){
            tmpvar=UtilZ.variance(data, i);
            if (tmpvar>var){
                var = tmpvar;
                node.partitionDimention = i;
            }
        }
        //如果方差=0，表示所有数据都相同，判定为叶子节点
        if(var==0){
            node.isLeaf=true;
            node.value=data.get(0);
            return;
        }
        
        //选择分割的值
        node.partitionValue=UtilZ.median(data, node.partitionDimention);
        
        double[][] maxmin=UtilZ.maxmin(data, dimentions);
        node.min = maxmin[0];
        node.max = maxmin[1];
        
        int size = (int)(data.size()*0.55);
        ArrayList<double[]> left = new ArrayList<double[]>(size);
        ArrayList<double[]> right = new ArrayList<double[]>(size);
        
        for(double[] d:data){
            if (d[node.partitionDimention]<node.partitionValue) {
                left.add(d);
            }else if(d[node.partitionDimention]>node.partitionValue){
                right.add(d);
            }else{
            	node.value = d;
            }
        }
       
       
        if(left.size()!=0){
        	Node leftnode = new Node();
        	node.left=leftnode;
        	buildDetail(leftnode, left, dimentions);
        }
        if(right.size()!=0){
        	Node rightnode = new Node();
            node.right=rightnode;
        	buildDetail(rightnode, right, dimentions);
        }
    }
    /**
     * 打印树，测试时用
     */
    public static void print(Node tree){
        printRec(tree,0);
    }
    
    private static void printRec(Node node,int lv){
//    	if(node==null) return;
        if(!node.isLeaf){
            for(int i=0;i<lv;i++)
                System.out.print("--");
            System.out.println(node.partitionDimention+":"+node.partitionValue);
            printRec(node.left,lv+1);
            printRec(node.right,lv+1);
        }else {
            for(int i=0;i<lv;i++)
                System.out.print("--");
            StringBuilder s = new StringBuilder();
            s.append('(');
            for(int i=0;i<node.value.length-1;i++){
                s.append(node.value[i]).append(',');
            }
            s.append(node.value[node.value.length-1]).append(')');
            System.out.println(s);
        }
    }
    /**
     * 
     * @param stack
     * @param node
     * @param depth 向下查找的次数
     */
    private static void pushAll(Stack<Node> stack,Node node,int depth){
    	if(node==null) return;
    	if(depth>5) return;
    	depth = depth+1;
    	stack.push(node);
    	if(node.left!=null){
    		pushAll(stack,node.left,depth);
    	}
    	if(node.right!=null){
    		pushAll(stack,node.right,depth);
    	}
    	
    }
    public double[] query(double[] input){
    	Node node = kdtree;
    	Stack<Node> stack = new Stack<Node>();
    	stack.push(node);
//    	int j = 0;
    	while(node!=null&&!node.isLeaf){
//    		System.out.println("j="+j++);
            if(input[node.partitionDimention]<node.partitionValue){
//	            stack.push(node.left);
            	pushAll(stack,node.left,1);
	            node=node.left;
            }else{
            	pushAll(stack,node.right,1);
//	            stack.push(node.right);
	            node=node.right;
                
            }
        }
    	while((node = stack.pop())==null);
    	Node resultNode = node;
    	double distance = UtilZ.distance(input, node.value);
//    	int i=0;
    	while(!stack.isEmpty()){
	    	Node lastestNode = node;
//    		double[] lastestValue = node.value;
	    	node = stack.pop();
//	    	System.out.println(i++);
	    	if(input[node.partitionDimention]-distance<node.value[node.partitionDimention]){
//	    		if(node.left!=null&&doubleEqual(node.left.value, lastestValue)){
	    		if(node.left!=null&&node.left==lastestNode){
//	    			Node n = ((Node)node.clone());
	    			if(node.right!=null)
	    			stack.push(node.right);
	    		}else if(node.right!=null&&node.right==lastestNode){
//	    			Node n = ((Node)node.clone());
	    			if(node.left!=null)
	    			stack.add(node.left);
	    		}
	    	}
	    	double distanceNew = UtilZ.distance(input, node.value);
	    	if(distanceNew<distance){
	    		distance = distanceNew;
	    		resultNode = node;
	    	}
    	}
    	return resultNode.value;
    }
    

    
    /**
     * 线性查找，用于和kdtree查询做对照
     * 1.判断kdtree实现是否正确
     * 2.比较性能
     * @param input
     * @param data
     * @return
     */
    public static double[] nearest(double[] input,double[][] data){
        double[] nearest=null;
        double dis = Double.MAX_VALUE;
        double tdis;
        for(int i=0;i<data.length;i++){
            tdis = UtilZ.distance(input, data[i]);
//        	tdis = UtilZ.distanceTest(input[0]+","+input[1],data[i][0]+","+data[i][1]);
            if(tdis<dis){
                dis=tdis;
                nearest = data[i];
            }
        }
        return nearest;
    }
    
    /**
     * 运行100000次，看运行结果是否和线性查找相同
     */
    public static void correct(){
        int count = 100000;
        int wrong=0;
        while(count-->0){
            int num = 100;
            double[][] input = new double[num][2];
            for(int i=0;i<num;i++){
                input[i][0]=Math.random()*10;
                input[i][1]=Math.random()*10;
            }
            double[] query = new double[]{Math.random()*50,Math.random()*50};
            
            KDTree tree=KDTree.build(input);
            double[] result = tree.query(query);
            double[] result1 = nearest(query,input);
            if (result[0]!=result1[0]||result[1]!=result1[1]) {
//                System.out.println("wrong");
//                break;
                wrong++;
            }
        }
        System.out.println(wrong);
    }
    
    public static void correct1(int iteration,int datasize,int d){
        int count = iteration;
        
        int num = datasize;
        double[][] input = new double[num][d];
        for(int i=0;i<num;i++){
        	for(int j=0;j<d;j++){
        		input[i][j] = Math.random()*num;
        	}
        }
        
        KDTree tree=KDTree.build(input);
        
        double[][] query = new double[iteration][d];
        for(int i=0;i<iteration;i++){
        	for(int j=0;j<d;j++){
        		query[i][j]= Math.random()*num*1.5;
        	}
        }
        int wrong = 0;
        long start = System.currentTimeMillis();
        for(int i=0;i<iteration;i++){
            double[] result1 = tree.query(query[i]);
            double[] result2 = nearest(query[i],input);
            for(int j=0;j<result1.length;j++){
            	if(result1[j]!=result2[j]){
            		wrong++;
            		break;
            	}
            }
        }
       double wrate = wrong/(double)iteration;
       double rrate = 1-wrate;
        System.out.println("wrong:"+wrong+", 错误率:"+(wrate*100)+"%"+", 正确率"+(rrate*100)+"%");
    }
    
    public static void performance(int iteration,int datasize){
        int count = iteration;
        
        int num = datasize;
        double[][] input = new double[num][2];
        for(int i=0;i<num;i++){
            input[i][0]=Math.random()*num;
            input[i][1]=Math.random()*num;
        }
        
        KDTree tree=KDTree.build(input);
//        print(tree.kdtree);
        double[][] query = new double[iteration][2];
        for(int i=0;i<iteration;i++){
            query[i][0]= Math.random()*num*1.5;
            query[i][1]= Math.random()*num*1.5;
        }
        System.out.println("datasize:"+datasize+";iteration:"+iteration);
        long start = System.currentTimeMillis();
        for(int i=0;i<iteration;i++){
//        	long t1 = System.currentTimeMillis();
            double[] result = tree.query(query[i]);
//            long t2 = System.currentTimeMillis();
//            System.out.println(t2-t1);
        }
        long timekdtree = System.currentTimeMillis()-start;
       
        System.out.println("kdtree:"+timekdtree);
        start = System.currentTimeMillis();
        for(int i=0;i<iteration;i++){
//        	long time1 = System.currentTimeMillis();
            double[] result = nearest(query[i],input);
//            long time2 = System.currentTimeMillis();
//            System.out.println("aaaa:"+(time2-time1));
        }
        long timelinear = System.currentTimeMillis()-start;
        
        
        System.out.println("linear:"+timelinear);
        System.out.println("linear/kdtree:"+(timelinear*1.0/timekdtree));
    }
    
    public static void performance(double[][] r,double[][] s){
        
        KDTree tree=KDTree.build(s);
        long start = System.currentTimeMillis();
        for(int i=0;i<r.length;i++){
            double[] result = tree.query(r[i]);
        }
        long timekdtree = System.currentTimeMillis()-start;
       
        System.out.println("kdtree:"+timekdtree);
        start = System.currentTimeMillis();
        for(int i=0;i<r.length;i++){
//        	long time1 = System.currentTimeMillis();
            double[] result = nearest(r[i],s);
//            long time2 = System.currentTimeMillis();
//            System.out.println("aaaa:"+(time2-time1));
        }
        long timelinear = System.currentTimeMillis()-start;
        
        
        System.out.println("linear:"+timelinear);
        System.out.println("linear/kdtree:"+(timelinear*1.0/timekdtree));
    }
    
    public static void performanceWithDemision(int iteration,int datasize,int d){
        int count = iteration;
        
        int num = datasize;
        double[][] input = new double[num][d];
        for(int i=0;i<num;i++){
        	for(int j=0;j<d;j++){
        		input[i][j] = Math.random()*num;
        	}
        }
        
        KDTree tree=KDTree.build(input);
        
        double[][] query = new double[iteration][d];
        for(int i=0;i<iteration;i++){
        	for(int j=0;j<d;j++){
        		query[i][j]= Math.random()*num*1.5;
        	}
        }
        
        long start = System.currentTimeMillis();
        for(int i=0;i<iteration;i++){
            double[] result = tree.query(query[i]);
        }
        long timekdtree = System.currentTimeMillis()-start;
        
        start = System.currentTimeMillis();
        for(int i=0;i<iteration;i++){
            double[] result = nearest(query[i],input);
        }
        long timelinear = System.currentTimeMillis()-start;
        
        System.out.println("datasize:"+datasize+";iteration:"+iteration);
        System.out.println("kdtree:"+timekdtree);
        System.out.println("linear:"+timelinear);
        System.out.println("linear/kdtree:"+(timelinear*1.0/timekdtree));
    }
    
    public static void test1(){
    	double[][] input = new double[6][2];
    	input[0][0]=2;
    	input[0][1]=3;
    	
    	input[1][0]=5;
    	input[1][1]=4;
    	
    	input[2][0]=9;
    	input[2][1]=6;
    	
    	input[3][0]=4;
    	input[3][1]=7;
    	
    	input[4][0]=8;
    	input[4][1]=1;
    	
    	input[5][0]=7;
    	input[5][1]=2;
    	KDTree tree=KDTree.build(input);
    	
    	double[] q = new double[2];
    	q[0]=2;
    	q[1]=4.5;
    	
//    	double[] result = tree.query(q);
    	double[] result = tree.query(q);
    	
    	System.out.println(result[0]+","+result[1]);
    	System.out.println(tree);
    }
    public static double[][] readFromR(String rPath) throws IOException{
    	FileInputStream fis = new FileInputStream(rPath);
    	BufferedInputStream bis = new BufferedInputStream(fis);
//    	BufferedReader br = new BufferedReader(in)
    	DataInputStream dis = new DataInputStream(bis);
    	String[] dimAndNum = rPath.split("_");
    	
    	int dim = Integer.parseInt(dimAndNum[1]);
    	int nums = Integer.parseInt(dimAndNum[2].substring(0, dimAndNum[2].indexOf(".")));
    	
    	double[][] datas = new double[nums][dim];
    	
    	String str = null;
    	int i=0;
//    	while((str=dis.readUTF())!=null){
    	while(i<nums){
    		str=dis.readLine();
    		String[] dataStr = str.split(";");
    		datas[i]=strArrayToDoubleArray(dataStr[1].split(","));
    		i++;
    	}
    	dis.close();
    	bis.close();
    	fis.close();
    	return datas;
    }
    
    public static double[][] readFromS(String sPath) throws IOException{
    	FileInputStream fis = new FileInputStream(sPath);
    	BufferedInputStream bis = new BufferedInputStream(fis);
//    	BufferedReader br = new BufferedReader(in)
    	DataInputStream dis = new DataInputStream(bis);
    	String[] dimAndNum = sPath.split("_");
    	
    	int dim = Integer.parseInt(dimAndNum[1]);
    	int nums = Integer.parseInt(dimAndNum[2].substring(0, dimAndNum[2].indexOf(".")));
    	
    	double[][] datas = new double[nums][dim];
    	
    	String str = null;
    	int i=0;
//    	while((str=dis.readUTF())!=null){
    	while(i<nums){
    		str=dis.readLine();
    		String[] dataStr = str.split(";");
    		datas[i]=strArrayToDoubleArray(dataStr[2].split(","));
    		i++;
    	}
    	dis.close();
    	bis.close();
    	fis.close();
    	return datas;
    }
    private static double[] strArrayToDoubleArray(String[] data){
    	double[] d = new double[data.length];
    	for (int i = 0; i < d.length; i++) {
			d[i] = Double.parseDouble(data[i]);
		}
    	return d;
    }
    public static void main(String[] args){
//    	correct();
//    	try {
////			readFromS("d:\\tmp\\S_2_100000.txt");
////			readFromR("d:\\tmp\\R_2_100000.txt");
//			
////			performance(readFromR("d:\\tmp\\R_2_100000.txt"),readFromS("d:\\tmp\\S_2_100000.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	test1();
//    	performance(10000,10000);
    	performanceWithDemision(100000, 100000, 2);
    	correct1(100000, 100000, 2);
    }
}