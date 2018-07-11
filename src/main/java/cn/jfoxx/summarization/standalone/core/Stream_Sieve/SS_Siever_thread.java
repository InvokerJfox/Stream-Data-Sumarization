package cn.jfoxx.summarization.standalone.core.Stream_Sieve;

import cn.jfoxx.summarization.standalone.gain.Calculator;

import java.util.ArrayList;

public class SS_Siever_thread extends Thread {

	public static int k;// max size of the sumZ

	public ArrayList<Integer> s_ids;// the sumZ'id of this SumZ
	public ArrayList<int[]> sumZ;// the sumZ of this SumZ

	public int[] f;// the feather had been included in sumZ :out

	public double Gain;// the gain of this SumZ
	public double tGain;// target Gain

	public ArrayList<int[]> os;// dependent deal Data
	public int startid;

	/*
	 * initial a new siever
	 * 
	 * @param _k:the size of sumZ
	 * 
	 * @param _tGain:the target gain of this Siever
	 * 
	 * @param d:the demension of this sumZ
	 */
	public SS_Siever_thread(int _k, double _tGain, int d, ArrayList<int[]> _os,
			int _startid) {
		k = _k;
		tGain = _tGain;
		Gain = 0;
		f = new int[d];
		s_ids = new ArrayList<Integer>();
		sumZ = new ArrayList<int[]>();

		os = _os;
		startid = _startid;

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
		double og = Calculator.GOOoutF(o, f);
		if (og >= low) {// supply the demand ��insert all information
			this.Gain += og;
			f = Calculator.AddFeatherByObject(f, o);
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
		double og = Calculator.GOOoutF(o, f, p);
		if (og >= low) {// supply the demand ��insert all information
			this.Gain += og;
			f = Calculator.AddFeatherByObject(f, o);
			s_ids.add(oid);
			sumZ.add(o);
			c++;
			// System.out.println(oid + " keeped");
		}
	}

	public void run() {
		// Uninterrupted deal left data
		for (int i = startid; !isInterrupted() && i < os.size(); i++) {
			SieveData(i, os.get(i));
		}
	}
}
