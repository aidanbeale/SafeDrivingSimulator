package simulation;

public class EventTimer {
	
	private long start;
	private long end;

	public void startTimer() {
		start = System.currentTimeMillis();
		System.out.println("start: " + start);
	}

	public void stopTimer() {
		end = System.currentTimeMillis();
		System.out.println("end: " + end);
	}

	public int reactionTime() {
		System.out.println((int) (end - start));
		return ((int) (end - start));
	}
}
