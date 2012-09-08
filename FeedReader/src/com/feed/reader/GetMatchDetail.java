package com.feed.reader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@SuppressWarnings("serial")
public class GetMatchDetail extends HttpServlet 
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		try 
		{
			String requestedMatch = req.getParameter("matchName");
			String requestedMatchDetail = "";
			WebDriver driver = new HtmlUnitDriver() ;
			driver.get("http://www.espncricinfo.com/ci/engine/current/match/scores/liveframe.html?mode=realtime");
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			List<WebElement> matchBoxList = driver.findElements(By.className("match_box"));
			for (WebElement match : matchBoxList) 
			{
				String tempMatchHeading = match.findElement(By.className("potMatchHeading")).findElement(By.xpath("./span/a")).getText();
				String matchHeading = tempMatchHeading.substring(0, tempMatchHeading.indexOf(" at")).trim();
				if(matchHeading.equalsIgnoreCase(requestedMatch))
				{
					requestedMatchDetail = getElementText("mat_scores", match) + "|" + getElementText("mat_players", match) + "|" +getElementText("mat_status", match);
					break;
				}
			}
			resp.setContentType("text/plain");
			resp.getWriter().print(requestedMatchDetail);
			resp.getWriter().flush();
		}
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
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
