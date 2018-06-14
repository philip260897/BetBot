package com.betbot.wm;

import java.util.Date;

public class Match 
{
	private MatchStatus status;
	private Date time;
	
	private String group;
	
	private String teamA;
	private String teamB;
	
	private int scoreA;
	private int scoreB;
	
	public Match(String group, String teamA, String teamB, MatchStatus status, int scoreA, int scoreB, Date time) {
		this.group = group;
		
		this.teamA = teamA;
		this.teamB = teamB;
		
		this.scoreA = scoreA;
		this.scoreB = scoreB;
		
		this.status = status;
		this.time = time;
	}

	
	
	public String getGroup() {
		return group;
	}



	public MatchStatus getStatus() {
		return status;
	}

	public Date getTime() {
		return time;
	}

	public String getTeamA() {
		return teamA;
	}

	public String getTeamB() {
		return teamB;
	}

	public int getScoreA() {
		return scoreA;
	}

	public int getScoreB() {
		return scoreB;
	}
}
