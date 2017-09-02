package Common;

import java.util.ArrayList;

/*
 * Power(p):
 * Atts:add all attributes,value = power;
 * 
 * Feather(f):
 * Atts:add all attributes,value = 0/1;
 */
public class SumZ_AttList {
	ArrayList<SumZ_Att> Atts = new ArrayList<SumZ_Att>();

	/*
	 * initial a all 1 Attributes
	 * 
	 * @param _d:size
	 * 
	 * @param value:default power
	 */
	public void initial(int _d, int value) {
		for (int i = 0; i < _d; i++) {
			SumZ_Att n = new SumZ_Att(i);
			n.power = value;
			Atts.add(n);
		}
	}
	
	public SumZ_Att Get(int index){
		return Atts.get(index);
	}
	
	public void Add(SumZ_Att a){
		Atts.add(a);
	}
	
	public SumZ_Att Remove(int index){
		return Atts.remove(index);
	}
	
	public int Size(){
		return Atts.size();
	}
}
