package ie.gmit.sw.ai.nn;

import ie.gmit.sw.ai.nn.activator.*;

public class GameCharacter {
	//Make static to train once for everyone
	private static NeuralNetwork nn;
	/*
	private static double[][] data = { //Health, Sword, Gun, Enemies
			{ 2, 0, 0, 0 }, { 2, 0, 0, 1 }, { 2, 0, 1, 1 }, { 2, 0, 1, 2 }, { 2, 1, 0, 2 },
			{ 2, 1, 0, 1 }, { 1, 0, 0, 0 }, { 1, 0, 0, 1 }, { 1, 0, 1, 1 }, { 1, 0, 1, 2 }, 
			{ 1, 1, 0, 2 }, { 1, 1, 0, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 1 }, 
			{ 0, 0, 1, 2 }, { 0, 1, 0, 2 }, { 0, 1, 0, 1 } };
 */
	
	private static double[][] data = { //Health, Sword, Enemies
			{ 2, 0, 0 }, { 2, 0, 1 }, { 2, 0, 1 }, { 2, 0, 2 }, { 2, 1, 2 },
			{ 2, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 2 }, 
			{ 1, 1, 2 }, { 1, 1, 1 }, { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 1 }, 
			{ 0, 0, 2 }, { 0, 1, 2 }, { 0, 1, 1 } };
	
	private static double[][] expected = { //Panic, Attack, Hide, Run
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, 
			{ 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, 
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, 
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 0.0 }, 
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };
	/*
	private static double[][] data = { //Health, Sword, Gun, Enemies
			{ 2, 0, 0 }, { 2, 0, 1 }, { 2, 0, 1 }, { 2, 0, 2 }, { 2, 1, 2 },
			{ 2, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 0, 2 }, 
			{ 1, 1, 2 }, { 1, 1, 1 }, { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 1 }, 
			{ 0, 0, 2 }, { 0, 1, 2 }, { 0, 1, 1 } };

	private static double[][] expected = { //Panic, Attack, Hide, Run
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, 
			{ 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, 
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, 
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 0.0 }, 
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };
	*/
	static{
		 nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 3, 4);
		 Trainator trainer = new BackpropagationTrainer(nn);
		 trainer.train(data, expected, 0.1, 1000000);
	}
	
	
	public GameCharacter() throws Exception{
		/*
	 nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 4, 3, 4);
	 Trainator trainer = new BackpropagationTrainer(nn);
	 trainer.train(data, expected, 0.6, 10000);
		*/
	}
	
	
	public void action(double health, double sword, double enemies) throws Exception{
		//int testIndex = 11;
		double[] params = {health, sword, enemies};
		//output layer
		double[] result = nn.process(params);
		
		for(double val : result){
			System.out.println(val);
		}
		
		//give back largest value in output layer
		int action = Utils.getMaxIndex(result) + 1;
		
	
		switch(action){
		
		case 1:
			panic();
			break;
		case 2:
			attack();
			break;
		case 3:
			hide();
			break;
		case 4:
			runAway();
			break;
		
		}
		
	}
	
	private void panic(){
		System.out.println("Panic......");
	}
	
	private void attack(){
		System.out.println("Attack......");
	}
	
	private void hide(){
		System.out.println("Hide......");
	}
	
	private void runAway(){
		System.out.println("Run......");
	}
	
	public static void main(String[] args)throws Exception {
		//GameCharacter gc = new GameCharacter();
		//gc.action(3, 4, 6, 1);
		//gc.action(0, 0, 8);
		/*
		gc.action(
				Double.parseDouble(args[0]), 
				Double.parseDouble(args[1]),
				Double.parseDouble(args[2]),
				Double.parseDouble(args[3])
			);*/
	}

}
