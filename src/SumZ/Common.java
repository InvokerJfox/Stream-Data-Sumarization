package SumZ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Step_SHS_Greedy_NoP.SHS_Object_Step_NoP;
import Stream_HierarchyStream_Step.HS_Object;

public class Common {

	static long st = 0;

	/*
	 * Gain of power
	 */
	public static double GOO(double[] p) {
		double g = 0;
		for (int i = 0; i < p.length; i++) {
			g += p[i];
		}
		return g;
	}

	/*
	 * Gain of a object without power
	 */
	public static double GOO(int[] o) {
		double g = 0;
		for (int i = 0; i < o.length; i++) {
			g += o[i];
		}
		return g;
	}

	/*
	 * Gain Of a Object with Power
	 */
	public static double GOO(int[] o, double[] p) {
		double g = 0;
		for (int i = 0; i < p.length; i++) {
			g += o[i] * p[i];
		}
		return g;
	}

	/*
	 * Gain of a Object without Power & without Feather // if f[i]=1 ,s+=0;
	 */
	public static double GOOoutF(int[] o, int[] f) {
		double g = 0;
		for (int i = 0; i < o.length; i++) {
			g += o[i] * (-1) * (f[i] - 1);
		}

		return g;
	}

	/*
	 * Gain of a Object with Power & without Feather // if f[i]=1 ,s+=0;
	 * 
	 * @param o:object
	 * 
	 * @param f:feather
	 * 
	 * @param p:power
	 */
	public static double GOOoutF(int[] o, int[] f, double[] p) {
		double g = 0;
		// include by f
		for (int i = 0; i < o.length; i++) {
			g += o[i] * p[i] * (-1) * (f[i] - 1);
		}
		return g;
	}

	/*
	 * Gain of a Object within Feather //f[i]=1;s+=1
	 */
	public static double GOOinF(int[] o, int[] f) {
		double g = 0;
		for (int i = 0; i < o.length; i++) {
			g += o[i] * f[i];// if f[i]=1 ,s+=0;
		}

		return g;
	}

	/*
	 * Gain of a Object within Feather //f[i]=1;s+=1
	 */
	public static double GOOinF(int[] o, int[] f, double[] p) {
		double g = 0;
		for (int i = 0; i < o.length; i++) {
			g += o[i] * p[i] * f[i];// if f[i]=1 ,s+=0;
		}

		return g;
	}

	public static double GOOinF(SHS_Object_Step_NoP o, int[] f, double[] p) {
		double g = 0;
		for (int i = 0; i < f.length; i++) {
			g += o.A[i] * p[i] * f[i];// if f[i]=1 ,s+=0;
		}

		return g;
	}
	
	public static double GOOinF(HS_Object o, int[] f, double[] p){
		double g = 0;
		for (int i = 0; i < f.length; i++) {
			g += o.A[i] * p[i] * f[i];// if f[i]=1 ,s+=0;
		}

		return g;
	}

	/*
	 * Gain of SumZ/objects without power
	 * 
	 * @param s:SumZ/objects
	 * 
	 * @param d:default Feather size
	 */
	public static double GOS(ArrayList<int[]> s, int d) {

		// get all feather included by s
		int[] f = Common.GetFeather(s, d);

		return GOO(f);
	}
	
	/*
	 * 
	 */
	public static double GOSinF(int[] s, int[] f) {
		return GOOinF(s, f);
	}

	public static int[] getFeather_SHS(ArrayList<SHS_Object_Step_NoP> s, int d) {

		// get all feather included by s
		int[] f;
		if (s.size() > 0) {
			f = new int[s.get(0).A.length];
			for (int i = 0; i < f.length; i++) {
				for (int j = 0; j < s.size(); j++) {
					if (s.get(j).A[i] == 1) {
						f[i] = 1;
						break;
					}
				}
			}
		} else {
			f = new int[d];
		}

		return f;
	}
	
