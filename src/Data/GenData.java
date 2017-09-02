package Data;

import java.io.IOException;

import SumZ.Common;

public class GenData {

	// global setting
	static int dimension = 50000; // total dimension
	static int no = 2000;// number of objects
	static String path = "E://SumZ_Output//Data.txt";

	// AvgDistibution setting
	static int avgD = 10;// per object average dimension size

	// Guassion Distribution setting
	static double v = 0.1;// variance

	public static void main(String[] args) throws IOException {
		// AvgDistibution();

		GuassionDistribution();
	}

	/*
	 * Data average Distribution
	 */
	public static void AvgDistibution() throws IOException {
		StringBuffer sb = new StringBuffer();// Data

		// initial setting

		// generate i objects
		for (int i = no; i > 0; i--) {
			char[] ob = new char[dimension];

			int od = avgD;

			for (int j = 0; j < dimension; j++) {
				// fill with '0'
				int jm = (int) (dimension / od * (Math.random() * 2));
				while (j < dimension && jm > 0) {
					ob[j] = '0';
					j += 1;
					jm--;
				}
				if (j < dimension) {
					ob[j] = '1';
				}
			}

			sb.append(ob);
			sb.append("\r\n");
		}

		Common.FileOutput(path, sb);

		System.out.println("Done!");

	}

	/*
	 * Data satisfy Guassion Distribution
	 */
	public static void GuassionDistribution() throws IOException {
		StringBuffer sb = new StringBuffer();// Data

		// initial setting
		int[] attcount = new int[dimension];

		double[] attPro = new double[dimension];// the probability of attribute
		// generate 1
		for (int i = 0; i < dimension; i++) {
			attPro[i] = Common.RandomGaussian01(1);
		}

		double[] obPro = new double[no];// the probability of object generate 1
		// double obPro = 1;// the probability of object is same
		for (int i = 0; i < no; i++) {
			obPro[i] = Common.RandomGaussian01(1);
		}

		// generate i objects
		for (int i = 0; i < no; i++) {
			char[] ob = new char[dimension];// object's data
			boolean all0 = true;

			for (int j = 0; j < dimension; j++) {
				double r = Math.round(0.15 * obPro[i] + 0.2 * attPro[j] + 0.3
						* Math.random()) + 48;
				ob[j] = (char) r;
				if (r != 48) {
					attcount[j] = 1;// this attribute had generate
					if (all0) {
						all0 = false;// a object at least include a attribute.
					}
				}
			}

			// this object is full of 0
			if (!all0) {
				sb.append(ob);
				sb.append("\r\n");
			} else {
				//random select a attribute if it's 0 ,set 1;
				int ta=(int) Math.round((dimension - 1) * Math.random());
				if (attcount[ta] ==0) {
					//set the never disappear attribute is 1
					attcount[ta] = 1;
					ob[ta]='1';
					sb.append(ob);
					sb.append("\r\n");
				} else {
					obPro[i] += Math.random() / 10;
					attPro[(int) Math.round((dimension - 1) * Math.random())] += Math
							.random() / 10;
					i--;
					continue;
				}
			}

		}

		Common.FileOutput(path, sb);

		System.out.println("Done!");

	}

	

}
