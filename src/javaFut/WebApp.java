package javaFut;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.warrenstrange.googleauth.*;


public class WebApp 
{
	private String url;
	private String email;
	private String password;
	private String oneTimePassword;
	private LinkedList<MenuItem> menuItems;
	private JavascriptExecutor jsDriver;
	private WebDriver driver;
	
	public WebApp(String email, String password, String oneTimePassword)
	{
		//User details
		this.url = "https://www.easports.com/fifa/ultimate-team/web-app/";
		this.email = email;
		this.password = password;
		this.oneTimePassword = oneTimePassword;
		
		//Web App Menu linked list initialize
		this.menuItems = new LinkedList<MenuItem>();
		
		System.setProperty("webdriver.gecko.driver", "/Users/vasco/JavaFUT/lib/geckodriver");
		this.driver = new FirefoxDriver();
		this.jsDriver = (JavascriptExecutor)driver;
	}
	
	public void login() throws InterruptedException
	{
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		
		//Click big login button
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn-standard call-to-action']"))).click();
		
		//Find email
		driver.findElement(By.id("email")).sendKeys(this.email);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		//Find password
		driver.findElement(By.id("password")).sendKeys(this.password);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		//Find login button and click
		driver.findElement(By.id("btnLogin")).click();
		
		//Check if it asks for code
		if(driver.findElement(By.id("btnSendCode")) != null)
		{
			//Select radio btn for Auth code
			driver.findElement(By.xpath("//input[@value='APP']")).click();
			
			GoogleAuthenticator gAuth = new GoogleAuthenticator();
			int code = gAuth.getTotpPassword(this.oneTimePassword);

			//Click in send code
			driver.findElement(By.id("btnSendCode")).click();
			driver.findElement(By.id("oneTimeCode")).sendKeys(String.valueOf(code));
			driver.findElement(By.id("btnSubmit")).click();
		}
		
		System.out.println("Logged in with success as " + this.getEmail());

	}
	
