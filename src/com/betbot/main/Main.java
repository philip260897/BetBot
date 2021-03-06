package com.betbot.main;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.betbot.bot.TelegramBot;
import com.betbot.bot.TelegramBotEvent;
import com.betbot.score.ScoreLoader;
import com.betbot.score.ScoreManager;
import com.betbot.score.Tip;
import com.betbot.score.Users;
import com.betbot.wm.LiveTicker;
import com.betbot.wm.Match;
import com.betbot.wm.WMManager;

public class Main 
{
	private static TelegramBot bot;
	private static WMManager wmManager;
	
	private static boolean register = true;
	
	public static void main(String[] args) 
	{
		Logger.LogForResult("Initializing TelegromBot");
		boolean botSuccess = InitTelegramBot();
		Logger.LogResult(botSuccess ? "OK" : "FAILED");
		
		wmManager = new WMManager();
		ScoreManager.init();
		LiveTicker.init();
		LiveTicker.setEnabled(true);
		
		wmManager.init();
		
		//System.out.println(ScoreManager.sorting());
		//System.out.println(ScoreManager.getHistory(ScoreManager.getUser("Fabian")));
		//testUsers();
		
		//bot.sendMessage("Alles klar bei dir?");
	}

	public static void testUsers() {
		Users[] users = generateTestUsers();
		
		for(Users user : users) {
			int score = 0;
			
			for(int i = 0; i < wmManager.getFinishedMatchCount(); i++) {
				Match match = wmManager.getMatches()[i];
				Tip tip = user.getTips()[i];
				
				if(tip.isValid()) {
					int s =  Utils.calculateScore(match, user.getTips()[i]);
					
					//System.out.println("\t"+match.getTeamA()+":"+match.getTeamB() + " " + match.getScoreA()+"-"+match.getScoreB() + " Tip: "+tip.getScoreA()+":"+tip.getScoreB() + " score: "+s);
					
					score+=s;
				}
			}
			user.setScore(score);
			Logger.Log("User: "+user.getUsername()+" - score: "+score);
		}
		
		List<Users> list = new ArrayList<Users>();
		for(Users user : users)
			list.add(user);
		ScoreLoader.saveUsers(list);
	}
	
	public static Users[] generateTestUsers()
	{
		Users phil = new Users("Philip");
		phil.getTips()[0] = new Tip(2, 1, false);
		
		phil.getTips()[1] = new Tip(1, 3);
		phil.getTips()[2] = new Tip(0, 2);
		phil.getTips()[3] = new Tip(2, 1);
		
		phil.getTips()[4] = new Tip(2, 0);
		phil.getTips()[5] = new Tip(2, 0);
		phil.getTips()[6] = new Tip(0, 1);
		phil.getTips()[7] = new Tip(0, 1);
		
		phil.getTips()[8] = new Tip(1, 0);
		phil.getTips()[9] = new Tip(2, 0);
		phil.getTips()[10] = new Tip(2, 0);
		
		Users ben = new Users("Ben :D");
		ben.getTips()[0] = new Tip(1, 0);
		
		ben.getTips()[1] = new Tip(0, 1);
		ben.getTips()[2] = new Tip(0, 1);
		ben.getTips()[3] = new Tip(1, 1);
		
		ben.getTips()[4] = new Tip(2, 1);
		ben.getTips()[5] = new Tip(2, 0);
		ben.getTips()[6] = new Tip(1, 1);
		ben.getTips()[7] = new Tip(2, 1);
		
		ben.getTips()[8] = new Tip(0, 0);
		ben.getTips()[9] = new Tip(1, 0);
		ben.getTips()[10] = new Tip(2, 1);
		
		Users adrian = new Users("Adrian");
		adrian.getTips()[0] = new Tip(2, 1);
		
		adrian.getTips()[1] = new Tip(0, 2);
		adrian.getTips()[2] = new Tip(0, 1);
		adrian.getTips()[3] = new Tip(1, 1);
		
		adrian.getTips()[4] = new Tip(3, 0);
		adrian.getTips()[5] = new Tip(2, 1);
		adrian.getTips()[6] = new Tip(1, 1);
		adrian.getTips()[7] = new Tip(2, 1);
		
		adrian.getTips()[8] = new Tip(1, 2);
		adrian.getTips()[9] = new Tip(2, 0);
		adrian.getTips()[10] = new Tip(3, 1);
		
		Users fabi = new Users("Fabi");
		fabi.getTips()[0] = new Tip(2, 0);
		
		fabi.getTips()[1] = new Tip(0, 2);
		fabi.getTips()[2] = new Tip(2, 1);
		fabi.getTips()[3] = new Tip(1, 0);
		
		fabi.getTips()[4] = new Tip(2, 0, false);
		fabi.getTips()[5] = new Tip(2, 1);
		fabi.getTips()[6] = new Tip(1, 1);
		fabi.getTips()[7] = new Tip(0, 1);
		
		Users holzer = new Users("Holzer");
		holzer.getTips()[0] = new Tip(2, 1);
		
		holzer.getTips()[1] = new Tip(0, 2);
		holzer.getTips()[2] = new Tip(1, 0);
		holzer.getTips()[3] = new Tip(1, 2);
		
		holzer.getTips()[4] = new Tip(1, 0);
		holzer.getTips()[5] = new Tip(2, 0);
		holzer.getTips()[6] = new Tip(1, 1);
		holzer.getTips()[7] = new Tip(2, 0);
		
		holzer.getTips()[8] = new Tip(0, 1);
		holzer.getTips()[9] = new Tip(2, 0);
		holzer.getTips()[10] = new Tip(2, 0);
		
		Users timon = new Users("Timon");
		timon.getTips()[0] = new Tip(3, 1);
		
		timon.getTips()[1] = new Tip(0, 2);
		timon.getTips()[2] = new Tip(1, 1);
		timon.getTips()[3] = new Tip(0, 1);
		
		timon.getTips()[4] = new Tip(3, 0);
		timon.getTips()[5] = new Tip(3, 0);
		timon.getTips()[6] = new Tip(1, 1);
		timon.getTips()[7] = new Tip(2, 1);
		
		timon.getTips()[8] = new Tip(0, 1);
		timon.getTips()[9] = new Tip(3, 1);
		timon.getTips()[10] = new Tip(3, 0);
		
		Users lipa = new Users("Lipa");
		lipa.getTips()[0] = new Tip(3, 1);
		
		lipa.getTips()[1] = new Tip(0, 3);
		lipa.getTips()[2] = new Tip(0, 2);
		lipa.getTips()[3] = new Tip(2, 3);
		
		lipa.getTips()[4] = new Tip(2, 0);
		lipa.getTips()[5] = new Tip(2, 1);
		lipa.getTips()[6] = new Tip(0, 2);
		lipa.getTips()[7] = new Tip(2, 0);
		
		lipa.getTips()[8] = new Tip(0, 1);
		lipa.getTips()[9] = new Tip(3, 1);
		lipa.getTips()[10] = new Tip(3, 1);
		
		return new Users[] {phil, adrian, ben, lipa, timon, holzer, fabi};
	}
	
	public static boolean InitTelegramBot()
	{
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		bot = new TelegramBot();
		if(register) {
	        try {
	        	
	            telegramBotsApi.registerBot(bot);
	            return true;
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
		}
        return false;
	}
	
	public static WMManager getWMManager() {
		return wmManager;
	}
	
	public static TelegramBot getTelegramBot() {
		return bot;
	}
}
