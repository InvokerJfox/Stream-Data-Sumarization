package StandardGreedy;

import java.util.ArrayList;

import SumZ.Common;

public class Greedy {

	public static ArrayList<Integer> s_ids;// the ids of sumZ
	public static ArrayList<int[]> sumZ;// the sumZ
	public static int d;// Dimension

	/*
	 * SumZ by Standard Greedy_Power algorithm
	 * 
	 * @param Data
	 * 
	 * @param k size of SumZ
	 * 
	 * @return : the sumZ
	 */
	public static ArrayList<int[]> SumZ(ArrayList<int[]> Data, int k, int _d) {

		// global args
		s_ids = new ArrayList<Integer>();// sumZ'ids Result
		sumZ = new ArrayList<int[]>();// sumZ Result
		d = _d;
		// int[] f = new int[Data[0].length];// the feathers had been included
		// in

		// the size of SumZ is k
		for (int j = 0; j < k; j++) {
			// per round
			// per round args
			int mid = -1;// the id of max gain per round
			double mg = 0;// the max gain per round

			for (int i = 0; i < Data.size(); i++) {
				// the gain of this object
				double og = Common.GOOoutF(Data.get(i),
						Common.GetFeather(sumZ, d));
				if (og > mg) {
					mg = og;
					mid = i;
				}
			}

			// save this round result
			if (mid == -1)
				return sumZ;// all feathers included
			s_ids.add(mid);
			sumZ.add(Data.get(mid));
			// f = Common.UpdateFeather(f, Data[mid]);
		}

		return sumZ;
	}

	/*
	 * SumZ by Standard Greedy_Power algorithm
	 * 
	 * @param Data
	 * 
	 * @param k size of SumZ
	 * 
	 * @return : the sumZ
	 */
	public static ArrayList<int[]> SumZWithP(ArrayList<int[]> Data, int k,
			int _d, double[] p) {

		// global args
		s_ids = new ArrayList<Integer>();// sumZ'ids Result
		sumZ = new ArrayList<int[]>();// sumZ Result
		d = _d;
		// int[] f = new int[Data[0].length];// the feathers had been included
		// in

		// the size of SumZ is k
		for (int j = 0; j < k; j++) {
			// per round
			// per round args
			int mid = -1;// the id of max gain per round
			double mg = 0;// the max gain per round

			for (int i = 0; i < Data.size(); i++) {
				// the gain of this object
				double og = Common.GOOoutF(Data.get(i),
						Common.GetFeather(sumZ, d),p);
				if (og > mg) {
					mg = og;
					mid = i;
				}
			}

			// save this round result
			if (mid == -1)
				return sumZ;// all feathers included
			s_ids.add(mid);
			sumZ.add(Data.get(mid));
			// f = Common.UpdateFeather(f, Data[mid]);
		}

		return sumZ;
	}

}
