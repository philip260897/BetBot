package com.betbot.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.betbot.score.Tip;
import com.betbot.score.Users;
import com.betbot.wm.Match;

public class Utils 
{
	public static String getText(String url) throws Exception 
	{
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
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
		String insults[] = {"ABS-Bremser","dumme Fickhure","Schwingtitte","Analbanane","Analdin", "Hodenknecht", "Wixgsicht", "Otto", "Gsicht", "Hurenknecht", "Wixkopf", "Hobelschlunze", "Schlingel", "Nichtsnutz"};
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
			//nur Tendenz: 2 Punkte
			if(winnerTip==winnerScore){
				return 2;
			}
			else 
				//Tendenz und Tordifferenz: 3 Punkte
				if(winnerTip==winnerScore && differenceTip == differenceScore){
					return 3;
				}
		return 0;
	}
}
