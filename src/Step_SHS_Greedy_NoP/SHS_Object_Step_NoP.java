package Step_SHS_Greedy_NoP;

public class SHS_Object_Step_NoP {

	public int oid;// id of this object
	public int[] A;// information of this object
	public int[] U_f;// update this layer's U_f
	public long rt;// running time

	/*
	 * @param _u:no need update ,set this null
	 */
	SHS_Object_Step_NoP(int _id, int[] _a, int[] _u, long _rt) {
		oid = _id;
		A = _a;
		U_f = _u;
		rt = _rt;
	}

}
