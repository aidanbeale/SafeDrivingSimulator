package simulation;

import java.util.Random;
import java.util.TimerTask;

public class EventHandler {

	private EventTimer timer;
	private boolean timerStarted = false;// Crash occurs if cars are 480 apart
	private boolean timerStopped = false;
	private long timerStartedTime;
	private long timerStoppedTime;
	private Random rand = new Random();

	public EventHandler() {
	}

	public void startEventTimer() {
		timerStarted = true;
		timer = new EventTimer();
		timerStartedTime = timer.startTimer();
	}

	public void stopEventTimer() {
		timerStoppedTime = timer.stopTimer();
	}

	public void startCrashEvent(Car aiCar1, Car aiCar2) {
		// In a crash event the car in front slows down and forces the user to apply the
		// brakes
		// If the user does not apply the brakes in time a crash occurs and the test
		// subject fails.

		final int minSpeed = 30;
		final int maxSpeed = 40;

		// aiCar1 needs to slow down randomly
		aiCar1.setSpeed(rand.nextInt(maxSpeed - minSpeed) + minSpeed);
		System.out.println("aiCar1 speed: " + aiCar1.getSpeed());

		// aiCar2 needs to be equal to or faster than aiCar1 or they will crash
		aiCar2.setSpeed(rand.nextInt(maxSpeed - aiCar1.getSpeed()) + minSpeed);
		System.out.println("aiCar2 speed: " + aiCar2.getSpeed());
	}

	public void startSpeedingEvent(Car userCar) {
		// In a speeding event, the cars speed will begin fluctuating more than normal
		// and rise above the 40km/h
		// limit. The user will need to apply the brakes to slow the car down and end
		// the event.

		userCar.setCarSpeedLimit(userCar.getCarSpeedLimit() + 1);
		System.out.println("userCar speed: " + userCar.getSpeed());
	}

	public int startGivewayEvent(Car userCar) {
		int posOfGiveway = userCar.getxPos() - 10000;
		return posOfGiveway;
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
