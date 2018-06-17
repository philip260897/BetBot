package com.betbot.wm;

public interface LiveTickerEvent {

	public void liveTickerStart(Match match);
	
	public void liveTickerEnd(Match match);
	
	public void liveTickerGoal(Match match);
	
}
