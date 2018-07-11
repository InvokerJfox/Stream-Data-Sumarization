package cn.jfoxx.summarization.standalone.core.Stream_Sieve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cn.jfoxx.util.file.FileControl;

public class SS_Main_Step2 {
    public static int k;// size of sumZ
    public static int d;// the demension of sumZ

    public static ArrayList<SS_Siever_Step> sievers = new ArrayList<SS_Siever_Step>();// sievers list
    public static int maxSiverid;// the beast siver

    public static double[] p;// power

    static String path = "E://SumZ_Output//Sievers//";
    static String delpath = "E://SumZ_Output//Sieve_Result//";
    public static int id = 0;// sievers' start id
    public static ArrayList<int[]> os;

    public static void SieversDeal(ArrayList<int[]> _os, int ssid, int _k, int _d)
            throws IOException {
        FileControl.delAllFile(delpath);
        k = _k;
        d = _d;
        os = _os;

        // load sievers
        LoadSieves(ssid);

        // all sievers deal data
        ArrayList<SS_Siever_Step> sf = sievers;
        for (int j = 0; j < sf.size(); j++) {
            sf.get(j).Start();
        }

    }

    public static void LoadSieves(int ssid) {
        while (loadPerSieve(ssid++))
            // load successful
            ;
    }

    public static boolean loadPerSieve(int id) {
        // saved information format
        // tGain:3000
        // startid:65
        // starttime:3321

        File file = new File(path + id + ".txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String ts = null;

            //first line
            ts = reader.readLine();
            double tgain = Double.valueOf(ts);
            //second line
            ts = reader.readLine();
            int sid = Integer.valueOf(ts);
            //last line
            ts = reader.readLine();
            long st = Integer.valueOf(ts);

            //save
            SS_Siever_Step s = new SS_Siever_Step(id, k, tgain, d, st, sid, os);
            sievers.add(s);

            reader.close();

            return true;
        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return false;

    }

}
