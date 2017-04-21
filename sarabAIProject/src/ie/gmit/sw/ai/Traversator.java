package ie.gmit.sw.ai;

import java.awt.*;

import ie.gmit.sw.ai.mazeAlgos.maze.Node;
public interface Traversator {
	public void traverse(Node[][] maze, Node start, Component viewer);

}
