package com.betbot.wm;

import com.betbot.main.Logger;
import com.betbot.main.Main;

public class LiveTicker 
{
	private static boolean enabled = false;
	
	public static void setEnabled(boolean enable) {
		LiveTicker.enabled= enable;
	}
	
	public static boolean isEnabled() {
		return LiveTicker.enabled;
	}
	
	public static void init()
	{
		Main.getWMManager().setLiveTickerEvent(new LiveTickerEvent() {

			@Override
			public void liveTickerStart(Match match) {
				if(!enabled) return;
				Logger.Log("[LiveTicker] LiveTicker enabled for Match "+match);
			}

			@Override
			public void liveTickerEnd(Match match) {
				if(!enabled) return;
				Logger.Log("[LiveTicker] LiveTicker ended for Match "+match+ " Score: "+match.getScoreA() + " - " + match.getScoreB());
			}

			@Override
			public void liveTickerGoal(Match match) {
				if(!enabled) return;
				Logger.Log("[LiveTicker] LiveTicker GOAAAAAAAAAL "+match.getTeamA() + " - " + match.getTeamB() + " "+match.getScoreA()+":"+match.getScoreB());
			}
			
		});
	}
}
