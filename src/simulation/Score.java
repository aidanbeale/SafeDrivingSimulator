/**
 * Copyright 2017 John Humphrys
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simulation;

/**
 * The Score model manages the scores
 *
 * @author John Humphrys
 *
 */
public class Score {

    private String event;
    private long optimalTime;
    private long yourTime;
    private int diff;
    private int score;
    private double scorePercentage;

    private final int REACTION_TIME = 3000; // ms

    /**
     * Calculates a score event from times
     *
     * @param event The event that took place
     * @param optimalTime Time the event started
     * @param yourTime Time the event stopped
     */
    public Score(String event, long optimalTime, long yourTime) {
        this.event = event;
        this.optimalTime = optimalTime;
        this.yourTime = yourTime;

        this.diff = (int) (yourTime - optimalTime);

        if ((REACTION_TIME - diff) > 0) {
            this.score = REACTION_TIME - diff;
            this.scorePercentage = (Double.valueOf(score) / Double.valueOf(REACTION_TIME)) * 100.0;
        } else {
            this.score = 0;
            this.scorePercentage = 0;
        }
    }

    /**
     * Constructor used when creating a negative
     *
     * @param event The event that took place
     * @param score The score awarded
     */
    public Score(String event, int score) {
        this.event = event;
        this.score = score;
    }

    /**
     * Returns the event as a proper string
     *
     * @return The evnet name
     */
    public String getEvent() {
        if (event.equals("crashevent")) {
            return "Crash Event";
        } else if (event.equals("failedAttempt")) {
            return "Failed Braking Attempt";
        } else if (event.equals("speedingEvent")) {
            return "Speeding Event";
        } else if (event.equals("givewayEvent")) {
            return "Giveway Event";
        }
        return null;
    }

    /**
     * Returns the optimal time
     *
     * @return The optimal time
     */
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

    public double getScorePercentage() {
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
