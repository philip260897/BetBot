package com.betbot.wm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.betbot.main.Logger;
import com.betbot.main.Utils;

public class WMManager implements Runnable
{
	private static String MATCH_URL = "http://api.football-data.org/v1/competitions/467/fixtures";
	private List<MatchEvent> matchEvents = new ArrayList<MatchEvent>();
	
	private Match[] matches;
	
	public WMManager() {
		
	}
	
	public void addMatchEvent(MatchEvent event) {
		matchEvents.add(event);
	}
	
	public void init()
	{
		matches = downloadMatches();
		for(Match match : matches)
			System.out.println(match);
		
		Logger.Log("Next Match: "+getNextMatch().toString());
	}
	
	public Match[] downloadMatches()
	{
		Logger.LogForResult("Trying to download Match info");
		try 
		{
			String json_raw = Utils.getText(MATCH_URL);
			Logger.LogResult("OK");
			
			List<Match> matches = new ArrayList<Match>();
			
			Logger.LogForResult("Trying to parse JSON");
			JSONObject obj = new JSONObject(json_raw);
			JSONArray fixtures = obj.getJSONArray("fixtures");
			for(int i = 0; i < fixtures.length(); i++)
			{
				JSONObject matchObj = fixtures.getJSONObject(i);
				String date = matchObj.getString("date").replaceAll("T", " ").replaceAll("Z", "");
				MatchStatus status = MatchStatus.valueOf(matchObj.getString("status"));
				String teamA = matchObj.getString("homeTeamName");
				String teamB = matchObj.getString("awayTeamName");
				
				int scoreA = 0;
				int scoreB = 0;
				
				try {
				scoreA = matchObj.getJSONObject("result").getInt("goalsHomeTeam");
				scoreB = matchObj.getJSONObject("result").getInt("goalsAwayTeam");
				} catch(Exception ex) {}
				
				Date ddate = Utils.getDate(date, "yyyy-MM-dd HH:mm:ss");
				
				Match match = new Match(teamA, teamB, status, scoreA, scoreB, ddate);
				matches.add(match);
			}
			Logger.LogResult("OK");
			return matches.toArray(new Match[matches.size()]);
		}
		catch(Exception ex) {ex.printStackTrace();Logger.LogResult("FAILED");}
		return null;
	}
	
	private Match getNextMatch() 
	{
		if(matches != null) {
			for(int i = 0; i < matches.length; i++) {
				Match match = matches[i];
				if(match.getTime().compareTo(new Date()) > 0)
					return match;
			}
		}
		return null;
	}
	
	private Match getCurrentMatch()
	{
		if(matches != null) {
			for(int i = 0; i < matches.length; i++) {
				Match match = matches[i];
				if(match.getStatus() == MatchStatus.IN_PLAY)
					return match;
			}
		}
		return null;
	}
	
	private void eventPreMatch(Match match) {
		for(MatchEvent event : matchEvents)
			event.PreMatch(match);
	}
	
	private void eventMatchStarted(Match match) {
		for(MatchEvent event : matchEvents)
			event.MatchStarted(match);
	}
	
	private void eventMatchFinished(Match match) {
		for(MatchEvent event : matchEvents)
			event.MatchFinished(match);
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
