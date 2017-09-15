package simulation;

public class Score {

	private String event;
	private long optimalTime;
	private long yourTime;
	private int diff;
	private int score;
	private int scorePercentage;
	
	private final int REACTION_TIME = 3000; // ms

	public Score(String event, long optimalTime, long yourTime) {
		this.event = event;
		this.optimalTime = optimalTime;
		this.yourTime = yourTime;
		
		this.diff = (int) (yourTime - optimalTime);
		this.score = REACTION_TIME - diff;
		this.scorePercentage = score / REACTION_TIME;
	}

	public Score(String event, int score) {
		this.event = event;
		this.score = score;
	}
	
	public String getEvent() {
		return event;
	}

	public long getOptimalTime() {
		return optimalTime;
	}

	public void setOptimalTime(long optimalTime) {
		this.optimalTime = optimalTime;
	}

	public long getYourTime() {
		return yourTime;
	}

	public void setYourTime(long yourTime) {
		this.yourTime = yourTime;
	}

	public long getDiff() {
		return diff;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScorePercentage() {
		return scorePercentage;
	}

	public void setScorePercentage(int scorePercentage) {
		this.scorePercentage = scorePercentage;
	}

}
