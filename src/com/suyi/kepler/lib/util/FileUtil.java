package com.suyi.kepler.lib.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static File[] getDirList(String path) throws Exception {
		// TODO Auto-generated method stub
		File dir = new File(path);
		if (!dir.exists()) {
			throw new Exception(path + " is not exists");
		}
		File[] fs = dir.listFiles();
		if (fs == null || fs.length == 0) {
			throw new Exception(path + " is null");
		}
		return fs;
	}
	
	
    // 复制文件夹   
    public static void copyDirectiory(String sourceDir, String targetDir)  
        throws IOException {  
        // 新建目标目录   
        (new File(targetDir)).mkdirs();  
        // 获取源文件夹当前下的文件或目录   
        File[] file = (new File(sourceDir)).listFiles();  
        for (int i = 0; i < file.length; i++) {  
            if (file[i].isFile()) {  
                // 源文件   
                File sourceFile=file[i];  
                // 目标文件   
               File targetFile=new File(new File(targetDir).getAbsolutePath()+File.separator+file[i].getName());  
               copyFile(sourceFile,targetFile);  
            }  
            if (file[i].isDirectory()) {  
                // 准备复制的源文件夹   
                String dir1=sourceDir + "/" + file[i].getName();  
                // 准备复制的目标文件夹   
                String dir2=targetDir + "/"+ file[i].getName();  
                copyDirectiory(dir1, dir2);  
            }  
        }  
    }  


	public static void copyDir(String tar, String src) throws IOException {

		// TODO Auto-generated method stub
		copyDirectiory(src, tar);
	}

	public static void copyFile(File sourcefile, File targetFile)
			throws IOException {

		FileInputStream input = new FileInputStream(sourcefile);
		BufferedInputStream inbuff = new BufferedInputStream(input);

		FileOutputStream out = new FileOutputStream(targetFile);
		BufferedOutputStream outbuff = new BufferedOutputStream(out);

		byte[] b = new byte[1024 * 30];
		int len = 0;
		while ((len = inbuff.read(b)) != -1) {
			outbuff.write(b, 0, len);
		}

		outbuff.flush();

		inbuff.close();
		outbuff.close();
		out.close();
		input.close();

	}

	public static void copyFile(String src, String tar) throws Exception {
		// TODO Auto-generated method stub

		File fs = new File(src);
		if (!fs.exists()) {
			throw new Exception(src + " src null");
		}
		
		File str = new File(tar);
		if(!str.exists()){
//			str.mkdirs();
			str.createNewFile();
		}
		
		copyFile(fs, str);
	}

	public static void deleteDir(String name) {
		// TODO Auto-generated method stub
		deleteDir(new File(name));
	}

	private static boolean deleteDir(File dir) {
		if (!dir.exists())
			return true;
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}


	public static String getSUName(File olddir, String f) {
		// TODO Auto-generated method stub
		if(olddir!=null){
			return olddir.getAbsolutePath().replace(f, "");
		}
		return null;
	}

}
