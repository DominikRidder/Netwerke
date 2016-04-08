import java.util.ArrayList;

public class Statistic {

	private int totalsize = 0;
	private int counter = 0;
	private int longest = -1;
	private int shortest = Integer.MAX_VALUE;
	
	public Statistic() {
		
	}
	
	public void record(String mess) {
		int wordlength = mess.length();
		
		counter++;
		totalsize += wordlength;
		longest = wordlength > longest ? wordlength : longest;
		shortest = wordlength < shortest ? wordlength : shortest;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Totalsize: "+totalsize);
		sb.append("\nCounter: "+counter);
		sb.append("\nLongest: "+longest);
		sb.append("\nShortest: "+shortest);
		
		return sb.toString();
	}
}
