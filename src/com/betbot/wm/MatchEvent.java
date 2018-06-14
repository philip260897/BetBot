package com.betbot.wm;

public interface MatchEvent 
{
	public void PreMatch(Match match);
	
	public void MatchStarted(Match match);
	
	public void MatchFinished(Match match);
}
