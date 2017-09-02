package Stream_Sieve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import SumZ.Common;

public class SS_Main_Step1 {
	public static int k;// size of sumZ
	public static double mg;// current max gain
	public static int d;// the demension of sumZ
	public static int maxI;// i:current max (1+e)^i
	public static double e;// e:(1+e)^i
	public static double mr;// m<(1+e)^i<mr*k*m
	public static ArrayList<SS_Siever_Step> sievers;// sievers list
	public static int maxSiverid;// the beast siver

	public static double[] p;// power

	static String path = "E://SumZ_Output//Sievers//";
	public static int id = 0;// sievers'id
	public static long tt;//temp time,when write in file ,stop count
	public static long st;//start time
	public static int ssid = 0;// sievers start id

	/*
	 * initial the args
	 * 
	 * @param m:current max gain
	 * 
	 * @parm st:start time
	 */
	public static void Initial(double _mg, double _e, double _mr, int _k,
			int _d, int[] o, long _st) throws IOException {
		Common.delAllFile(path);
		
		e = _e;
		mr = _mr;
		k = _k;
		mg = _mg;
		d = _d;

		p = Common.IntArrToDouArr(o);
		// initial have not add a new siever
		maxI = (int) Math.floor(Math.log10(_mg) / Math.log10(1 + e)) - 1;

		sievers = new ArrayList<SS_Siever_Step>();

		tt=_st;
		st=_st;
		Disperse(0);
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
	public static void Disperse(int oid) throws IOException {

		// delete low bound
		try {
			while (sievers.size() > 0)
				if (sievers.get(0).tGain < mg) {
					// delete the siever's file
					File f = new File(path + sievers.get(0).id + ".txt");
					if (f.exists()) {
						f.delete();
						//System.out.println("A Sieve Deleted!");
						sievers.remove(0);
						ssid++;
					} else {
						break;
					}

				} else
					break;// lowest > m,all > m

		} catch (Exception e) {
		} finally {
		}

		// New (1+e)^i
		int i = maxI + 1;//
		for (; Math.pow(1 + e, i) < mr * k * mg; i++) {
			double tGain = Math.pow(1 + e, i);
			// Add a new SS_Siever_NoPower
			SS_Siever_Step s = new SS_Siever_Step(id, tGain);
			sievers.add(s);

			// args
			String tpath = path + id + ".txt";
			StringBuffer sb = new StringBuffer();// Data

			// save this siever to file
			// save information format
			// tGain:3000
			// startid:65
			// starttime:3321

			sb.append(tGain + "\r\n");
			sb.append(oid + "\r\n");
			sb.append(Common.StartTick() - st);

			Common.FileOutput(tpath, sb);

			//System.out.println("Sieve " + id + ": Build! tGain:"+tGain);
			id++;
		}
		maxI = i;
		
		st=Common.StartTick();//reset time recorder
	}

	/*
	 * deal with next object
	 * 
	 * @param oid:the object's id
	 * 
	 * @param o:the object information
	 * 
	 * @param st :start time
	 */
	public static void DealNext(int oid, int[] o) throws IOException {
		// max Gain Changed?
		double tmg = Common.GOO(o);
		if (tmg > mg) {
			mg = tmg;
			Disperse(oid);
		}
	}

	/*
	 * deal with next object
	 * 
	 * @param oid:the object's id
	 * 
	 * @param o:the object information
	 * 
	 * @param st :start time
	 */
	public static void DealNextWithP(int oid, int[] o) throws IOException {
		// Update Pwer
		p = Common.AddPower(p, o);

		// max Gain Changed?
		double tmg = Common.GOO(o, p);
		if (tmg > mg) {
			mg = tmg;
			Disperse(oid);
		}
	}
}
