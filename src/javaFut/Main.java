package javaFut;

import java.io.IOException;
import java.util.Scanner;

public class Main 
{
	public static Scanner userInput;
	public static boolean botStatus;
	public static WebApp webapp;
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		//Initialize database with sleep between requests in seconds
		new Database(1);
		Player.loadPlayers();
		
		//Initialize scanner;
		userInput = new Scanner(System.in);
		
		//Initialize web app
		webapp = new WebApp("#####@gmail.com", "PASSWORD", "GOOGLE AUTH CODES GENERATOR CODE");
		webapp.login();
		webapp.initializeMenuItems();
		
		//Menu
		System.out.println("JavaFUT - The best java fut bot ever made");
		System.out.println("\n");
		int selection;
		while(true)
		{
			System.out.println("Bot Status > " + botStatus);
			//System.out.println("Coins > " + webapp.getCoins());
			System.out.println("\n");
			
			System.out.println("[1] Start snipping");
			System.out.println("[2] Add Item");
			
			selection = Integer.parseInt(userInput.nextLine());
			
			switch(selection)
			{
				case 1:
					startBot();
					break;
				
				case 2:
					addItem();
					break;
			}
		}
	}
	
	public static void startBot() throws InterruptedException
	{
		botStatus = true;
		webapp.openMenu("Transfers");
		webapp.openMenu("Transfer Market");
		
		while(botStatus)
		{
			webapp.searchPlayers();	
			Thread.sleep(Database.getSleep() * 1000);
		}
	}
	
	public static void addItem() throws IOException
	{
		System.out.print("Name -> ");
		String name = userInput.nextLine();
		
		System.out.print("Rating -> ");
		int rating = Integer.parseInt(userInput.nextLine());
		
		System.out.print("Buy Now -> ");
		int buyNow = Integer.parseInt(userInput.nextLine());
		
		System.out.print("Sell Now -> ");
		int sellNow = Integer.parseInt(userInput.nextLine());

		new Player(name, null, buyNow, sellNow, null, true, rating);
		
		Player.loadPlayers();
		
		clearConsole();
	}
	
	public static void clearConsole()
	{
		try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
}
