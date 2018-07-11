package cn.jfoxx.summarization.standalone.core.Stream_HierarchySieve;

import java.util.ArrayList;

import cn.jfoxx.summarization.standalone.gain.Calculator;

public class SHS_Siever {

    int k;// the max size of objects in the layer
    int d;// Dimension
    double[] p;// power
    double e;// min gain

    // the ids of objects selected in this layer
    ArrayList<Integer> s_ids = new ArrayList<Integer>();
    // the detail infomation of SumZ;
    ArrayList<int[]> sumZ = new ArrayList<int[]>();

    int[] WaitU_F;// waiting for deal U_f
    ArrayList<int[]> WaitData = new ArrayList<int[]>();// waiting for deal Data
    ArrayList<Integer> Waitids = new ArrayList<Integer>();
    ;// waiting for deal
    // Datas' ids
    boolean IsWait = false;// Is there wait U_F

    ArrayList<int[]> rm_Data = new ArrayList<int[]>();// removed data
    ArrayList<Integer> rm_ids = new ArrayList<Integer>();// removed datas' ids

    double EG;// the expectation gain of this layer
    double G;// the real gain of this layer

    int[] U_f;// the usefully feathers of this layer,1:useful e.g. 0000 1111 :
    // in
    int[] G_f;// the included feathers of this layer,1:useful e.g. 0000 0011 :
    // out
    int[] L_f;// the left feathers of this layer,1:useful e.g. 0000 1100 : in

    /*
     * initial a new siever
     */
    public SHS_Siever(int _k, double _EG, int[] _U_f, int _d) {
        k = _k;
        d = _d;
        EG = _EG;
        G = 0;
        U_f = _U_f;
        G_f = new int[_d];
        L_f = U_f;
    }

    /*
     * deal a new Object
     *
     * @param oid:the id of this object
     *
     * @Param o:object
     *
     * @param p:power
     *
     * @param e:min gain
     *
     * @return 1:throw;2:keep;3:do it on next layer
     */
    public int deal(int oid, int[] o, double[] _p, double _e) {
        p = _p;
        double gi = Calculator.GOOinF(o, L_f, p);// Gain of this object
        double e = _e;

        // 1.if the gain is 0 - throw
        if (gi <= e) {
            return 1;
        }
        // 2.if the gain >= (EG-G)/(k-c) - keep
        else if (gi >= EG / k) {
            SumZadd(oid, o);
            return 2;
        }
        // 3.others,give it to next layer - next
        else {
            return 3;
        }

    }

    /*
     * deal a new Object
     *
     * @param oid:the id of this object
     *
     * @Param o:object
     *
     * @return 1:throw;2:keep;3:do it on next layer
     */
    public int deal(int oid, int[] o) {
        double gi = Calculator.GOOinF(o, L_f, p);// Gain of this object

        // 1.if the gain is 0 - throw
        if (gi <= e) {
            return 1;
        }
        // 2.if the gain >= (EG-G)/(k-c) - keep
        else if (gi >= EG / k) {
            SumZadd(oid, o);
            return 2;
        }
        // 3.others,give it to next layer - next
        else {
            return 3;
        }

    }

    /*
     * With switch sieve
     */
    public int deal_greedy(int oid, int[] o, double[] _p, int _e) {
        p = _p;
        double e = _e;
        double gi = Calculator.GOOinF(o, U_f, p);// Gain of this object

        // 1.if the gain is 0 - throw
        if (gi <= e) {
            return 1;
        }
        // 2.if the gain >= EG / k - maybe keep
        // else if (gi >= EG / k) {
        if (sumZ.size() < k) {// sumZ is not full
            SumZadd(oid, o);
            return 2;
        } else {
            // 1.choose the best result replace one object in sumZ
            int swapid = SwapEachOne(o);

            // 2.swap the object in sumZ,
            if (swapid != -1) {
                replace(swapid, oid, o);
                return 2;
            }
            return 3;
        }
        // }
        // 3.others,give it to next layer - next
        // else {
        // return 3;
        // }
    }

    public int deal_greedy(int oid, int[] o) {
        double gi = Calculator.GOOinF(o, U_f, p);// Gain of this object

        // 1.if the gain is 0 - throw
        if (gi <= e) {
            return 1;
        }
        // 2.if the gain >= EG / k - maybe keep
        // else if (gi >= EG / k) {
        if (sumZ.size() < k) {// sumZ is not full
            SumZadd(oid, o);
            return 2;
        } else {
            // 1.choose the best result replace one object in sumZ
            int swapid = SwapEachOne(o);

            // 2.swap the object in sumZ,
            if (swapid != -1) {
                replace(swapid, oid, o);
                return 2;
            }
            return 3;
        }
        // }
        // 3.others,give it to next layer - next
        // else {
        // return 3;
        // }

    }

