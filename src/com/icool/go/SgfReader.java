package com.icool.go;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SgfReader {

	private static final int ACode = (int) 'a';

	public static HashMap<String, String> parseHeader(String s) {
		HashMap<String, String> map = new HashMap<String, String>();

		Pattern pattern = Pattern.compile("([A-Z][A-Z])(\\[.*?\\])");
		Matcher m = pattern.matcher(s);

		System.out.println(m.groupCount());
		while (m.find()) {
			map.put(m.group(1), m.group(2)) ;
//			System.out.println(m.group(1));
//			System.out.println(m.group(2));
		}

		return map;
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public static SgfData loadSGF(InputStream ins) throws IOException {

		SgfData data = new SgfData();

		BufferedReader bufReader = new BufferedReader(new InputStreamReader(
				ins, "GBK"));
		String line = "";
		String str = "";
		String headerStr = "";
		int i, j;

		while ((line = bufReader.readLine()) != null)
			str += line;

		System.out.println("sgf=" + str);
		i = str.indexOf(";");
		j = str.indexOf(";", i + 1);
		headerStr = str.substring(i + 1, j);
		data.header = parseHeader(headerStr) ;
//		System.out.println(headerStr);

		if (str.length() == 0)
			return null;

		String[] ds = str.split(";");
		System.out.println("ds=" + ds.length);
		if (ds.length < 4) {
			return null;
		}
		for (String s : ds) {
			if (s == "")
				continue;

			String ns = s.toLowerCase();
			// System.out.println("ns=" + ns);
			if (ns.length() == 5
					&& (ns.charAt(0) == 'b' || ns.charAt(0) == 'w')) {
				char xChar = ns.charAt(2);
				char yChar = ns.charAt(3);

				int x = (int) xChar - ACode;
				int y = (int) yChar - ACode;
				// System.out.println("x=" + x + ",y=" + y);
				Coordinate c = new Coordinate(x, y);
				data.coordinates.add(c);
			}
		}
		// System.out.println("cs=" + cs.size());
		return data;
	}

	public static void main(String[] args) {
		String s = "GN[报知新闻主办]DT[1934年(报知新闻1月5日至12日,24日至2月2日连载)]PB[木谷实 九段]PW[吴清源 五段]KM[4]RE[共182手 白中盘胜]US[余军]";

		SgfReader.parseHeader(s);
	}
}
