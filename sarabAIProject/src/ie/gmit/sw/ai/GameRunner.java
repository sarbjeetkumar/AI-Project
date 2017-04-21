package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ie.gmit.sw.ai.mazeAlgos.maze.MazeGenerator;
import ie.gmit.sw.ai.mazeAlgos.maze.MazeGeneratorFactory;
import ie.gmit.sw.ai.mazeAlgos.maze.MazeView;
import ie.gmit.sw.ai.mazeAlgos.maze.*;
import ie.gmit.sw.ai.mazeAlgos.traversers.Traversator;
import ie.gmit.sw.ai.nn.GameCharacter;

public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 15;
	public static GameFuzzyLogic gfl = new GameFuzzyLogic();
	GameCharacter gc = new GameCharacter();
	private double fuzzyValue;
	private int health;
	private int score;
	private int injury, damage;
	private int enemies = 1;
	private boolean weapon;
	private int spider;
	private int weaponValue;
	private char[][] node;
	private GameView view;
	private Maze model;
	private Node goal;
	private int currentRow;
	private int currentCol;
	private static int bombNumber =0;
	
	public GameRunner() throws Exception{
		
		
		//MazeGeneratorFactory factory = MazeGeneratorFactory.getInstance();
		//MazeGenerator generator = factory.getMazeGenerator(MazeGenerator.GeneratorAlgorithm.BinaryTree, MAZE_DIMENSION, MAZE_DIMENSION);
		
		model = new Maze(MAZE_DIMENSION);
    	view = new GameView(model);
    	node = model.getMaze();
    	goal = model.getGoalNode();
    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	
    	placePlayer();
    	health = 100;
    	spider = 100;
    	score = 100;
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
        //goal.setGoalNode(true);
        
        //System.out.println("Goal Node Position: " + "Row: "+ goal.getRow() + " " + "Col: " + goal.getCol());
        
        BestFirstTraversator t = new BestFirstTraversator(goal);    
        t.traverse(node, node[currentRow][currentCol], view);
        
        
  
	}
	
	private void placePlayer(){   	
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, '5'); //A Spartan warrior is at index 5
    	updateView(); 		
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) 
        {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++; 
        	bombcheck(currentRow, currentCol);
        	swordcheck(currentRow, currentCol);
        	helpcheck(currentRow, currentCol);
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) 
        {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;
        	bombcheck(currentRow, currentCol);
        	swordcheck(currentRow, currentCol);
        	helpcheck(currentRow, currentCol);
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) 
        {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        	bombcheck(currentRow, currentCol);
        	swordcheck(currentRow, currentCol);
        	helpcheck(currentRow, currentCol);
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) 
        {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;
        	bombcheck(currentRow, currentCol);
        	swordcheck(currentRow, currentCol);
        	helpcheck(currentRow, currentCol);
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore

    
	private boolean isValidMove(int row, int col){
		if (row <= model.size() - 1 && col <= model.size() - 1 && (model.get(row, col) == ' ') || (model.get(row, col) == '\u0036') 
				 ||	(model.get(row, col) == '\u0037') || (model.get(row, col) == '\u0038') || (model.get(row, col) == '\u0039')
				 || (model.get(row, col) == '\u003A') || (model.get(row, col) == '\u003B') || (model.get(row, col) == '\u003C')
				 || (model.get(row, col) == '\u003D'))
			{
				if ((model.get(row, col) == '\u0036')//If current row/col is a spider
				 ||	(model.get(row, col) == '\u0037') || (model.get(row, col) == '\u0038') || (model.get(row, col) == '\u0039')
				 || (model.get(row, col) == '\u003A') || (model.get(row, col) == '\u003B') || (model.get(row, col) == '\u003C')
				 || (model.get(row, col) == '\u003D'))
			{
			
				if(weapon){weaponValue = 100; spider = spider - 50;}else{weaponValue = 0;};
				//System.out.println("Spider = " + spider);
				fuzzyValue = gfl.fight(weaponValue, spider, health);
				damage = (int)fuzzyValue;
				injury = score - damage;
				health = health - injury;
				try {
					gc.action(health, weaponValue, enemies);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(health <= 0){
					displayGUI();
					System.exit(0);
				}
				
				
				System.out.println("Health = " + health);
				System.out.println("Injury = " + injury);
				System.out.println("Damage = " + damage);
				//displayGUIFight();
				
			}	
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			return true;
		}	
		else{
			return false; //Can't move
		}
	}
	
	public boolean bombcheck(int row, int col){
		if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row + 1, col) == '\u0033'){
			model.set(row + 1, col, '0');
			bombNumber++;
			System.out.println("Number of Bombs: " + bombNumber );
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row - 1, col) == '\u0033'){
			model.set(row - 1, col, '0');
			bombNumber++;
			System.out.println("Number of Bombs: " + bombNumber );
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row , col + 1) == '\u0033'){
			model.set(row, col + 1, '0');
			bombNumber++;
			System.out.println("Number of Bombs: " + bombNumber );
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col - 1) == '\u0033'){
			model.set(row, col - 1, '0');
			bombNumber++;
			System.out.println("Number of Bombs: " + bombNumber );
			return false;
		}
		else{
			return false;
		}
	}
	
	public boolean helpcheck(int row, int col){
		if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row + 1, col) == '\u0032'){
			model.set(row + 1, col, '0');
			System.out.println("got help");
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row - 1, col) == '\u0032'){
			model.set(row - 1, col, '0');
			System.out.println("got help");
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row , col + 1) == '\u0032'){
			model.set(row, col + 1, '0');
			System.out.println("got help");
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col - 1) == '\u0032'){
			model.set(row, col - 1, '0');
			System.out.println("got help");
			return false;
		}
		else{
			return false;
		}
	}
	
	
	public boolean swordcheck(int row, int col){
		if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row + 1, col) == '\u0031'){
			model.set(row + 1, col, '0');
			weapon = true;
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row - 1, col) == '\u0031'){
			model.set(row - 1, col, '0');
			weapon = true;
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row , col + 1) == '\u0031'){
			model.set(row, col + 1, '0');
			weapon = true;
			return false;
		}
		else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col - 1) == '\u0031'){
			model.set(row, col - 1, '0');
			weapon = true;
			return false;
		}
		else{
			return false; 
		}
	}
	
	 public void displayGUI()
	 {
	    JOptionPane.showMessageDialog(null, getPanel(), "END : ", JOptionPane.INFORMATION_MESSAGE);
	 }
	 
	 public void displayGUIFight()
	 {
	    JOptionPane.showMessageDialog(null, getPanelFight(), "Fight : ", JOptionPane.INFORMATION_MESSAGE);
	 }
	 
	 public JPanel getPanelFight()
	 {
		JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
	    JLabel Jhealth = getLabel("Health: " + health);
	    JLabel Jinjury = getLabel("Injury: " + injury);
	    JLabel JfuzzyValue = getLabel("Fuzzy Value: " + fuzzyValue);
		
	    JLabel end = getLabel("Press ok to continue !!!");
	    
	    //panel.add(health);
	  
	   // panel.add(JfuzzyValue);
	    panel.add(Jhealth);
	    panel.add(Jinjury);
	    panel.add(end);

	    return panel;
	 }
	 
	 public JPanel getPanel()
	 {
		JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
	    JLabel end = getLabel("You Died Game over");
	   
	    panel.add(end);

	    return panel;
	 }

	 public JLabel getLabel(String title) {
	   return new JLabel(title);
	 }
	
	private Sprite[] getSprites() throws Exception{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		
		sprites[0] = new Sprite("Hedge", "resources/hedge.png");
		sprites[1] = new Sprite("Sword", "resources/sword.png");
		sprites[2] = new Sprite("Help", "resources/help.png");
		sprites[3] = new Sprite("Bomb", "resources/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", "resources/h_bomb.png");
		sprites[5] = new Sprite("Spartan Warrior", "resources/spartan_1.png", "resources/spartan_2.png");
		sprites[6] = new Sprite("Black Spider", "resources/black_spider_1.png", "resources/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", "resources/blue_spider_1.png", "resources/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", "resources/brown_spider_1.png", "resources/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", "resources/green_spider_1.png", "resources/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", "resources/grey_spider_1.png", "resources/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", "resources/orange_spider_1.png", "resources/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", "resources/red_spider_1.png", "resources/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", "resources/yellow_spider_1.png", "resources/yellow_spider_2.png");
		sprites[14] = new Sprite("Goal node", "resources/key.png");
		return sprites;
	}
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
	}
}