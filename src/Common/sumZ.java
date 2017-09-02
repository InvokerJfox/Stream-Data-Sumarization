package Common;

import java.util.ArrayList;

public class sumZ {
	public ArrayList<SumZ_Object> s = new ArrayList<SumZ_Object>();

	public SumZ_Object Get(int index) {
		return s.get(index);
	}

	public void Add(SumZ_Object o) {
		s.add(o);
	}

	public void Add(ArrayList<SumZ_Object> o) {
		s.addAll(o);
	}

	public SumZ_Object Remove(int index) {
		return s.remove(index);
	}

	public int Size() {
		return s.size();
	}

	/*
	 * return top n sumZs
	 */
	public ArrayList<SumZ_Object> GetTopSumz(int k) {
		if (k > s.size())
			return s;
		else {
			ArrayList<SumZ_Object> n = new ArrayList<SumZ_Object>();
			for (int i = 0; i < k; i++) {
				n.add(s.get(i));
			}
			return n;
		}
	}
}
