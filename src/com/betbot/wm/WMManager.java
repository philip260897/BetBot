package com.betbot.wm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.betbot.main.Logger;
import com.betbot.main.Utils;

public class WMManager 
{
	private static String MATCH_URL = "http://api.football-data.org/v1/competitions/467/fixtures";
	
	private List<MatchEvent> matchEvents = new ArrayList<MatchEvent>();
	
	public WMManager() {
		
	}
	
	public void addMatchEvent(MatchEvent event) {
		matchEvents.add(event);
	}
	
	public void downloadMatches()
	{
		Logger.LogForResult("Trying to download Match info");
		try 
		{
			String json_raw = Utils.getText(MATCH_URL);
			Logger.LogResult("OK");
			
			JSONObject obj = new JSONObject(json_raw);
			JSONArray fixtures = obj.getJSONArray("fixtures");
			for(int i = 0; i < fixtures.length(); i++)
			{
				JSONObject matchObj = fixtures.getJSONObject(i);
				String date = matchObj.getString("date");
				MatchStatus status = MatchStatus.valueOf(matchObj.getString("status"));
				String teamA = matchObj.getString("homeTeamName");
				String teamB = matchObj.getString("awayTeamName");
				
				int scoreA = 0;
				int scoreB = 0;
				
				try {
				scoreA = matchObj.getJSONObject("result").getInt("goalsHomeTeam");
				scoreB = matchObj.getJSONObject("result").getInt("goalsAwayTeam");
				} catch(Exception ex) {}
				

				
				
				System.out.println(teamA+" "+teamB+" "+status+" "+date+" "+scoreA + " " + scoreB );
			}
		}
		catch(Exception ex) {ex.printStackTrace();Logger.LogResult("FAILED");}
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
}
