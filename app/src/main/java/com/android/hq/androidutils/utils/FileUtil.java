
package com.android.hq.androidutils.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * Created by heqiang on 17-2-5.
 *
 * Java实现对文件的复制、删除
 * 计算目录大小
 * 获取文件MD5功能
 */
public class FileUtil {
	private final static String TAG = "FileUtil";

	/**
	 * 
	 * copy file
	 * base on nio
	 *
	 * @param src
	 *            source file
	 * @param dest
	 *            target file
	 * @throws IOException
	 */
	public static void copyFile(File src, File dest) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			if (!dest.exists()) {
				dest.createNewFile();
			}
			inChannel = new FileInputStream(src).getChannel();
			outChannel = new FileOutputStream(dest).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	/**
	 * delete file
	 * 
	 * @param file
	 *            file
	 * @return true if delete success
	 */
	public static boolean deleteFile(File file) {
		if (!file.exists()) {
			return true;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFile(f);
			}
		}
		return file.delete();
	}

	/**
	 * get dir size
	 *
	 * @param file
	 * @return M
     */
	public static double getDirSize(File file) {
		if(!file.exists()) {
			Log.w(TAG, file.toString() + " may not exists !");
			return 0.0D;
		} else if(!file.isDirectory()) {
			double size = (double)file.length() / 1024.0D / 1024.0D;
			return size;
		} else {
			File[] files = file.listFiles();
			double size = 0.0D;
			int length = files.length;

			for(int i = 0; i < length; ++i) {
				File f = files[i];
				size += getDirSize(f);
			}
			return size;
		}
	}

	/**
	 * get md5
	 *
	 * @param file
	 * @return
     */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[8192];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			Log.e(TAG, "getFileMD5", e);
			return null;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				Log.e(TAG, "getFileMD5", e);
			}
		}
		BigInteger bigInt = new BigInteger(digest.digest());
		return bigInt.toString();
	}
}
