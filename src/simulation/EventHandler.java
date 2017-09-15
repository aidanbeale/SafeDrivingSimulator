package simulation;

import java.util.TimerTask;

public class EventHandler {

	private boolean speedingEvent;
	private boolean crashEvent; // Crash occurs if cars are 480 apart
	private boolean childEvent;
	private EventTimer timer;
	private boolean timerStarted = false;
	private boolean timerStopped = false;

	public EventHandler(boolean speedingEvent, boolean crashEvent, boolean childEvent) {
		super();
		this.speedingEvent = speedingEvent;
		this.crashEvent = crashEvent;
		this.childEvent = childEvent;
	}

	public void startCrashEvent() {
		timerStarted = true;
		timer = new EventTimer();
		timer.startTimer();
	}
	
	public void stopCrashEvent() {
		timer.stopTimer();
	}

	public void startSpeedingEvent() {

	}

	public void startChildEvent() {

	}

	public int calculateScore() {
		// Max score is 3000
		// Min score is 0
		
		if (timer.reactionTime() > 0) {
			return 3000 - timer.reactionTime();
		} else {
			return 0;
		}
	}

	public boolean getTimerStarted() {
		return timerStarted;
	}

	public boolean isTimerStopped() {
		return timerStopped;
	}

	public void setTimerStarted(boolean timerStarted) {
		this.timerStarted = timerStarted;
	}

	public void setTimerStopped(boolean timerStopped) {
		this.timerStopped = timerStopped;
	}
	
	
}
