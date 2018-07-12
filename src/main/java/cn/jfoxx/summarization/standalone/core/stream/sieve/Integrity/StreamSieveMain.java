package cn.jfoxx.summarization.standalone.core.stream.sieve.Integrity;

import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.get.GDoubleMatrix;

import java.util.ArrayList;

public class StreamSieveMain {
    public static int k;// size of sumZ
    public static double mg;// current max gain
    public static int d;// the demension of sumZ
    public static int maxI;// i:current max (1+e)^i
    public static double e;// e:(1+e)^i
    public static double mr;// m<(1+e)^i<mr*k*m
    public static ArrayList<StreamSiever> sievers;// sievers list
    public static int maxSiverid;// the beast siver

    public static double[] p;// power

    /*
     * initial the args
     *
     * @param m:current max gain
     */
    public static void Initial(double _mg, double _e, double _mr, int _k,
                               int _d, int[] o) {
        e = _e;
        mr = _mr;
        k = _k;
        mg = _mg;
        d = _d;

        p = GDoubleMatrix.fromIntergerMatrix(o);
        // initial have not add a new siever
        maxI = (int) Math.floor(Math.log10(_mg) / Math.log10(1 + e)) - 1;
        // System.out.println("maxI:"+maxI);
        sievers = new ArrayList<StreamSiever>();

        Disperse();
    }

    /*
     * get the index in Sievers of max SumZ
     */
    public static void GetMaxSieveid() {
        double mGain = 0;
        ArrayList<StreamSiever> sf = sievers;
        for (int j = 0; j < sf.size(); j++) {
            StreamSiever s = sf.get(j);
            if (s.Gain > mGain) {
                mGain = s.Gain;
                maxSiverid = j;
            }
        }
    }

    /*
     * get the max SumZ with GetMaxSieveid
     */
    public static ArrayList<int[]> GetMaxSumZ() {
        GetMaxSieveid();
        return sievers.get(maxSiverid).sumZ;
    }

    /*
     * get the max SumZ'ids without GetMaxSieveid
     */
    public static ArrayList<Integer> GetMaxSumZid() {
        GetMaxSieveid();
        return sievers.get(maxSiverid).s_ids;
    }

    /*
     * Get the max SumZ's feathers;
     */
    public static int[] GetMaxSumZf() {
        GetMaxSieveid();
        return sievers.get(maxSiverid).f;
    }

    /*
     * m<(1+e)^i<km
     *
     * @ m:maxGain
     *
     * @ e
     *
     * @ k :size of sumZ
     */
    public static void Disperse() {
        // delete low bound
        try {
            while (sievers.size() > 0) {
                if (sievers.get(0).tGain < mg) {
                    // System.out.println(sievers.get(0).tGain +"/"+ mg);
                    // System.out.println("size:"+sievers.size());
                    sievers.remove(0);
                } else {
                    break;// lowest > m,all > m
                }
            }

        } catch (Exception e) {
        } finally {
        }

        // New (1+e)^i
        int i = maxI + 1;//
        for (; Math.pow(1 + e, i) < mr * k * mg; i++) {
            //System.out.println("Math.pow(1 + e, i):"+Math.pow(1 + e, i));
            double tGain = Math.pow(1 + e, i);
            // Add a new SS_Siever_NoPower
            StreamSiever s = new StreamSiever(k, tGain, d);
            sievers.add(s);
            // System.out.println("size:"+sievers.size()+" add:"+tGain);
        }
        maxI = i;
    }

    /*
     * deal with next object
     *
     * @param oid:the object's id
     *
     * @param o:the object information
     */
    public static void DealNext(int oid, int[] o) {
        // max Gain Changed?
        double tmg = Calculator.GOO(o);
        if (tmg > mg) {
            mg = tmg;
            Disperse();
        }

        // all filter
        ArrayList<StreamSiever> sf = StreamSieveMain.sievers;
        for (StreamSiever aSf : sf) {
            aSf.SieveData(oid, o);
        }
    }

    /*
     * deal with next object
     *
     * @param oid:the object's id
     *
     * @param o:the object information
     */
    public static void DealNextWithP(int oid, int[] o) {
        // Update Pwer
        p = Calculator.AddPower(p, o);

        // max Gain Changed?
        double tmg = Calculator.GOO(o, p);
        // System.out.println(oid+":" + tmg);
        if (tmg > mg) {
            mg = tmg;
            Disperse();
        }

        // all filter
        ArrayList<StreamSiever> sf = StreamSieveMain.sievers;
        for (StreamSiever aSf : sf) {
            aSf.SieveData(oid, o, p);
        }
    }
}
