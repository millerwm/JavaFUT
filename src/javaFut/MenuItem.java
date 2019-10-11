package javaFut;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuItem 
{
	private String name;
	private String cssSelector;
	private String blockShield;
	
	public MenuItem(String name, String cssSelector)
	{
		this.name = name;
		this.cssSelector = cssSelector;
	}
	
	public MenuItem(String name, String cssSelector, String blockShield)
	{
		this.name = name;
		this.cssSelector = cssSelector;
		this.blockShield = blockShield;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getCssSelector()
	{
		return this.cssSelector;
	}
	
	public void open(WebApp webapp)
	{
		if(this.blockShield != null)
		{
			JavascriptExecutor jsDriver = (JavascriptExecutor) webapp.getDriver();
			try
			{				
				WebElement element = webapp.getDriver().findElement(By.xpath(this.blockShield));
				jsDriver.executeScript("arguments[0].style.visibility='hidden'", element);
				WebDriverWait wait = new WebDriverWait(webapp.getDriver(), 5);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(this.getCssSelector()))).click();
			}
			catch(NoSuchElementException e)
			{
				WebDriverWait wait = new WebDriverWait(webapp.getDriver(), 5);
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(this.getCssSelector()))).click();
			}
		}
		else
		{
			WebDriverWait wait = new WebDriverWait(webapp.getDriver(), 5);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(this.getCssSelector()))).click();
		}
	}
}
