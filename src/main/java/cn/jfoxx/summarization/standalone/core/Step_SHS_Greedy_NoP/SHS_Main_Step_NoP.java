package cn.jfoxx.summarization.standalone.core.Step_SHS_Greedy_NoP;

import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.file.FileControl;
import cn.jfoxx.util.get.GIntergerMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SHS_Main_Step_NoP {
    public static int k;// size of sumZ
    public static int lk;// size of per layer
    public static ArrayList<SHS_Layers_Step_NoP> layers;

    public static double cmk;// current max gain per object * k
    public static int d;// dimension
    static double e;// min sieve gain,if f(o)<e,delete o;

    public static double[] p;// power

    public static ArrayList<int[]> Data;

    public static ArrayList<SHS_Object_Step_NoP> sumZ;// sumZ
    public static ArrayList<Integer> s_ids;// sumZs' ids

    static String path = "E://SumZ_Output//Layers//";

    /*
     * initial the args
     *
     * @param m:current max gain
     */
    public static void initial(ArrayList<int[]> os, double _cm, int _k,
                               int _lk, int _d, double[] _p, double _e) {
        FileControl.delAllFile(path);

        Data = os;

        k = _k;
        lk = _lk;
        d = _d;
        cmk = _cm;
        e = _e;

        p = _p;

        layers = new ArrayList<SHS_Layers_Step_NoP>();

        sumZ = new ArrayList<SHS_Object_Step_NoP>();
        s_ids = new ArrayList<Integer>();

    }

    /*
     * Deal all data in each layer
     */
    public static void Deal() throws IOException {
        // 1.Load data from os,and deal in the 1st layer
        layers = new ArrayList<SHS_Layers_Step_NoP>();
        SHS_Layers_Step_NoP l = new SHS_Layers_Step_NoP(layers.size(), p, k,
                lk, d, e, cmk);
        l.LoadData(Data);
        layers.add(l);

        l.Deal(true);

        // 2.Is there any U_f Left/Other layers
        int fid = 0;// file id
        File file = new File(path + "U_f" + fid + ".txt");
        while (file.exists()) {
            if (layers.size() > (100)) {
                System.out.println("More than " + (100)
                        + " levels , stoped.");
                break;
            }
            cmk *= 0.1;
            l = new SHS_Layers_Step_NoP(layers.size(), p, k, lk, d, e, cmk);
            l.LoadData(Data);
            l.LoadU_f(fid);
            layers.add(l);
            l.Deal(false);
            fid++;
            file = new File(path + "U_f" + fid + ".txt");
        }

    }

    /*
     * Get SumZ
     */
    public static void GetSumZ() {
        // load sumZ until size k
        int fid = 0;// file start index

        // save information format - object
        // id: 2537
        // attributes:111100000000000
        // ....
        while (sumZ.size() < k) {
            File file = new File(path + "SumZ" + fid + ".txt");
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String ts = null;

                while ((ts = reader.readLine()) != null) {
                    // first line - id
                    int id = Integer.valueOf(ts);
                    // second line - U_f
                    ts = reader.readLine();
                    int[] a = GIntergerMatrix.fromBinaryString(ts);

                    // save
                    SHS_Object_Step_NoP s = new SHS_Object_Step_NoP(id, a,
                            null, 0);
                    if (sumZ.size() < k)// only get top k
                    {
                        sumZ.add(s);
                        s_ids.add(s.oid);
                    } else
                        break;
                }
                fid++;
                reader.close();

            } catch (IOException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }

    /*
     * Get SumZ feathers by SumZ
     */
    public static int[] GetSumZf() {
        GetSumZ();
        return Calculator.getFeather_SHS(sumZ, d);
    }
}
