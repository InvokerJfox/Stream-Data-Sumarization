package Stream_HierarchyStream;

import java.util.ArrayList;

import Common.*;

public class HS_Main {

	public static int k;// size of sumZ
	public static int lk;// size of per layer
	public static ArrayList<HS_Layers> layers;

	static double e;// min sieve gain,if f(o)<e,delete o;

	public static SumZ_AttList p;// power

	public static ArrayList<SumZ_Object> Data;

	public static sumZ sumZ;// sumZ

	/*
	 * initial the args
	 * 
	 * @param m:current max gain
	 */
	public static void initial(ArrayList<SumZ_Object> os, int _k, int _lk,
			SumZ_AttList _p, double _e) {

		Data = os;
		k = _k;
		lk = _lk;
		e = _e;
		p = _p;

		sumZ = new sumZ();

	}

	/*
	 * Deal all data in each layer
	 */
	public static void DealAll() {
		// 1.Load data from os,and deal in the 1st layer
		layers = new ArrayList<HS_Layers>();
		HS_Layers l = new HS_Layers(layers.size(), null, p, lk, e);
		l.UpdateU_f(p);
		layers.add(l);
		int lid;// now ,dealing level;s id;
		SumZ_AttList lf;// now,dealing level's l_f

		// Deal All Data
		for (int i = 0; i < Data.size(); i++) {
			// System.out.println("dealing : " + i);
			// waiting for deal Object
			SumZ_Object o = Data.get(i);

			// reset
			lid = 0;
			lf = null;

			// Deal in Hierarchy Model until end
			while (o != null) {
				if (lid > k * 2) {
					// System.out.println("Layers more than " + k * 2);
					break;// max level count
				}
				// Get Dealing Level
				if (lid < layers.size()) {
					l = layers.get(lid);
					// Update layer if lf!=null
					if (lf != null)
						l.UpdateU_f(lf);
				} else {
					// System.out.println("add new layer!");
					// new level
					l = new HS_Layers(layers.size(), null, p, lk, e);
					l.UpdateU_f(layers.get(lid - 1).L_f);
					layers.add(l);
					continue;
				}

				// Deal Data
				switch (l.DealOne(o)) {
				case 1:
					o = null;
					break;
				case 2:
					o = l.lower_Data;
					lf = l.L_f;
					lid++;// next level
					break;
				case 3:
					o = l.lower_Data;
					lid++;// next level
					break;
				default:
					System.out.println("deal Error");
				}
			}
		}
	}

	/*
	 * Get SumZ
	 */
	public static void GetSumZ() {

		int i = 0;// level id ,from 0
		// add sumZ until |S|=k
		while (sumZ.Size() < k) {
			HS_Layers l = layers.get(i);
			if (l != null) {
				if (l.sumZ.Size() != 0) {
					// no full until k
					sumZ.Add(l.sumZ.GetTopSumz(k - sumZ.Size()));
				}
				i++;// next level
			} else {
				System.out.println("Layers out of size!");
			}
		}
	}

	/*
	 * Get SumZ feathers by SumZ
	 */
	public static SumZ_AttList GetSumZf() {
		GetSumZ();
		return Gain.GetFeather(sumZ);
	}

	/*
	 * show top k layers
	 */
	public static void printTopLayers(int k) {
		for (int i = 0; i < k; i++) {
			HS_Layers l = layers.get(i);
			// layer information
			System.out.println("Layer " + i + " : ");

			if (l.sumZ.Size() > 0) {
				// objects' ids
				System.out.println("Objects : "
						+ Convert.sumZids(l.sumZ, " / "));
			} else {
				System.out.println("Objects : [empty] ");
			}
		}
	}
}
