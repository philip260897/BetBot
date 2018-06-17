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
	
	private int index;
	
	public Match(String teamA, String teamB, MatchStatus status, int scoreA, int scoreB, Date time, int index) {
		
		this.teamA = teamA;
		this.teamB = teamB;
		
		this.scoreA = scoreA;
		this.scoreB = scoreB;
		
		this.status = status;
		this.time = time;
		this.index = index;
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
	
	public int getIndex() {
		return index;
	}
	
	public void setScoreA(int score) {
		scoreA = score;
	}
	
	public void setMatchStatus(MatchStatus status) {
		this.status =status;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Match) {
			Match other = (Match)o;
			if(this.teamA == other.teamA && this.teamB == other.teamB && this.time.compareTo(other.getTime()) == 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "["+teamA+":"+scoreA+" - "+teamB+":"+scoreB+"] "+Utils.getFormated(time, "dd-MM-yy HH:mm:ss") + " "+status;
	}
}
