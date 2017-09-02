package SumZ;

import java.io.IOException;
import java.util.ArrayList;

import StandardGreedy.Greedy;
import Stream_Greedy.SG_Main;
import Stream_HierarchySieve.SHS_Layers;
import Stream_Sieve.SS_Main;
import Stream_Sieve.SS_Main_thread;

public class SumZ_Main_Old {

	static int d = 50000; // total dimension
	static int no = 2000;// 2000;// number of objects
	static String path = "E://SumZ_Output//Data.txt";
	static int k = 20;//
	static int lk=1;// lk
	static int e = (int) (d * 0.001);// SHS min sieve gain
	static double mr = 3;// sieve k*m*mr; deal the delete problem
	static double cmk = 10000;

	static ArrayList<Double> Cov = new ArrayList<Double>();

	public static void main(String[] args) throws InterruptedException,
			IOException {
		// GetFIS();//Generate FIS

		// Load Data
		ArrayList<int[]> os = Data.LoadData.LoadDataByLines(path, no, d);

		// 1.NoPower Comparison
		double[] p = Common.SPW01(os);// it's generate data cost.because
		// some attribute is never disappear

		//while (k++ < 20) {
			//lk = (int) Math.sqrt(k);

			// 1.1 Standard Greedy
			// Common.StartTick();
			Standard_Greedy(os, false, p);
			// Common.EndTick("runnning time:");

			// 1.2 Stream Greedy
			// Common.StartTick();
			StreamGreedy(os, false, p);
			// Common.EndTick("runnning time:");

			// 1.3 Sieve Stream
			// 1.3.1 No Thread
			Common.StartTick();
			SieveStream(os, false, p);
			Common.EndTick("");
			
			// 1.3.2 Thread
			// Common.StartTick();
			// SieveStream_thread(os, false, p);
			// Common.EndTick("runnning time:");

			// 1.3.3 parallel
			// SumZ_SS_Step.main(args);

			// 1.4 HierarchySieve - Sieve
			// Common.StartTick();
			// StreamHierarchySieve(os, false, p, false);
			// Common.EndTick("runnning time:");

			// 1.4 HierarchySieve - Greedy
			Common.StartTick();
			StreamHierarchySieve(os, false, p, true);
			Common.EndTick("runnning time:");

			// 2.WithPower Changed Comparison

			// 2.1 Standard Greedy
			// Standard_Greedy(os, true, null);

			// 2.2 Stream_Greedy
			// StreamGreedy(os, true, null);

			// 2.3 Stream_Sieve
			// SieveStream(os, true, null);

			// 2.4 Stream hierarchySieve -Sieve
			// StreamHierarchySieve(os, true, null, false);

			// 2.5 Stream hierarchySieve -Greedy
			// StreamHierarchySieve(os, true, null, true);

		//}

		//ShowResult();
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
	private static void StreamHierarchySieve(ArrayList<int[]> os,
			boolean IsPower, double[] p, boolean IsGreedy) {

		// double cm = Common.GOO(o);// maxGain
		SHS_Layers.Initial(cmk, k, lk, d, p, IsPower, IsGreedy);
		// stream deal
		for (int i = 0; i < os.size(); i++) {
			SHS_Layers.DealNext(i, os.get(i), e);
			// System.out.println(i);
		}

		// output result
		p = SHS_Layers.p;
		ArrayList<int[]> sumZ = SHS_Layers.GetSumZ();

		int[] s_f = Common.GetFeather(sumZ, d);

		// coverage
		String isgrd = "(Sieve)";
		if (IsGreedy)
			isgrd = "(Greedy)";
		System.out.print("StreamHierarchySieve" + isgrd
				+ " algorithm Coverage : " + Common.Coverage(s_f, p)
				+ "% \nthe SumZ is : "
				+ Common.IntegerArrToString(SHS_Layers.GetSumZids(), " / ")
				+ "\n");

		// Save Result
		Cov.add(Common.Coverage(s_f, p));

		// Show Layers
		// SHS_Layers.printTopLayers(SHS_Layers.sievers.size());
	}

	/*
	 * static Deal - Greedy_Power algorithm
	 * 
	 * @param os: Data
	 * 
	 * @param IsPower:Does calc with power? true:p=null
	 * 
	 * @param p:power
	 */
	public static void Standard_Greedy(ArrayList<int[]> os, boolean IsPower,
			double[] p) {
		// result args
		ArrayList<int[]> sumZ;
		int[] s_f;

		if (IsPower) {
			// Update the power
			p = Common.GetPower(os);

			// output result
			sumZ = Greedy.SumZWithP(os, k, d, p);
			s_f = Common.GetFeather(sumZ, d);

		} else {
			// calculate without power
			// output result
			sumZ = Greedy.SumZ(os, k, d);
			s_f = Common.GetFeather(sumZ, d);
		}
		// coverage
		System.out.print("Standard_Greedy algorithm Coverage : "
				+ Common.Coverage(s_f, p) + "% \nthe SumZ is : "
				+ Common.IntegerArrToString(Greedy.s_ids, " / ") + "\n");

		// Save Result
		Cov.add(Common.Coverage(s_f, p));
	}

	/*
	 * Stream Deal - StreamGreedy algorithm
	 * 
	 * @param os: Data
	 * 
	 * @param IsPower:Does calc with power?
	 * 
	 * @param p:power
	 */
	private static void StreamGreedy(ArrayList<int[]> os, boolean IsPower,
			double[] p) {
		// result args
		ArrayList<int[]> sumZ;
		int[] s_f;

		// initial the firstk data
		ArrayList<int[]> firstk = new ArrayList<int[]>();

		for (int i = 0; i < k; i++) {
			firstk.add(os.get(i));
		}

		if (IsPower) {
			// calculate the firstk power
			SG_Main.Initial(k, d, firstk, Common.GetPower(firstk));

			// stream deal
			for (int i = k; i < os.size(); i++) {
				SG_Main.ChooseDataWithP(i, os.get(i));
			}
		} else {
			SG_Main.Initial(k, d, firstk);

			// stream deal
			for (int i = k; i < os.size(); i++) {
				SG_Main.ChooseData(i, os.get(i));
			}
		}

		// output result
		sumZ = SG_Main.sumZ;

		s_f = Common.GetFeather(sumZ, d);

		if (IsPower)
			p = SG_Main.p;

		// coverage
		System.out.print("StreamGreedy algorithm Coverage : "
				+ Common.Coverage(s_f, p) + "% \nthe SumZ is : "
				+ Common.IntegerArrToString(SG_Main.s_ids, " / ") + "\n");
		// Save Result
		Cov.add(Common.Coverage(s_f, p));

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
			double[] p) {
		// result args
		int[] s_f;

		// initial the first data
		int[] o = os.get(0);

		double mg = Common.GOO(o);// maxGain
		SS_Main.Initial(mg, 0.1, mr, k, d, o);// object =power

		if (IsPower) {
			// stream deal
			for (int i = 0; i < os.size(); i++) {
				SS_Main.DealNextWithP(i, os.get(i));
			}
		} else {
			// stream deal
			for (int i = 0; i < os.size(); i++) {
				SS_Main.DealNext(i, os.get(i));
			}

		}

		// output result
		s_f = SS_Main.GetMaxSumZf();

		if (IsPower)
			p = SS_Main.p;

		// coverage
		System.out.print("SieveStream algorithm Coverage : "
				+ Common.Coverage(s_f, p) + "% \nthe SumZ is : "
				+ Common.IntegerArrToString(SS_Main.GetMaxSumZid(), " / ")
				+ "\n");

		// Save Result
		Cov.add(Common.Coverage(s_f, p));
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
	public static void SieveStream_thread(ArrayList<int[]> os, boolean IsPower,
			double[] p) throws InterruptedException {
		// result args
		int[] s_f;

		// initial the first data
		int[] o = os.get(0);

		double mg = Common.GOO(o);// maxGain
		SS_Main_thread.Initial(mg, 0.1, mr, k, d, o, os);// object =power

		if (IsPower) {
			// stream deal
			for (int i = 0; i < os.size(); i++) {
				SS_Main_thread.DealNextWithP(i, os.get(i));
			}
		} else {
			// stream deal
			for (int i = 0; i < os.size(); i++) {
				SS_Main_thread.DealNext(i, os.get(i));
			}

		}

		// output result
		s_f = SS_Main_thread.GetMaxSumZf();

		if (IsPower)
			p = SS_Main_thread.p;

		// coverage
		System.out.print("SieveStream(thread) algorithm Coverage : "
				+ Common.Coverage(s_f, p)
				+ "% \nthe SumZ is : "
				+ Common.IntegerArrToString(SS_Main_thread.GetMaxSumZid(),
						" / ") + "\n");

		// Save Result
		Cov.add(Common.Coverage(s_f, p));
	}

	public static void ShowResult() {
		System.out.println(Common.DoubleArrToString(Cov, "\r"));
	}
}
