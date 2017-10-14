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

package controller;

import simulation.Score;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The File Utility class is used to write results to a .xml file
 *
 * @author John Humphrys
 *
 */
public class FileUtility {

    /**
     * Method writes the results to file
     *
     * @param scoreList
     *            The arraylist of Score from the results scene
     * @return A boolean if the results were written to file
     */
    public boolean writeBookings(ArrayList<Score> scoreList) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter("SDS_results.xml");
        } catch (IOException e1) {
            System.out.println("Unable to write to file");
            e1.printStackTrace();
        }

        // Format time to include milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSSS");

        try {
            fileWriter.append("<results>");
            // Write to file
            for (Score s : scoreList) {
                fileWriter.append("<score>");
                if (s.getEvent().equals("Failed Braking Attempt")) {
                    fileWriter.append("<event>" + s.getEvent().toString() + "</event>" + "<optimalTime>" + "N/A" + "</optimalTime>" + "<yourTime>" + "N/A" + "</yourTime>" + "<difference>" + "N/A" + "</difference>"
                            + "<score>" + String.valueOf(s.getScore()).toString() + "</score>" + "<scorePercentage>" + "N/A" +  "</scorePercentage>" + "\n");
                } else {
                    fileWriter.append("<event>" + s.getEvent() + "</event>" + "<optimalTime>" + sdf.format(s.getOptimalTime()) + "</optimalTime>"
                            + "<yourTime>" + sdf.format(s.getYourTime()) + "</yourTime>" + "<difference>" + String.valueOf(s.getDiff())  + "ms" + "</difference>"
                            + "<score>" + String.valueOf(s.getScore()) + "</score>" + "<scorePercentage>" + String.valueOf(s.getScorePercentage()) + "%" + "</scorePercentage>" + "\n");
                }
                fileWriter.append("</score>");
            }

            fileWriter.append("</results>");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to close file writer connection");
            return false;
        }
        return true;

    }
}
