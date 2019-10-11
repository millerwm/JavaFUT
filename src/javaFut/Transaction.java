package javaFut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction 
{
	private String playerName;
	private boolean isSell;
	private int amount;
	private Date datetime;
	
	public Transaction(String playerName, boolean isSell, int amount)
	{
		this.playerName = playerName;
		this.isSell = isSell;
		this.amount = amount;
		this.datetime = new Date();
	}
	
	public String getPlayerName()
	{
		return this.playerName;
	}
	
	public boolean getIsSell()
	{
		return this.isSell;
	}
	
	public int getAmount()
	{
		return this.amount;
	}
	
	public Date getDateTime()
	{
		return this.datetime;
	}
	
	public String getDateToString()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return dateFormat.format(this.datetime);
	}
}
