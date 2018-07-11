package cn.jfoxx.summarization.standalone.entity;

import java.util.ArrayList;

public class SumZ {
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
     * return top K sumZs
     */
    public ArrayList<SumZ_Object> TopK(int k) {
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

    public String toString(String c) {
        String r = "";
        for (SumZ_Object value : s) {
            r += value.id + c;
        }
        return r.substring(0, r.length() - c.length());
    }
}
