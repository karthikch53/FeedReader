package com.feed.reader;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class MatchDetail extends HttpServlet {

	private static final long serialVersionUID = -5168947540909939106L;
	final static PersistenceManager pm = PMF.get().getPersistenceManager();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		DataMap maps;
		String requestedMatch = req.getParameter("matchName");
		Key key = KeyFactory.createKey(DataMap.class.getSimpleName(), "dataMap");
		maps = pm.getObjectById(DataMap.class,key);
		resp.setContentType("text/plain");
		if(null!=maps  && null!= maps.getMatchDetail())
		{
			resp.getWriter().print(maps.getMatchDetail().get(requestedMatch));
		}
		else
		{
			resp.getWriter().print("");
		}
		resp.getWriter().flush();
	}
}