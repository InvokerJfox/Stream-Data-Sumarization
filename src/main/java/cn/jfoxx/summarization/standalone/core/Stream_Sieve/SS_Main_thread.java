package cn.jfoxx.summarization.standalone.core.Stream_Sieve;

import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.get.GDoubleMatrix;

import java.util.ArrayList;

public class SS_Main_thread {
	public static int k;// size of sumZ
	public static double mg;// current max gain
	public static int d;// the demension of sumZ
	public static int maxI;// i:current max (1+e)^i
	public static double e;// e:(1+e)^i
	public static double mr;// m<(1+e)^i<mr*k*m
	public static ArrayList<SS_Siever_thread> sievers;// sievers list
	public static int maxSiverid;// the beast siver

	public static double[] p;// power

	public static ArrayList<int[]> os;

	/*
	 * initial the args
	 * 
	 * @param m:current max gain
	 */
	public static void Initial(double _mg, double _e, double _mr, int _k,
			int _d, int[] o, ArrayList<int[]> _os) {
		e = _e;
		mr = _mr;
		k = _k;
		mg = _mg;
		d = _d;
		os = _os;

		p = GDoubleMatrix.fromIntergerMatrix(o);
		// initial have not add a new siever
		maxI = (int) Math.floor(Math.log10(_mg) / Math.log10(1 + e)) - 1;
		sievers = new ArrayList<SS_Siever_thread>();

		Disperse(0);
	}

	/*
	 * get the index in Sievers of max SumZ
	 * 
	 * @return if sievers is running ,return false
	 */
	public static void GetMaxSieveid() throws InterruptedException {
		double mGain = 0;
		ArrayList<SS_Siever_thread> sf = sievers;
		// check every siever is ending
		int ok = 0;
		while (ok < sf.size()) {
			ok = 0;
			for (int j = 0; j < sf.size(); j++) {
				SS_Siever_thread s = sf.get(j);
				if (s.isAlive()) {
					Thread.sleep(200);
					break;
				} else {
					ok++;
				}

			}
		}

		for (int j = 0; j < sf.size(); j++) {
			SS_Siever_thread s = sf.get(j);
			if (s.Gain > mGain) {
				mGain = s.Gain;
				maxSiverid = j;
			}
		}
	}

	/*
	 * get the max SumZ with GetMaxSieveid
	 */
	public static ArrayList<int[]> GetMaxSumZ() throws InterruptedException {
		GetMaxSieveid();
		return sievers.get(maxSiverid).sumZ;
	}

	/*
	 * get the max SumZ'ids without GetMaxSieveid
	 */
	public static ArrayList<Integer> GetMaxSumZid() throws InterruptedException {
		GetMaxSieveid();
		return sievers.get(maxSiverid).s_ids;
	}

	/*
	 * Get the max SumZ's feathers;
	 */
	public static int[] GetMaxSumZf() throws InterruptedException {
		GetMaxSieveid();
		return sievers.get(maxSiverid).f;
	}

	/*
	 * m<(1+e)^i<km
	 * 
	 * @ m:maxGain
	 * 
	 * @ e
	 * 
	 * @ k :size of sumZ
	 */
	public static void Disperse(int startid) {
		// delete low bound
		try {
			while (sievers.size() > 0)
				if (sievers.get(0).tGain < mg)
					sievers.remove(0);
				else
					break;// lowest > m,all > m

		} catch (Exception e) {
		} finally {
		}

		// New (1+e)^i
		int i = maxI + 1;//
		for (; Math.pow(1 + e, i) < mr * k * mg; i++) {
			double tGain = Math.pow(1 + e, i);
			// Add a new SS_Siever_NoPower
			SS_Siever_thread s = new SS_Siever_thread(k, tGain, d, os, startid);
			sievers.add(s);
		}
		maxI = i;
	}

	/*
	 * deal with next object
	 * 
	 * @param oid:the object's id
	 * 
	 * @param o:the object information
	 */
	public static void DealNext(int oid, int[] o) {
		// max Gain Changed?
		double tmg = Calculator.GOO(o);
		if (tmg > mg) {
			mg = tmg;
			Disperse(oid);
		}

		// all filter
		ArrayList<SS_Siever_thread> sf = sievers;
		for (int j = 0; j < sf.size(); j++) {
			sf.get(j).SieveData(oid, o);
		}
	}

	/*
	 * deal with next object
	 * 
	 * @param oid:the object's id
	 * 
	 * @param o:the object information
	 */
	public static void DealNextWithP(int oid, int[] o) {
		// Update Pwer
		p = Calculator.AddPower(p, o);

		// max Gain Changed?
		double tmg = Calculator.GOO(o, p);
		if (tmg > mg) {
			mg = tmg;
			Disperse(oid);
		}
		// all filter
		ArrayList<SS_Siever_thread> sf = sievers;
		for (int j = 0; j < sf.size(); j++) {
			sf.get(j).SieveData(oid, o, p);
		}
	}
}
