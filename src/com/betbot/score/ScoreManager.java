package com.betbot.score;

import com.betbot.bot.TelegramBotEvent;
import com.betbot.main.Logger;
import com.betbot.main.Main;
import com.betbot.wm.Match;
import com.betbot.wm.MatchEvent;

public class ScoreManager {
	public static void init(){
		Main.getWMManager().addMatchEvent(new MatchEvent(){

			@Override
			public void PreMatch(Match match) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void MatchStarted(Match match) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void MatchFinished(Match match) {
				// TODO Auto-generated method stub
			}
			
		});
		Main.getTelegramBot().addTelegramBotEvent(new TelegramBotEvent(){

			@Override
			public void MessageReceived(String message, String sender) {
				// TODO Auto-generated method stub

			}

			@Override
			public void CommandReceived(String cmd, String[] args, String sender) {
				// TODO Auto-generated method stub
				if(cmd.equalsIgnoreCase("register")){
					Users user = new Users();
					user.setUsername(sender);
					user.setScore(0);
					user.setTip1(0);
					user.setTip2(0);
					Logger.LogResult("Registred");
				}
				//if(cmd.equalsIgnoreCase("tip1"));
			}
			
		});
	}
}
