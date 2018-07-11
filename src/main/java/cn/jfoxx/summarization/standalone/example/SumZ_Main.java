package cn.jfoxx.summarization.standalone.example;

import java.io.IOException;
import java.util.ArrayList;

import cn.jfoxx.summarization.standalone.core.StandardGreedy.Greedy;
import cn.jfoxx.summarization.standalone.core.Stream_Greedy.SG_Main;
import cn.jfoxx.summarization.standalone.core.Stream_HierarchySieve.SHS_Layers;
import cn.jfoxx.summarization.standalone.core.Stream_HierarchyStream.HS_Main;
import cn.jfoxx.summarization.standalone.core.Stream_Sieve.SS_Main;
import cn.jfoxx.summarization.standalone.data.LoadData;
import cn.jfoxx.summarization.standalone.entity.SumZ_AttList;
import cn.jfoxx.summarization.standalone.entity.SumZ_Object;
import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.get.GString;

public class SumZ_Main {
    static int d = 500; // total dimension
    static int no = 2000;// 2000;// number of objects
    static String path = "E://SumZ_Output//Data.txt";
    static int k = 10;//
    static int lk = 1;// lk
    static int e = 1;// SHS min sieve gain
    static double mr = 2;// sieve k*m*mr; deal the delete problem
    static double cmk = 30;// SHS use it

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

        // 1.1 Standard Greedy
        Standard_Greedy(os, false, p);

        // 1.2 Stream Greedy
        StreamGreedy(os, false, p);

        // 1.3 Sieve Stream
        SieveStream(os, false, p);

        // 1.4 HierarchySieve - Greedy
        StreamHierarchySieve(os, false, p, true);

        // 1.5 HierarchyStream
        ArrayList<SumZ_Object> data = LoadData.LoadData_SumZ(path, no, d);
        SumZ_AttList np = Calculator.GetPower01(data);
        HierarchyStream(data, np);

        // }

        // ShowResult();
    }

    private static void HierarchyStream(ArrayList<SumZ_Object> os,
                                        SumZ_AttList p) {
        HS_Main.initial(os, k, lk, p, e);
        HS_Main.DealAll();

        SumZ_AttList s_f = HS_Main.GetSumZf();

        // coverage
        System.out.print("HierarchyStream algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + HS_Main.sumZ.toString(" / ") + "\n");

        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));
    }

    /*
     * Stream Deal - Stream Hierarchy Sieve algorithm
     *
     * @param os: Data
     *
     * @param p:power
     *
     * @param IsGreedy: Greedy or Sieve method
     */
    private static void StreamHierarchySieve(ArrayList<int[]> os,
                                             boolean IsPower, double[] p, boolean IsGreedy) {

        // double cm = Calculator.GOO(o);// maxCalculator
        SHS_Layers.Initial(cmk, k, lk, d, p, IsPower, IsGreedy);
        // stream deal
        for (int i = 0; i < os.size(); i++) {
            //System.out.println(i);
            SHS_Layers.DealNext(i, os.get(i), e);
        }

        // output result
        p = SHS_Layers.p;
        ArrayList<int[]> sumZ = SHS_Layers.GetSumZ();

        int[] s_f = Calculator.GetFeather(sumZ, d);

        // coverage
        String isgrd = "(Sieve)";
        if (IsGreedy)
            isgrd = "(Greedy)";
        System.out.print("StreamHierarchySieve" + isgrd
                + " algorithm Coverage : " + Calculator.Coverage(s_f, p)
                + "% \nthe SumZ is : "
                + GString.fromList(SHS_Layers.GetSumZids(), " / ")
                + "\n");

        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));

        // Show Layers
        // SHS_Layers.printTopLayers(SHS_Layers.sievers.size());
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
    public static void Standard_Greedy(ArrayList<int[]> os, boolean IsPower,
                                       double[] p) {
        // result args
        ArrayList<int[]> sumZ;
        int[] s_f;

        if (IsPower) {
            // Update the power
            p = Calculator.GetPower(os);

            // output result
            sumZ = Greedy.SumZWithP(os, k, d, p);
            s_f = Calculator.GetFeather(sumZ, d);

        } else {
            // calculate without power
            // output result
            sumZ = Greedy.SumZ(os, k, d);
            s_f = Calculator.GetFeather(sumZ, d);
        }
        // coverage
        System.out.print("Standard_Greedy algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + GString.fromList(Greedy.s_ids, " / ") + "\n");

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
            SG_Main.Initial(k, d, firstk, Calculator.GetPower(firstk));

            // stream deal
            for (int i = k; i < os.size(); i++) {
                SG_Main.ChooseDataWithP(i, os.get(i));
            }
        } else {
            SG_Main.Initial(k, d, firstk);

            // stream deal
            for (int i = k; i < os.size(); i++) {
                SG_Main.ChooseData(i, os.get(i));
            }
        }

        // output result
        sumZ = SG_Main.sumZ;

        s_f = Calculator.GetFeather(sumZ, d);

        if (IsPower)
            p = SG_Main.p;

        // coverage
        System.out.print("StreamGreedy algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + GString.fromList(SG_Main.s_ids, " / ") + "\n");
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
    public static void SieveStream(ArrayList<int[]> os, boolean IsPower,
                                   double[] p) {
        // result args
        int[] s_f;

        // initial the first data
        int[] o = os.get(0);

        double mg = Calculator.GOO(o);// maxCalculator
        SS_Main.Initial(mg, 0.1, mr, k, d, o);// object =power

        if (IsPower) {
            // stream deal
            for (int i = 0; i < os.size(); i++) {
                SS_Main.DealNextWithP(i, os.get(i));
            }
        } else {
            // stream deal
            for (int i = 0; i < os.size(); i++) {
                SS_Main.DealNext(i, os.get(i));
            }

        }

        // output result
        s_f = SS_Main.GetMaxSumZf();

        if (IsPower)
            p = SS_Main.p;

        // coverage
        System.out.print("SieveStream algorithm Coverage : "
                + Calculator.Coverage(s_f, p) + "% \nthe SumZ is : "
                + GString.fromList(SS_Main.GetMaxSumZid(), " / ")
                + "\n");

        // Save Result
        Cov.add(Calculator.Coverage(s_f, p));
    }

    public static void ShowResult() {
        System.out.println(GString.fromList(Cov, "\r"));
    }
}
