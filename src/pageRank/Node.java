package pageRank;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class Node{
	private float pr;
	private String[] linkNodes;
	
	public float getPr() {
		return pr;
	}
	public void setPr(float pr) {
		this.pr = pr;
	}
	
	public String[] getLinkNodes() {
		return linkNodes;
	}
	public void setLinkNodes(String[] linkNodes) {
		this.linkNodes = linkNodes;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(pr);
		if(linkNodes!=null){
			sb.append('\t')
			  .append(StringUtils.join(linkNodes, "\t"));
		}
		return sb.toString();
	}
	
	public static Node formatFromString(String val){
		String parts[] = val.split("\t");
		Node n = new Node();
		n.setPr(Float.parseFloat(parts[0]));
		if(parts.length>1){
			n.setLinkNodes(Arrays.copyOfRange(parts, 1,parts.length));
		}
		
		return n;
	}
	
}
