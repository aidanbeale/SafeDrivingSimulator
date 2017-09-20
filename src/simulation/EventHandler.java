 *
 *   Copyright 2017 John Humphrys
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package simulation;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

/**
 * Used to handle different events occuring
 * 
 * @author John Humphrys
 *
 */
public class EventHandler {

	private EventTimer timer;
	private boolean timerStarted = false;
	private boolean timerStopped = false;
	private long timerStartedTime;
	private long timerStoppedTime;
	private Random rand = new Random();
	private ArrayList<Integer> givewayLocations = new ArrayList<>();

	public EventHandler() {
	}

	/**
	 * Starts the timer when the event begins
	 */
	public void startEventTimer() {
		timerStarted = true;
		timer = new EventTimer();
		timerStartedTime = timer.startTimer();
	}

	/**
	 * Stops the timer when required
	 */
	public void stopEventTimer() {
		timerStoppedTime = timer.stopTimer();
	}

	/**
	 * Manages a crash event that occurs
	 * 
	 * @param aiCar1 Car1 that is in front of the userCar
	 * @param aiCar2 Car2 that is in front of the userCar
	 */
	public void startCrashEvent(Car aiCar1, Car aiCar2) {
		// In a crash event the car in front slows down and forces the user to apply the
		// brakes
		// If the user does not apply the brakes in time a crash occurs and the test
		// subject fails.

		final int minSpeed = 30;
		final int maxSpeed = 40;

		// aiCar1 needs to slow down randomly
		aiCar1.setCarSpeedLimit((rand.nextInt(maxSpeed - minSpeed) + minSpeed));
		System.out.println("aiCar1 speed: " + aiCar1.getSpeed());

		// aiCar2 needs to be equal to or faster than aiCar1 on average or they will crash
		aiCar2.setCarSpeedLimit((rand.nextInt(maxSpeed - minSpeed) + minSpeed + 1));
		System.out.println("aiCar2 speed: " + aiCar2.getSpeed());
	}

	/**
	 * Manages a speeding event
	 * 
	 * @param userCar The car that is speeding
	 */
	public void startSpeedingEvent(Car userCar) {
		// In a speeding event, the cars speed will begin fluctuating more than normal
		// and rise above the 40km/h
		// limit. The user will need to apply the brakes to slow the car down and end
		// the event.

		userCar.setCarSpeedLimit(userCar.getCarSpeedLimit() + 1);
		System.out.println("userCar speed: " + userCar.getSpeed());
	}

	/*
	 * Hazard removed on request
	 */
	public int startGivewayEvent(Car userCar) {
		int posOfGiveway = userCar.getxPos() - 10000;
		return posOfGiveway;
	}
	
	/**
	 * Hazard removed on request
	 * 
	 * @param loc
	 */
	public void addGivewayLocation(int loc) {
		givewayLocations.add(loc);
	}

	/**
	 * Hazard removed on request
	 * 
	 * @return
	 */
	public int getClosestGivewayLoc() {
		int j = -1000000;
		for (Integer i : givewayLocations) {
			if (i > j) {
				j = i;
			}
		}
		return j;
	}
	
	/**
	 * Hazard removed on request
	 * 
	 * @return
	 */
	public ArrayList<Integer> getGivewayLocations() {
		return givewayLocations;
	}

	/**
	 * Used to calculate the score
	 * 
	 * @return The total score
	 */
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
