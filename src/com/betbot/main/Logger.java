package com.betbot.main;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Logger 
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("[dd/MM/yy HH:mm:ss]");
	
	public static void Log(String message)
	{
		Timestamp timestampp = new Timestamp(System.currentTimeMillis());
		String timestamp = sdf.format(timestampp);
		
		Log(timestamp, message);
	}
	
	public static void LogForResult(String message)
	{
		Timestamp timestampp = new Timestamp(System.currentTimeMillis());
		String timestamp = sdf.format(timestampp);
		
		LogN(timestamp, message);
	}
	
	public static void LogResult(String result) {
		System.out.println(result);
	}
	
	public static void LogN(String timestamp, String message)
	{
		System.out.print(timestamp+" "+message+"...");
	}
	
	public static void Log(String timestamp, String message)
	{
		System.out.println(timestamp+" "+message);
	}
}
