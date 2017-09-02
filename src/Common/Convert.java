package Common;

import java.util.ArrayList;

public class Convert {
	/*
	 * Array display as string -- int[]
	 * 
	 * @param a:array
	 * 
	 * @param c:separtor char
	 */
	public static String IntegerArrToString(ArrayList<Integer> a, String c) {
		String res = "";
		for (int i = 0; i < a.size(); i++) {
			res += a.get(i) + c;
		}
		return res.substring(0, res.length() - c.length());
	}

	/*
	 * Array display as string -- double[]
	 * 
	 * @param a:array
	 * 
	 * @param c:separtor char
	 */
	public static String DoubleArrToString(ArrayList<Double> a, String c) {
		String res = "";
		for (int i = 0; i < a.size(); i++) {
			res += a.get(i) + c;
		}
		return res.substring(0, res.length() - c.length());
	}

	/*
	 * Int[] to double[]
	 */
	public static double[] IntArrToDouArr(int[] o) {
		double[] r = new double[o.length];
		for (int i = 0; i < o.length; i++) {
			r[i] = o[i];
		}
		return r;
	}

	/*
	 * "123/25/52" to ArrayList<Integer>:{123,25,52} split by "/"
	 */
	public static ArrayList<Integer> StringToIntArr(String ts, String split) {
		String[] r = ts.split(split);
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < r.length; i++) {
			res.add(Integer.valueOf(r[i]));
		}
		return res;
	}

	/*
	 * "01010101" to int[] "{0,1,0,0,0,1,0,1}
	 */
	public static int[] StringToIntArr(String ts) {
		int[] res = new int[ts.length()];
		for (int i = 0; i < ts.length(); i++) {
			res[i] = Integer.valueOf(ts.charAt(i) - 48);
		}
		return res;
	}

	public static String IntArrToString(int[] a) {
		String s = "";
		for (int i = 0; i < a.length; i++) {
			s += a[i];
		}
		return s;
	}

	public static String sumZids(sumZ s, String c) {
		String r = "";
		for (int i = 0; i < s.Size(); i++) {
			r += s.Get(i).id + c;
		}
		return r.substring(0, r.length() - c.length());
	}

}
