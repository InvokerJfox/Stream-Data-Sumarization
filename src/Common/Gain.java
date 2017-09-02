package Common;

import java.util.ArrayList;

public class Gain {

	/*
	 * Return the value of f[id] return :0 means no Existf this attribute
	 */
	public static int Getf(int id, SumZ_AttList f) {
		for (int i = 0; i < f.Size(); i++) {
			if (f.Get(i).id == id) {
				return f.Get(i).power;
			}
		}
		return 0;
	}

	/*
	 * add the value of f[id] if it Existf
	 */
	public static boolean Addf(int id, SumZ_AttList f, int value) {
		for (int i = 0; i < f.Size(); i++) {
			if (f.Get(i).id == id) {
				f.Get(i).power += value;
				return true;
			}
		}
		return false;
	}

	/*
	 * Is Exits id(index) in SumZ_AttList(f)
	 */
	public static boolean Existf(int id, SumZ_AttList f) {
		for (int i = 0; i < f.Size(); i++) {
			if (f.Get(i).id == id)
				return true;
		}
		return false;
	}

	/*
	 * Gain of power
	 */
	public static double GOO(SumZ_AttList p) {
		double g = 0;
		for (int i = 0; i < p.Size(); i++) {
			g += p.Get(i).power;
		}
		return g;
	}

	/*
	 * Gain of a object without power
	 */
	public static double GOO(SumZ_Object o) {
		return o.Size();
	}

	/*
	 * Gain Of a Object with Power
	 */
	public static double GOO(SumZ_Object o, SumZ_AttList p) {
		double g = 0;
		for (int i = 0; i < o.Size(); i++) {
			g += Getf(o.Get(i).id, p);
		}
		return g;
	}

	/*
	 * Gain Of a Feather with Power
	 */
	public static double GOO(SumZ_AttList f, SumZ_AttList p) {
		double g = 0;
		for (int i = 0; i < f.Size(); i++) {
			g += Getf(f.Get(i).id, p);
		}
		return g;
	}

	/*
	 * Gain of a Object without Power & without Feather // if f[i]=1 ,s+=0;
	 */
	public static double GOOoutF(SumZ_Object o, SumZ_AttList f) {
		double g = 0;
		for (int i = 0; i < o.Size(); i++) {
			g += (-1) * (Getf(o.Get(i).id, f) - 1);
		}
		return g;
	}

