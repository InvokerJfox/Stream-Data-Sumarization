package Stream_HierarchyStream_Step;

public class HS_Object {
	public int oid;// id of this object
	public int[] A;// information of this object
	public int[] U_f;// update this layer's U_f
	public long rt;// running time

	/*
	 * @param _u:no need update ,set this null
	 */
	HS_Object(int _id, int[] _a, int[] _u, long _rt) {
		oid = _id;
		A = _a;
		U_f = _u;
		rt = _rt;
	}
}
