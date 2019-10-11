package javaFut;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Player
{
	private String name;
	private String quality;
	private int buyNow;
	private int sellNow;
	private String time;
	private int rating;
	
	public Player(String name, String quality, int buyNow, int sellNow, String time, boolean toSave, int rating) throws IOException
	{
		this.name = name;
		this.quality = quality;
		this.buyNow = buyNow;
		this.sellNow = sellNow;
		this.time = time;
		this.rating = rating;
		
		File toSnipeTxt = new File(Database.getSnipeTxt());
		if(!toSnipeTxt.exists()) toSnipeTxt.createNewFile();
		Scanner sc = new Scanner(toSnipeTxt);
		BufferedWriter bw = new BufferedWriter( new FileWriter(Database.getSnipeTxt(), true));
		
		bw.write(this.getName() + "," + this.getQuality() + "," + this.getBuyNow() + "," + this.getSellNow() + "," + this.getTime() + "," + this.rating);
	
		
		sc.close();
		bw.close();
	}
	
	public Player(String name, String quality, int buyNow, int sellNow, String time, int rating) throws IOException
	{
		this.name = name;
		this.quality = quality;
		this.buyNow = buyNow;
		this.sellNow = sellNow;
		this.time = time;
		this.rating = rating;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getQuality()
	{
		return this.quality;
	}
	
	public int getBuyNow()
	{
		return this.buyNow;
	}
	
	public int getSellNow()
	{
		return this.sellNow;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public int getRating()
	{
		return this.rating;
	}
	
	public static void loadPlayers() throws NumberFormatException, IOException
	{
		File toSnipeTxt = new File(Database.getSnipeTxt());
		Scanner sc = new Scanner(toSnipeTxt);
		
		while(sc.hasNextLine())
		{
			String[] itemAtributtes = sc.nextLine().split(",");
			Database.addPlayer(new Player(itemAtributtes[0], itemAtributtes[1], Integer.parseInt(itemAtributtes[2]), Integer.parseInt(itemAtributtes[3]), itemAtributtes[4], Integer.parseInt(itemAtributtes[5])));
		}
		
		sc.close();
	}
}

