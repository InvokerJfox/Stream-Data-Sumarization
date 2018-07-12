package cn.jfoxx.summarization.standalone.core.stream.sieve.step;

import cn.jfoxx.util.file.FileControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StreamSieverStep2 {
    public static int k;// size of sumZ
    public static int d;// the demension of sumZ

    public static ArrayList<StreamSieveStepObject> sievers = new ArrayList<StreamSieveStepObject>();// sievers list
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
        ArrayList<StreamSieveStepObject> sf = sievers;
        for (StreamSieveStepObject aSf : sf) {
            aSf.Start();
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
            String ts;

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
            StreamSieveStepObject s = new StreamSieveStepObject(id, k, tgain, d, st, sid, os);
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
