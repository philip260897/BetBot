package com.betbot.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.betbot.bot.TelegramBot;
import com.betbot.bot.TelegramBotEvent;
import com.betbot.score.ScoreManager;
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
		
		ScoreManager.init();		
		
		if(botSuccess)
		{
			Logger.Log("Listening in Telegram Group");
			StartListening();
		}
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
			public void MessageReceived(String message, String sender) {
				System.out.println("Message received: "+message+" ["+sender+"]");
			}

			@Override
			public void CommandReceived(String cmd, String[] args, String sender) {
				
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
