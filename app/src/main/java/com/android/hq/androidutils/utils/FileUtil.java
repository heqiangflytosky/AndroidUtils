
package com.android.hq.androidutils.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by heqiang on 17-2-5.
 *
 * Java实现对文件的复制、删除
 * 计算目录大小
 * 获取文件MD5功能
 * 解压zip文件
 * 获取文件扩展名
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

	public static boolean unZip(File zipFile, File targetDirectory) {
		Log.d(TAG, "unzip: " + zipFile.getAbsolutePath() + ", " + targetDirectory.getAbsolutePath());
		InputStream is;
		ZipInputStream zis;
		try {
			String filename;
			is = new FileInputStream(zipFile);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];
			int count;

			while ((ze = zis.getNextEntry()) != null) {
				filename = ze.getName();
				Log.d(TAG, "unzip entry: " + filename);
				if (ze.isDirectory()) {
					File directory = new File(targetDirectory, filename);
					directory.mkdirs();
					continue;
				}

				File targetFile = new File(targetDirectory, filename);
				File targetParentFile = targetFile.getParentFile();
				if (!targetParentFile.exists()) {
					targetParentFile.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(targetFile);

				while ((count = zis.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				zis.closeEntry();
			}

			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String getFileExtension(File file) {
		String name = file.getName();
		int lastDotIndex = name.lastIndexOf('.');
		if (lastDotIndex >= 0) {
			return name.substring(lastDotIndex);
		} else {
			return "";
		}
	}
}