	/*
	 * Gain of a Object without Power & without Feather // if f[i]=1 ,s+=0;
	 */
	public static double GOOoutF(SumZ_AttList o, SumZ_AttList f) {
		double g = 0;
		for (int i = 0; i < o.Size(); i++) {
			g += (-1) * (Getf(o.Get(i).id, f) - 1);
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
	public static double GOOoutF(SumZ_Object o, SumZ_AttList f, SumZ_AttList p) {
		double g = 0;
		// include by f
		for (int i = 0; i < o.Size(); i++) {
			int aid = o.Get(i).id;
			g += Getf(aid, p) * (-1) * (Getf(aid, f) - 1);
		}
		return g;
	}

	/*
	 * Gain of a Object within Feather //f[i]=1;s+=1
	 */
	public static double GOOinF(SumZ_Object o, SumZ_AttList f) {
		double g = 0;
		for (int i = 0; i < o.Size(); i++) {
			g += Getf(o.Get(i).id, f);
		}

		return g;
	}
	
	/*
	 * Gain of a Object within Feather //f[i]=1;s+=1
	 */
	public static double GOOinF(SumZ_AttList o, SumZ_AttList f) {
		double g = 0;
		for (int i = 0; i < o.Size(); i++) {
			g += Getf(o.Get(i).id, f);
		}

		return g;
	}
	
	/*
	 * Gain of a Object within Feather //f[i]=1;s+=1
	 */
	public static double GOOinF(SumZ_Object o, SumZ_AttList f, SumZ_AttList p) {
		double g = 0;
		for (int i = 0; i < o.Size(); i++) {
			int aid = o.Get(i).id;
			g += Getf(aid, p) * Getf(aid, f);
		}

		return g;
	}
	
	/*
	 * Gain of SumZ/objects with power :full atts
	 * 
	 * @param s:SumZ/objects
	 * 
	 * @param p:power
	 */
	public static double GOS(sumZ s, SumZ_AttList p) {

		// get all feather included by s
		SumZ_AttList f = GetFeather(s);

		return GOO(f, p);
	}
	
	/*
	 * 
	 */
	public static double GOSinF(sumZ s, SumZ_AttList f) {

		// get all feather included by s
		SumZ_AttList ss = GetFeather(s);

		return GOOinF(ss, f);
	}
	
	/*
	 * Get all feather that included by given Data
	 * 
	 * @param data
	 */
	public static SumZ_AttList GetFeather(sumZ data) {
		SumZ_AttList f = new SumZ_AttList();

		// Per Object
		for (int i = 0; i < data.Size(); i++) {
			// Per Attribute
			for (int j = 0; j < data.Get(i).Size(); j++) {
				// Is exits in f
				if (!Existf(data.Get(i).Get(j).id, f)) {
					f.Add(data.Get(i).Get(j));
				}
			}
		}

		return f;
	}

	/*
	 * the gain of f(Si) - f(Sj) with power
	 */
	public static double dFs(sumZ si, sumZ sj, SumZ_AttList p) {
		return GOO(GetFeather(si), p) - GOO(GetFeather(sj), p);
	}

	/*
	 * the gain of f(Si) - f(Sj) without power
	 */
	public static double dFs(sumZ si, sumZ sj) {
		return GOO(GetFeather(si)) - GOO(GetFeather(sj));
	}

	/*
	 * Update Feather by a new object e.g. 0000 0011 + 1100 0001 =1100 0011
	 * 
	 * @param old :feather
	 * 
	 * @param o :new object
	 */
	public static SumZ_AttList AddFeatherByObject(SumZ_AttList f, SumZ_Object o) {
		// Per Object.A
		for (int i = 0; i < o.Size(); i++) {
			// Is exits in f
			if (!Existf(o.Get(i).id, f)) {
				f.Add(o.Get(i));
			}
		}
		return f;
	}

	/*
	 * Get the left feather of u .0011 1100 - 0000 1111 = 0000 1100 :full atts
	 * 
	 * @param u: old feather
	 * 
	 * @param g:grand feaher
	 */
	public static SumZ_AttList FeatherWithinF(SumZ_AttList useful,
			SumZ_AttList grand) {
		SumZ_AttList n = new SumZ_AttList();

		for (int i = 0; i < useful.Size(); i++) {
			if (Existf(useful.Get(i).id, grand)) {
				n.Add(useful.Get(i));
			}
		}
		return n;
	}

	/*
	 * Get the left feather of u .0011 1100 - 0000 1111 = 0011 0000 :full atts
	 * 
	 * @param u: old feather
	 * 
	 * @param g:grand feaher
	 */
	public static SumZ_AttList FeatherWithoutF(SumZ_AttList useful,
			SumZ_AttList grand) {
		SumZ_AttList n = new SumZ_AttList();

		for (int i = 0; i < useful.Size(); i++) {
			if (!Existf(useful.Get(i).id, grand)) {
				n.Add(useful.Get(i));
			}
		}
		return n;
	}

	/*
	 * Calculate the coverage of SumZ :full atts
	 * 
	 * @param f :the feather of SumZ
	 * 
	 * @param p :power,If calculate without power,this set int[] with 1 or 0
	 */
	public static double Coverage(SumZ_AttList s, SumZ_AttList p) {
		double sumS = Gain.GOO(s);
		double sumAll = Gain.GOO(p);// the sum of all objects' features

		return Math.round((double) sumS / sumAll * 10000) / 100.0;
	}

	/*
	 * Get Power By Count :full atts
	 * 
	 * @param os Data
	 */
	public static SumZ_AttList GetPower(ArrayList<SumZ_Object> data) {
		SumZ_AttList p = new SumZ_AttList();

		// Per Object
		for (int i = 0; i < data.size(); i++) {
			// Per Attributes
			for (int j = 0; j < data.get(i).Size(); j++) {
				if (!Existf(data.get(i).Get(j).id, p))
					p.Add(new SumZ_Att(data.get(i).Get(j).id, 1));
				else
					Addf(data.get(i).Get(j).id, p, 1);
			}
		}

		return p;
	}

	/*
	 * Set Power with 0(never disappear)/1(disappear) :full atts
	 * 
	 * @param os:Data
	 * 
	 * @param _d:dimension
	 */
	public static SumZ_AttList GetPower01(ArrayList<SumZ_Object> data) {
		SumZ_AttList p = new SumZ_AttList();
		// initial power

		// Per Object
		for (int i = 0; i < data.size(); i++) {
			// Per Attributes
			for (int j = 0; j < data.get(i).Size(); j++) {
				if (!Existf(data.get(i).Get(j).id, p))
					p.Add(new SumZ_Att(data.get(i).Get(j).id, 1));
			}
		}
		return p;
	}
}
