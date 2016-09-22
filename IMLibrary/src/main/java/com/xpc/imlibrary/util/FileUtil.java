package com.xpc.imlibrary.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;

import com.xpc.imlibrary.config.IMConstant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * 文件工具类
 * 
 * @author qiaocb
 * @time 2015-10-26 下午3:13:13
 */
public class FileUtil {
	private static final int FILE_BUFFER_SIZE = 51200;
	private static final String TAG = FileUtil.class.getSimpleName();
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	private Context context;

	public FileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 是否有SD卡
	 * 
	 * @return
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		String sdPath = "";
		if (isExternalStorageWritable()) {
			// 得到当前外部存储设备的目录( /SDCARD )
			sdPath = Environment.getExternalStorageDirectory() + "";
		} else {
			sdPath = Environment.getExternalStorageDirectory().toString();// 获取跟目录
		}
		return sdPath;
	}

	/**
	 * 获取当前应用 SD卡的绝对路径
	 * 
	 * @return String:路径 null获取失败
	 */
	public String getAbsolutePath() {
		File root = context.getExternalFilesDir(null);
		if (root != null) {
			return root.getAbsolutePath();
		}
		return null;
	}

	/**
	 * 获取当前应用的 SD卡缓存文件夹绝对路径
	 * 
	 * @return String:路径 null获取失败
	 */
	public String getCachePath() {
		File root = context.getExternalCacheDir();
		if (root != null) {
			return root.getAbsolutePath();
		}
		return null;
	}

	/**
	 * 获取文件的目录
	 *            路径
	 * @return 目录
	 */
	public static String getPath(String pathName) {
		int end = pathName.lastIndexOf("/");
		if (end != -1) {
			return pathName.substring(0, end);
		} else {
			return null;
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 *            路径
	 * @return 文件名称
	 */
	public static String getFileName(String path) {
		if (StringUtil.isEmpty(path)) {
			return null;
		}
		File f = new File(path);
		String name = f.getName();
		f = null;
		return name;
	}

	/**
	 * 获取文件路径中的名称(路径最后一个/后面的为名称)
	 * 
	 * @param pathandname
	 *            路径
	 * @return 名称
	 */
	public static String getPathFileName(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.length();
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return null;
		}
	}
	
	/**
	 * 获取文件路径中的名称(无后缀)(路径最后一个/后面的为名称)
	 * 
	 * @param pathandname
	 *            路径
	 * @return 名称
	 */
	public static String getPathFileNameNoSuffix(String pathandname) {
		int start = pathandname.lastIndexOf("/");
		int end = pathandname.indexOf(".");
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, end);
		} else {
			return null;
		}
	}

	/**
	 * 判断文件路径是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return boolean true:存在 false:不存在
	 */
	public static boolean isExistFile(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			MyLog.e("param invalid, filePath: " + filePath);
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 读取文件(相对路径)
	 * 
	 * @param filePath
	 *            文件相对路径
	 * @return InputStream
	 */
	public static InputStream readFileToInputStream(String filePath) {
		if (null == filePath) {
			MyLog.e("Invalid param. filePath: " + filePath);
			return null;
		}
		InputStream is = null;
		try {
			if (isExistFile(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			MyLog.e("Exception, ex: " + ex.toString());
			return null;
		}
		return is;
	}

	/**
	 * 读取文件(绝对路径)
	 * 
	 * @param context
	 *            当前上下文
	 * @param mLocalImageUri
	 *            绝对路径
	 * @return InputStream
	 */
	public static InputStream getInputStreamFromUri(Context context,
													Uri mLocalImageUri) {
		InputStream in = null;
		try {
			if (mLocalImageUri.getScheme() == null) {
				// 绝对路径
				File file = new File(mLocalImageUri.toString());
				in = new FileInputStream(file);
			} else if (mLocalImageUri.getScheme().equalsIgnoreCase("content")
					|| mLocalImageUri.getScheme().equalsIgnoreCase("file")) {
				// content://xxx或file://xxx格式的本地uri
				in = context.getContentResolver().openInputStream(
						mLocalImageUri);
			}
		} catch (Exception e) {
			MyLog.e("IOexception");
			e.printStackTrace();
		}
		return in;
	}

	/**
	 * 读取文件(文件路径)
	 * 
	 * @param filePath
	 * @return String
	 */
	public String readFileToString(String filePath) {
		if (!isExternalStorageWritable()) {
			MyLog.i(TAG, "SD卡不可用，保存失败");
			return null;
		}

		File file = new File(filePath);
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			reader.close();

		} catch (FileNotFoundException e) {
			MyLog.i(TAG, e.getMessage());
		} catch (IOException e) {
			MyLog.i(TAG, e.getMessage());
		}

		return sb.toString();
	}

	/**
	 * 读文件(文件uri地址)
	 * 
	 * @param context
	 *            当前上下文
	 * @param uri
	 *            文件地址
	 * @return byte[]
	 */
	public static byte[] readFile(Context context, Uri uri) {
		if (null == context || null == uri) {
			MyLog.e("Invalid param. ctx: " + context + ", uri: " + uri);
			return null;
		}

		InputStream is = null;
		String scheme = uri.getScheme();
		if (scheme.equalsIgnoreCase("file")) {
			is = readFileToInputStream(uri.getPath());
		}

		try {
			is = context.getContentResolver().openInputStream(uri);
			if (null == is) {
				return null;
			}

			byte[] bret = input2byte(is);
			is.close();
			is = null;

			return bret;
		} catch (FileNotFoundException fne) {
			MyLog.e("FilNotFoundException, ex: " + fne.toString());
		} catch (Exception ex) {
			MyLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (Exception ex) {
				}
				;
			}
		}
		return null;
	}

	/**
	 * 读取文件内容，从第startLine行开始，读取lineCount行
	 * 
	 * @param file
	 *            文件
	 * @param startLine
	 *            开始行
	 * @param lineCount
	 *            末尾行
	 * @return 读到文字的list,如果list.size<lineCount则说明读到文件末尾了
	 */
	public static List<String> readFile(File file, int startLine, int lineCount) {
		if (file == null || startLine < 1 || lineCount < 1) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		FileReader fileReader = null;
		List<String> list = null;
		LineNumberReader lnr = null;
		try {
			list = new ArrayList<String>();
			fileReader = new FileReader(file);
			lnr = new LineNumberReader(fileReader);
			boolean end = false;
			for (int i = 1; i < startLine; i++) {
				if (lnr.readLine() == null) {
					end = true;
					break;
				}
			}
			if (end == false) {
				for (int i = startLine; i < startLine + lineCount; i++) {
					String line = lnr.readLine();
					if (line == null) {
						break;
					}
					list.add(line);

				}
			}
		} catch (Exception e) {
			// MyLog.e(TAG, "read log error!", e);
		} finally {
			if (lnr != null) {
				try {
					lnr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 创建目录
	 * 
	 *            文件路径
	 * @return boolean true:创建成功 false:失败
	 */
	public static boolean createDir(String pathName) {
		File file = null;
		file = new File(pathName);
		if (!file.exists()) {
			String filePath = getPath(pathName);
			file = new File(filePath);
			if (!file.exists())
				return file.mkdirs(); // 文件不存在则创建目录
		}
		return true;
	}

	/**
	 * 删除指定目录或文件
	 * 
	 * @param filePath
	 *            目录
	 * @return boolean true:删除成功 false:删除失败
	 */
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			MyLog.e("Invalid param. filePath: " + filePath);
			return false;
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}
		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				MyLog.d("delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}
		MyLog.d("delete filePath: " + file.getAbsolutePath());
		file.delete();
		return true;
	}

	/**
	 * 删除 files目录下所有文件
	 * 
	 * @return
	 */
	public boolean deleteAllFile() {
		if (!isExternalStorageWritable()) {
			MyLog.i(TAG, "SD卡不可用，保存失败");
			return false;
		}
		File dir = context.getExternalFilesDir(null);
		if (dir != null) {
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}

		return true;
	}

	/**
	 * 计算 files目录下文件的大小
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public String getCacheSize(String path) {
		if (!isExternalStorageWritable()) {
			MyLog.i(TAG, "SD卡不可用，保存失败");
			return null;
		}

		if (null == path) {
			MyLog.e("Invalid param. filePath: " + path);
			return null;
		}
		File fileDir = new File(path);
		if (fileDir == null || !fileDir.exists()) {
			return null;
		}

		long sum = 0;

		if (fileDir != null) {
			for (File file : fileDir.listFiles()) {
				sum += file.length();
			}
		}

		if (sum < 1024) {
			return sum + "字节";
		} else if (sum < 1024 * 1024) {
			return (sum / 1024) + "K";
		} else {
			return (sum / (1024 * 1024)) + "M";
		}
	}

	/**
	 * 计算 files目录下文件的大小
	 * 
	 * @return
	 */
	public String getCacheSize() {
		if (!isExternalStorageWritable()) {
			MyLog.i(TAG, "SD卡不可用，保存失败");
			return null;
		}

		long sum = 0;
		File dir = context.getExternalFilesDir(null);

		if (dir != null) {
			for (File file : dir.listFiles()) {
				sum += file.length();
			}
		}

		if (sum < 1024) {
			return sum + "字节";
		} else if (sum < 1024 * 1024) {
			return (sum / 1024) + "K";
		} else {
			return (sum / (1024 * 1024)) + "M";
		}
	}

	/**
	 * 写文件(文件流)
	 * 
	 * @param filePath
	 *            写入路径
	 * @param inputStream
	 *            文件流
	 * @return boolean true:写入成功 false:写入失败
	 */
	public static boolean writeFile(String filePath, InputStream inputStream) {
		if (null == filePath || filePath.length() < 1) {
			MyLog.e("Invalid param. filePath: " + filePath);
			return false;
		}
		try {
			File file = new File(filePath);
			if (file.exists()) {// 文件存在
				deleteDirectory(filePath);
			}

			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			boolean ret = createDir(pth);
			if (!ret) {
				MyLog.e("createDirectory fail path = " + pth);
				return false;
			}

			boolean isCreateSuccess = file.createNewFile();
			if (!isCreateSuccess) {// 创建失败
				MyLog.e("createNewFile fail filePath = " + filePath);
				return false;
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int c = inputStream.read(buf);
			while (-1 != c) {
				fileOutputStream.write(buf, 0, c);
				c = inputStream.read(buf);
			}

			fileOutputStream.flush();
			fileOutputStream.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 写文件(文件内容)(不续写)
	 * 
	 * @param filePath
	 *            写入路径
	 * @param fileContent
	 *            文件内容
	 * @return boolean true:写入成功 false:写入失败
	 */
	public static boolean writeFile(String filePath, String fileContent) {
		return writeFile(filePath, fileContent, false);
	}

	/**
	 * 写文件(文件内容)(续写)
	 * 
	 * @param filePath
	 *            写入路径
	 * @param fileContent
	 *            文件内容
	 * @param append
	 * @return boolean true:写入成功 false:写入失败
	 */
	public static boolean writeFile(String filePath, String fileContent,
									boolean append) {
		if (null == filePath || fileContent == null || filePath.length() < 1
				|| fileContent.length() < 1) {
			MyLog.e("Invalid param. filePath: " + filePath
					+ ", fileContent: " + fileContent);
			return false;
		}

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					return false;
				}
			}

			BufferedWriter output = new BufferedWriter(new FileWriter(file,
					append));
			output.write(fileContent);
			output.flush();
			output.close();
		} catch (IOException ioe) {
			MyLog.e("writeFile ioe: " + ioe.toString());
			return false;
		}
		return true;
	}

	/**
	 * 写文件(byte[])
	 * 
	 * @param filePath
	 *            写入路径
	 * @param content
	 *            写入byte[]
	 * @return boolean boolean 是否成功
	 */
	public static boolean writeFile(String filePath, byte[] content) {
		if (null == filePath || null == content) {
			MyLog.e("Invalid param. filePath: " + filePath + ", content: "
					+ content);
			return false;
		}

		FileOutputStream fos = null;
		try {
			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			File pf = null;
			pf = new File(pth);
			if (pf.exists() && !pf.isDirectory()) {
				pf.delete();
			}
			pf = new File(filePath);
			if (pf.exists()) {
				if (pf.isDirectory()) {
					deleteDirectory(filePath);
				} else {
					pf.delete();
				}
			}

			pf = new File(pth + File.separator);
			if (!pf.exists()) {
				if (!pf.mkdirs()) {
					MyLog.e("Can't make dirs, path=" + pth);
				}
			}

			fos = new FileOutputStream(filePath);
			fos.write(content);
			fos.flush();
			fos.close();
			fos = null;
			pf.setLastModified(System.currentTimeMillis());

			return true;

		} catch (Exception ex) {
			MyLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception ex) {
				}
				;
			}
		}
		return false;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 *            文件路径
	 * @return long 文件大小（B）
	 */
	public static long getFileSize(String filePath) {
		if (null == filePath) {
			MyLog.e("Invalid param. filePath: " + filePath);
			return 0;
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}
		return file.length();
	}

	/**
	 * 获取文件最后修改时间
	 * 
	 * @param filePath
	 * @return long (用该时间与历元（1970 年 1 月 1 日，00:00:00 GMT）的时间差来计算此值（以毫秒为单位）)
	 */
	public static long getFileModifyTime(String filePath) {
		if (null == filePath) {
			MyLog.e("Invalid param. filePath: " + filePath);
			return 0;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}
		return file.lastModified();
	}

	/**
	 * 设置文件最后修改时间
	 * 
	 * @param filePath
	 * @param modifyTime
	 * @return boolean 是否成功
	 */
	public static boolean setFileModifyTime(String filePath, long modifyTime) {
		if (null == filePath) {
			MyLog.e("Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}

		return file.setLastModified(modifyTime);
	}

	/**
	 * 复制文件
	 * 
	 * @param fromPath
	 *            被复制文件路径
	 * @param toPath
	 *            要复制到的路径
	 */
	public static void copyFile(String fromPath, String toPath) {
		if (!isExternalStorageWritable()) {
			MyLog.i(TAG, "SD卡不可用，保存失败");
			return;
		}

		File fileFrom = new File(fromPath);
		File fileTo = new File(toPath);

		try {

			FileInputStream fis = new FileInputStream(fileFrom);
			FileOutputStream fos = new FileOutputStream(fileTo);
			byte[] buffer = new byte[1024];
			int cnt = 0;

			while ((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}

			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			MyLog.i(TAG, e.getMessage());
		} catch (IOException e) {
			MyLog.i(TAG, e.getMessage());
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param cr
	 * @param fromPath
	 *            被复制文件路径
	 * @param destUri
	 *            要复制到的路径
	 * @return boolean 是否成功
	 */
	public static boolean copyFile(ContentResolver cr, String fromPath,
								   String destUri) {
		if (null == cr || null == fromPath || fromPath.length() < 1
				|| null == destUri || destUri.length() < 1) {
			MyLog.e("copyFile Invalid param. cr=" + cr + ", fromPath="
					+ fromPath + ", destUri=" + destUri);
			return false;
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(fromPath);

			// check output uri
			String path = null;
			Uri uri = null;

			String lwUri = destUri.toLowerCase();
			if (lwUri.startsWith("content://")) {
				uri = Uri.parse(destUri);
			} else if (lwUri.startsWith("file://")) {
				uri = Uri.parse(destUri);
				path = uri.getPath();
			} else {
				path = destUri;
			}

			// open output
			if (null != path) {
				File fl = new File(path);
				String pth = path.substring(0, path.lastIndexOf("/"));
				File pf = new File(pth);

				if (pf.exists() && !pf.isDirectory()) {
					pf.delete();
				}

				pf = new File(pth + File.separator);

				if (!pf.exists()) {
					if (!pf.mkdirs()) {
						MyLog.e("Can't make dirs, path=" + pth);
					}
				}

				pf = new File(path);
				if (pf.exists()) {
					if (pf.isDirectory())
						deleteDirectory(path);
					else
						pf.delete();
				}

				os = new FileOutputStream(path);
				fl.setLastModified(System.currentTimeMillis());
			} else {
				os = new ParcelFileDescriptor.AutoCloseOutputStream(
						cr.openFileDescriptor(uri, "w"));
			}

			// copy file
			byte[] dat = new byte[1024];
			int i = is.read(dat);
			while (-1 != i) {
				os.write(dat, 0, i);
				i = is.read(dat);
			}

			is.close();
			is = null;

			os.flush();
			os.close();
			os = null;

			return true;

		} catch (Exception ex) {
			MyLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (Exception ex) {
				}
				;
			}
			if (null != os) {
				try {
					os.close();
				} catch (Exception ex) {
				}
				;
			}
		}
		return false;
	}

	/**
	 * InputStream转换为byte[]
	 * 
	 * @param is
	 *            inputStream
	 * @throws Exception
	 * @return byte[]
	 */
	public static byte[] input2byte(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		int c = is.read(buf);
		while (-1 != c) {
			baos.write(buf, 0, c);
			c = is.read(buf);
		}
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

	/************* ZIP file operation ***************/

	/**
	 * 读ZIP文件
	 * 
	 * @param zipFileName
	 *            压缩文件
	 * @param crc
	 * @return boolean 是否成功
	 */
	public static boolean readZipFile(String zipFileName, StringBuffer crc) {
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(
					zipFileName));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				long size = entry.getSize();
				crc.append(entry.getCrc() + ", size: " + size);
			}
			zis.close();
		} catch (Exception ex) {
			MyLog.e("Exception: " + ex.toString());
			return false;
		}
		return true;
	}

	/**
	 * 读压缩文件
	 * 
	 * @param zipFileName
	 *            压缩文件名称
	 * @return byte[] 读取出来的压缩byte
	 */
	public static byte[] readGZipFile(String zipFileName) {
		if (isExistFile(zipFileName)) {
			MyLog.i("zipFileName: " + zipFileName);
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(zipFileName);
				int size;
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((size = fin.read(buffer, 0, buffer.length)) != -1) {
					baos.write(buffer, 0, size);
				}
				return baos.toByteArray();
			} catch (Exception ex) {
				MyLog.i("read zipRecorder file error");
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 把文件压缩为ZIP文件
	 * 
	 * @param baseDirName
	 * @param fileName
	 * @param targerFileName
	 * @throws IOException
	 * @return boolean 是否成功
	 */
	public static boolean zipFile(String baseDirName, String fileName,
								  String targerFileName) throws IOException {
		if (baseDirName == null || "".equals(baseDirName)) {
			return false;
		}
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			return false;
		}

		String baseDirPath = baseDir.getAbsolutePath();
		File targerFile = new File(targerFileName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				targerFile));
		File file = new File(baseDir, fileName);

		boolean zipResult = false;
		if (file.isFile()) {
			zipResult = fileToZip(baseDirPath, file, out);
		} else {
			zipResult = dirToZip(baseDirPath, file, out);
		}
		out.close();
		return zipResult;
	}

	/**
	 * 解压ZIP文件
	 * 
	 * @param fileName
	 * @param unZipDir
	 * @throws Exception
	 * @return boolean 是否成功
	 */
	public static boolean unZipFile(String fileName, String unZipDir)
			throws Exception {
		File f = new File(unZipDir);

		if (!f.exists()) {
			f.mkdirs();
		}

		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(fileName);
		Enumeration<?> enumeration = zipfile.entries();
		byte data[] = new byte[FILE_BUFFER_SIZE];
		MyLog.i("unZipDir: " + unZipDir);

		while (enumeration.hasMoreElements()) {
			entry = (ZipEntry) enumeration.nextElement();

			if (entry.isDirectory()) {
				File f1 = new File(unZipDir + "/" + entry.getName());
				MyLog.i("entry.isDirectory XXX " + f1.getPath());
				if (!f1.exists()) {
					f1.mkdirs();
				}
			} else {
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				String name = unZipDir + "/" + entry.getName();
				RandomAccessFile m_randFile = null;
				File file = new File(name);
				if (file.exists()) {
					file.delete();
				}

				file.createNewFile();
				m_randFile = new RandomAccessFile(file, "rw");
				int begin = 0;

				while ((count = is.read(data, 0, FILE_BUFFER_SIZE)) != -1) {
					try {
						m_randFile.seek(begin);
					} catch (Exception ex) {
						MyLog.e("exception, ex: " + ex.toString());
					}

					m_randFile.write(data, 0, count);
					begin = begin + count;
				}

				file.delete();
				m_randFile.close();
				is.close();
			}
		}

		return true;
	}

	/**
	 * 文件压缩为ZipOutputStream流
	 * 
	 * @param baseDirPath
	 * @param file
	 * @param out
	 * @throws IOException
	 * @return boolean 是否成功
	 */
	private static boolean fileToZip(String baseDirPath, File file,
									 ZipOutputStream out) throws IOException {
		FileInputStream in = null;
		ZipEntry entry = null;

		byte[] buffer = new byte[FILE_BUFFER_SIZE];
		int bytes_read;
		try {
			in = new FileInputStream(file);
			entry = new ZipEntry(getEntryName(baseDirPath, file));
			out.putNextEntry(entry);

			while ((bytes_read = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes_read);
			}
			out.closeEntry();
			in.close();
		} catch (IOException e) {
			MyLog.e("Exception, ex: " + e.toString());
			return false;
		} finally {
			if (out != null) {
				out.closeEntry();
			}

			if (in != null) {
				in.close();
			}
		}
		return true;
	}

	/**
	 * 目录文件夹压缩为ZipOutputStream流
	 * 
	 * @param baseDirPath
	 *            压缩路径
	 *            压缩的文件
	 * @param out
	 * @throws IOException
	 * @return boolean 是否成功
	 */
	private static boolean dirToZip(String baseDirPath, File dir,
									ZipOutputStream out) throws IOException {
		if (!dir.isDirectory()) {
			return false;
		}

		File[] files = dir.listFiles();
		if (files.length == 0) {
			ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));

			try {
				out.putNextEntry(entry);
				out.closeEntry();
			} catch (IOException e) {
				MyLog.e("Exception, ex: " + e.toString());
			}
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				fileToZip(baseDirPath, files[i], out);
			} else {
				dirToZip(baseDirPath, files[i], out);
			}
		}
		return true;
	}

	/**
	 * 获取文件条目
	 * 
	 * @param baseDirPath
	 *            路径
	 * @param file
	 *            文件
	 * @return
	 */
	private static String getEntryName(String baseDirPath, File file) {
		if (!baseDirPath.endsWith(File.separator)) {
			baseDirPath = baseDirPath + File.separator;
		}

		String filePath = file.getAbsolutePath();
		if (file.isDirectory()) {
			filePath = filePath + "/";
		}

		int index = filePath.indexOf(baseDirPath);
		return filePath.substring(index + baseDirPath.length());
	}

	/**
	 * 保存图片到制定路径
	 * 
	 * @param filepath
	 *            保存的路径
	 * @param bitmap
	 *            保存的图片
	 */
	public void saveBitmap(String filepath, Bitmap bitmap) {
		if (!isExternalStorageWritable()) {
			MyLog.i(TAG, "SD卡不可用，保存失败");
			return;
		}
		if (bitmap == null) {
			return;
		}
		try {
			File file = new File(filepath);
			FileOutputStream outputstream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
			outputstream.flush();
			outputstream.close();
		} catch (FileNotFoundException e) {
			MyLog.i(TAG, e.getMessage());
		} catch (IOException e) {
			MyLog.i(TAG, e.getMessage());
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param filename
	 * @return
	 */
	public boolean isExistsFile(String filename) {
		File dir = context.getExternalFilesDir(null);
		File file = new File(dir, filename);
		return file.exists();
	}

	/**
	 * 文件转String
	 * 
	 * @param file
	 *            文件
	 * @param encoding
	 *            编码
	 * @return String
	 */
	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file));
			} else {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			}
			// 将输入流写入输出流
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return writer.toString();
	}

	/**
	 * 文件转Base64
	 * 
	 * @param path
	 *            文件路径
	 * @return Base64 String
	 */
	public static String fileToEncodeBase64(String path) {
		String result = null;
		FileInputStream inputFile = null;
		try {
			File file = new File(path);
			inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputFile.read(buffer);

			result = Base64.encodeToString(buffer, Base64.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Base64转文件
	 * 
	 * @param base64Code
	 * @param savePath
	 * @throws Exception
	 */
	public static void encoderBase64ToFile(String base64Code, String savePath)
			throws Exception {
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		FileOutputStream out = new FileOutputStream(savePath);
		out.write(buffer);
		out.close();
	}

	/**
	 * 保存byte流到指定文件路径下
	 * 
	 * @param filePath
	 *            要保存到本地的路径
	 * @param by
	 *            保存的字节流
	 * @return
	 */
	public static boolean saveByteToFile(String filePath, byte[] by) {
		boolean isSuccess = false;
		if (!StringUtil.isEmpty(filePath) && by.length > 0) {
			FileOutputStream fileOutputStream = null;
			File file = new File(filePath);
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(by);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return isSuccess;
	}

	/**
	 * 获取录音文件保存地址
	 * 
	 * @return
	 */
	private static File getVoiceCachePath() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + IMConstant.HHXH_RECORD);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 创建录音文件路径
	 * 
	 * @return
	 */
	public static String createVoiceAmrPath() {
		String name = System.currentTimeMillis() + ".amr";
		return getVoiceCachePath().getPath() + File.separator + name;
	}
	
//	/**
//	 * 清除浏览器缓存
//	 */
//	public static void removeCookie() {
//		  CookieSyncManager.createInstance(MyApplication.getContext());
//		  CookieManager cookieManager = CookieManager.getInstance();
//		  cookieManager.removeAllCookie();
//		  CookieManager.getInstance().removeSessionCookie();
//		  CookieSyncManager.getInstance().sync();
//		  CookieSyncManager.getInstance().startSync();
//		 }
	
}
