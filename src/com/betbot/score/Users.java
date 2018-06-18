package com.betbot.score;

public class Users implements Comparable<Users> {
	private String username;
	private int score;
	private Tip[] tips = new Tip[64];
	
	@Deprecated
	public Users() {
		this("");
	}
	
	public Users(String username){
		for(int i = 0; i<tips.length; i++){
			tips[i] = new Tip();
		}
		this.username = username;
		this.score = 0;
	}
	
	public String getUsername() {
		return username;
	}
	
	@Deprecated
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
	
	public int compareTo(Users compare) {
		// TODO Auto-generated method stub
		int comparescore = ((Users) compare).getScore();
		return comparescore-this.getScore();
	}



}
