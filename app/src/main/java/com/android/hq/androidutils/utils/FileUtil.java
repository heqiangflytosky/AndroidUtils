
package com.android.hq.androidutils.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by heqiang on 17-2-5.
 *
 * Java实现对文件的复制、保存、读取和删除
 * 计算目录大小
 * 获取文件MD5功能
 * 解压zip文件
 * 获取文件扩展名
 * 获取文件名字（去掉扩展名）
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
	public static void copyFile(File src, File dest){
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			if (!dest.exists()) {
				dest.createNewFile();
			}
			inChannel = new FileInputStream(src).getChannel();
			outChannel = new FileOutputStream(dest).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		}catch (IOException e){
			Log.e(TAG,"copyFile error",e);
		}finally {
			closeQuietly(inChannel);
			closeQuietly(outChannel);
		}
	}

	/**
	 * Copy file
	 *
	 * @return true if succeed, false if fail
	 */
	public static boolean copyFile2(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = saveToFile(in, destFile);
			} finally {
				in.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "Fail to copyFile, srcFile=" + srcFile + ", destFile=" + destFile, e);
			result = false;
		}
		return result;
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

	public static void createFile(File target) {
		if (!target.exists()) {
			try {
				File fileParent = target.getParentFile();
				if(!fileParent.exists()) {
					fileParent.mkdirs();
				}
				target.createNewFile();
			} catch (IOException e) {
				Log.e(TAG,"create file error",e);
			}
		}
	}

	/**
	 * Copy data from a source stream to destFile
	 *
	 * @return true if succeed, false if failed.
	 */
	public static boolean saveToFile(InputStream inputStream, File destFile) {
		if (inputStream == null || destFile == null) {
			return false;
		}
		FileOutputStream out = null;
		try {
			if (destFile.exists()) {
				if (!destFile.delete()) {
					return false;
				}
			}
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) >= 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
			out.getFD().sync();
			return true;
		} catch (IOException e) {
			Log.e(TAG, "Fail to saveToFile, destFile=" + destFile, e);
		} finally {
			closeQuietly(out);
		}
		return false;
	}

	public static boolean saveToFile(byte[] data, File destFile) {
		if (data == null) {
			return false;
		}
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(destFile);
			output.write(data);
			return true;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Fail to saveToFile, destFile=" + destFile, e);
		} catch (IOException e) {
			Log.e(TAG, "Fail to saveToFile, destFile=" + destFile, e);
		} finally {
			closeQuietly(output);
		}
		return false;
	}

	public static boolean saveToFile(ByteBuffer byteBuffer, int position, File destFile) {
		if (null == byteBuffer) {
			return false;
		}
		FileChannel channel = null;
		RandomAccessFile file= null;
		try {
			file = new RandomAccessFile(destFile, "rw");
			channel = file.getChannel();
			channel.write(byteBuffer, position);
			return true;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Fail to saveToFile, destFile=" + destFile, e);
		} catch (IOException e) {
			Log.e(TAG, "Fail to saveToFile, destFile=" + destFile, e);
		} finally {
			closeQuietly(channel);
			closeQuietly(file);
		}
		return false;
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


	public static String getFileNameWithoutExtension(File file) {
		String name = file.getName();
		int lastDotIndex = name.lastIndexOf('.');
		if (lastDotIndex >= 0) {
			return name.substring(0, lastDotIndex);
		} else {
			return name;
		}
	}

	public static String getFileNameWithoutExtension(String fileName) {
		if (!TextUtils.isEmpty(fileName)) {
			int lastDotIndex = fileName.lastIndexOf('.');
			if (lastDotIndex > -1) {
				return fileName.substring(0, lastDotIndex);
			}
		}
		return fileName;
	}

	public static String readFileAsString(File file) throws IOException {
		return new String(readFileAsBytes(file), Charset.forName("UTF-8"));
	}

	public static String readFileAsString(String path) throws IOException {
		return readFileAsString(path, "UTF-8");
	}

	/**
	 * Returns the contents of 'path' as a string.
	 */
	public static String readFileAsString(String path, String encoding) throws IOException {
		return new String(readFileAsBytes(path), Charset.forName(encoding));
	}

	public static String readUriAsString(Context context, Uri uri) throws IOException {
		InputStream is = context.getContentResolver().openInputStream(uri);
		return readStreamAsString(is, "UTF-8", true);
	}

	/**
	 * Returns the contents of 'path' as a string. The contents are assumed to be UTF-8.
	 */
	public static String readStreamAsString(InputStream input, boolean autoClose) throws IOException {
		return readStreamAsString(input, "UTF-8", autoClose);
	}

	/**
	 * Returns the contents of 'path' as a string.
	 */
	public static String readStreamAsString(InputStream input, String encoding, boolean autoClose) throws IOException {
		return new String(readStreamAsBytes(input, 0, autoClose), Charset.forName(encoding));
	}

	public static byte[] readFileAsBytes(String path) throws IOException {
		return readFileAsBytes(new File(path));
	}

	public static byte[] readFileAsBytes(File file) throws IOException {
		FileInputStream f = null;
		try {
			f = new FileInputStream(file);
			return readStreamAsBytes(f, (int)(file.length()), true);
		} finally {
			closeQuietly(f);
		}
	}

	public static byte[] readStreamAsBytes(InputStream input, int length, boolean autoClose) throws IOException {
		try {
			ByteArrayOutputStream stream;
			if (length > 0)  {
				stream = new ByteArrayOutputStream(length);
			} else {
				stream = new ByteArrayOutputStream();
			}
			byte[] buffer = new byte[8192];
			while (true) {
				int byteCount = input.read(buffer);
				if (byteCount == -1) {
					return stream.toByteArray();
				}
				stream.write(buffer, 0, byteCount);
			}
		} finally {
			if (autoClose) {
				try {
					if (input != null) {
						input.close();
					}
				} catch (IOException e) {
					// ignore this exception
				}
			}
		}
	}

	public static ByteBuffer readStreamAsBuffer(InputStream input, int position, int length, boolean autoClose) throws IOException {
		try {
			if (position < 0 || length < 0) {
				throw new IndexOutOfBoundsException();
			}
			long skipped = input.skip(position);
			if (skipped < 0) {
				throw new IOException(TAG + ": readStreamAsBuffer: unexpected EOF");
			}
			int bufferMaxSize = 8192;
			int size = Math.min(bufferMaxSize, length);

			int remaining = length;
			byte[] bytes = new byte[size];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			while (remaining > 0) {
				int byteCount = input.read(bytes, 0, Math.min(size, remaining));
				if (byteCount < 0) {
					break;
				}
				stream.write(bytes, 0, byteCount);
				remaining -= byteCount;
			}
			int total = stream.size();
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(total);
			byteBuffer.put(stream.toByteArray(), 0, total);
			return byteBuffer;
		} finally {
			if (autoClose) {
				closeQuietly(input);
			}
		}
	}


	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				Log.e(TAG, "close error:", e);
			}
		}
	}
}
