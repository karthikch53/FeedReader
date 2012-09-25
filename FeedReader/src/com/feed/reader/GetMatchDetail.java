package com.feed.reader;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class GetMatchDetail extends HttpServlet 
{
	
	final static PersistenceManager pm = PMF.get().getPersistenceManager();
	final static Logger logger = Logger.getLogger(GetMatchDetail.class.getName());
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		
		DataMap maps;
		WebDriver driver =null;
		String matchHeading = "";
		LinkedHashMap<String, String> matchDetailMap;
		String requestedMatchDetail = "";
		
		if (req.getHeader("X-AppEngine-Cron") == null) {
			return;
		}
		
		logger.log(Level.INFO, "Starting the scheduled task of getting individual match Detail @ " + Calendar.getInstance().getTime());
		
		
		Key key = KeyFactory.createKey(DataMap.class.getSimpleName(), "dataMap");
		try
		{
			maps = pm.getObjectById(DataMap.class,key);
		}
		catch(Exception e)
		{
			maps = null;
		}
		
		if(maps==null)
		{
			maps = new DataMap();
			maps.setKey(key);
		}
		
		try 
		{
			logger.log(Level.INFO, "Iterating through match list and get individual scores");
			matchDetailMap = new LinkedHashMap<String, String>();
			maps.setKey(key);
			driver = new HtmlUnitDriver() ;
			driver.get("http://www.espncricinfo.com/ci/engine/current/match/scores/liveframe.html?mode=realtime");
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> matchBoxList = driver.findElements(By.className("match_box"));
			for (WebElement match : matchBoxList) 
			{
				String tempMatchHeading = match.findElement(By.className("potMatchHeading")).findElement(By.xpath("./span/a")).getText();
				matchHeading = tempMatchHeading.substring(0, tempMatchHeading.indexOf(" at")).trim();
			    requestedMatchDetail = getElementText("mat_scores", match) + "|" + getElementText("mat_players", match) + "|" +getElementText("mat_status", match);
			    matchDetailMap.put(matchHeading, requestedMatchDetail);
			}
			logger.log(Level.INFO, "Completed iterating through the match list.");
			maps.setMatchDetail(matchDetailMap);
		}
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		Transaction txn = pm.currentTransaction();
		try 
		{
			txn.begin();
			pm.makePersistent(maps);
			txn.commit();
			logger.log(Level.INFO, "Committed score data.");
		} 
		finally 
		{
			if (txn.isActive()) 
			{
				txn.rollback();
			}
		}
	}
	
	private static String getElementText(String cssClassName, WebElement element) 
	{
		String text = null;
		try 
		{
			WebElement child = element.findElement(By.className(cssClassName));
			if (child != null) 
			{
				text = child.getText();
			}
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			text = "";
		}
		return text;
	}
}
