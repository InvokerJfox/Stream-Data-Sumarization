package cn.jfoxx.summarization.standalone.core.stream.sieve.step;

import cn.jfoxx.util.get.GArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StreamSieverStep3 {
	public static int k;// size of sumZ
	public static int d;// the demension of sumZ

	public static ArrayList<StreamSieveStepObject> sievers = new ArrayList<StreamSieveStepObject>();// sievers
	// list
	public static ArrayList<Integer> maxSieversids;
	public static long maxrunningtime = 0;
	public static double maxgain = 0;
	public static int sc = 0;// the number of sievers

	public static double[] p;// power

	static String path = "E://SumZ_Output//Sieve_Result//";
	public static int id = 0;// sievers' start id

	public static void LoadSieves(int ssid) {
		while (loadPerResult(ssid++))
			// load successful
			sc++;
	}

	public static boolean loadPerResult(int id) {
		// saved information format
		// Gain:3000.0
		// sids:134/321/62
		// totaltime:24123

		File file = new File(path + id + ".txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String ts;

			// first line
			ts = reader.readLine();
			double gain = Double.valueOf(ts);
			// second line
			ts = reader.readLine();
			ArrayList<Integer> sids = GArrayList.fromString(ts, "/");
			// last line
			ts = reader.readLine();
			long rt = Integer.valueOf(ts);

			// save
			StreamSieveStepObject s = new StreamSieveStepObject(gain, rt, sids);
			sievers.add(s);

			reader.close();

			return true;
		} catch (IOException e) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return false;

	}

	/*
	 * get the index in Sievers of max SumZ
	 */
	public static void GetMaxSieve(int ssid) {
		// load result
		LoadSieves(ssid);

		// get max
		ArrayList<StreamSieveStepObject> sf = sievers;
		for (StreamSieveStepObject s : sf) {
			if (s.Gain > maxgain) {
				maxgain = s.Gain;
				maxSieversids = s.s_ids;
			}
			if (s.runningTime > maxrunningtime) {
				maxrunningtime = s.runningTime;
			}
		}
	}

}
