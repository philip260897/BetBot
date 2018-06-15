package com.betbot.score;



import java.util.ArrayList;
import java.util.List;

import com.betbot.bot.TelegramBot;
import com.betbot.bot.TelegramBotEvent;
import com.betbot.main.Logger;
import com.betbot.main.Main;
import com.betbot.main.Utils;
import com.betbot.wm.Match;
import com.betbot.wm.MatchEvent;

public class ScoreManager {
	static int i = 0;
	static List<Users> users = new ArrayList<Users>();
	public static void init(){
		Main.getWMManager().addMatchEvent(new MatchEvent(){

			@Override
			public void PreMatch(Match match) {
				// TODO Auto-generated method stub
				Logger.LogResult("Registred prematch");
				
			}

			@Override
			public void MatchStarted(Match match) {
				Logger.LogResult("Registred Match Started");
				Main.getTelegramBot().sendMessage("Spiel beginnt in einer Stunde! \nTipps abgeben!\n/1 SCORE für "+match.getTeamA()+"\n/2 SCORE für "+ match.getTeamB());
				match.toString();
				
			}

			@Override
			public void MatchFinished(Match match) {
				int winnerScore = 0;
				winnerScore = Utils.isWinner(match.getScoreA(), match.getScoreB());
				int winnerTip;
				int differenceTip;
				int differenceScore = Math.abs(match.getScoreA()-match.getScoreB());
				for(Users s : users){
					
					winnerTip = Utils.isWinner(s.getTip1(),s.getTip2());
					differenceTip = Math.abs(s.getTip1()-s.getTip2());
					
						//genauer Tipp: 5 Punkte
						if(s.getTip1()==match.getScoreA()&&s.getTip2()==match.getScoreB()){
						s.setScore(s.getScore()+5);
					}
					else
						//nur Tendenz: 2 Punkte
						if(winnerTip==winnerScore){
						s.setScore(s.getScore()+2);
					}
					else 
						//Tendenz und Tordifferenz: 3 Punkte
						if(winnerTip==winnerScore && differenceTip == differenceScore){
						s.setScore(s.getScore()+3);
					}
					
				}
			}
			
		});
		Main.getTelegramBot().addTelegramBotEvent(new TelegramBotEvent(){

			@Override
			public void MessageReceived(String message, String sender, long chatId) {
				// TODO Auto-generated method stub

			}

			@Override
			public void CommandReceived(String cmd, String[] args, String sender, long chatId) {
				// TODO Auto-generated method stub
				if(args.length>1){
					Main.getTelegramBot().sendMessage("Unterstütze nur EIN Argument du dummer Hurensohn");
				}
				if(cmd.equalsIgnoreCase("register")){
					Users user = new Users();
					user.setUsername(sender);
					user.setScore(0);
					user.setTip1(0);
					user.setTip2(0);
					Logger.LogResult("Registred");
					users.add(user);
				}
				if(cmd.equalsIgnoreCase("getscore")){
					//Logger.LogResult("Registred");
					for(Users s : users) {
						   if(s.getUsername().contains(sender)) {
						            Logger.Log(""+s.getScore());
						   }
						}
					
				}
				if(cmd.equalsIgnoreCase("1")){
					//Logger.LogResult("Registred");
					for(Users s : users) {
						   if(s.getUsername().contains(sender)) {
							  s.setTip1(Integer.parseInt(args[0]));
						   }
						}									
				}
				
				if(cmd.equalsIgnoreCase("2")){
					//Logger.LogResult("Registred");
					for(Users s : users) {
						   if(s.getUsername().contains(sender)) {
							  s.setTip1(Integer.parseInt(args[0]));
						   }
						}									
				}
				
				
			}
			
		});
	}
}
