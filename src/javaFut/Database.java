package javaFut;

import java.util.LinkedList;

public class Database 
{
	private static LinkedList<Player> players;
	private static int sleepBetweenRequests;
	private static String toSnipeTxt;
	
	public Database(int sleepSecs)
	{
		sleepBetweenRequests = sleepSecs;
		players = new LinkedList<Player>();
		toSnipeTxt =  System.getProperty("user.home") + "/JavaFUT.txt";
	}
	
	public static LinkedList<Player> getPlayers()
	{
		return players;
	}
	
	public static void addPlayer(Player item)
	{
		players.add(item);
	}
	
	public static Player getPlayer(String name)
	{		
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).getName().equals(name))
			{
				return players.get(i);
			}
		}
		
		return null;
	}

	public static int getSleep()
	{
		return sleepBetweenRequests;
	}
	
	public static String getSnipeTxt()
	{
		return toSnipeTxt;
	}
	
}