	public void initializeMenuItems()
	{
		this.menuItems.add(new MenuItem("Home", "button[class='ut-tab-bar-item icon-home']"));
		this.menuItems.add(new MenuItem("Transfers", "button[class='ut-tab-bar-item icon-transfer']"));
		this.menuItems.add(new MenuItem("Transfer Market", "div[class='tile col-1-1 ut-tile-transfer-market']", "//div[@class='ut-click-shield showing interaction']"));
		this.menuItems.add(new MenuItem("Transfer List", "div[class='tile has-separator col-1-2 ut-tile-transfer-list']"));
		this.menuItems.add(new MenuItem("Transfer Targets", "div[class='tile has-separator col-1-2 ut-tile-transfer-targets']"));
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public String getCoins()
	{
		
		WebDriverWait wait = new WebDriverWait(driver, 100);
		return wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div[class='view-navbar-currency-coins']")))).getText();
	}
	
	public WebDriver getDriver()
	{
		return this.driver;
	}
	
	public void openMenu(String name)
	{
		for(int i = 0; i < this.menuItems.size(); i++)
		{
			if(this.menuItems.get(i).getName().equals(name))
			{
				this.menuItems.get(i).open(this);
			}
		}
	}
	
	public void searchPlayers() throws InterruptedException
	{
		for(int i = 0; i < Database.getPlayers().size(); i++)
		{
			this.updateSearchBar(Database.getPlayers().get(i).getName());
			List<WebElement> playersResult = driver.findElements(By.xpath("//ul[@class='ut-button-group playerResultsList']//*"));
			for(int k = 0; k < playersResult.size(); k++)
			{
				//if name is equal (remove any kind of accents first)
				if(removeAccents(playersResult.get(i).findElement(By.cssSelector("span[class='btn-text'")).getText()).equals(Database.getPlayers().get(i).getName()))
				{
					//if rating is equal
					if(playersResult.get(i).findElement(By.cssSelector("span[class='btn-subtext'")).getText().equals(String.valueOf(Database.getPlayers().get(i).getRating())))
					{
						//Click in player button
						playersResult.get(i).findElement(By.cssSelector("span[class='btn-text'")).click();
						break;
					}
				}
			}
			
			//Update max buy now
			int buyNow = Database.getPlayers().get(i).getBuyNow();
			this.updateMaxBuyNow(String.format("%,d", buyNow).replace(".", ","));
			
			//Sleep before request
			Thread.sleep(1000);
			
			//Click search button
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn-standard call-to-action']"))).click();
			
			//Check if no results found first
			try
			{
				driver.findElement(By.cssSelector("div[class='ut-no-results-view'"));
				Thread.sleep(2000);
				//Back to Transfer Market clicking back arrow button
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='ut-navigation-button-control']"))).click();
			}
			catch(NoSuchElementException e)
			{
				//Iterate results list
				try
				{
					List<WebElement> playersSearchResult = driver.findElements(By.xpath("//div[@class='paginated-item-list ut-pinned-list']/descendant::ul/descendant::li"));
					for(int j = 0; j < playersSearchResult.size(); j++)
					{
						//Check if are still results
						try
						{
							//Select the item in the results list clicking on it
							playersSearchResult.get(j).click();						
						}
						catch(NoSuchElementException ee)
						{
							break;
						}
						
						//Click the buy now button
						wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn-standard buyButton currency-coins']"))).click();
					
						//Click the ok confirmation button
						WebElement confirmationBtn = driver.findElement(By.cssSelector("span[class='btn-text']"));
						wait.until(ExpectedConditions.elementToBeClickable(confirmationBtn)).click();
						
						//Check if we bought player or not first
						try
						{
							//Find list transfer market button
							WebElement listMarketBtn = driver.findElement(By.cssSelector("div[class='ut-quick-list-panel-view']"));
							listMarketBtn = listMarketBtn.findElement(By.cssSelector("div[class='ut-button-group']"));
							
							//Click list transfer market button
							listMarketBtn.findElement(By.cssSelector("button[class='accordian']")).click();
							Thread.sleep(1500);
							
							//Update Player Sell now Price
							this.updateMarketListPrice(String.format("%,d", Database.getPlayers().get(i).getSellNow()).replace(".", ","));
							wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn-standard call-to-action']"))).click();
							
							//Back to Transfer Market clicking back arrow button
							wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='ut-navigation-button-control']"))).click();
							break;
						}
						catch(NoSuchElementException ee)
						{
							System.out.println("We failed to buy " + Database.getPlayers().get(i).getName() + " probably someone bought it first");
							//Back to Transfer Market clicking back arrow button
							wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='ut-navigation-button-control']"))).click();
							break;
						}
					}
				}
				catch(NoSuchElementException ee)
				{
					wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='ut-navigation-button-control']"))).click();
					break;
				}
				
			}
		}
	}
	
	public void updateSearchBar(String name)
	{
		jsDriver.executeScript("document.getElementsByClassName(\"ut-text-input-control\")[0].value = '" + name + "'");
		jsDriver.executeScript("document.getElementsByClassName(\"ut-text-input-control\")[0].dispatchEvent(new Event('input'))");
	}
	
	public void updateMaxBuyNow(String buyNow)
	{
		String[] randomMinBin = new String[4];
		
		//Bid > 10000 steps 1000 coins
		if(Integer.parseInt(buyNow.replace(",", "")) >= 100000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 1000).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 2000).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 3000).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 4000).replace(".", ",")
			};
		}
		//Bid > 10000 steps 250 coins
		else if(Integer.parseInt(buyNow.replace(",", "")) >= 10000 && Integer.parseInt(buyNow.replace(",", "")) < 100000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 250).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 500).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 750).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 1000).replace(".", ",")
			};
		}
		//Bid > 1000 steps 100 coins
		else if(Integer.parseInt(buyNow.replace(",", "")) >= 1000 && Integer.parseInt(buyNow.replace(",", "")) < 10000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 100).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 200).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 300).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 400).replace(".", ",")
			};
		}
		//Bid > 150 steps 50 coins
		else if(Integer.parseInt(buyNow.replace(",", "")) >= 150 && Integer.parseInt(buyNow.replace(",", "")) < 1000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 50).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 100).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 150).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 200).replace(".", ",")
			};
		}
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[1].value = '" + randomMinBin[new Random().nextInt(4)] + "'");
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[1].dispatchEvent(new Event('change'))");
		
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[3].value = '" + buyNow + "'");
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[3].dispatchEvent(new Event('change'))");
	}

	public void updateMarketListPrice(String buyNow)
	{
		String[] randomMinBin = new String[4];
		
		//Bid > 10000 steps 1000 coins
		if(Integer.parseInt(buyNow.replace(",", "")) >= 100000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 1000).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 2000).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 3000).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 4000).replace(".", ",")
			};
		}
		//Bid > 10000 steps 250 coins
		else if(Integer.parseInt(buyNow.replace(",", "")) >= 10000 && Integer.parseInt(buyNow.replace(",", "")) < 100000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 250).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 500).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 750).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 1000).replace(".", ",")
			};
		}
		//Bid > 1000 steps 100 coins
		else if(Integer.parseInt(buyNow.replace(",", "")) >= 1000 && Integer.parseInt(buyNow.replace(",", "")) < 10000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 100).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 200).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 300).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 400).replace(".", ",")
			};
		}
		//Bid > 150 steps 50 coins
		else if(Integer.parseInt(buyNow.replace(",", "")) >= 150 && Integer.parseInt(buyNow.replace(",", "")) < 1000)
		{
			randomMinBin = new String[] {
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 50).replace(".", ","), 
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 100).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 150).replace(".", ","),
				String.format("%,d", Integer.parseInt(buyNow.replace(",", ""))  - 200).replace(".", ",")
			};
		}
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[0].value = '" + randomMinBin[new Random().nextInt(4)] + "'");
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[0].dispatchEvent(new Event('change'))");
		
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[1].value = '" + buyNow + "'");
		jsDriver.executeScript("document.getElementsByClassName(\"numericInput\")[1].dispatchEvent(new Event('change'))");
	}
	
	public static String removeAccents(String text) 
	{
	    return text == null ? null : Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
