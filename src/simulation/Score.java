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

		if ((REACTION_TIME - diff) > 0) {
			this.score = REACTION_TIME - diff;
			this.scorePercentage = score / REACTION_TIME;
		} else {
			this.score = 0;
			this.scorePercentage = 0;
		}

	}

	public Score(String event, int score) {
		this.event = event;
		this.score = score;
	}

	public String getEvent() {
		if (event.equals("crashevent")) {
			return "Crash Event";
		} else if (event.equals("failedAttempt")) {
			return "Failed Braking Attempt";
		}
		return null;
	}

	public long getOptimalTime() {
		if (event.equals("failedAttempt")) {
			return -1;
		} else {
			return optimalTime;
		}
	}

	public void setOptimalTime(long optimalTime) {
		this.optimalTime = optimalTime;
	}

	public long getYourTime() {
		if (event.equals("failedAttempt")) {
			return -1;
		} else {
			return yourTime;
		}

	}

	public void setYourTime(long yourTime) {
		this.yourTime = yourTime;
	}

	public long getDiff() {
		if (event.equals("failedAttempt")) {
			return -1;
		} else {
			return diff;
		}

	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScorePercentage() {
		if (event.equals("failedAttempt")) {
			return -1;
		} else {
			return scorePercentage;
		}
	}

	public void setScorePercentage(int scorePercentage) {
		this.scorePercentage = scorePercentage;
	}

}
