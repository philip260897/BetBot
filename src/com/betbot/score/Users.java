package com.betbot.score;

public class Users {
	private String username;
	private int score;
	private Tip[] tips = new Tip[64];
	
	public Users(){
		for(int i = 0; i<tips.length; i++){
			tips[i] = new Tip();
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Tip[] getTips() {
		return tips;
	}
	public void setTips(Tip[] tips) {
		this.tips = tips;
	}
}
