package com.suyi.kepler.packagefor3;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.suyi.kepler.lib.util.FileUtil;
import com.suyi.kepler.lib.util.StringUtil;
import com.suyi.kepler.lib.util.Su_UTF_FileReader;
import com.suyi.kepler.lib.util.Su_UTF_FileWriter;
import com.suyi.kepler.lib.util.ZipCompressor;

public class PackageFor3Main {

	public static String path = "C:/Users/suweiguang/Desktop/disanfang";
	public static String ApkLib_path = "E:/android/workspace/jd_apklib";
	public static String Kjd_Demo_path = "E:/android/workspace/Kjd_Demo";
	public static String jd_apklib = "jd_apklib";
	public static String Kjd_Demo = "Kjd_Demo";
	public static String image = "safe.jpg";
	public static String image_path = "res/drawable";

	public static String oldPackage = "com.example.sdklogindemo";
	public static String oldPID = "7DA9CEF6540029AA95E09135D3AB4AED";
	public static String oldSec = "e74f246c3bee44b5b968a5326614f402";

	public static String properties = "project.properties";
	public static String projectLib = "projectLib.properties";

	public static String[] delete_lib_path = {"libs/JDSDK_h.jar", "/historylibs","bin","gen","proguard-project.txt","lint.xml",".git" };
	
	public static String[] delete_dm_path = { ".git", "dev.MD", "debug.keystore",
		"/gen", projectLib };

	String apkLibPathNew, demoPathNew, packageName, newPID, newSec;
	private boolean isDev = false;//打包zip只留下zip

	HashMap<String, String> rePmap = new HashMap<String, String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("采集N信息，并直接动态生成工程，进行执行");

		PackageFor3Main mKepLib = new PackageFor3Main();

		try {
			mKepLib.getPackageName_Image();

			mKepLib.copyLibPack();
			mKepLib.setLibjar();
			mKepLib.changeImage();
			mKepLib.deleLib();
			
			

			mKepLib.copyDemo();
			mKepLib.changepackName();
			mKepLib.setLibMode();// 修改lib的引用 指向新的
			mKepLib.deleDemo();

			mKepLib.zip();
			 
			

			System.out.println("完成：" + mKepLib.apkLibPathNew);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deleLib() {
		// TODO Auto-generated method stub
		
		for (String fn : delete_lib_path) {
			String name = apkLibPathNew + "/" + fn;
			FileUtil.deleteDir(name);
		}
		
	}

	private void setLibjar() throws Exception {
		// TODO Auto-generated method stub

		String srcName = getHisJar();
		String src = ApkLib_path + "/historylibs/" + srcName;
		
		if (!new File(src).exists())
			throw new Exception("no used jar " + src);
		
		String to = apkLibPathNew + "/libs/" + srcName;

		FileUtil.copyFile(src, to);

		if (!new File(to).exists())
			throw new Exception("copy 失败");

		try {
			System.out.println("使用版本" + src.split("historylibs")[1]);
		} catch (Exception e) {

		}

		
	}

	private String getHisJar() throws Exception {
		// TODO Auto-generated method stub

		File[] list = FileUtil.getDirList(ApkLib_path + "/historylibs");

		for (File f : list) {
			String name = f.getName();
			if (name.endsWith("Use")) {
				return name.replace("Use", "jar").replace("now_", "");
			}
		}

		throw new Exception(" Ujar is null");
	}

	private void setLibMode() throws IOException {
		// TODO Auto-generated method stub
		String libproperties = demoPathNew + "/" + properties;
		String libpropertiesneed = demoPathNew + "/" + projectLib;

		File need = new File(libpropertiesneed);
		need.mkdirs();
		need.createNewFile();
		FileUtil.copyFile(new File(libproperties), need);

	}

	private void zip() throws IOException {
		// TODO Auto-generated method stub
		
		String allzipName=path + "/" + packageName + ".zip";
		
		new ZipCompressor(allzipName).compressExe(path
				+ "/" + packageName);
		
		new ZipCompressor(apkLibPathNew +".zip").compressExe(apkLibPathNew);
		
		FileUtil.copyFile(new File(allzipName), new File(path + "/" + packageName+"/"+packageName+".zip"));
		
//		FileUtil.deleteDir(allzipName);
		
	}

	private void deleDemo() {
		// TODO Auto-generated method stub

		for (String fn : delete_dm_path) {
			String name = demoPathNew + "/" + fn;
			FileUtil.deleteDir(name);

		}

	}

	private void changepackName() throws IOException {
		// TODO Auto-generated method stub
		changePackageName(new File(demoPathNew));
	}

	private void changePackageName(File olddir) throws IOException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		if (olddir.isDirectory()) {

			// {// copy dir
			// String newfilepath = olddir.getAbsolutePath().replace(
			// packagePath, newdirstr);
			// File newFile = new File(newfilepath);
			// newFile.mkdirs();
			// }

			File[] files = olddir.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					changePackageName(f);
				}
			}
		} else if (olddir.isFile()
				&& (!olddir.getAbsolutePath().endsWith(".class"))) {

			if (olddir.getAbsolutePath().endsWith(".java")
					|| olddir.getAbsolutePath().endsWith(".xml")) {

				changePackageInFile(olddir);
			}

		}

	}

	private void changePackageInFile(File olddir) throws IOException {
		// TODO Auto-generated method stub

		Su_UTF_FileReader reader = new Su_UTF_FileReader(olddir, rePmap);

		List<String> list = reader.read();

		olddir.createNewFile();// 建立新文件
		Su_UTF_FileWriter writer = new Su_UTF_FileWriter(olddir);
		writer.writeUTF8(list, olddir);

	}

	private void copyDemo() throws IOException {
		// TODO Auto-generated method stub
		demoPathNew = path + "/" + packageName + "/" + Kjd_Demo;
		FileUtil.copyDir(demoPathNew, Kjd_Demo_path);
	}

	private void changeImage() throws Exception {
		// TODO Auto-generated method stub

		FileUtil.copyFile(path + "/当前配置/" + image, apkLibPathNew + "/" + image_path
				+ "/" + image);

	}

	private void copyLibPack() throws IOException {
		// TODO Auto-generated method stub

		apkLibPathNew = path + "/" + packageName + "/" + jd_apklib;

		if (new File(apkLibPathNew).exists()) {
			if (isDev) {
				System.out.println("存在 删除" + packageName);
				FileUtil.deleteDir(apkLibPathNew);
			}
		}

		FileUtil.copyDir(apkLibPathNew, ApkLib_path);
	}

	private void getPackageName_Image() throws Exception {
		// TODO Auto-generated method stub

		File[] list = FileUtil.getDirList(path+"/当前配置");

		boolean is = false;
		for (File f : list) {
			String name = f.getName();
			if (name.endsWith("NAM")) {
				packageName = name.replace(".NAM", "");
			}
			if (name.endsWith(".PID")) {
				newPID = name.replace(".PID", "");
			}
			if (name.endsWith(".SEC")) {
				newSec = name.replace(".SEC", "");
			} else if (name.equals(image)) {
				is = true;
			}
		}

		if (!is || StringUtil.isEmpty(packageName)
				|| StringUtil.isEmpty(newPID) || StringUtil.isEmpty(newSec)) {
			throw new Exception(
					" safe image；packageName ；newPID  newSec ；is null");
		}

		rePmap.put(oldPackage, packageName);
		rePmap.put(oldPID, newPID);
		rePmap.put(oldSec, newSec);

	}

}
