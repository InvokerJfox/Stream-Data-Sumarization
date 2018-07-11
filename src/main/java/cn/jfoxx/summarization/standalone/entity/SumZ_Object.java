package cn.jfoxx.summarization.standalone.entity;

import java.util.ArrayList;

/*
 * Object:
 * A:only add include attributes
 */
public class SumZ_Object {
	public int id;// id of this object
	public ArrayList<SumZ_Att> A = new ArrayList<SumZ_Att>();// information of
																// this object
	public long rt;// running time

	/*
	 * @param _id :id
	 * 
	 * @param _a:information
	 * 
	 * @param _u:no need update ,set this null
	 */
	public SumZ_Object(int _id) {
		id = _id;
	}

	SumZ_Object(int _id, int[] _a) {
		id = _id;
		Add(_a);
	}

	public SumZ_Att Get(int index) {
		return A.get(index);
	}

	/*
	 * add a attribute with id
	 */
	public void Add(SumZ_Att a) {
		A.add(a);
	}

	public SumZ_Att Remove(int index) {
		return A.remove(index);
	}

	public int Size() {
		return A.size();
	}

	/*
	 * input a list of attributes ,but add only with a[i]=1
	 */
	public void Add(int[] _a) {
		for (int i = 0; i < _a.length; i++) {
			if (_a[i] != 0) {
				SumZ_Att a = new SumZ_Att(i);
				A.add(a);
			}
		}
	}
}
