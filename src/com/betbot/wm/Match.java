package com.betbot.wm;

import java.util.Date;

import com.betbot.main.Utils;

public class Match 
{
	private MatchStatus status;
	private Date time;
	
	private String teamA;
	private String teamB;
	
	private int scoreA;
	private int scoreB;
	
	public Match(String teamA, String teamB, MatchStatus status, int scoreA, int scoreB, Date time) {
		
		this.teamA = teamA;
		this.teamB = teamB;
		
		this.scoreA = scoreA;
		this.scoreB = scoreB;
		
		this.status = status;
		this.time = time;
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
	
	@Override
	public String toString() {
		return "["+teamA+":"+scoreA+" - "+teamB+":"+scoreB+"] "+Utils.getFormated(time, "dd-MM-yy HH:mm:ss") + " "+status;
	}
}
