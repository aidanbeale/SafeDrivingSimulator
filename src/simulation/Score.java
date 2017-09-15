package simulation;

public class Score {

	private int totalScore = 0;
	private int crashScore = 0;
	private int childScore = 0;
	private int speedingScore = 0;
	private int lostScore = 0;

	private void calculateTotalScore() {
		totalScore = crashScore + childScore + speedingScore - lostScore;
	}

	public int getTotalScore() {
		calculateTotalScore();
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getCrashScore() {
		return crashScore;
	}

	public void setCrashScore(int crashScore) {
		this.crashScore = crashScore;
	}

	public int getChildScore() {
		return childScore;
	}

	public void setChildScore(int childScore) {
		this.childScore = childScore;
	}

	public int getSpeedingScore() {
		return speedingScore;
	}

	public void setSpeedingScore(int speedingScore) {
		this.speedingScore = speedingScore;
	}

	public int getLostScore() {
		return lostScore;
	}

	public void setLostScore(int lostScore) {
		this.lostScore = lostScore;
	}

}
