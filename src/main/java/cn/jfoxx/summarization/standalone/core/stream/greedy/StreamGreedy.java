package cn.jfoxx.summarization.standalone.core.stream.greedy;

import cn.jfoxx.summarization.standalone.gain.Calculator;

import java.util.ArrayList;

public class StreamGreedy {
	public static int k;// size of sumZ
	public static int d;// dimension
	public static ArrayList<Integer> s_ids;// sumZ'ids list
	public static ArrayList<int[]> sumZ;// sumZ's object information

	public static double[] p;// power of dataset

	/*
	 * initial the args and give the first k objects
	 * 
	 * @_k:k
	 * 
	 * @firstk : the first k objects
	 */
	public static void Initial(int _k, int _d, ArrayList<int[]> firstk) {
		k = _k;
		d = _d;
		sumZ = firstk;
		s_ids = new ArrayList<Integer>();
		int i = 0;
		while (i < k)
			s_ids.add(i++);
	}

	/*
	 * initial the args and give the first k objects
	 * 
	 * @_k:k
	 * 
	 * @firstk : the first k objects
	 */
	public static void Initial(int _k, int _d, ArrayList<int[]> firstk,
			double[] _p) {
		k = _k;
		d = _d;
		sumZ = firstk;
		s_ids = new ArrayList<Integer>();
		int i = 0;
		p = _p;
		while (i < k)
			s_ids.add(i++);
	}

	/*
	 * (Per)SumZ deal with a new Object
	 * 
	 * @param oid:if the object's gain is enough,we will save the id of this
	 * object
	 * 
	 * @param o:info of object(o)
	 */
	public static void ChooseData(int oid, int[] o) {
		// 1.choose the best result replace one object in sumZ
		int swapid = SwapEachOne(o);

		// 2.swap the object in sumZ,
		if (swapid != -1) {
			replace(swapid, oid, o);
		}
	}

	public static void ChooseDataWithP(int oid, int[] o) {
		// 1.Update Power
		p = Calculator.AddPower(p, o);

		// 2.choose the best result replace one object in sumZ
		int swapid = SwapEachOneWithP(o);

		// 3.swap the object in sumZ,
		if (swapid != -1) {
			replace(swapid, oid, o);
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
	private static int SwapEachOne(int[] o) {

		double maxg = Calculator.GOO(Calculator.GetFeather(sumZ, d));
		int maxid = -1;

		// traversal all sumZ
		for (int i = 0; i < k; i++) {
			ArrayList<int[]> tsumZ = new ArrayList<int[]>();
			for (int j = 0; j < k; j++) {
				if (i != j)
					tsumZ.add(sumZ.get(j));
				else
					tsumZ.add(o);
			}

			double g = Calculator.GOO(Calculator.GetFeather(tsumZ, d));
			if (g > maxg) {
				maxg = g;
				maxid = i;
			}

		}

		return maxid;
	}

	/*
	 * Swap Each One object in sumZ with object(o) choose the max gain of swap
	 * result
	 * 
	 * @param o :new object
	 * 
	 * @return : beat replace object index.if no will return -1;
	 */
	private static int SwapEachOneWithP(int[] o) {

		double maxg = Calculator.GOS(sumZ, d, p);// the gain of sumZ
		int maxid = -1;

		// traversal all sumZ
		for (int i = 0; i < k; i++) {
			ArrayList<int[]> tsumZ = new ArrayList<int[]>();
			for (int j = 0; j < k; j++) {
				if (i != j)
					tsumZ.add(sumZ.get(j));
				else
					tsumZ.add(o);
			}

			double g = Calculator.GOS(tsumZ, d, p);
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
	public static void replace(int id, int oid, int[] o) {
		s_ids.remove(id);
		s_ids.add(oid);
		sumZ.remove(id);
		sumZ.add(o);
	}

}
