package com.betbot.score;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.betbot.main.Utils;

public class ScoreLoader 
{
	public static List<Users> loadUsers()
	{
		try {
			String json = Utils.readFile("save.json");
			if(json != null) {

				List<Users> users = new ArrayList<Users>();
				JSONArray array = new JSONArray(json);
				for(int i = 0; i < array.length(); i++) {
					JSONObject root = (JSONObject)array.getJSONObject(i);
					String username = root.getString("username");
					int score = root.getInt("score");
					
					JSONArray tips = root.getJSONArray("tips");
					Tip ttips[] = new Tip[tips.length()];
					for(int j = 0; j < tips.length(); j++) {
						JSONObject tip = (JSONObject)tips.getJSONObject(j);
						Tip t = new Tip();
						t.setScoreA(tip.getInt("scoreA"));
						t.setScoreB(tip.getInt("scoreB"));
						t.setValid(tip.getBoolean("valid"));
						ttips[j] = t;
					}
					
					Users user = new Users(username);
					user.setScore(score);
					user.setTips(ttips);
					users.add(user);
				}
				return users;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveUsers(List<Users> users)
	{
		JSONArray rootArray = new JSONArray();
		for(Users user : users)
		{
			JSONObject root = new JSONObject();
			root.put("username", user.getUsername());
			root.put("score", user.getScore());
			
			JSONArray tips = new JSONArray();
			for(Tip tip : user.getTips())
			{
				JSONObject jtip = new JSONObject();
				jtip.put("scoreA", tip.getScoreA());
				jtip.put("scoreB", tip.getScoreB());
				jtip.put("valild", tip.isValid());
				tips.put(jtip);
			}
			root.put("tips", tips);
			rootArray.put(root);
		}

		Utils.writeFile("save.json", rootArray.toString());
	}
}
