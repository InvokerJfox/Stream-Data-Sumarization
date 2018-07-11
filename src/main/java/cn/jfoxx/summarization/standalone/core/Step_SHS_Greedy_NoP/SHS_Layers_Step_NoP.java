package cn.jfoxx.summarization.standalone.core.Step_SHS_Greedy_NoP;

import cn.jfoxx.summarization.standalone.gain.Calculator;
import cn.jfoxx.util.get.GIntergerMatrix;
import cn.jfoxx.util.get.GString;
import cn.jfoxx.util.file.FileControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class SHS_Layers_Step_NoP {

    public int id;// layer's id
    public int k;// size of sumZ
    public int lk;// size of sumZ in this layer lk < k
    int d;// Dimension
    double[] p;// power
    double e;// min sieve gain,if f(o)<e,delete o;

    // the SumZ;
    ArrayList<SHS_Object_Step_NoP> sumZ = new ArrayList<SHS_Object_Step_NoP>();

    // waiting for deal Data
    ArrayList<SHS_Object_Step_NoP> wait_Data = new ArrayList<SHS_Object_Step_NoP>();

    // waiting for deal U_f
    ArrayList<SHS_Object_Step_NoP> wait_U_f = new ArrayList<SHS_Object_Step_NoP>();

    // this layer SendU_f
    ArrayList<SHS_Object_Step_NoP> send_U_f = new ArrayList<SHS_Object_Step_NoP>();

    double EG;// the expectation gain of this layer
    // double G;// the real gain of this layer

    int[] U_f;// the usefully feathers of this layer,1:useful e.g. 0000 1111 :
    // in
    int[] G_f;// the included feathers of this layer,1:useful e.g. 0000 0011 :
    // out
    int[] L_f;// the left feathers of this layer,1:useful e.g. 0000 1100 : in

    static String path = "E://SumZ_Output//Layers//";

    /*
     * initial this Layer
     * 
     * @param _id:this layer'id
     */
    public SHS_Layers_Step_NoP(int _id, double[] _p, int _k, int _lk, int _d,
                               double _e, double _eg) {
        id = _id;

        k = _k;
        lk = _lk;
        d = _d;
        p = _p;
        e = _e;

        EG = _eg;
        U_f = Calculator.All1(d);

        G_f = new int[_d];
        L_f = U_f;
    }

    /*
     * load the data of this layer in file
     * 
     * @param fid:upper layer's id
     */
    public void LoadU_f(int fid) {
        // if not the first layer ,load upper layer's output

        // save information format - object
        // id: 2537
        // attributes:
        // L_f: 111100000000000
        // runningtime: 3321

        File file = new File(path + "U_f" + fid + ".txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String ts = null;

            while ((ts = reader.readLine()) != null) {
                // first line - id
                int id = Integer.valueOf(ts);
                // second line - U_f
                ts = reader.readLine();
                int[] uf = GIntergerMatrix.fromBinaryString(ts);

                // save
                SHS_Object_Step_NoP u = new SHS_Object_Step_NoP(id, null, uf, 0);
                wait_U_f.add(u);
            }
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

    public void UpdateU_f(int[] _u_f) {
        U_f = _u_f;
        L_f = Calculator.FeatherWithoutF(U_f, G_f);
    }

    /*
     * 
     */
    public void LoadData(ArrayList<int[]> os) {
        for (int i = 0; i < os.size(); i++) {
            SHS_Object_Step_NoP o = new SHS_Object_Step_NoP(i, os.get(i), null,
                    0);
            wait_Data.add(o);
        }
    }

    /*
     * deal all wait_Data & save result
     */
    public void Deal(boolean firstlayer) throws IOException {

        // Deal Data until U_f Update
        if (firstlayer) {
            for (SHS_Object_Step_NoP aWait_Data : wait_Data) {
                DealOne(aWait_Data);
            }
        } else {
            int j = 0;
            int updateUf = 0;

            for (int i = updateUf; i < wait_Data.size(); i++) {
                // deal data
                if (i == updateUf) {
                    // update u_f
                    UpdateU_f(wait_U_f.get(j).U_f);
                    // next u_f
                    if (j < wait_U_f.size() - 1) {
                        j++;
                        updateUf = wait_U_f.get(j).oid;
                    } else {
                        updateUf = wait_Data.size() + 1;
                    }
                }
                DealOne(wait_Data.get(i));
            }
        }

        // save Result
        SaveToFile();
    }

    /*
     * deal a new Object
     * 
     * @Param o:object
     * 
     * @return 1:throw;2:keep;3:do it on next layer
     */
    public int DealOne(SHS_Object_Step_NoP o) throws IOException {
        double gi = Calculator.GOOinF(o, L_f, p);// Calculator of this object

        // 1.if the gain is 0 - throw
        if (gi < e) {
            return 1;
        }
        // 2.if the gain >= EG / k - maybe keep
        else if (gi >= EG / lk) {
            if (sumZ.size() < lk) {// sumZ is not full
                SumZadd(o);
                return 2;
            } else {
                // 1.choose the best result replace one object in sumZ
                int swapid = SwapEachOne(o);

                // 2.swap the object in sumZ,
                if (swapid != -1) {
                    replace(swapid, o);
                    return 2;
                }
                return 3;
            }
        }
        // 3.others,give it to next layer - next
        else {
            // output to file
            return 3;
        }
    }

    /*
     * Swap Each One object in sumZ with object(o) choose the max gain of swap
     * result
     * 
     * @param o :new object
     * 
     * @return : beat replace object index.if no will return -1;
     */
    private int SwapEachOne(SHS_Object_Step_NoP o) {

        // the gain of sumZ
        double maxg = Calculator.GOSinF(Calculator.getFeather_SHS(sumZ, d), U_f);
        int maxid = -1;

        // traversal all sumZ
        for (int i = 0; i < lk; i++) {
            ArrayList<SHS_Object_Step_NoP> tsumZ = new ArrayList<SHS_Object_Step_NoP>();
            for (int j = 0; j < lk; j++) {
                if (i != j)
                    tsumZ.add(sumZ.get(j));
                else
                    tsumZ.add(o);
            }

            double g = Calculator.GOSinF(Calculator.getFeather_SHS(tsumZ, d), U_f);
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
    public void replace(int id, SHS_Object_Step_NoP o) throws IOException {
        sumZ.remove(id);
        sumZ.add(o);
        G_f = Calculator.getFeather_SHS(sumZ, d);
        L_f = Calculator.FeatherWithoutF(U_f, G_f);
        sendU_f(o.oid);
    }

    /*
     * sumZ add a new object and update G_f..
     * 
     * @param o:Object
     */
    public void SumZadd(SHS_Object_Step_NoP o) throws IOException {
        sumZ.add(o);
        G_f = Calculator.AddFeatherByObject(G_f, o.A);
        L_f = Calculator.FeatherWithoutF(U_f, G_f);
        sendU_f(o.oid);
    }

    /*
     * Write the U_f to file for next layer
     */
    private void sendU_f(int oid) throws IOException {
        SHS_Object_Step_NoP o = new SHS_Object_Step_NoP(oid, null, L_f, 0);
        send_U_f.add(o);

    }

    private void SaveToFile() throws IOException {
        // 1.output to file L_f
        // args
        String tpath = path + "U_f" + id + ".txt";
        StringBuffer sb = new StringBuffer();// Data

        // save information format - object
        // id: 2537
        // U_f: 111100000000000

        int i = 0;// wait u_f :old
        int j = 0;// send u_f :new
        while (i < wait_U_f.size() || j < send_U_f.size()) {
            if (i == wait_U_f.size()) {
                sb.append("\r\n").append(send_U_f.get(j).oid).append("\r\n");
                sb.append(GString.fromBinaryIntMatrix(send_U_f.get(j).U_f));
                j++;
            } else if (j == send_U_f.size()) {
                sb.append("\r\n").append(wait_U_f.get(i).oid).append("\r\n");
                sb.append(GString.fromBinaryIntMatrix(wait_U_f.get(i).U_f));
                i++;
            } else if (send_U_f.get(j).oid < wait_U_f.get(i).oid) {
                sb.append("\r\n").append(send_U_f.get(j).oid).append("\r\n");
                sb.append(GString.fromBinaryIntMatrix(send_U_f.get(j).U_f));
                j++;
            } else if (send_U_f.get(j).oid > wait_U_f.get(i).oid) {
                sb.append("\r\n").append(wait_U_f.get(i).oid).append("\r\n");
                sb.append(GString.fromBinaryIntMatrix(wait_U_f.get(i).U_f));
                i++;
            } else {
                sb.append("\r\n").append(wait_U_f.get(j).oid).append("\r\n");
                sb.append(GString.fromBinaryIntMatrix(wait_U_f.get(j).U_f));
                i++;
                j++;
            }

        }
        if (sb.length() > 0)
            FileControl.FileOutput(tpath, new StringBuffer(sb.substring(2)));

        // 2.output sumZ
        tpath = path + "SumZ" + id + ".txt";
        sb = new StringBuffer();// Data
        // save information format - object
        // id: 2537
        // A: 111100000000000
        if (sumZ.size() > 0) {
            sb.append(sumZ.get(0).oid).append("\r\n");
            sb.append(GString.fromBinaryIntMatrix(sumZ.get(0).A));
        }
        for (i = 1; i < sumZ.size(); i++) {
            sb.append("\r\n").append(sumZ.get(i).oid).append("\r\n");
            sb.append(GString.fromBinaryIntMatrix(sumZ.get(i).A));
            // sb.append(0+ "\r\n");
        }
        if (sb.length() > 0)
            FileControl.FileOutput(tpath, sb);
    }

}
