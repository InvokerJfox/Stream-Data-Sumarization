package cn.jfoxx.summarization.standalone.entity;

public class SumZ_Att {
	public int id;
	public int power;

	SumZ_Att(int _id) {
		id = _id;
	}

	public SumZ_Att(int _id, int value) {
		id = _id;
		power = value;
	}
}