	public static int[] getFeather_HS(ArrayList<HS_Object> s, int d) {

		// get all feather included by s
		int[] f;
		if (s.size() > 0) {
			f = new int[s.get(0).A.length];
			for (int i = 0; i < f.length; i++) {
				for (int j = 0; j < s.size(); j++) {
					if (s.get(j).A[i] == 1) {
						f[i] = 1;
						break;
					}
				}
			}
		} else {
			f = new int[d];
		}

		return f;
	}
	
	/*
	 * Gain of SumZ/objects with power
	 * 
	 * @param s:SumZ/objects
	 * 
	 * @param p:power
	 */
	public static double GOS(ArrayList<int[]> s, int d, double[] p) {

		// get all feather included by s
		int[] f = Common.GetFeather(s, d);

		return GOO(f, p);
	}

	/*
	 * the gain of f(Si) - f(Sj) with power
	 */
	public static double dFs(ArrayList<int[]> si, ArrayList<int[]> sj,
			double[] p, int d) {
		return GOO(GetFeather(si, d), p) - GOO(GetFeather(sj, d), p);
	}

	/*
	 * the gain of f(Si) - f(Sj) without power
	 */
	public static double dFs(ArrayList<int[]> si, ArrayList<int[]> sj, int d) {
		return GOO(GetFeather(si, d)) - GOO(GetFeather(sj, d));
	}

	/*
	 * Update Feather by a new object e.g. 0000 0011 + 1100 0001 =1100 0011
	 * 
	 * @param old :feather
	 * 
	 * @param o :new object
	 */
	public static int[] AddFeatherByObject(int[] old, int[] o) {
		int[] n = new int[old.length];
		for (int i = 0; i < old.length; i++) {
			n[i] = old[i] | o[i];
		}
		return n;
	}

	/*
	 * Get the left feather of u .0011 1100 - 0000 1111 = 0000 1100
	 * 
	 * @param u: old feather
	 * 
	 * @param g:grand feaher
	 */
	public static int[] FeatherWithinF(int[] useful, int[] grand) {
		int[] n = new int[useful.length];
		for (int i = 0; i < useful.length; i++) {
			n[i] = useful[i] & grand[i];
		}
		return n;
	}

	/*
	 * Get the left feather of u .0011 1100 - 0000 1111 = 0000 1100
	 * 
	 * @param u: old feather
	 * 
	 * @param g:grand feaher
	 */
	public static int[] FeatherWithoutF(int[] useful, int[] grand) {
		int[] n = new int[useful.length];
		for (int i = 0; i < useful.length; i++) {
			if (useful[i] == 1 && grand[i] == 0)
				n[i] = 1;
		}
		return n;
	}

	/*
	 * Get Power By Count
	 * 
	 * @param os Data
	 */
	public static double[] GetPower(ArrayList<int[]> os) {
		double[] p = new double[os.get(0).length];
		for (int i = 0; i < os.size(); i++) {
			for (int j = 0; j < p.length; j++)
				p[j] += os.get(i)[j];
		}
		return p;

	}

	/*
	 * add the object to power
	 */
	public static double[] AddPower(double[] p, int[] o) {
		for (int i = 0; i < o.length; i++) {
			p[i] += o[i];
		}
		return p;
	}

