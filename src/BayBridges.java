import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BayBridges {

	private static class Coordinate{
		double x;
		double y;
		
		public Coordinate(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "("+Double.toString(x)+", "+Double.toString(y) +")";
		}
	}
	
	private static class Bridge implements Comparable<Bridge>{
		int label;
		Coordinate endPt1;
		Coordinate endPt2;
		
		Bridge(int label, Coordinate pt1, Coordinate pt2){
			this.label = label;
			this.endPt1 = pt1;
			this.endPt2 = pt2;			
		}
		/**
		 * if slope AB < slope AC then ABC anti-clock.
		 * */
		private boolean antiClock(Coordinate A, Coordinate B, Coordinate C){
			//return (B.y - A.y)/(B.x - A.x) <  (C.y - A.y)/(C.x - A.x) ;
			return (C.y-A.y)*(B.x-A.x) > (B.y-A.y)*(C.x-A.x);
		}
		/**
		 * returns true if this bridge intersects with the other bridge.
		 * Two bridges(AB, CD) intersect if AB separates C, D and CD separates A, B. 
		 * AB separates C, D if ACB and ADB have different orientation(clockwise/anti-clock)
		 * Logic adapted from. http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/   
		 * */
		public boolean intersects(Bridge other){
			return antiClock(this.endPt1, other.endPt1, this.endPt2) != antiClock(this.endPt1, other.endPt2, this.endPt2) &&
					antiClock(other.endPt1, this.endPt1, other.endPt2) != antiClock(other.endPt1, this.endPt2, other.endPt2);
		}

		public int getLabel(){
			return label;
		}
		
		@Override
		public String toString() {
			return label+": (["+endPt1+"], ["+endPt2+"])";
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Bridge)) return false;
			Bridge other = (Bridge) obj;
			return this.label == other.label;
		}
		
		@Override
		public int compareTo(Bridge o) {
			return Integer.compare(this.label, o.label);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//read from input file passed as an argument.
		//create Bridges.
		Set<Bridge> bridges = new HashSet<>();
		if (args.length < 1){
			System.out.println("Please specify the input file.");
			System.exit(-1);
		}
		String file = args[0];
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;
		while ((line = in.readLine())!= null){
			if (line.length() < 0) continue;
			
			int label = Integer.parseInt(line.substring(0, line.indexOf(":"))); 
			//parse.
			line = line.substring(line.indexOf("["), line.lastIndexOf("]")+1);
			line = line.replace("[", "").replace("]", "");
			String toks[] = line.split(",");
			if (toks.length < 4){
				System.out.println("ERROR: Malformed input file. "+ file);
				continue;
			}			
			bridges.add( new Bridge(label, new Coordinate(Double.parseDouble(toks[0]), Double.parseDouble(toks[1]) ),
								    new Coordinate(Double.parseDouble(toks[2]), Double.parseDouble(toks[3]) ) ));
			
			}
		in.close();
		
		//find safe bridges. greedy approach.
		//A bridge which intersects with none is always safe. 
		//At each iteration remove the bridge with maximum number intersections. 
		Set<Bridge> safe = new HashSet<>();
		while(bridges.size() > 0){
			int maxIntrCount = 0;
			Bridge maxIntrBr = null;
			
			for (Bridge br:bridges){
				int intrCount = 0;
				//count intersections.
				for (Bridge oBr:bridges){
					if (! br.equals(oBr) && br.intersects(oBr))intrCount++;
				}
				if (intrCount == 0) safe.add(br);
				else if (intrCount > maxIntrCount){
					maxIntrCount = intrCount;
					maxIntrBr = br;
				}
			}
			//prepare bridges for next iteration			
			bridges.remove(maxIntrBr);
			bridges.removeAll(safe);
		}
		
		//output.
		List<Integer> results = new ArrayList<Integer>();
		for (Bridge br:safe){
			results.add(br.getLabel());
		}
		Collections.sort(results);
		
		for (Integer i:results){
			System.out.println(i);
		}	
		
		System.exit(0);
	}
}
