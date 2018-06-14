package score;

import com.betbot.main.Main;
import com.betbot.wm.Match;
import com.betbot.wm.MatchEvent;

public class ScoreManager {
	public void init(){
		Main.getWMManager().addMatchEvent(new MatchEvent(){

			@Override
			public void PreMatch(Match match) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void MatchStarted(Match match) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void MatchFinished(Match match) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
