package com.betbot.score;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

public class ScoreManager 
{
	private static int i = 0;
	private static List<Users> users = new ArrayList<Users>();

	
	public static void init(){
		users = ScoreLoader.loadUsers();
		Logger.Log("Loaded "+users.size()+" users!");
		
		Main.getWMManager().addMatchEvent(new MatchEvent(){
			
			
			@Override
			public void PreMatch(Match[] match) {
				// TODO Auto-generated method stub

				int matchCount = match.length;
				String message = "Spiel beginnt in einer Stunde! \nTipps abgeben!\n/bet ID SCORE:SCORE\n\n";
				for(int i = 0; i<matchCount; i++){
					message += "MATCH ID = "+match[i].getIndex()+"\n"+match[i].getTeamA()+"-"+ match[i].getTeamB()+"\n";
				}
				Main.getTelegramBot().sendMessage(message);
			}

			@Override
			public void MatchStarted(Match[] match) {
				int matchCount = match.length;
				String message = "Spiel beginnt! \nKeine Tipps mehr!\n\n";
				for(int i = 0; i<matchCount; i++){
					message += match[i].getTeamA()+"-"+ match[i].getTeamB()+"\n";
				}
				Main.getTelegramBot().sendMessage(message);
				
			}

			@Override
			public void MatchFinished(Match[] matchi) {
				updateScores();
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
				if(args.length>2){
					Main.getTelegramBot().sendMessage("Unterstütze nur ZWEI Argumente du "+Utils.insultGenerator(),chatId);
				}else{
					if(cmd.equalsIgnoreCase("register")){
						//Check if schon registriert
						Users s = getUser(sender);

						if(s == null){
							Users user = new Users(sender);
							Logger.Log("User: "+ sender +" Registered");
							users.add(user);
							
							//TESTING PURPOSES
							//Random rand = new Random();
							//user.setScore(rand.nextInt(50));
							//----------------

							ScoreLoader.saveUsers(users);
						}else{
							Main.getTelegramBot().sendMessage("Schon registriert du " + Utils.insultGenerator(),chatId);
						}


					}
					if(cmd.equalsIgnoreCase("allscores")){
						updateScores();
						String message = sorting();
						Main.getTelegramBot().sendMessage(message, chatId);
					}
					if(cmd.equalsIgnoreCase("getscore")){
						updateScores();
						Users s = getUser(sender);
						Main.getTelegramBot().sendMessage(""+s.getScore(), chatId);
							
						}

					}
					if(cmd.equalsIgnoreCase("history")) {
						Users s = getUser(sender);
						if(s != null)
							Main.getTelegramBot().sendMessage(getHistory(s));
					}
					if(cmd.equalsIgnoreCase("bet")){
						Match currentMatch[] = Main.getWMManager().getCurrentMatches();
						int checkInPlay = 0;
						if(match[currentMatch.length].getStatus() == MatchStatus.IN_PLAY && checkInPlay == 0){
							Main.getTelegramBot().sendMessage("Jetzt wird nicht gewettet du "+Utils.insultGenerator(),chatId);
							checkInPlay = 1;
						}else{
							Users s = getUser(sender); 
							if(args.length == 2 && args[1].length() == 3 
									&& StringUtils.isNumeric((args[0])) 
									&& args[1].charAt(1) == ':'  
									&& Character.isDigit((args[1].charAt(0))) 
									&& Character.isDigit((args[1].charAt(2))))
							{
								Match matchTip = Main.getWMManager().getMatches()[Integer.parseInt(args[0])];
								if(matchTip.getTime().after(new Date())) {
								
									s.getTips()[Integer.parseInt(args[0])].setScoreA(Character.getNumericValue(args[1].charAt(0)));
									s.getTips()[Integer.parseInt(args[0])].setScoreB(Character.getNumericValue(args[1].charAt(2)));
									s.getTips()[Integer.parseInt(args[0])].setValid(true);
									ScoreLoader.saveUsers(users);
								
								} else {
									Logger.Log("User "+sender+" hat versucht zu cheaten der "+Utils.insultGenerator());
								}
							}
							else { 
								Main.getTelegramBot().sendMessage("Falsche Eingabe du "+Utils.insultGenerator(),chatId);
							}



						}

					}

					if(cmd.equalsIgnoreCase("matches")){
						Match todayMatch[] = Main.getWMManager().getTodaysMatches();
						int matchCount = todayMatch.length;
						String message = "Heutige Matches:\n/bet ID SCORE:SCORE\n";
						for(int i = 0; i < matchCount; i++){
							message += "\nMATCH ID = "+todayMatch[i].getIndex()+"\n"+todayMatch[i].getTeamA()+"-"+ todayMatch[i].getTeamB()+"\n";
						}
						Main.getTelegramBot().sendMessage(message,chatId);
					}
					if(cmd.equalsIgnoreCase("tipstoday")){
						Match todayMatch[] = Main.getWMManager().getTodaysMatches();
						int matchCount = todayMatch.length;
						String message = "Deine Tipps du "+Utils.insultGenerator()+"\n";
						//Main.getTelegramBot().sendMessage("Deine Tipps du "+Utils.insultGenerator()+":\n");
						for(int i = 0; i < matchCount; i++){
							Users s = getUser(sender);
							message += "\nID: "+ todayMatch[i].getIndex() + "\n"
									+todayMatch[i].getTeamA()+
									"-"+todayMatch[i].getTeamB()+" "+s.getTips()[todayMatch[i] .getIndex()].getScoreA()+":"+s.getTips()[todayMatch[i].getIndex()].getScoreB()+"\n";

						}
						Main.getTelegramBot().sendMessage(message,chatId);
					}
				}


			

		});
	}
	public static Users getUser(String sender){
		for(Users s : users){
			if(s.getUsername().contains(sender)) {
				return s;
			}
		}
		return null;
	}
	
	public static String getHistory(Users user) {
		String history = "";
		int total = 0;
		for(int i = 0; i < Main.getWMManager().getFinishedMatchCount(); i++) {
			Match match = Main.getWMManager().getMatches()[i];
			Tip tip = user.getTips()[i];
			int score = Utils.calculateScore(match, tip);
			if(tip.isValid())
				total += score;
			history += i + " " + match.getTeamA()+"("+match.getScoreA()+") - "+match.getTeamB()+"("+match.getScoreB()+") "+tip.getScoreA()+":"+tip.getScoreB()+" score: "+ score+ " "+(!tip.isValid()?"INVALID":"")+"\n";
		}
		history += "Total score: "+total;
		
		return history;
	}
	
	public static void updateScores() {
		for(Users user : users) {
			int score = 0;
			
			for(int i = 0; i < Main.getWMManager().getFinishedMatchCount(); i++) {
				Match match = Main.getWMManager().getMatches()[i];
				Tip tip = user.getTips()[i];
				int s =  Utils.calculateScore(match, tip);
							
				if(tip.isValid()) {
					score+=s;
				}
				
			}
			user.setScore(score);
		}
		ScoreLoader.saveUsers(users);
	}
	
	public static String sorting(){
		updateScores();
		String message = "";
		Collections.sort(users);
		for(Users s : users){
			message += ""+s.getUsername() + " - Score: " + s.getScore() + "\n";
		}
		return message;
		
	}
}
