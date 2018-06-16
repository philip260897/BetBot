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
import com.betbot.wm.Match;
import com.betbot.wm.WMManager;

public class Main 
{
	private static TelegramBot bot;
	private static WMManager wmManager;
	
	public static void main(String[] args) 
	{
		Logger.LogForResult("Initializing TelegromBot");
		boolean botSuccess = InitTelegramBot();
		Logger.LogResult(botSuccess ? "OK" : "FAILED");
		
		wmManager = new WMManager();
		ScoreManager.init();
		wmManager.init();
		
		//testUsers();
		
		if(botSuccess)
		{
			Logger.Log("Listening in Telegram Group");
			StartListening();
			//bot.sendMessage("Du Hodenknecht");
		}
		
		//bot.sendMessage("Alles klar bei dir?");
	}

	public static void testUsers() {
		Users[] users = generateTestUsers();
		
		for(Users user : users) {
			int score = 0;
			
			for(int i = 0; i < wmManager.getFinishedMatchCount(); i++) {
				Match match = wmManager.getMatches()[i];
				
				score += Utils.calculateScore(match, user.getTips()[i]);
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
		
		Users ben = new Users("Ben :D");
		ben.getTips()[0] = new Tip(1, 0);
		
		ben.getTips()[1] = new Tip(0, 1);
		ben.getTips()[2] = new Tip(0, 1);
		ben.getTips()[3] = new Tip(1, 1);
		
		ben.getTips()[4] = new Tip(2, 1);
		ben.getTips()[5] = new Tip(2, 0);
		ben.getTips()[6] = new Tip(1, 1);
		ben.getTips()[7] = new Tip(2, 1);
		
		Users adrian = new Users("Adrian");
		adrian.getTips()[0] = new Tip(2, 1);
		
		adrian.getTips()[1] = new Tip(0, 2);
		adrian.getTips()[2] = new Tip(0, 1);
		adrian.getTips()[3] = new Tip(1, 1);
		
		adrian.getTips()[4] = new Tip(3, 0);
		adrian.getTips()[5] = new Tip(2, 1);
		adrian.getTips()[6] = new Tip(1, 1);
		adrian.getTips()[7] = new Tip(2, 1);
		
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
		
		Users timon = new Users("Timon");
		timon.getTips()[0] = new Tip(3, 1);
		
		timon.getTips()[1] = new Tip(0, 2);
		timon.getTips()[2] = new Tip(1, 1);
		timon.getTips()[3] = new Tip(0, 1);
		
		timon.getTips()[4] = new Tip(3, 0);
		timon.getTips()[5] = new Tip(3, 0);
		timon.getTips()[6] = new Tip(1, 1);
		timon.getTips()[7] = new Tip(2, 1);
		
		Users lipa = new Users("Lipa");
		lipa.getTips()[0] = new Tip(3, 1);
		
		lipa.getTips()[1] = new Tip(0, 3);
		lipa.getTips()[2] = new Tip(0, 2);
		lipa.getTips()[3] = new Tip(2, 3);
		
		lipa.getTips()[4] = new Tip(2, 0);
		lipa.getTips()[5] = new Tip(2, 1);
		lipa.getTips()[6] = new Tip(0, 2);
		lipa.getTips()[7] = new Tip(2, 0);
		
		return new Users[] {phil, adrian, ben, lipa, timon, holzer, fabi};
	}
	
	public static boolean InitTelegramBot()
	{
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
        	bot = new TelegramBot();
            telegramBotsApi.registerBot(bot);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public static void StartListening()
	{
		bot.addTelegramBotEvent(new TelegramBotEvent() {

			@Override
			public void MessageReceived(String message, String sender, long chatId) {
				System.out.println("Message received: "+message+" ["+sender+"]");
			}

			@Override
			public void CommandReceived(String cmd, String[] args, String sender, long chatId) {
				
				String argss = "";
				for(String s : args) {
					argss += s + " ";
				}
				
				System.out.println("Command received: "+cmd+" ["+sender+"] "+ argss);
				
			}
			
		});
	}
	
	public static WMManager getWMManager() {
		return wmManager;
	}
	
	public static TelegramBot getTelegramBot() {
		return bot;
	}
}
