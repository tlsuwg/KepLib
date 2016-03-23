package com.suyi.kepler.settingMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.suyi.kepler.lib.util.FileUtil;
import com.suyi.kepler.lib.util.StringUtil;
import com.suyi.kepler.lib.util.Su_UTF_FileReader;
import com.suyi.kepler.lib.util.Su_UTF_FileWriter;

public class ChangeSrcForMode {


	static String ForEx = " -f E:/android/workspace/jdsdk_src -m isX5Mode -zhs f";

	// ChangeSrcForMode -f E:/android/workspace/jdsdk_src -m isX5Mode -d t

	String target;

	// $$isX5Mode&&+-{}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.println();

		System.out.println("修改代码，生成不同分支代码");

		System.out.println();
		System.out.println();

		if (args == null || args.length == 0) {
			System.err
					.println("参数null,like this : java -jar changeResForMode.jar "
							+ ForEx);
			// return;
			args = new String[] { ForEx };
		}

		ChangeSrcForMode mChangeSrcForMode = new ChangeSrcForMode();
		try {
			mChangeSrcForMode.getSetting(args);
			mChangeSrcForMode.changeSrc();
			System.out.println();
			System.out.println();
			System.out.println(mChangeSrcForMode.target + "");
			System.out.println("！完成修改");
			System.out.println("请刷新项目");
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeSrc() throws IOException {
		// TODO Auto-generated method stub
		changePackageName(new File(f));
	}

	private void changePackageName(File olddir) throws IOException {
		// TODO Auto-generated method stub

		if (olddir.isDirectory()) {
			File[] files = olddir.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					changePackageName(f);
				}
			}
		} else if (olddir.isFile() && (!olddir.getName().endsWith(".class"))) {
			if (olddir.getName().endsWith(".java")) {
				changePackageInFile(olddir, 1);
			} else if (olddir.getName().endsWith(".xml")) {
				changePackageInFile(olddir, 2);
			}
		}
	}

	// $$isX5Mode&&+-{}

	String javahead = "//";
	String xmlhead = "<!--";
	String xmlend = "-->";

	private void changePackageInFile(File olddir, int type) throws IOException {
		// TODO Auto-generated method stub

		Su_UTF_FileReader reader = new Su_UTF_FileReader(olddir, null);
		List<String> list = reader.read();
		List<String> listnew = new ArrayList<String>();

		boolean isStart = false, isEnd = true;
		boolean isHasTag = false, isAllChange = false, isNoHaveChange = false, isChangeTag = false;

		boolean isA = false;// +

		int changeSize = 0;
		int getStart = 0, getEnd = 0;
		int noHaveChange = 0;
		for (String info : list) {
			String newinfo = info;
			if (!isStart) {
				if (info.contains(key) && info.contains("{..{")) {
					// System.out.println("Start");

					isHasTag = true;

					if (info.contains("}..}")) {
						if (!isZhuShi) {
							if (newinfo.contains("false")) {
								newinfo = info.replace("false", "true");
								isChangeTag = true;
							}
						} else {
							if (newinfo.contains("true")) {
								newinfo = info.replace("true", "false");
								isChangeTag = true;
							}
						}

						listnew.add(newinfo);
						continue;
					}

					isA = info.contains("+");

					isStart = true;
					isEnd = false;
					listnew.add(newinfo);
					getStart++;
					continue;
				}

			} else {
				if (info.contains(key) && info.contains("}..}")) {
					isStart = false;
					isEnd = true;
					// System.out.println("end");
					listnew.add(newinfo);
					getEnd++;
					continue;
				}

				if (type == 1) {// java
					if (isZhuShi) {
						if (isA) {
							if (info.startsWith(javahead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = javahead + info;
							}
						} else {
							if (!info.startsWith(javahead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = info.replace(javahead, "");
							}
						}
					} else {
						if (isA) {
							if (!info.startsWith(javahead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = info.replace(javahead, "");
							}
						} else {

							if (info.startsWith(javahead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = javahead + info;
							}
						}

					}
				} else if (type == 2) {

					if (isZhuShi) {

						if (isA) {
							if (info.startsWith(xmlhead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = xmlhead + info + xmlend;
							}
						} else {
							if (!info.startsWith(xmlhead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = info.replace(xmlhead, "").replace(
										xmlend, "");
							}
						}
					} else {

						if (isA) {
							if (!info.startsWith(xmlhead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = info.replace(xmlhead, "").replace(
										xmlend, "");
							}
						} else {
							if (!info.startsWith(xmlhead)) {
								noHaveChange++;
							} else {
								changeSize++;
								newinfo = info.replace(xmlhead, "").replace(
										xmlend, "");
							}
						}
					}
				}
			}

			// System.out.println(newinfo);
			listnew.add(newinfo);
		}

		if (isStart || !isEnd) {
			throw new IOException("上下文不呼应{。。{  NNNNN }。。},请修改便签"
					+ olddir.getAbsolutePath());
		}

		if (changeSize + getEnd + getStart == listnew.size()) {
			isAllChange = true;
		}

		if (noHaveChange > 0 && changeSize == 0) {
			isNoHaveChange = true;
		}

		if (isHasTag) {
			if (changeSize > 0 || isChangeTag) {
				// olddir.createNewFile();// 建立新文件
				if (!isAllChange) {
					System.out.println("修改" + FileUtil.getSUName(olddir, f));
				} else {
					System.err.println("完全修改" + olddir.getAbsolutePath());
				}
				Su_UTF_FileWriter writer = new Su_UTF_FileWriter(olddir);
				writer.writeUTF8(listnew, olddir);
				
			} else {
				if (isNoHaveChange) {
					System.err.println("不是本次修改完成："
							+ FileUtil.getSUName(olddir, f));
				} else {
					System.out.println("标签" + FileUtil.getSUName(olddir, f));
				}
			}
		}
	}

	String f, m, zhs;
	String key;
	boolean isZhuShi;

	private void getSetting(String[] args) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String info : args) {
			sb.append(info);
		}

		String s = sb.toString();
		String ss[] = s.split("-");
		if (ss == null || ss.length == 0) {
			throw new Exception("no setting ");
		}

		for (String sss : ss) {
			if (sss != null && sss.length() > 0) {
				if (sss.startsWith("f")) {
					f = sss.replace("f", "").trim();
				} else if (sss.startsWith("m")) {
					m = sss.replace("m", "").trim();
				} else if (sss.startsWith("zhs")) {
					zhs = sss.replace("zhs", "").trim();
				}
			}
		}

		if (StringUtil.isEmpty(f) || StringUtil.isEmpty(m)
				|| StringUtil.isEmpty(zhs)
				|| (!"t".equals(zhs) && !"f".equals(zhs))) {
			throw new Exception("参数错误" + sb.toString());
		}

		key = "$$" + m + "&&";
		isZhuShi = zhs.equals("t");

		javahead = javahead + " " + key;
		xmlhead = xmlhead + " " + key;
		xmlend = key + " " + xmlend;
		target = "工程：" + f + "; 修改" + m + "; 注释" + (isZhuShi ? "关闭" : "打开");
		System.out.println(target);
		System.out.println();
		System.out.println();
	}

}
