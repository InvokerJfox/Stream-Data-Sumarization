package Stream_HierarchySieve;

import java.util.ArrayList;

import SumZ.Common;

public class SHS_Layers {

	public static int k;// size of sumZ
	public static int lk;// size of per layer
	public static ArrayList<SHS_Siever> sievers;
	public static double cmk;// current max gain per object * k
	public static int d;// dimension

	public static double[] p;// power

	public static boolean IsGreedy;// greedy or sieve method
	public static boolean IsPower;// Update Power?

	public static ArrayList<int[]> sumZ;// sumZ
	public static ArrayList<Integer> s_ids;// sumZs' ids

	/*
	 * deal with next object
	 * 
	 * @param oid: object index
	 * 
	 * @param o:object
	 * 
	 * @param p:power
	 * 
	 * @param e:min sieve gain
	 */
	public static void DealNext(int oid, int[] o, int e) {
		// 1.Update attributes' weight
		if (IsPower)
			p = Common.AddPower(p, o);

		// 2.HotSpots Changed?
		// UpdateHotSpots()

		// 3.Update HierarchySieve Model
		// Update HierarchySieve()

		// 4.Insert A Object
		// false:sieve true:greedy
		SeiveThis(oid, o, e);
	}

	/*
	 * initial the args
	 * 
	 * @param m:current max gain
	 */
	public static void Initial(double _cm, int _k, int _lk, int _d,
			double[] _p, boolean _ispower, boolean _isgreedy) {
		k = _k;
		lk = _lk;
		d = _d;
		cmk = _cm;

		IsPower = _ispower;
		IsGreedy = _isgreedy;

		if (IsPower)
			p = new double[d];
		else
			p = _p;

		sievers = new ArrayList<SHS_Siever>();

		sumZ = new ArrayList<int[]>();
		s_ids = new ArrayList<Integer>();

	}

	/*
	 * Sieve the Next Object
	 * 
	 * @param o:Object
	 * 
	 * @param p:power
	 * 
	 * @param e:min sieve gain
	 */
	public static void SeiveThis(int oid, int[] o, int e) {

		int l = 0;// level id ,from 0
		double mg = cmk;// the up level gain;
		int[] f = Common.All1(p.length);

		// deal this object ,until gain <=e
		while (true) {
			if (l < sievers.size()) {
				// 1.get this layer's siever
				SHS_Siever s = sievers.get(l);
				// 2.Update this layer and Give the next layer waitData
				if (s.UpdateFLayer(IsGreedy)) {
					if (l + 1 < sievers.size()) {
						sievers.get(l + 1).T_AddWaitData(s.T_GetRMData());
						sievers.get(l + 1).T_AddWaitids(s.T_GetRMids());
					}
				}
				// 3.deal object & it's result greedy/sieve methods
				int result;
				if (IsGreedy) {
					result = s.deal_greedy(oid, o, p, e);
				} else {
					result = s.deal(oid, o, p, e);
				}
				if (result == 1)
					break;// sieved
				else if (result == 2) {// keep
					// calculate left_f for next layer
					f = s.GetLeftf();
					// if next layer isn't null, accept new U_f
					if (l + 1 < sievers.size())
						sievers.get(l + 1).T_WatiU_F(f);
					// deal successful
					// System.out.println(oid + " Done! in(Sieve) Layer :" + l);
					break;
				} else {// deal in next level
					mg = s.EG;
					// calculate left_f for next layer
					f = s.GetLeftf();
					l++;// next level
				}
			} else {
				if (l  > 100) {
					//System.out.println("layers more than 100");
					break;
				} else {
					// set a new siever, maxgain = (k-1) / k * gain
					mg = (double) 0.9 * mg;
					SHS_Siever n = new SHS_Siever(lk, mg, f, d);
					sievers.add(n);
					// System.out.println("Add a new layer:" + l);

					// again this new layer.
				}
			}
		}
	}

	/*
	 * update max gain of per object
	 */
	public static void NewMaxG(double _cm) {
		cmk = _cm * k;
	}

	/*
	 * Return Max SumZ & SumZs' ids
	 */
	public static void GenerateSumZ() {
		int l = 0;// level id ,from 0
		int c = 0;// current sumZ's size
		while (true) {// deal this object ,until gain is 0
			SHS_Siever s = sievers.get(l);
			if (s != null) {
				int sc = s.sumZ.size();
				if (sc == 0)
					;// this layer is empty;
				else if (c + sc <= k) {
					// no full until k
					sumZ.addAll(s.GetTopSumZ(sc));
					s_ids.addAll(s.GetTopSids(sc));
					c += s.sumZ.size();
				} else {
					sumZ.addAll(s.GetTopSumZ(k - c));
					s_ids.addAll(s.GetTopSids(k - c));
					return;
				}
				l++;// next level
			}
		}
	}

	/*
	 * Return sumZ with GenerateSumZ
	 */
	public static ArrayList<int[]> GetSumZ() {
		GenerateSumZ();
		return sumZ;
	}

	/*
	 * Return sumZ without GenerateSumZ
	 */
	public static ArrayList<Integer> GetSumZids() {
		return s_ids;
	}

	/*
	 * show top k layers
	 */
	public static void printTopLayers(int k) {
		for (int i = 0; i < k; i++) {
			SHS_Siever s = sievers.get(i);
			// layer information
			System.out.println("Layer " + i + " : " + " EG: " + s.EG + " G: "
					+ s.G);

			if (s.s_ids.size() > 0) {
				// objects' ids
				System.out.println("Objects : "
						+ Common.IntegerArrToString(
								s.GetTopSids(s.s_ids.size()), " / "));
			} else {
				System.out.println("Objects : [empty] ");
			}
		}
	}

}
