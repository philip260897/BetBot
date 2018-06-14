package com.betbot.bot;

public interface TelegramBotEvent 
{
	public void MessageReceived(String message, String sender, long chatId);

	public void CommandReceived(String cmd, String[] args, String sender, long chatId);
}
