package com.betbot.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.betbot.bot.TelegramBot;
import com.betbot.bot.TelegramBotEvent;

public class Main 
{
	private static TelegramBot bot;
	
	public static void main(String[] args) 
	{
		Logger.LogForResult("Initializing TelegromBot");
		boolean success = InitTelegramBot();
		Logger.LogResult(success ? "OK" : "FAILED");
		
	}

	public static boolean InitTelegramBot()
	{
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
        	bot = new TelegramBot();
            telegramBotsApi.registerBot(bot);
            
    		bot.setTelegramBotEvent(new TelegramBotEvent() {

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
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
	}
}
