package ie.gmit.sw.ai.mazeAlgos.maze;

public interface MazeGenerator {
	public enum GeneratorAlgorithm {BinaryTree, HuntAndKill, RandomDepthFirst, RecursiveBacktracker, RecursiveDivision, RandomizedPrim, RandomizedKruskal};
	
	public abstract void setGoalNode();
	public abstract Node getGoalNode();
	public abstract Node[][] getMaze();
	public abstract void generateMaze();
}