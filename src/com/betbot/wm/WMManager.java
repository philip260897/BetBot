package com.betbot.wm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.betbot.main.Logger;
import com.betbot.main.Utils;

public class WMManager
{
	private static String MATCH_URL = "http://api.football-data.org/v1/competitions/467/fixtures";
	private List<MatchEvent> matchEvents = new ArrayList<MatchEvent>();
	
	private Match[] matches;
	
	private Match nextMatch;
	private Match currentMatch;
	
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
		
		update();
	}
	
	private void update()
	{
		Match current = getCurrentMatch();
		if(currentMatch == null && current != null) {
			setReminderTimer(current);
			setMatchStartTimer(current);
			setMatchUpdateTimer(current);
		}
		else
		{
			Match next = getNextMatch();
			if(!next.equals(nextMatch)) {
				setReminderTimer(next);
				setMatchStartTimer(next);
				setMatchUpdateTimer(next);
			}
			nextMatch = next;
		}
	}
	
	private Match[] downloadMatches()
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
				MatchStatus status = null;
				//if(i == 1)
					//status = MatchStatus.IN_PLAY;
				///else
					status = MatchStatus.valueOf(matchObj.getString("status"));
				String teamA = matchObj.getString("homeTeamName");
				String teamB = matchObj.getString("awayTeamName");
				
				int scoreA = 0;
				int scoreB = 0;
				
				try {
				scoreA = matchObj.getJSONObject("result").getInt("goalsHomeTeam");
				scoreB = matchObj.getJSONObject("result").getInt("goalsAwayTeam");
				} catch(Exception ex) {}
				
				Date ddate = Utils.getDate(date, "yyyy-MM-dd HH:mm:ss");
				ddate = Utils.subtractHour(ddate, -2);
				//ddate.setHours(11);
				//ddate.setMinutes(25);
				
				Match match = new Match(teamA, teamB, status, scoreA, scoreB, ddate, i);
				matches.add(match);
			}
			Logger.LogResult("OK");
			return matches.toArray(new Match[matches.size()]);
		}
		catch(Exception ex) {ex.printStackTrace();Logger.LogResult("FAILED");}
		return null;
	}
	
	private Match getMatchUpdate(int index) {
		//Logger.LogForResult("Trying to download Match info");
		try 
		{
			String json_raw = Utils.getText(MATCH_URL);
			//Logger.LogResult("OK");

			//Logger.LogForResult("Trying to parse JSON");
			JSONObject obj = new JSONObject(json_raw);
			JSONArray fixtures = obj.getJSONArray("fixtures");

			JSONObject matchObj = fixtures.getJSONObject(index);
			String date = matchObj.getString("date").replaceAll("T", " ").replaceAll("Z", "");
			MatchStatus status = MatchStatus.valueOf(matchObj.getString("status"));
			//MatchStatus status = MatchStatus.FINISHED;
			String teamA = matchObj.getString("homeTeamName");
			String teamB = matchObj.getString("awayTeamName");

			int scoreA = 0;
			int scoreB = 0;

			try {
				scoreA = matchObj.getJSONObject("result").getInt("goalsHomeTeam");
				scoreB = matchObj.getJSONObject("result").getInt("goalsAwayTeam");
			} catch(Exception ex) {}

			Date ddate = Utils.getDate(date, "yyyy-MM-dd HH:mm:ss");
			ddate = Utils.subtractHour(ddate, -2);

			Match match = new Match(teamA, teamB, status, scoreA, scoreB, ddate, index);


			//Logger.LogResult("OK");
			return match;
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

	private void setReminderTimer(Match match) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(match != null) {
					Logger.Log("Prematch Triggered! "+match.toString());
					eventPreMatch(match);
				}else {Logger.Log("ReminderTimer failed! No next match!");}
			}
			
		}, Utils.subtractHour(match.getTime(), 1));
		Logger.Log("ReminderTimer set for Match "+match.toString());
	}
	
	private void setMatchStartTimer(Match match) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				//currentMatch = nextMatch;
				if(match != null) {
					currentMatch = match;
					eventMatchStarted(match);
					Logger.Log("StartTimer Triggered!");
				}else {Logger.Log("StartTimer failed! No next match!");}
			}
			
		}, match.getTime());
		Logger.Log("StartTimer set for Match "+match.toString());
	}
	
	private void setMatchUpdateTimer(Match match) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(match != null) {
					Logger.Log("Updating match stats");
					Match m = getMatchUpdate(match.getIndex());
					if(m != null) {
					
						currentMatch = getMatchUpdate(match.getIndex());//detect score changes?
						
						if(currentMatch.getStatus() == MatchStatus.FINISHED) {
							eventMatchFinished(currentMatch);
							currentMatch = null;
							nextMatch = null;
							update();
							timer.cancel();
						}
					}
				}else {Logger.Log("UpdateTimer failed! No next match!");}
			}
			
		}, match.getTime(), 1000 * 3 * 60);
		Logger.Log("UpdateTimer set for Match "+match.toString());
	}

	public Match[] getMatches() {
		return matches;
	}
	
	public Match getCurrenMatch() {
		return currentMatch == null ? nextMatch : currentMatch;
	}
	
	//public Match getNextMatch() {
	//	return nextMatch;
	//}
}
