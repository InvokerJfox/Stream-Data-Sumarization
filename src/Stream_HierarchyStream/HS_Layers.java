package Stream_HierarchyStream;

import java.util.ArrayList;

import Common.*;

public class HS_Layers {

	public int id;// layer's id
	public int k;// size of sumZ
	public int lk;// size of sumZ in this layer lk < k
	SumZ_AttList p;// power
	double e;// min sieve gain,if f(o)<e,delete o;

	// the SumZ;
	sumZ sumZ = new sumZ();
	// upper level's sumZ
	sumZ upper_sumZ;

	// the usefully feathers of this layer,
	// 1:useful e.g. 0000 1111 :in
	SumZ_AttList U_f = new SumZ_AttList();

	// the included feathers of this layer,
	// 1:useful e.g. 0000 0011 : out
	SumZ_AttList G_f = new SumZ_AttList();

	// the left feathers of this layer,
	// 1:useful e.g. 0000 1100: in
	SumZ_AttList L_f = new SumZ_AttList();

	// ---------------before deal---------------------
	// waiting for deal Data
	ArrayList<SumZ_Object> wait_Data = new ArrayList<SumZ_Object>();
	boolean IsWait = false;// Is there wait U_F
	// waiting for deal U_f
	SumZ_AttList wait_U_f;

	// ----------------after deal---------------------
	// Data for Upper Level
	SumZ_Object upper_Data;
	// Data for Lower Level
	SumZ_Object lower_Data;

	/*
	 * initial this Layer
	 * 
	 * @param _id:this layer'id
	 */
	public HS_Layers(int _id, sumZ _upper_sumZ, SumZ_AttList _p, int _lk,
			double _e) {
		id = _id;
		upper_sumZ = _upper_sumZ;
		lk = _lk;
		p = _p;
		e = _e;
	}

	/*
	 * Load upper level's sumZ,and update this level's sumZ
	 */
	public void UpdateUpperSumZ(sumZ upper) {
		upper_sumZ = upper;
		UpdateU_f(Gain.GetFeather(upper_sumZ));
	}

	/*
	 * update u_f
	 */
	public void UpdateU_f(SumZ_AttList _u_f) {
		U_f = _u_f;
		L_f = Gain.FeatherWithoutF(U_f, G_f);
	}

	/*
	 * 
	 */
	public void LoadData(ArrayList<SumZ_Object> os) {
		wait_Data = os;
	}

	/*
	 * deal a new Object
	 * 
	 * @Param o:object
	 * 
	 * @return : 1:throw; 2:keep;--Give L_F(sumZ),and Give Left(or replaced) to
	 * Lower Level 3:unDeal;--Give to Lower Level 4:?;--Give to Upper Level
	 */
	public int DealOne(SumZ_Object o) {
		double gi = Gain.GOOinF(o, U_f);// Gain of this object
		// System.out.println(o.id+" in layer:"+id+" Gain:"+gi);
		// 1.if the gain is 0 - throw
		if (gi < e) {
			// System.out.println("Sieve!");
			return 1;
		}
		if (sumZ.Size() < lk) {// sumZ is not full
			SumZadd(o);
			// System.out.println("Add!");
			return 2;
		} else {
			// 1.choose the best result replace one object in sumZ
			int swapid = SwapEachOne(o);

			// 2.swap the object in sumZ,
			if (swapid != -1) {
				lower_Data = replace(swapid, o);
				// System.out.println("Replace!");
				return 2;
			}
			lower_Data = o;
			// System.out.println("Next!");
			return 3;
		}
	}

	/*
	 * Swap Each One object in sumZ with object(o) choose the max gain of swap
	 * result
	 * 
	 * @param o :new object
	 * 
	 * @return : beat replace object index.if no will return -1;
	 */
	private int SwapEachOne(SumZ_Object o) {

		// the gain of sumZ
		double maxg = Gain.GOSinF(sumZ, U_f);
		int maxid = -1;
		// traversal all sumZ
		for (int i = 0; i < lk; i++) {
			sumZ tsumZ = new sumZ();
			// replace i==j
			for (int j = 0; j < lk; j++) {
				if (i == j)
					tsumZ.Add(o);
				else
					tsumZ.Add(sumZ.Get(j));
			}

			double g = Gain.GOSinF(tsumZ, U_f);
			// System.out.println(g + "/" + maxg);
			if (g > maxg) {
				maxg = g;
				maxid = i;
			}

		}

		return maxid;
	}

	/*
	 * replace the object(id) in sumZ with object(oid)
	 * 
	 * @param id: the index of object in sumZ
	 * 
	 * @param oid: new object's true id.
	 * 
	 * @param o:the infomation of new object
	 */
	public SumZ_Object replace(int id, SumZ_Object o) {
		SumZ_Object rm = sumZ.Remove(id);
		sumZ.Add(o);
		G_f = Gain.GetFeather(sumZ);
		L_f = Gain.FeatherWithoutF(U_f, G_f);
		return rm;
	}

	/*
	 * sumZ add a new object and update G_f..
	 * 
	 * @param o:Object
	 */
	public void SumZadd(SumZ_Object o) {
		sumZ.Add(o);
		G_f = Gain.AddFeatherByObject(G_f, o);
		L_f = Gain.FeatherWithoutF(U_f, G_f);
	}

}