    /*
     * Swap Each One object in sumZ with object(o) choose the max gain of swap
     * result
     *
     * @param o :new object
     *
     * @return : beat replace object index.if no will return -1;
     */
    private int SwapEachOne(int[] o) {

        double maxg = Calculator.GOSinF(Calculator.GetFeather(sumZ, d), U_f);// the gain
        // sumZ
        int maxid = -1;

        // traversal all sumZ
        for (int i = 0; i < k; i++) {
            ArrayList<int[]> tsumZ = new ArrayList<int[]>();
            // replace i==j
            for (int j = 0; j < k; j++) {
                if (i == j)
                    tsumZ.add(o);
                else
                    tsumZ.add(sumZ.get(j));
            }

            double g = Calculator.GOSinF(Calculator.GetFeather(tsumZ, d), U_f);
            // System.out.println(g+"/"+maxg);
            if (g > maxg) {
                maxg = g;
                maxid = i;
            }

        }

        return maxid;
    }

    /*
     * replace the object(id) in sumZ with object(oid)
     *
     * @param id: the index of object in sumZ
     *
     * @param oid: new object's true id.
     *
     * @param o:the infomation of new object
     */
    public void replace(int id, int oid, int[] o) {
        s_ids.remove(id);
        s_ids.add(oid);
        sumZ.remove(id);
        sumZ.add(o);
        G_f = Calculator.GetFeather(sumZ, d);
        L_f = Calculator.FeatherWithoutF(U_f, G_f);
        // not use G
    }

    /*
     * sumZ add a new object and update G_f..
     *
     * @param o:Object
     */
    public void SumZadd(int oid, int[] o) {
        sumZ.add(o);
        s_ids.add(oid);
        G_f = Calculator.AddFeatherByObject(G_f, o);
        L_f = Calculator.FeatherWithoutF(U_f, G_f);
        G += Calculator.GOO(G_f);
    }

    /*
     * transfer New U_F waiting for deal
     */
    public void T_WatiU_F(int[] _U_f) {
        WaitU_F = _U_f;
        IsWait = true;
    }

    /*
     * transfer New DataSet waiting for deal
     */
    public void T_AddWaitData(ArrayList<int[]> data) {
        WaitData.addAll(data);
    }

    /*
     * transfer New DataSet'ids waiting for deal
     */
    public void T_AddWaitids(ArrayList<Integer> ids) {
        Waitids.addAll(ids);
    }

    /*
     * Get rmData
     */
    public ArrayList<int[]> T_GetRMData() {
        ArrayList<int[]> t = rm_Data;
        rm_Data = new ArrayList<int[]>();
        return t;
    }

    /*
     * Get rmids;
     */
    public ArrayList<Integer> T_GetRMids() {
        ArrayList<Integer> t = rm_ids;
        rm_ids = new ArrayList<Integer>();
        return t;
    }

    /*
     * update this layer's information by new Feather
     *
     * @param IsGreedy: Greedy or Sieve method
     *
     * @return : true:WaitingData for next level
     */
    public boolean UpdateFLayer(boolean IsGreedy) {
        if (IsWait) {// Is U_F changed
            // Update U_f....
            U_f = WaitU_F;
            L_f = U_f;
            WaitData.addAll(sumZ);
            Waitids.addAll(s_ids);

            // Recalculate SumZ
            sumZ = new ArrayList<int[]>();
            s_ids = new ArrayList<Integer>();

            IsWait = false;
        }
        if (WaitData.size() > 0) {// Is any waitData
            for (int i = 0; i < WaitData.size(); i++) {
                int res;
                if (IsGreedy) {
                    res = deal_greedy(Waitids.get(i), WaitData.get(i));
                } else {
                    res = deal(Waitids.get(i), WaitData.get(i));
                }
                // PLAN A :Sieve
                // if (deal(Waitids.get(0), WaitData.get(0)) == 3) {
                // PLAN B :GREEDY
                if (res == 3) {
                    // if doesn't deal in this layer
                    rm_Data.add(WaitData.get(i));
                    rm_ids.add(Waitids.get(i));
                }

            }

            // Clear Waitdata & Waitids
            WaitData = new ArrayList<int[]>();
            Waitids = new ArrayList<Integer>();

            if (rm_Data.size() > 0) {
                return true;// some Data undeal
            }
        }

        return false;
    }

    /*
     * Return top k SumZ
     *
     * @param top:k
     */
    public ArrayList<int[]> GetTopSumZ(int top) {
        ArrayList<int[]> s = new ArrayList<int[]>();
        for (int i = 0; i < top; i++) {
            s.add(sumZ.get(i));
        }
        return s;
    }

    /*
     * Return top k sumZs' ids
     *
     * @param top:k
     */
    public ArrayList<Integer> GetTopSids(int top) {
        ArrayList<Integer> s = new ArrayList<Integer>();
        for (int i = 0; i < top; i++) {
            s.add(s_ids.get(i));
        }
        return s;
    }

    /*
     * Get the left U_f of the this layer
     */
    public int[] GetLeftf() {
        // U_f:0011 1111 G_f 0011 0000
        // U_f - G_f =0000 1111
        return L_f;
    }
}
