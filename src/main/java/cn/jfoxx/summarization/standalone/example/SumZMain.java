package cn.jfoxx.summarization.standalone.example;

import cn.jfoxx.summarization.standalone.core.global.greedy.standard.StandardGreedy;
import cn.jfoxx.summarization.standalone.core.stream.greedy.StreamGreedy;
import cn.jfoxx.summarization.standalone.core.stream.hierarchy.HierarchySieveMain;
import cn.jfoxx.summarization.standalone.core.stream.sieve.Integrity.StreamSieveMain;
import cn.jfoxx.summarization.standalone.entity.SumZ_AttList;
import cn.jfoxx.summarization.standalone.entity.SumZ_Object;
import cn.jfoxx.summarization.standalone.example.data.LoadData;
import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.get.GString;

import java.io.IOException;
import java.util.ArrayList;

public class SumZMain {
    private static int d = 500; // total dimension
    private static int no = 2000;// 2000;// number of objects
    private static String path = "E://SumZ_Output//Data.txt";
    private static int k = 10;//
    private static int lk = 1;// lk
    private static int e = 1;// SHS min sieve gain
    private static double mr = 2;// sieve k*m*mr; deal the delete problem
    private static double cmk = 30;// SHS use it

    static ArrayList<Double> Cov = new ArrayList<Double>();

    public static void main(String[] args) throws InterruptedException,
            IOException {
        // GetFIS();//Generate FIS

        // Load Data
        ArrayList<int[]> os = LoadData.LoadDataByLines(path, no, d);

        // 1.NoPower Comparison
        double[] p = Calculator.SPW01(os);// it's generate data cost.because
        // some attribute is never disappear

        // while (k++ < 20) {
        // lk = (int) Math.sqrt(k);

        // 1.1 Standard StandardGreedy
        StandardGreedy(os, false, p);

        // 1.2 Stream StandardGreedy
        StreamGreedy(os, false, p);

        // 1.3 Sieve Stream
        SieveStream(os, false, p);

        // 1.4 HierarchyStream
        ArrayList<SumZ_Object> data = LoadData.LoadData_SumZ(path, no, d);
        SumZ_AttList np = Calculator.GetPower01(data);
        HierarchyStream(data, np);

        // }

        // ShowResult();
    }

    private static void HierarchyStream(ArrayList<SumZ_Object> os,
                                        SumZ_AttList p) {
        HierarchySieveMain.initial(os, k, lk, p, e);
        HierarchySieveMain.DealAll();

        SumZ_AttList s_f = HierarchySieveMain.GetSumZf();

        // coverage
        System.out.print("HierarchyStream algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + HierarchySieveMain.sumZ.toString(" / ") + "\n");

        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));
    }

    /*
     * static Deal - Greedy_Power algorithm
     *
     * @param os: Data
     *
     * @param IsPower:Does calc with power? true:p=null
     *
     * @param p:power
     */
    private static void StandardGreedy(ArrayList<int[]> os, boolean IsPower,
                                       double[] p) {
        // result args
        ArrayList<int[]> sumZ;
        int[] s_f;

        if (IsPower) {
            // Update the power
            p = Calculator.GetPower(os);

            // output result
            sumZ = StandardGreedy.SumZWithP(os, k, d, p);
            s_f = Calculator.GetFeather(sumZ, d);

        } else {
            // calculate without power
            // output result
            sumZ = StandardGreedy.SumZ(os, k, d);
            s_f = Calculator.GetFeather(sumZ, d);
        }
        // coverage
        System.out.print("Standard_Greedy algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + GString.fromList(StandardGreedy.s_ids, " / ") + "\n");

        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));
    }

    /*
     * Stream Deal - StreamGreedy algorithm
     *
     * @param os: Data
     *
     * @param IsPower:Does calc with power?
     *
     * @param p:power
     */
    private static void StreamGreedy(ArrayList<int[]> os, boolean IsPower,
                                     double[] p) {
        // result args
        ArrayList<int[]> sumZ;
        int[] s_f;

        // initial the firstk data
        ArrayList<int[]> firstk = new ArrayList<int[]>();

        for (int i = 0; i < k; i++) {
            firstk.add(os.get(i));
        }

        if (IsPower) {
            // calculate the firstk power
            StreamGreedy.Initial(k, d, firstk, Calculator.GetPower(firstk));

            // stream deal
            for (int i = k; i < os.size(); i++) {
                StreamGreedy.ChooseDataWithP(i, os.get(i));
            }
        } else {
            StreamGreedy.Initial(k, d, firstk);

            // stream deal
            for (int i = k; i < os.size(); i++) {
                StreamGreedy.ChooseData(i, os.get(i));
            }
        }

        // output result
        sumZ = StreamGreedy.sumZ;

        s_f = Calculator.GetFeather(sumZ, d);

        if (IsPower)
            p = StreamGreedy.p;

        // coverage
        System.out.print("StreamGreedy algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + GString.fromList(StreamGreedy.s_ids, " / ") + "\n");
        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));

    }

    /*
     * Stream Deal - SieveStream algorithm
     *
     * @param os: Data
     *
     * @param power:Does calc with power?
     *
     * @param p:power
     */
    private static void SieveStream(ArrayList<int[]> os, boolean IsPower,
                                    double[] p) {
        // result args
        int[] s_f;

        // initial the first data
        int[] o = os.get(0);

        double mg = Calculator.GOO(o);// maxCalculator
        StreamSieveMain.Initial(mg, 0.1, mr, k, d, o);// object =power

        if (IsPower) {
            // stream deal
            for (int i = 0; i < os.size(); i++) {
                StreamSieveMain.DealNextWithP(i, os.get(i));
            }
        } else {
            // stream deal
            for (int i = 0; i < os.size(); i++) {
                StreamSieveMain.DealNext(i, os.get(i));
            }

        }

        // output result
        s_f = StreamSieveMain.GetMaxSumZf();

        if (IsPower)
            p = StreamSieveMain.p;

        // coverage
        System.out.print("SieveStream algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + GString.fromList(StreamSieveMain.GetMaxSumZid(), " / ")
                + "\n");

        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));
    }

    public static void ShowResult() {
        System.out.println(GString.fromList(Cov, "\r"));
    }
}
