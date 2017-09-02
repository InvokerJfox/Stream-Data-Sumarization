package Stream_Sieve;

import java.util.ArrayList;

import SumZ.Common;

public class SS_Siever {
	public static int k;// max size of the sumZ

	public ArrayList<Integer> s_ids;// the sumZ'id of this SumZ
	public ArrayList<int[]> sumZ;// the sumZ of this SumZ

	public int[] f;// the feather had been included in sumZ :out

	public double Gain;// the gain of this SumZ
	public double tGain;// target Gain

	/*
	 * initial a new siever
	 * 
	 * @param _k:the size of sumZ
	 * 
	 * @param _tGain:the target gain of this Siever
	 * 
	 * @param d:the demension of this sumZ
	 */
	public SS_Siever(int _k, double _tGain, int d) {
		k = _k;
		tGain = _tGain;
		Gain = 0;
		f = new int[d];
		s_ids = new ArrayList<Integer>();
		sumZ = new ArrayList<int[]>();

	}

	/*
	 * (Per)SumZ deal with a new Object
	 * 
	 * @param oid:if the object's gain is enough,we will save the id of this
	 * object
	 */
	public void SieveData(int oid, int[] o) {
		// full!
		int c = sumZ.size();
		if (c == k) {
			return;
		}
		// low bound of Gain
		double low = (tGain - Gain) / (k - c);
		double og = Common.GOOoutF(o, f);
		if (og >= low) {// supply the demand £¬insert all information
			this.Gain += og;
			f = Common.AddFeatherByObject(f, o);
			s_ids.add(oid);
			sumZ.add(o);
			c++;
			// System.out.println(oid + " keeped");
		}
	}

	/*
	 * (Per)SumZ deal with a new Object
	 * 
	 * @param oid:if the object's gain is enough,we will save the id of this
	 * object
	 */
	public void SieveData(int oid, int[] o, double[] p) {
		// full!
		int c = sumZ.size();
		if (c == k) {
			return;
		}
		// low bound of Gain
		double low = (tGain - Gain) / (k - c);
		double og = Common.GOOoutF(o, f, p);
		if (og >= low) {// supply the demand £¬insert all information
			this.Gain += og;
			f = Common.AddFeatherByObject(f, o);
			s_ids.add(oid);
			sumZ.add(o);
			c++;
			// System.out.println(oid + " keeped");
		}
	}
}
