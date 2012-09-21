package com.feed.reader;

import java.io.IOException;
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class MatchList extends HttpServlet {

	private static final long serialVersionUID = 693496189900319407L;
	final static PersistenceManager pm = PMF.get().getPersistenceManager();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		DataMap maps;
		String matchList = "";
		Key key = KeyFactory.createKey(DataMap.class.getSimpleName(), "dataMap");
		maps = pm.getObjectById(DataMap.class,key);
		resp.setContentType("text/plain");
		if(null!=maps  && null!= maps.getMatchDetail())
		{
			Iterator<String> it = maps.getMatchDetail().keySet().iterator();
			while(it.hasNext())
			{
				matchList += it.next() + ",";
			}
			if(!matchList.equalsIgnoreCase(""))
			{
				matchList = matchList.substring(0,matchList.length()-1);
			}
			resp.getWriter().print(matchList);
		}
		else
		{
			resp.getWriter().print("");
		}
		resp.getWriter().flush();
	}
}
