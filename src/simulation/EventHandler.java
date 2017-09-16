package simulation;

import java.util.TimerTask;

public class EventHandler {

	private EventTimer timer;
	private boolean timerStarted = false;// Crash occurs if cars are 480 apart
	private boolean timerStopped = false;
	private long timerStartedTime;
	private long timerStoppedTime;

	public EventHandler() {}

	public void startCrashEventTimer() {
		timerStarted = true;
		timer = new EventTimer();
		timerStartedTime = timer.startTimer();
	}
	
	public void stopCrashEventTimer() {
		timerStoppedTime = timer.stopTimer();
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
	
	public boolean getTimerStopped() {
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

	public EventTimer getTimer() {
		return timer;
	}

	public void setTimer(EventTimer timer) {
		this.timer = timer;
	}

	public long getTimerStartedTime() {
		return timerStartedTime;
	}

	public void setTimerStartedTime(long timerStartedTime) {
		this.timerStartedTime = timerStartedTime;
	}

	public long getTimerStoppedTime() {
		return timerStoppedTime;
	}

	public void setTimerStoppedTime(long timerStoppedTime) {
		this.timerStoppedTime = timerStoppedTime;
	}
	
}
