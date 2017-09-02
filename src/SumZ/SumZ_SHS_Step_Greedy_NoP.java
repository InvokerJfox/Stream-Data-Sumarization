package SumZ;

import java.io.IOException;
import java.util.ArrayList;

import Step_SHS_Greedy_NoP.SHS_Main_Step_NoP;

public class SumZ_SHS_Step_Greedy_NoP {
	static int d = 50000; // total dimension
	static int no = 2000;// 2000;// number of objects
	static String path = "E://SumZ_Output//Data.txt";
	static int k = 20;//
	static int lk = 1;// the size of per layer
	static double e = 1;// min sieve gain,if f(o)<e,delete o;
	static double cmk=10000;

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
		Common.StartTick();
		StreamHierarchySieve(os, p);
	}

	/*
	 * Stream Deal - Stream Hierarchy Sieve algorithm
	 * 
	 * @param os: Data
	 * 
	 * @param p:power
	 * 
	 * @param IsGreedy: Greedy or Sieve method
	 */
	private static void StreamHierarchySieve(ArrayList<int[]> os, double[] p)
			throws IOException {
		// 1.Initial Layer & Deal Data
		SHS_Main_Step_NoP.initial(os, cmk, k, lk, d, p, e);
		SHS_Main_Step_NoP.Deal();
		
		// 2.output result
		int[] s_f = SHS_Main_Step_NoP.GetSumZf();

		// coverage
		System.out.print("StreamHierarchySieve(Parallel Greedy)"
				+ " algorithm Coverage : " + Common.Coverage(s_f, p)
				+ "% \nthe SumZ is : "
				+ Common.IntegerArrToString(SHS_Main_Step_NoP.s_ids, " / ")
				+ "\n");

		// Save Result
		// Cov.add(Common.Coverage(s_f, p));

		// Show Layers
		// SHS_Layers.printTopLayers(SHS_Layers.sievers.size());
	}
}
