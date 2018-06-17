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
	private LiveTickerEvent tickerEvent;
	
	private Match[] matches;
	
	private Match[] nextMatch;
	private Match[] currentMatch;
	
	public WMManager() {
		
	}
	
	public void setLiveTickerEvent(LiveTickerEvent event) {
		this.tickerEvent = event;
	}
	
	public void addMatchEvent(MatchEvent event) {
		matchEvents.add(event);
	}
	
	public void init()
	{
		matches = downloadMatches();
		if(matches != null)
		{
			//DO NOT REMOVE================
			for(Match m : matches)
				m.toString();
			//DO NOT REMOVE================
			
			Logger.Log("Loaded Matches: "+matches.length+" Finished Matches: "+this.getFinishedMatchCount());
			Match[] current = this.getCurrentMatches();
			if(current != null)
			{
				Logger.Log("Currently playing Matches: "+current.length);
				for(Match match : current)
					Logger.Log("\t"+match);
			}
			
			Match[] nexts = updateNextMatches();
			Logger.Log("Upcoming matches: "+nexts.length);
			for(Match m : nexts) {
				Logger.Log("\t"+m);
			}
			
			update();
		}
		else {Logger.Log("Failed to initialize Matches! Plz try again!");}
	}
	
	private void update()
	{
		Match[] current = updateCurrentMatches();
		if(currentMatch == null && current != null) {
			setReminderTimer(current);
			setMatchStartTimer(current);
			setMatchUpdateTimer(current);
		}
		else
		{
			Match[] next = updateNextMatches();
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
		String json_raw = Utils.getText(MATCH_URL);
		if(json_raw == null) {
			Logger.LogResult("FAILED");
			Logger.LogForResult("Trying to load cached Matches");
			json_raw = Utils.readFile("API_Cache.json");
			if(json_raw == null) {
				Logger.LogResult("FAILED");
				return null;
			}
		} else {
			Utils.writeFile("API_Cache.json", json_raw);
		}
		Logger.LogResult("OK");	
			
		return parseMatches(json_raw);
	}
	
	private Match[] parseMatches(String json) {
		List<Match> matches = new ArrayList<Match>();
		
		Logger.LogForResult("Trying to parse JSON");
		JSONObject obj = new JSONObject(json);
		JSONArray fixtures = obj.getJSONArray("fixtures");
		Date fix = new Date();
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
			
			Match match = new Match(teamA, teamB, status, scoreA, scoreB, ddate, i);
			matches.add(match);
		}
		Logger.LogResult("OK");
		return matches.toArray(new Match[matches.size()]);
	}
	
	private Match[] getMatchUpdates(Match[] matches) {
		Match[] update = downloadMatches();
		if(update != null) {
			Match[] updated = new Match[matches.length];
			for(int i = 0; i < matches.length; i++) {
				updated[i] = update[matches[i].getIndex()];
			}
			return updated;
		}
		return null;
	}
	
	private Match[] updateNextMatches() {
		List<Match> matches = new ArrayList<Match>();
		Match next = updateNextMatch();
		
		for(int i = 0; i < this.matches.length; i++) {
			if(this.matches[i].getTime().compareTo(next.getTime()) == 0) {
				matches.add(this.matches[i]);
			}
		}
		
		return matches.toArray(new Match[matches.size()]);
	}
	
	private Match updateNextMatch() 
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
	
	private Match[] updateCurrentMatches()
	{
		List<Match> m = new ArrayList<Match>();
		if(matches != null) {
			for(int i = 0; i < matches.length; i++) {
				Match match = matches[i];
				if(match.getStatus() == MatchStatus.IN_PLAY)
					m.add(match);
			}
			if(m.size() == 0) return null;
			return m.toArray(new Match[m.size()]);
		}
		return null;
	}
	
	/*private Match getCurrentMatch()
	{
		if(matches != null) {
			for(int i = 0; i < matches.length; i++) {
				Match match = matches[i];
				if(match.getStatus() == MatchStatus.IN_PLAY)
					return match;
			}
		}
		return null;
	}*/
	
	private void eventPreMatch(Match[] match) {
		for(MatchEvent event : matchEvents)
			event.PreMatch(match);
	}
	
	private void eventMatchStarted(Match[] match) {
		for(MatchEvent event : matchEvents)
			event.MatchStarted(match);
	}
	
	private void eventMatchFinished(Match[] match) {
		for(MatchEvent event : matchEvents)
			event.MatchFinished(match);
	}
	
	private void eventLiveTickerStarted(Match match) {
		if(tickerEvent != null)
			tickerEvent.liveTickerStart(match);
	}
	
	private void eventLiveTickerEnd(Match match) {
		if(tickerEvent != null)
			tickerEvent.liveTickerEnd(match);
	}
	
	private void eventLiveTickerGoal(Match match) {
		if(tickerEvent != null)
			tickerEvent.liveTickerGoal(match);
	}

	private void setReminderTimer(Match[] match) {
		Logger.Log("Setting ReminderTimer for "+match.length + " match(es) ("+Utils.getFormated(match[0].getTime(), "yyyy-MM-dd HH:mm:ss")+")");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(match != null) {
					Logger.Log("[Event] Triggering Prematch ("+match.length+")");
					for(Match m : match)
						Logger.Log("	"+m.toString());
					
					eventPreMatch(match);
				}else {Logger.Log("[Event] ReminderTimer failed! No next match!");}
			}
			
		}, Utils.subtractHour(match[0].getTime(), 1));
		
	}
	
	private void setMatchStartTimer(Match[] match) {
		Logger.Log("Setting StartTimer for "+match.length + " match(es) ("+Utils.getFormated(match[0].getTime(), "yyyy-MM-dd HH:mm:ss")+")");
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(match != null) {
					currentMatch = match;
					eventMatchStarted(match);
					
					for(int i = 0; i < match.length; i++)
						eventLiveTickerStarted(match[i]);
					
					Logger.Log("[Event] Triggering Match Start ("+match.length+")");
					for(Match m : match)
						Logger.Log("	"+m.toString());
					
				}else {Logger.Log("[Event] StartTimer failed! No next match!");}
			}
			
		}, match[0].getTime());
		
	}
	
	private void setMatchUpdateTimer(Match[] match) {
		Logger.Log("Setting MatchUpdateTimer for "+match.length + " match(es) ("+Utils.getFormated(match[0].getTime(), "yyyy-MM-dd HH:mm:ss")+")");
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(match != null) {
					Logger.Log("[Event] Triggering Match Updates ("+match.length+")");
					for(Match m : match)
						Logger.Log("	"+m.toString());
					
					Match[] m = getMatchUpdates(match);

					if(m != null) {
						Logger.Log("Match Updates: ");
						for(Match m1 : m)
							Logger.Log("	"+m1.toString());
						
						//Live Ticker update
						for(int i = 0; i < m.length; i++) {
							if(hasGoalsChanged(currentMatch[i], m[i])) {
								eventLiveTickerGoal(m[i]);
							}
							if(hasMatchFinished(currentMatch[i], m[i])) {
								eventLiveTickerEnd(m[i]);
							}
						}
						
						
						currentMatch = m;//detect score changes?
						
						boolean finished = true;
						for(Match c : currentMatch) {
							if(c.getStatus() != MatchStatus.FINISHED) {
								finished = false;
							} 
						}
						
						if(finished) {
							eventMatchFinished(currentMatch);
							currentMatch = null;
							nextMatch = null;
							update();
							timer.cancel();
						}
					} else {
						Logger.Log("[Event] Match update failed!");
					}
				}else {Logger.Log("[Event] UpdateTimer failed! No next match!");}
			}
			
		}, match[0].getTime(), 1000 * 3 * 60);
	}

	private boolean hasGoalsChanged(Match match1, Match match2) {
		return !(match1.getScoreA() == match2.getScoreA() && match1.getScoreB() == match2.getScoreB());
	}
	
	private boolean hasMatchFinished(Match match1, Match match2) {
		return match1.getStatus() == MatchStatus.IN_PLAY && match2.getStatus() == MatchStatus.FINISHED;
	}
	
	public Match[] getMatches() {
		return matches;
	}
	
	public Match[] getCurrentMatches() {
		return currentMatch == null ? nextMatch : currentMatch;
	}
	
	public Match[] getPlayingMatches() {
		return currentMatch;
	}
	
	public Match[] getTodaysMatches() {
		Date today = new Date();
		List<Match> matches = new ArrayList<Match>();
		for(Match match : this.matches) {
			if(match.getTime().getDate() == today.getDate() && match.getTime().getMonth() == today.getMonth() && match.getTime().getYear() == today.getYear()) {
				matches.add(match);
			}
		}
		if(matches.size() > 0)
			return matches.toArray(new Match[matches.size()]);
		return null;
	}
	
	public int getFinishedMatchCount() {
		int count = 0;
		for(int i = 0; i < matches.length; i++) {
			if(matches[i].getStatus() == MatchStatus.FINISHED) {
				count++;
			}
		}
		return count;
	}
	
	public int getUnfinishedMatchCount() {
		return matches.length - getFinishedMatchCount();
	}
	
}
