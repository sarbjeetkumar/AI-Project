package ie.gmit.sw.ai;

import java.awt.Color;

import ie.gmit.sw.ai.mazeAlgos.maze.Node;

public class TraversatorStats {
	public static void printStats(Node node, long time, int visitCount){
		double depth = 0;
		
		while (node != null){			
			node = node.getParent();
			if (node != null) node.setColor(Color.YELLOW);
			depth++;
			if(node!=null){
				if(depth%3==0){
					//node.setData('Q');
				}
			}
		}
		
        System.out.println("Visited " + visitCount + " nodes in " + time + "ms.");
        System.out.println("Found goal at a depth of " + String.format("%.0f", depth));
        System.out.println("EBF = B* = k^(1/d) = " + String.format("%.2f", Math.pow((double) visitCount, (1.00d / depth))));    
	}
}
