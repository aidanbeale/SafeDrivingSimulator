package simulation;

public class EventTimer {

	private long start;
	private long end;

	public long startTimer() {
		start = System.currentTimeMillis();
		return start;
	}

	public long stopTimer() {
		end = System.currentTimeMillis();
		return end;
	}

	public int reactionTime() {
		return ((int) (end - start));
	}
}
