package linuxuser0;
import robocode.*;
import java.awt.Color;
import java.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Genetic - a robot by linuxuser0
 * TO-DO: Awesomeness (and make win lazy eight CHECK, and inject winners TODO), and increase population size (make dynamic, too!) TODO
 * 
 */
public class Genetic extends Robot
{
	public enum Actions {
		FORWARD, BACKWARD, LEFT, RIGHT;
	}
	
	static Random rand = new Random();

	final static int INITIAL_POPULATION = 5; //all time population?
	final static int ACTIONS_LENGTH = 8; //actions a robot performs in a loop - these can be redefined round to round ;)
	static double actcount = 0; //should reset with each round	
	static int counter; // counts rounds

	static ArrayList<Actions[]> topten;	
	static ArrayList<Actions[]> actions;
	static ArrayList<Double> fitnesslevels; //number of actions accomplished
	
	public void run() {
		
		setColors(Color.green, Color.green, Color.green); // Genetic colors. Ha.
		setup(); // GET THIS TO WORK! - if index overflows, redo population

		while(true) {
			doActions();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		fire(1);
	}

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}
	
	public void onHitWall(HitWallEvent e) {
		back(20);
	}	
	
	public void onDeath(DeathEvent e) { // record data, just another day in the lab
		finishup(false);
	}
	
	public void onWin(WinEvent e){
		finishup(true);
	}
	
	public void finishup(boolean win){
		updateTopTen();
		counter++;
		if(win){
			fitnesslevels.add(Double.POSITIVE_INFINITY);
		}
		else{
			 fitnesslevels.add(actcount);
		}
		actcount = 0;
		System.out.println(counter);
		System.out.println(INITIAL_POPULATION);
		System.out.println(counter == INITIAL_POPULATION);
		//if the counter is out of bounds (all tests have been run), restart and breed
		if(counter == INITIAL_POPULATION){ //at lastindex + 1
			counter = 0;
			System.out.println("WHOA THERE");
			System.out.println(counter);
			System.out.println(fitnesslevels.indexOf(Collections.max(fitnesslevels)));
			Actions[] a = actions.get(fitnesslevels.indexOf(Collections.max(fitnesslevels))); //#1 - FIX BUGS
			fitnesslevels.remove(a);
			Actions[] b = actions.get(fitnesslevels.indexOf(Collections.max(fitnesslevels))); //#2 - FIX BUGS
			
			for(int c = 0; c < INITIAL_POPULATION; c++){ //holds population, only breeds 2
				actions.set(c, breed(a, b));
			}
			System.out.println("CHANGIN THE SYSTEM");
			fitnesslevels.clear(); // IMPORTANT;
		}
		
		System.out.println("FITNESS:");
		//DEBUG:
		for( Double fitness : fitnesslevels ){
			System.out.println(fitness);
		}
		System.out.println("END FITNESS");
		
	}
	
	public Actions[] breed(Actions[] a, Actions[] b) {
		
		Actions[] child = new Actions[ACTIONS_LENGTH];

		for(int c = 0; c < ACTIONS_LENGTH; c++){
			child[c] = rand.nextBoolean() ? a[c]: b[c];
		}
		
		return child;
	}
	
	public void makeAction(Actions[] acts){
		actions.add(acts);
	}
	
	public void setup() {
		if(getRoundNum() == 0){ // First Round -> set up initial population
			topten = new ArrayList<Actions[]>(INITIAL_POPULATION);
			actions = new ArrayList<Actions[]>(INITIAL_POPULATION);	
			fitnesslevels = new ArrayList<Double>(INITIAL_POPULATION);
			counter = 0; // keeps track of indexes
		
			makeAction(new Actions[]{Actions.FORWARD, Actions.BACKWARD, Actions.LEFT, Actions.RIGHT, Actions.BACKWARD, Actions.FORWARD, Actions.RIGHT, Actions.LEFT});
			makeAction(new Actions[]{Actions.FORWARD, Actions.BACKWARD, Actions.LEFT, Actions.RIGHT, Actions.BACKWARD, Actions.RIGHT, Actions.FORWARD, Actions.LEFT});
			makeAction(new Actions[]{Actions.FORWARD, Actions.RIGHT, Actions.BACKWARD, Actions.RIGHT, Actions.BACKWARD, Actions.FORWARD, Actions.RIGHT, Actions.LEFT});
			makeAction(new Actions[]{Actions.LEFT, Actions.BACKWARD, Actions.FORWARD, Actions.RIGHT, Actions.BACKWARD, Actions.FORWARD, Actions.RIGHT, Actions.LEFT});
			makeAction(new Actions[]{Actions.FORWARD, Actions.BACKWARD, Actions.BACKWARD, Actions.BACKWARD, Actions.RIGHT, Actions.FORWARD, Actions.RIGHT, Actions.LEFT});
		}
	}

	public void doActions() {
		System.out.println(counter);
		Actions[] todo = actions.get(counter);
		
		for ( Actions act : todo ) {// fix this!
			switch(act) {
				case FORWARD:
					ahead(100);
					break;
				case BACKWARD:
					back(100);
					break;
				case LEFT:
					turnLeft(90);
					break;
				case RIGHT:
					turnRight(90);
					break;
			}
			actcount++;
		}
	}
	
	public void updateTopTen(){ //throw these in from time to time...
		//First, get indexes of top ten.
		//Then return a list of those indexes mapped to the Actions AL
		ArrayList<Double> toptenscores = new ArrayList<Double>(fitnesslevels);
		Collections.sort(toptenscores);
		
		if(toptenscores.size() > 10){
			toptenscores.subList(0, toptenscores.size()-10).clear(); //clear all but top ten ;)
		}
		
		topten.clear();
		for( Double score : toptenscores ){
			topten.add( actions.get( fitnesslevels.indexOf(score) ) );
		}
		System.out.println(topten.toString());
	}
}
								 				
					