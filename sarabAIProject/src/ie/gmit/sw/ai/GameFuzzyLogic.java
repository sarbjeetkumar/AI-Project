package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class GameFuzzyLogic {
		
	//Fuzzy logic class
	public double fight(int weapon, int enemy, int player)
	{
		String fileName = "fcl/strength.fcl";//Declare fileName
		FIS fis = FIS.load(fileName, true);//Load file
		
		FunctionBlock functionBlock = fis.getFunctionBlock("strength");
		
		fis.setVariable("weapon", weapon);//set variable for weapon
		fis.setVariable("enemyStrength", enemy);//set variable for enemy
		fis.setVariable("playerStrength", player);//set variable for player
		fis.evaluate();
		
		Variable victory = functionBlock.getVariable("victory");
        System.out.println("Fuzzy Value: " + victory.getValue());
		
		 return victory.getValue();
	}
	
}
