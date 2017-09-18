package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import simulation.Score;

public class FileUtility {

	public boolean writeBookings(ArrayList<Score> scoreList) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter("SDS_results.csv");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSSS");

		try {

			for (Score s : scoreList) {
				if (s.getEvent().equals("Failed Braking Attempt")) {
					fileWriter.append(s.getEvent().toString() + "," + "N/A" + "," + "N/A" + "," + "N/A" + ","
							+ String.valueOf(s.getScore()).toString() + "," + "N/A" + "\n");
				} else {
					fileWriter.append(s.getEvent() + "," + sdf.format(s.getOptimalTime()) + ","
							+ sdf.format(s.getYourTime()) + "," + String.valueOf(s.getDiff()) + "ms" + ","
							+ String.valueOf(s.getScore()) + "," + String.valueOf(s.getScorePercentage()) + "%"+ "\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
