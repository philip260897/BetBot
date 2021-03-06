package com.betbot.bot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.betbot.main.Logger;

public class TelegramBot extends TelegramLongPollingBot
{
	private List<TelegramBotEvent> telegramEvents = new ArrayList<TelegramBotEvent>();
	private long CHAT_ID = -241083590L; //R6Bot
	//private long CHAT_ID = 13451740L;
	private boolean first = false;
	
	public void addTelegramBotEvent(TelegramBotEvent event) {
		this.telegramEvents.add(event);
	}

	@Override
	public void onUpdateReceived(Update update) 
	{
		
		if(update.hasMessage() && update.getMessage().hasText())
		{
			if(!first) {
				Logger.Log("Current Telegram Chat_ID: "+update.getMessage().getChatId());
				first = true;
			}
			
			
			if(!update.getMessage().getText().startsWith("/")) {
				eventMessageReceived(update.getMessage().getText(), update.getMessage().getFrom().getFirstName(), update.getMessage().getChatId());
			} else {
				String[] split = update.getMessage().getText().split(" ");
				String cmd = split[0].replaceAll("/", "");
				String[] args = new String[split.length-1];
				for(int i = 1; i < split.length; i++) {
					args[i - 1] = split[i];
				}
				eventCommandReceived(cmd, args, update.getMessage().getFrom().getFirstName(), update.getMessage().getChatId());
			}
		}
	}

	public void sendMessage(String message, long chatId)
	{
        SendMessage messager = new SendMessage().setChatId(chatId).setText(message);
        try 
        {
            execute(messager);
        } 
        catch (TelegramApiException e) 
        {
            e.printStackTrace();
        }
	}
	
	public void sendMessage(String message) {
		sendMessage(message, CHAT_ID);
	}
	
	@Override
	public String getBotUsername() {
		//return "BettingWCBot";
		return "R6StatTestBot";
	}
	
	@Override
	public String getBotToken() {
		return "515370867:AAEouxKQB49H9zxfP_L4xQnpJQgVmT7afOI"; // R6
		//return "619959278:AAEnChV3o2ji7-_Zxu8m-_YsQg48Gpea0JQ";
	}
	
	private void eventMessageReceived(String message, String sender, long chatId)
	{
		for(TelegramBotEvent event : telegramEvents)
			event.MessageReceived(message, sender, chatId);
	}
	
	private void eventCommandReceived(String cmd, String[] args, String sender, long chatId)
	{
		for(TelegramBotEvent event : telegramEvents)
			event.CommandReceived(cmd, args, sender, chatId);
	}
}
