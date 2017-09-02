package SumZ;

import java.io.IOException;
import java.util.ArrayList;

import Stream_Sieve.SS_Main_Step1;
import Stream_Sieve.SS_Main_Step2;
import Stream_Sieve.SS_Main_Step3;

public class SumZ_SS_Step {
	static int d = 50000; // total dimension
	static int no = 2000;// 2000;// number of objects
	static String path = "E://SumZ_Output//Data.txt";
	static int k = 20;//
	static double mr=3;//sieve k*m*mr; deal the delete problem

	static ArrayList<Double> Cov = new ArrayList<Double>();

	public static long st;// start time

	public static void main(String[] args) throws InterruptedException,
			IOException {
		// GetFIS();//Generate FIS

		// Load Data
		ArrayList<int[]> os = Data.LoadData.LoadDataByLines(path, no, d);

		// 1.NoPower Comparison
		double[] p = Common.SPW01(os);// it's generate data cost.because
		// some attribute is never disappear

		// Sieve Stream
		st = Common.StartTick();
		SieveStream(os, false, p);

	}

	/*
	 * Stream Deal - SieveStream algorithm
	 * 
	 * @param os: Data
	 * 
	 * @param power:Does calc with power?
	 * 
	 * @param p:power
	 */
	public static void SieveStream(ArrayList<int[]> os, boolean IsPower,
			double[] p) throws IOException {
		// result args

		// initial the first data
		int[] o = os.get(0);

		double mg = Common.GOO(o);// maxGain
		SS_Main_Step1.Initial(mg, 0.1, mr, k, d, o, st);// object =power

		// 1.Generte Sievers
		if (IsPower) {
			// stream deal
			for (int i = 0; i < os.size(); i++) {
				SS_Main_Step1.DealNextWithP(i, os.get(i));
			}
		} else {
			// stream deal
			for (int i = 0; i < os.size(); i++) {
				SS_Main_Step1.DealNext(i, os.get(i));
			}

		}

		// 2.Per Sievers deal Data
		SS_Main_Step2.SieversDeal(os, SS_Main_Step1.ssid, k, d);

		// 3.Get Max SumZ
		SS_Main_Step3.GetMaxSieve(SS_Main_Step1.ssid);

		// Show Result
		if (IsPower) {
			// Update the power
			p = Common.GetPower(os);
		}

		// coverage
		System.out.print("SieveStream(Parallel) algorithm Coverage : "
				+ Math.round(SS_Main_Step3.maxgain / Common.GOO(p) * 100)
				+ "%\nthe SumZ is : "
				+ Common.IntegerArrToString(SS_Main_Step3.maxSieversids, " / ")
				+ "\nParallel : " + SS_Main_Step3.sc + "\nRunning time:"
				+ SS_Main_Step3.maxrunningtime / 1000.0 + "s\n");

		// Save Result
		Cov.add(SS_Main_Step3.maxgain / Common.GOO(p));
	}
}
