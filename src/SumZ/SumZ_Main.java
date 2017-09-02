package SumZ;

import java.io.IOException;
import java.util.ArrayList;

import Common.*;
import StandardGreedy.Greedy;
import Stream_Greedy.SG_Main;
import Stream_HierarchySieve.SHS_Layers;
import Stream_HierarchyStream.HS_Main;
import Stream_Sieve.SS_Main;

public class SumZ_Main {
	static int d = 644; // total dimension
	static int no = 10000;// 2000;// number of objects
	static String path = "E://SumZ_Output//2458285_644//NewCensus0.txt";
	static int k = 10;//
	static int lk = 1;// lk
	static int e = 1;// SHS min sieve gain
	static double mr = 2;// sieve k*m*mr; deal the delete problem
	static double cmk = 30;// SHS use it

	static ArrayList<Double> Cov = new ArrayList<Double>();

	public static void main(String[] args) throws InterruptedException,
			IOException {
		// GetFIS();//Generate FIS

		// Load Data
		ArrayList<int[]> os = Data.LoadData.LoadDataByLines(path, no, d);

		// 1.NoPower Comparison
		double[] p = Common.SPW01(os);// it's generate data cost.because
		// some attribute is never disappear

		// while (k++ < 20) {
		// lk = (int) Math.sqrt(k);

		// 1.1 Standard Greedy
		Standard_Greedy(os, false, p);

		// 1.2 Stream Greedy
		StreamGreedy(os, false, p);

		// 1.3 Sieve Stream
		SieveStream(os, false, p);

		// 1.4 HierarchySieve - Greedy
		StreamHierarchySieve(os, false, p, true);

		// 1.5 HierarchyStream
		ArrayList<SumZ_Object> data = Data.LoadData.LoadData_SumZ(path, no, d);
		SumZ_AttList np = Gain.GetPower01(data);
		HierarchyStream(data, np);

		// }

		// ShowResult();
	}

	private static void HierarchyStream(ArrayList<SumZ_Object> os,
			SumZ_AttList p) {
		HS_Main.initial(os, k, lk, p, e);
		HS_Main.DealAll();

		SumZ_AttList s_f = HS_Main.GetSumZf();

		// coverage
		System.out.print("HierarchyStream algorithm Coverage : "
				+ Gain.Coverage(s_f, p) + "% \nthe SumZ is : "
				+ Convert.sumZids(HS_Main.sumZ, " / ") + "\n");

		// Save Result
		Cov.add(Gain.Coverage(s_f, p));
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
			//System.out.println(i);
			SHS_Layers.DealNext(i, os.get(i), e);
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

	public static void ShowResult() {
		System.out.println(Common.DoubleArrToString(Cov, "\r"));
	}
}
