package com.arcadiaprep.app.arca.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	public static boolean copy(InputStream in,OutputStream out){
		byte[] buffer = new byte[512];
		int i =0;
		try {
			while((i=in.read(buffer))>0){
				out.write(buffer, 0, i);
			}
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	public static boolean createDirectory(String path) {
		path = path.replace('/', File.separatorChar);
		File file = new File(path);
		if (file.isDirectory()) {
			return true;
		} else {//
			return makedir(path);
		}
	}

	/**
	 * Method:makedir(String dirPath) Description: 
	 * 
	 * @param cstrPath
	 *            eturn boolean
	 */
	public static final boolean makedir(String dirPath) {
		boolean bRet = true;
		String lpcstrParent;

		String cstrParent;
		int iPos = 0;
		int iLen;

		if (dirPath == null || dirPath.equals(""))
			return false;

		iLen = dirPath.length();
		if (dirPath.lastIndexOf('\\') != -1)
			iPos = dirPath.lastIndexOf('\\');
		else
			iPos = dirPath.lastIndexOf('/');

		cstrParent = dirPath.substring(0, iPos);

		if (cstrParent == null || cstrParent.equals(""))
			return false;  
		lpcstrParent = cstrParent.substring(0, cstrParent.length());
		if (cstrParent.length() > 3) { 
			bRet = exist(lpcstrParent);
		}

		if (!bRet)
			bRet = makedir(lpcstrParent);

		if (bRet) {
			File newDir = new File(dirPath);
			if (!newDir.exists()) {
				newDir.mkdirs();
				bRet = true;
			}
		}
		return bRet;
	}
	
	/**
	 * Method:exist(String file) Description:
	 * 
	 * @param file
	 *           return boolean
	 */
	public static final boolean exist(String file) {
		boolean bRet = false;
		File newFile = new File(file);
		bRet = newFile.exists(); 
		return bRet;
	}
	
}
