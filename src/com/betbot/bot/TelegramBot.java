package com.betbot.bot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class TelegramBot extends TelegramLongPollingBot
{
	private List<TelegramBotEvent> telegramEvents = new ArrayList<TelegramBotEvent>();
	
	public void addTelegramBotEvent(TelegramBotEvent event) {
		this.telegramEvents.add(event);
	}

	@Override
	public void onUpdateReceived(Update update) 
	{
		if(update.hasMessage() && update.getMessage().hasText())
		{
			if(!update.getMessage().getText().startsWith("/")) {
				eventMessageReceived(update.getMessage().getText(), update.getMessage().getFrom().getFirstName());
			} else {
				String[] split = update.getMessage().getText().split(" ");
				String cmd = split[0].replaceAll("/", "");
				String[] args = new String[split.length-1];
				for(int i = 1; i < split.length; i++) {
					args[i - 1] = split[i];
				}
				eventCommandReceived(cmd, args, update.getMessage().getFrom().getFirstName());
			}
		}
	}

	@Override
	public String getBotUsername() {
		return "R6StatTestBot";
	}
	
	@Override
	public String getBotToken() {
		return "515370867:AAEouxKQB49H9zxfP_L4xQnpJQgVmT7afOI";
	}
	
	private void eventMessageReceived(String message, String sender)
	{
		for(TelegramBotEvent event : telegramEvents)
			event.MessageReceived(message, sender);
	}
	
	private void eventCommandReceived(String cmd, String[] args, String sender)
	{
		for(TelegramBotEvent event : telegramEvents)
			event.CommandReceived(cmd, args, sender);
	}
}
