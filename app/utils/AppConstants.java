package utils;

/**
* This class file will be used to store all of the constant values that are 
* responsible for proper game implementation. 
*
* This class will be also used to store any global 
* variables that can be accessed from several Java classes. 
*
**/
public class AppConstants {

	public static boolean isDebugMode=true; // variable to view system logs, set this to false to hide logs from the terminal
	
	public static  long gameTaskInterval= 5000; // 5 seconds
	public static long allowedHeartbeatTimeGap = 10000; //10 seconds
	
	// Board constants
	public static int boardWidth=9;
	public static int boardHeight=5;
	
	
	// Card constants
	public static int minCardsInHand=3;
	public static int maxCardsInHand=6;
	public static int maxCardsInDeck=20;
	
	
	// Player constants
	public static int playerMaxHealth=20;
	
	public static void printLog(String message) {
		if(isDebugMode)
			System.out.println(message);
	}
	
	public static void callSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
