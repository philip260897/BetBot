package com.betbot.score;

public class Tip {
	
	private int scoreA;
	private int scoreB;
	private boolean valid;
	
	public Tip() {
		valid = false;
	}
	
	public Tip(int scoreA, int scoreB) {
		this(scoreA, scoreB, true);
	}
	
	public Tip(int scoreA, int scoreB, boolean valid) {
		this.scoreA = scoreA;
		this.scoreB = scoreB;
		this.valid = valid;
	}
	
	public int getScoreA() {
		return scoreA;
	}
	public void setScoreA(int scoreA) {
		this.scoreA = scoreA;
	}
	public int getScoreB() {
		return scoreB;
	}
	public void setScoreB(int scoreB) {
		this.scoreB = scoreB;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
}
