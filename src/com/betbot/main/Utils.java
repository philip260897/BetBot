package com.betbot.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.betbot.score.Tip;
import com.betbot.wm.Match;

public class Utils 
{
	public static String getText(String url) 
	{
		try {
	        URL website = new URL(url);
	        
	        URLConnection connection = website.openConnection();
	        connection.setRequestProperty("X-Auth-Token", "9f296fbd4e524b3fbae33dc3d1a78bdf");
	        BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream()));
	
	        StringBuilder response = new StringBuilder();
	        String inputLine;
	
	        while ((inputLine = in.readLine()) != null) 
	            response.append(inputLine);
	
	        in.close();
	
	        //return null;
	        return response.toString();
		}catch(Exception ex) {ex.printStackTrace();}
		return null;
    }
	
	public static String readFile(String filePath) {
		try {
			File file = new File(filePath);
			if(file.exists()) {
				String json = "";
				String line = "";
				BufferedReader reader = new BufferedReader(new FileReader(filePath));
				while((line = reader.readLine()) != null) {
					json += line;
				}
				reader.close();
				return json;
			}
		}catch(Exception ex) {ex.printStackTrace();}
		return null;
	}
	
	public static void writeFile(String file, String text) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(text);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Date getDate(String timestamp, String format)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	    Date parsedDate = null;
		try {
			parsedDate = dateFormat.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return parsedDate;
	}
	
	public static String getFormated(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
		//return "";
	}
	
	public static Date subtractHour(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(date.getYear(), date.getMonth(), date.getDate());
		cal.add(Calendar.HOUR, hours*-1);
		Date d = cal.getTime();
		d.setDate(cal.get(Calendar.DATE));
		d.setMonth(cal.get(Calendar.MONTH));
		d.setYear(cal.get(Calendar.YEAR));
		return d;
	}
	public static int isWinner(int scoreA, int scoreB){
		if(scoreA>scoreB){
		return 1;
		}
		else if(scoreA<scoreB){
			return 2;
		}
		else if(scoreA==scoreB){
			return 0;
		}
		else{return -1;}
		
		
	}
	public static String insultGenerator(){

		Random rand = new Random();
		String insults[] = {"Spast", "ABS-Bremser","dumme Fickhure","Schwingtitte","Analbanane","Analdin", "Hodenknecht", "Wixgsicht", "Otto", "Gsicht", "Hurenknecht", "Wixkopf", "Hobelschlunze", "Schlingel", "Nichtsnutz"};
		int  n = rand.nextInt(insults.length);
		return insults[n];
	}
	
	public static int calculateScore(Match match, Tip tip) 
	{
		int winnerScore = Utils.isWinner(match.getScoreA(), match.getScoreB());
		int differenceScore = Math.abs(match.getScoreA()-match.getScoreB());

		int winnerTip = Utils.isWinner(tip.getScoreA(), tip.getScoreB());
		int differenceTip = Math.abs(tip.getScoreA()-tip.getScoreB());

		//genauer Tipp: 5 Punkte
		if(tip.getScoreA()==match.getScoreA()&& tip.getScoreB()==match.getScoreB()){
			return 5;
		}
		else
			//Tendenz und Tordifferenz: 3 Punkte
			if(winnerTip==winnerScore && differenceTip == differenceScore){
				return 3;
			}
			else 

				//nur Tendenz: 2 Punkte
				if(winnerTip==winnerScore){
					return 2;
				}
		return 0;
	}
}
