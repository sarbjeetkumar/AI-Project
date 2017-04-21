package ie.gmit.sw.ai;

import java.awt.Component;
import java.util.*;

import ie.gmit.sw.ai.mazeAlgos.maze.Node;
public class BestFirstTraversator implements Traversator{
	private Node goal;
	
	public BestFirstTraversator(Node goal){
		this.goal = goal;
	}
	
	public void traverse(Node[][] maze, Node node, Component viewer) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);
		
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	
		while(!queue.isEmpty()){
			node = queue.poll();
			node.setVisited(true);	
			visitCount++;
			viewer.repaint();
		/*	
			if (node.isGoalNode() || node.getData() =='G'){
				//System.out.println("goal node found...");
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
		        viewer.repaint();
				break;
			}*/
			
			Node[] children = node.children(maze);
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]);
				}
			}
			
			//Sort the whole queue. Effectively a priority queue, first in, best out
			Collections.sort(queue,(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));		
		}
	}

	public void traverse(char[][] node, char c, GameView view) {
		// TODO Auto-generated method stub
		
	}
}
