package utils;

/**
* This class file will be used to store all of the constant values that are 
* responsible for proper game implementation. 
*
* This class will be also used to store any global 
* variables and methods that can be accessed from several Java classes. 
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
	
	//string array of player 1 deck
	public static String[] deck1Cards = {
			StaticConfFiles.c_azure_herald,
			StaticConfFiles.c_azurite_lion,
			StaticConfFiles.c_comodo_charger,
			StaticConfFiles.c_fire_spitter,
			StaticConfFiles.c_hailstone_golem,
			StaticConfFiles.c_ironcliff_guardian,
			StaticConfFiles.c_pureblade_enforcer,
			StaticConfFiles.c_silverguard_knight,
			StaticConfFiles.c_sundrop_elixir,
			StaticConfFiles.c_truestrike
		};
		
	public static String[] deck2Cards = {
			StaticConfFiles.c_blaze_hound,
			StaticConfFiles.c_bloodshard_golem,
			StaticConfFiles.c_entropic_decay,
			StaticConfFiles.c_hailstone_golem,
			StaticConfFiles.c_planar_scout,
			StaticConfFiles.c_pyromancer,
			StaticConfFiles.c_serpenti,
			StaticConfFiles.c_rock_pulveriser,
			StaticConfFiles.c_staff_of_ykir,
			StaticConfFiles.c_windshrike,
	};
	
	
	
	
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
