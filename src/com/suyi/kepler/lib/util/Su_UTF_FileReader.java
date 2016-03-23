package com.suyi.kepler.lib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Su_UTF_FileReader {

	File olddir;

	HashMap<String, String> rePmap;

	public Su_UTF_FileReader(File olddir, HashMap<String, String> rePmap) {
		// TODO Auto-generated constructor stub
		this.olddir = olddir;
		this.rePmap = rePmap;
	}

	public List<String> read() {
		// TODO Auto-generated method stub
		List<String> list = new LinkedList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(olddir));
			String str = null;
			while ((str = reader.readLine()) != null) {
				
				if (rePmap != null) {
					Iterator<String> it = rePmap.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						String val = rePmap.get(key);
						if (str.contains(key)) {
							// System.out.println(str);
							str = str.replaceAll(key, val);
							// System.out.println(str);
						}
					}
				}

				list.add(str);
			}
			reader.close();
			reader = null;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