	/*
	 * Set Power with 0(never disappear)/1(disappear)
	 * 
	 * @param os:Data
	 */
	public static double[] SPW01(ArrayList<int[]> os) {
		double[] p = new double[os.get(0).length];
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < os.size(); j++)
				if (os.get(j)[i] == 1) {
					p[i] = 1;
					break;
				}

		}

		return p;
	}

	/*
	 * Calculate the coverage of SumZ
	 * 
	 * @param f :the feather of SumZ
	 * 
	 * @param p :power,If calculate without power,this set int[] with 1 or 0
	 */
	public static double Coverage(int[] f, double[] p) {
		int sumS = 0;
		int sumAll = 0;// the sum of all objects' features
		for (int j = 0; j < f.length; j++)// % traversal all features
		{
			if (f[j] != 0)// feature is not 0
			{
				sumS += p[j];
			}
			sumAll += p[j];
		}

		return Math.round((double) sumS / sumAll * 10000) / 100.0;
	}

	/*
	 * Get all feather that included by given Data if the data is null ,please
	 * set a default size(d) of feather
	 * 
	 * @param data
	 */
	public static int[] GetFeather(ArrayList<int[]> data, int d) {
		int[] f;
		if (data.size() > 0) {
			f = new int[data.get(0).length];
			for (int i = 0; i < data.get(0).length; i++) {
				for (int j = 0; j < data.size(); j++) {
					if (data.get(j)[i] == 1) {
						f[i] = 1;
						break;
					}
				}
			}
		} else {
			f = new int[d];
		}

		return f;
	}

	/*
	 * Random a number satisfy Gaussian distribution
	 * 
	 * @param e:mathematical expectation
	 * 
	 * @param v:variance
	 */
	public static double RandomGaussian(double e, double v) {
		java.util.Random r = new java.util.Random();
		return Math.sqrt(v) * r.nextGaussian() + e;
	}

	/*
	 * Random a number satisfy 0-1 Gaussian distribution
	 * 
	 * @param v:variance 0.1 is not bad
	 */
	public static double RandomGaussian01(double v) {
		double g = Math
				.abs(Math.round(Common.RandomGaussian(0, v) * 10000) / 10000.0);
		while (g > 1) {
			g = Math.abs(Math.round(Common.RandomGaussian(0, v) * 10) / 10.0);
		}
		return g;
	}

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
	 * set start time
	 */
	public static long StartTick() {
		st = System.currentTimeMillis();// start time
		return st;
	}

	/*
	 * Show end time
	 */
	public static void EndTick(String info) {
		System.out.print(info + (System.currentTimeMillis() - st) / 1000f
				+ " s\n");
	}

	/*
	 * return a Int[] full of 1.
	 */
	public static int[] All1(int l) {
		int[] a = new int[l];
		for (int i = 0; i < l; i++) {
			a[i] = 1;
		}
		return a;
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
	 * Data saving
	 */
	public static void FileOutput(String path, StringBuffer sb)
			throws IOException {
		File file = new File(path);
		if (file.getParent() != null && !new File(file.getParent()).exists()) {
			new File(file.getParent()).mkdirs();
		}
		if (!file.exists())
			file.createNewFile();
		FileOutputStream out = new FileOutputStream(file, false);
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
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
			res[i] = Integer.valueOf(ts.charAt(i)-48);
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

	// 删除指定文件夹下所有文件
	// @param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 删除文件夹里面的文件
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取目录下所有文件(按时间排序)
	 * 
	 * @param path
	 * @return
	 */
	public static List<File> getFileSort(String path) {

		List<File> list = getFiles(path, new ArrayList<File>());

		if (list != null && list.size() > 0) {

			Collections.sort(list, new Comparator<File>() {
				public int compare(File file, File newFile) {
					if (file.lastModified() < newFile.lastModified()) {
						return 1;
					} else if (file.lastModified() == newFile.lastModified()) {
						return 0;
					} else {
						return -1;
					}

				}
			});

		}

		return list;
	}

	/**
	 * 
	 * 获取目录下所有文件
	 * 
	 * @param realpath
	 * @param files
	 * @return
	 */
	public static List<File> getFiles(String realpath, List<File> files) {

		File realFile = new File(realpath);
		if (realFile.isDirectory()) {
			File[] subfiles = realFile.listFiles();
			for (File file : subfiles) {
				if (file.isDirectory()) {
					getFiles(file.getAbsolutePath(), files);
				} else {
					files.add(file);
				}
			}
		}
		return files;
	}
}
