package cn.jfoxx.summarization.standalone.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cn.jfoxx.summarization.standalone.entity.SumZ_Att;
import cn.jfoxx.summarization.standalone.entity.SumZ_Object;

public class LoadData {
	/*
	 * Convert Data to int[][] by lines
	 */
	public static ArrayList<int[]> LoadDataByLines(String path, int row,
			int column) {
		File file = new File(path);
		BufferedReader reader = null;
		int r = 0;
		ArrayList<int[]> Data = new ArrayList<int[]>();// int[row][column];
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			// load Data
			for (r = 0; r < row; r++) {
				tempString = reader.readLine();
				int[] temp = new int[column];
				for (int c = 0; c < column; c++) {
					temp[c] = tempString.charAt(c) - 48;// char(1/0) to int(1/0)
				}
				Data.add(temp);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return Data;
	}

	public static ArrayList<SumZ_Object> LoadData_SumZ(String path, int row,
													   int column) {
		File file = new File(path);
		BufferedReader reader = null;
		int r = 0;
		ArrayList<SumZ_Object> Data = new ArrayList<SumZ_Object>();// int[row][column];
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			// load Data
			for (r = 0; r < row; r++) {
				tempString = reader.readLine();
				SumZ_Object temp = new SumZ_Object(r);
				for (int c = 0; c < column; c++) {
					if ((tempString.charAt(c) - 48) == 1) {
						SumZ_Att ta = new SumZ_Att(c, 1);
						temp.A.add(ta);// char(1/0) to int(1/0)
					}
				}
				Data.add(temp);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return Data;
	}
}
