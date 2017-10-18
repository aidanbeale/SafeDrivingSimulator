/**
 * Copyright 2017 John Humphrys, Aidan Beale
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
 * The EventTimer classes manages getting the stop and start times of events
 *
 * @author John Humphrys
 *
 */
public class EventTimer {

    private long start;
    private long end;

    /**
     * Starts the timer
     *
     * @return The millis time the timer was started at
     */
    public long startTimer() {
        start = System.currentTimeMillis();
        return start;
    }

    /**
     * Stops the timer
     *
     * @return The millis time the timer was stopped at
     */
    public long stopTimer() {
        end = System.currentTimeMillis();
        return end;
    }

    /**
     * Calculates the reaction time
     *
     * @return the reaction time as an int
     */
    public int reactionTime() {
        return ((int) (end - start));
    }
}
