package cn.jfoxx.summarization.standalone.core.stream.sieve.step;

import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.file.FileControl;
import cn.jfoxx.util.get.GString;

import java.io.IOException;
import java.util.ArrayList;

public class StreamSieveStepObject {
    public int id;// this sieve's id

    public static int k;// max size of the sumZ

    public ArrayList<Integer> s_ids;// the sumZ'id of this SumZ
    public ArrayList<int[]> sumZ;// the sumZ of this SumZ

    public int[] f;// the feather had been included in sumZ :out

    public double Gain;// the gain of this SumZ
    public double tGain;// target Gain

    public long runningTime;// running time
    public int startId;// start data index
    public ArrayList<int[]> data;// DetSet

    private String path = "E://SumZ_Output//Sieve_Result//";

    /*
     * initial a new siever
     */
    public StreamSieveStepObject(int _id, double _tGain) {
        id = _id;
        tGain = _tGain;
    }

    /*
     * initial a new siever
     */
    public StreamSieveStepObject(double _Gain, long _rt, ArrayList<Integer> _s_ids) {
        Gain = _Gain;
        runningTime = _rt;
        s_ids = _s_ids;
    }

    /*
     * initial a new siever
     *
     * @param _id:id
     *
     * @param _k:the size of sumZ
     *
     * @param _tGain:the target gain of this Siever
     *
     * @param _d:the demension of this sumZ
     *
     * @param _starttime :start time
     *
     * @param _data :left DataSet
     */
    public StreamSieveStepObject(int _id, int _k, double _tGain, int d,
                                 long _startTime, int _startId, ArrayList<int[]> _data) {
        id = _id;
        k = _k;
        tGain = _tGain;
        Gain = 0;
        f = new int[d];
        s_ids = new ArrayList<Integer>();
        sumZ = new ArrayList<int[]>();

        runningTime = _startTime;
        startId = _startId;
        data = _data;
    }

    /*
     * Run this seiver
     */
    public void Start() throws IOException {
        long st = System.currentTimeMillis();// this sieve start time
        // deal all data
        for (int i = startId; i < data.size(); i++) {
            SieveData(i, data.get(i));
        }

        // running end
        runningTime += System.currentTimeMillis() - st;
        // save result
        SaveResult();
    }

    public void SaveResult() throws IOException {
        // args
        if (Gain != 0) {
            path += id + ".txt";
            StringBuffer sb = new StringBuffer();// Data

            sb.append(Gain).append("\r\n");
            sb.append(GString.fromList(s_ids, "/")).append("\r\n");
            sb.append(runningTime);

            FileControl.FileOutput(path, sb);

            //System.out.println("Sieve " + id + ": Done!");
        }
    }

    /*
     * (Per)SumZ deal with a new Object
     *
     * @param oid:if the object's gain is enough,we will save the id of this
     * object
     */
    public void SieveData(int oid, int[] o) {
        // full!
        int c = s_ids.size();
        if (c == k) {
            return;
        }
        // low bound of Gain
        double low = (tGain - Gain) / (k - c);
        double og = Calculator.GOOoutF(o, f);
        if (og >= low) {// supply the demand ��insert all information
            this.Gain += og;
            f = Calculator.AddFeatherByObject(f, o);
            s_ids.add(oid);
            sumZ.add(o);
            //System.out.println(oid + " keeped");
        }
    }

    /*
     * (Per)SumZ deal with a new Object
     *
     * @param oid:if the object's gain is enough,we will save the id of this
     * object
     */
    public void SieveData(int oid, int[] o, double[] p) {
        // full!
        int c = sumZ.size();
        if (c == k) {
            return;
        }
        // low bound of Gain
        double low = (tGain - Gain) / (k - c);
        double og = Calculator.GOOoutF(o, f, p);
        if (og >= low) {// supply the demand ��insert all information
            this.Gain += og;
            f = Calculator.AddFeatherByObject(f, o);
            s_ids.add(oid);
            sumZ.add(o);
            // System.out.println(oid + " keeped");
        }
    }
}
