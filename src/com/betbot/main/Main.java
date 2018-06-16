package com.betbot.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.betbot.bot.TelegramBot;
import com.betbot.bot.TelegramBotEvent;
import com.betbot.score.ScoreLoader;
import com.betbot.score.ScoreManager;
import com.betbot.score.Tip;
import com.betbot.score.Users;
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
		
		/*Users[] users = new Users[2];
		Users user1 = new Users();
		user1.setUsername("xStachelbaer");
		Tip tip = new Tip();
		tip.setScoreA(5);
		tip.setScoreB(2);
		user1.getTips()[0] = tip;
		
		Users user2 = new Users();
		user2.setUsername("Hurenknecht");
		Tip tip2 = new Tip();
		tip2.setScoreA(1);
		tip2.setScoreB(0);
		user2.getTips()[0] = tip2;
		
		users[0] = user1;
		users[1] = user2;
		ScoreLoader.saveUsers(users);
		
		Users[] users2 = ScoreLoader.loadUsers();
		for(Users user : users2)
		{
			System.out.println(user.getUsername()+ " " + user.getTips()[0].getScoreA() + " " + user.getTips()[0].getScoreB());
		}*/
		
		if(botSuccess)
		{
			Logger.Log("Listening in Telegram Group");
			StartListening();
			//bot.sendMessage("Du Hodenknecht");
		}
		
		//bot.sendMessage("Alles klar bei dir?");
	}

	public static Users[] generateTestUsers()
	{
		User phil = new User();
		
		return null;
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
