package com.betbot.score;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.betbot.bot.TelegramBot;
import com.betbot.bot.TelegramBotEvent;
import com.betbot.main.Logger;
import com.betbot.main.Main;
import com.betbot.main.Utils;
import com.betbot.wm.Match;
import com.betbot.wm.MatchEvent;
import com.betbot.wm.MatchStatus;

public class ScoreManager {
	static int i = 0;
	static List<Users> users = new ArrayList<Users>();
	public static void init(){
		Main.getWMManager().addMatchEvent(new MatchEvent(){

			@Override
			public void PreMatch(Match[] match) {
				// TODO Auto-generated method stub
				Logger.LogResult("Registred Match Started");
				int matchCount = match.length;
				for(int i = 0; i<matchCount; i++){
					Main.getTelegramBot().sendMessage("Spiel beginnt in einer Stunde! \nTipps abgeben!\n/bet ID SCORE:SCORE\nMATCH ID = "+match[i].getIndex()+"\n"+match[i].getTeamA()+"-"+ match[i].getTeamB());
					match.toString();
				}
			}

			@Override
			public void MatchStarted(Match[] match) {

				
			}

			@Override
			public void MatchFinished(Match[] match) {
				int winnerScore = 0;
				int matchCount = match.length;
				for(int i = 0; i<matchCount; i++){
					winnerScore = Utils.isWinner(match[i].getScoreA(), match[i].getScoreB());
					int winnerTip;
					int differenceTip;
					int differenceScore = Math.abs(match[i].getScoreA()-match[i].getScoreB());
					for(Users s : users){
						
						winnerTip = Utils.isWinner(s.getTips()[match[i].getIndex()].getScoreA(),s.getTips()[match[i].getIndex()].getScoreB());
						differenceTip = Math.abs(s.getTips()[match[i].getIndex()].getScoreA()-s.getTips()[match[i].getIndex()].getScoreB());
						
							//genauer Tipp: 5 Punkte
							if(s.getTips()[match[i].getIndex()].getScoreA()==match[i].getScoreA()&& s.getTips()[match[i].getIndex()].getScoreB()==match[i].getScoreB()){
								s.setScore(s.getScore()+5);
						}
						else
							//nur Tendenz: 2 Punkte
							if(winnerTip==winnerScore && differenceTip == differenceScore){
								s.setScore(s.getScore()+3);
						}

						else 
							//Tendenz und Tordifferenz: 3 Punkte
							if(winnerTip==winnerScore){
								s.setScore(s.getScore()+2);
						}
						
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
				Match[] match = Main.getWMManager().getCurrentMatches();
				int registered = 0;
				if(args.length>2){
					Main.getTelegramBot().sendMessage("Unterstütze nur ZWEI Argumente du "+Utils.insultGenerator());
				}else{
				if(cmd.equalsIgnoreCase("register")){
					//Check if schon registriert
					for(Users s : users){
						if(s.getUsername().contains(sender)) {
							Main.getTelegramBot().sendMessage("Schon registriert du " + Utils.insultGenerator());
							registered = 1;
						}
					}
					if(registered==0){
					Users user = new Users(sender);
					Logger.LogResult("Registred");
					users.add(user);
					}
				}
				if(cmd.equalsIgnoreCase("getscore")){
					//Logger.LogResult("Registered");
					for(Users s : users) {
						   if(s.getUsername().contains(sender)) {
						         Logger.Log(""+s.getScore());
						   }
						}
					
				}
				if(cmd.equalsIgnoreCase("bet")){
					int matchCount = match.length;
					int checkInPlay = 0;
						if(match[i].getStatus() == MatchStatus.IN_PLAY && checkInPlay == 0){
							Main.getTelegramBot().sendMessage("Jetzt wird nicht gewettet du "+Utils.insultGenerator());
							checkInPlay = 1;
						}else{
						for(Users s : users) {
							   if(s.getUsername().contains(sender)) {
								  if(StringUtils.isNumeric((args[0])) && args[1].charAt(1) == ':'  && Character.isDigit((args[1].charAt(0))) && Character.isDigit((args[1].charAt(2)))){
									  s.getTips()[Integer.parseInt(args[0])].setScoreA(Character.getNumericValue(args[1].charAt(0)));
									  s.getTips()[Integer.parseInt(args[0])].setScoreB(Character.getNumericValue(args[1].charAt(2)));
								  }
								  else { 
										Main.getTelegramBot().sendMessage("Falsche Eingabe du "+Utils.insultGenerator());
								  }
							   }
							}
						
						}
					
				}
				
				if(cmd.equalsIgnoreCase("2")){
					//Logger.LogResult("Registred");
					for(Users s : users) {
						   if(s.getUsername().contains(sender)) {
							  //s.setTip1(Integer.parseInt(args[0]));
						   }
						}									
				}
				if(cmd.equalsIgnoreCase("tipstoday")){
					int matchCount = match.length;
					//Logger.LogResult("Registred");
					for(int i = 0; i < matchCount; i++){
						for(Users s : users) {
							   if(s.getUsername().contains(sender)) {
								   Main.getTelegramBot().sendMessage("Deine Tipps du "+Utils.insultGenerator()+":\n"	
										   								+Main.getWMManager().getCurrentMatches()[i].getTeamA()+
										   								"-"+Main.getWMManager().getCurrentMatches()[i].getTeamB()+" "+s.getTips()[match[i].getIndex()].getScoreA()+":"+s.getTips()[match[i].getIndex()].getScoreB());
							   }
							}
					}
				}
				}
				
				
			}
			
		});
	}
}
