package com.betbot.bot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class TelegramBot extends TelegramLongPollingBot
{
	private TelegramBotEvent telegramEvent;
	
	public void addTelegramBotEvent(TelegramBotEvent event) {
		this.telegramEvent = event;
	}

	@Override
	public void onUpdateReceived(Update update) 
	{
		if(update.hasMessage() && update.getMessage().hasText())
		{
			if(!update.getMessage().getText().startsWith("/")) {
				if(telegramEvent != null)telegramEvent.MessageReceived(update.getMessage().getText(), update.getMessage().getFrom().getFirstName());
			} else {
				String[] split = update.getMessage().getText().split(" ");
				String cmd = split[0].replaceAll("/", "");
				String[] args = new String[split.length-1];
				for(int i = 1; i < split.length; i++) {
					args[i - 1] = split[i];
				}
				if(telegramEvent != null)telegramEvent.CommandReceived(cmd, args, update.getMessage().getFrom().getFirstName());
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
	
}
