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

public class GetAllMatches extends HttpServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4870495445416386425L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		try 
		{
			WebDriver driver = new HtmlUnitDriver() ;
			String matchHeading = "";
			driver.get("http://www.espncricinfo.com/ci/engine/current/match/scores/liveframe.html?mode=realtime");
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			List<WebElement> matchBoxList = driver.findElements(By.className("match_box"));
			for (WebElement match : matchBoxList) 
			{
				String tempMatchHeading = match.findElement(By.className("potMatchHeading")).findElement(By.xpath("./span/a")).getText();
				matchHeading += tempMatchHeading.substring(0, tempMatchHeading.indexOf(" at")).trim() + ",";
			}
			if(!matchHeading.equalsIgnoreCase(""))
			{
				matchHeading = matchHeading.substring(0,matchHeading.length()-1);
			}
			resp.setContentType("text/plain");
			resp.getWriter().print(matchHeading);
			resp.getWriter().flush();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
	}
}
