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

public class ScoreLoader 
{
	public static Users[] loadUsers()
	{
		try {
			File file = new File("save.json");
			if(file.exists()) {
				String json = "";
				String line = "";
				BufferedReader reader = new BufferedReader(new FileReader("save.json"));
				while((line = reader.readLine()) != null) {
					json += line;
				}
				reader.close();

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
						ttips[j] = t;
					}
					
					Users user = new Users();
					user.setUsername(username);
					user.setScore(score);
					user.setTips(ttips);
					users.add(user);
				}
				return users.toArray(new Users[users.size()]);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveUsers(Users[] users)
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
				tips.put(jtip);
			}
			root.put("tips", tips);
			rootArray.put(root);
		}
		try {
			FileWriter writer = new FileWriter("save.json");
			writer.write(rootArray.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rootArray.toString());
	}
}
