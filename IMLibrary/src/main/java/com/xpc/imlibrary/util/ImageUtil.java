package com.xpc.imlibrary.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.xpc.imlibrary.config.IMConstant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;


/**
 * 图片处理
 * 
 * @author qiaocbao
 * @time 2014-12-29 下午6:31:01
 */
public class ImageUtil {
	/** 拍照保存图片名称 */
	public static final String tempPhoto = "photo.png";
	/** 截图保存图片名称 */
	public static final String screenShotPhoto = "screenShotPhoto.png";
	/** 拍照图片保存地址 */
	public static final String PHOTO_GRAPH_PATH = FileUtil.getSDCardPath()
			+ IMConstant.HHXH_IMGDIR+tempPhoto;
	/** 截图图片保存地址 */
	public static final String SCREENSHOT_GRAPH_PATH = FileUtil.getSDCardPath()
			+ IMConstant.HHXH_IMGDIR + screenShotPhoto;
	/** 最大加载进内存大小 */
	public final static int MAX_SIZE_LOADINMEMORY = 480 * 480;
	/** 2G网络最大质量 */
	public final static int MAX_QUALITY_IN_2G = 80;
	/** 3G网络最大质量 */
	public final static int MAX_QUALITY_IN_3G = 80;
	/** wifi网络最大质量 */
	public final static int MAX_QUALITY_IN_WIFI = MAX_QUALITY_IN_3G;
	/** 2G加载最大尺寸 */
	public final static int MAX_SIZE_IN_2G = 720;// 720P
	/** 3G加载最大尺寸 */
	public final static int MAX_SIZE_IN_3G = ((Runtime.getRuntime().maxMemory() / (float) (1024 * 1024))) <= 64 ? 1024
			: 1280;// 1080P
	/** WIFI加载最大尺寸 */
	public final static int MAX_SIZE_IN_WIFI = MAX_SIZE_IN_3G;
	private static Random random = new Random();
	/**拍照保存图片路径*/
	public final static String DIR_PATH=FileUtil.getSDCardPath()+ IMConstant.HHXH_IMGDIR;

	/**
	 * 根据时间加2位随机数生成图片名称
	 * 
	 * @return 图片名称
	 */
	public static String getPhotoName() {
		return System.nanoTime() + random.nextInt(100) + ".jpg";
	}

	/**
	 * 指定图片宽度全屏，高度按原图的宽高缩放(宽高)
	 * 
	 * @param activity
	 *            当前上下文
	 * @param view
	 *            图片显示的控件
	 * @param width
	 *            图片宽
	 * @param height
	 *            图片高
	 */
	public static void setViewScope(Activity activity, View view, int width,
									int height) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		LayoutParams params = view.getLayoutParams();
		params.width = screenWidth;
		params.height = screenWidth * height / width;
		view.setLayoutParams(params);
	}

	/**
	 * 指定图片宽度全屏，高度按原图的宽高比例缩放(比例)
	 * 
	 * @param activity
	 *            当前上下文
	 * @param view
	 *            图片显示的控件
	 * @param aspectRatio
	 *            高宽比(高/宽)
	 */
	public static void setViewScope(Activity activity, View view,
									float aspectRatio) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		LayoutParams params = view.getLayoutParams();
		params.width = screenWidth;
		params.height = (int) (screenWidth * aspectRatio);
		view.setLayoutParams(params);
	}

	/**
	 * 获取不同尺寸的图片(服务器图片有3种压缩方式)
	 * 
	 * @param photoUrl
	 *            图片原地址
	 * @param size
	 *            图片大小尺寸(现有三种，参数分别为1、2、3，图片从小到大)
	 * @return 对应图片路径
	 */
	public static String photoSizeUrl(String photoUrl, int size) {
		StringBuffer sb = new StringBuffer();
		if (photoUrl != null && photoUrl.lastIndexOf(".") != -1) {
			String photoBefore = photoUrl.substring(0,
					photoUrl.lastIndexOf("."));
			String photoAfter = photoUrl.substring(photoUrl.lastIndexOf("."),
					photoUrl.length());
			sb.append(photoBefore);
			if (size == 1) {
				sb.append("@1x");
			} else if (size == 2) {
				sb.append("@2x");
			} else if (size == 3) {
				sb.append("@4x");
			}
			sb.append(photoAfter);
			return sb.toString();
		}
		return photoUrl;

	}

	/**
	 * 获取图片大小
	 * 
	 * @param bitmap
	 * @return
	 */
	public static int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
			// 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
	}

	/**
	 * 根据图片名称获取本地图片
	 * 
	 * @param context
	 * @param photoName
	 *            图片名称
	 * @return 图片id
	 */
	public static int getIdentifierDrawable(Context context, String photoName) {
		int drawableId = 0;
		try {
			drawableId = context.getResources().getIdentifier(photoName,
					"drawable", context.getPackageName());
		} catch (Exception e) {
			drawableId = 0;
		}
		return drawableId;
	}

	/**
	 * 调用系统相机照相,获取原图像
	 * 
	 * @param activity
	 *            当前activity
	 * @param dir
	 *            图片路径
	 * @param filename
	 *            图片名称
	 * @param cmd
	 *            requestCode 请求code
	 * @return boolean是否成功
	 */
	public static boolean takePhoto(final Activity activity, final String dir,
									final String filename, final int cmd) {
		String filePath = dir+filename;
		// final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		MyLog.d("test", "MediaStore.ACTION_IMAGE_CAPTURE"
				+ MediaStore.ACTION_IMAGE_CAPTURE);
		final Intent intent = new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE == null ? "android.media.action.IMAGE_CAPTURE"
						: MediaStore.ACTION_IMAGE_CAPTURE);
		final File cameraDir = new File(dir);
		if (!cameraDir.exists()) {
			cameraDir.mkdirs();
		}
		final File file = new File(filePath);
		final Uri outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		try {
			activity.startActivityForResult(intent, cmd);

		} catch (final ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 选择相册图片
	 * 
	 * @param activity
	 *            当前activity
	 * @param requestCode
	 *            请求code
	 */
	public static void choosePhoto(Activity activity, int requestCode) {
		Intent getImage = new Intent();
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/*");
		// if (android.os.Build.VERSION.SDK_INT >=
		// android.os.Build.VERSION_CODES.KITKAT) {
		// getImage.setAction(Intent.ACTION_OPEN_DOCUMENT);
		// } else {
		getImage.setAction(Intent.ACTION_GET_CONTENT);
		// }
		activity.startActivityForResult(getImage, requestCode);
	}

	/**
	 * 调用系统相机拍照
	 * 
	 * @param activity
	 *            当前activity
	 * @param requestCode
	 *            请求code
	 */
	public static void takePhoto(Activity activity, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 裁剪图片
	 * 
	 * @param activity
	 *            当前activity
	 * @param uri
	 *            所选图片uri
	 * @param requestCode
	 *            请求code
	 * @param x
	 *            输出尺寸x
	 */
	public static void croppedPhoto(Activity activity, Uri uri,
									int requestCode, int x, int y, int widthScale, int heightScale) {
		double scale = ((double)widthScale)/heightScale;
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", widthScale);// 裁剪框比例
		intent.putExtra("aspectY", heightScale);
		if(x > y){ //以小的为基准
			x = (int) (y*scale);
		}else{
			y = (int) (x/scale);
		}
		intent.putExtra("outputX",x);// 输出图片大小
		intent.putExtra("outputY",y);
		
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void croppedPhoto(Activity activity, Uri uri,
									int requestCode, int x, int y) {
		    croppedPhoto(activity, uri, requestCode, x, y,1,1);
	 }
	
	

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 *            图片
	 * @return String base64
	 */
	public static String bitmapToBase64(Bitmap bitmap) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, baos);
				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (OutOfMemoryError e2) {
			MyLog.e("bitmapToBase64----------OutOfMemoryError");
			baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 50, baos);
			byte[] bitmapBytes = baos.toByteArray();
			result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return Bitmap
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		Bitmap bitmap = null;
		byte[] bytes = null;
		try {
			bytes = Base64.decode(base64Data, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (OutOfMemoryError e) {// 有时无法catch
			e.printStackTrace();
			Options options = new Options();
			options.inSampleSize = 2;// 图片高宽度都为原来的二分之一，即图片大小为原来的大小的四分之一
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
		}
		return bitmap;
	}

	/**
	 * Bitmap转Drawable
	 * 
	 * @param bm
	 * @return
	 */
	public static Drawable bitmapToDrawable(Context context, Bitmap bm) {
		BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
		return bd;
	}

	/**
	 * Drawable转Bitmap
	 * 
	 * @param drawable
	 *            图片
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable){
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();  
		}  

		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 把bitmap转换成byte[]
	 * 
	 * @param bitmap
	 *            图片
	 * @return byte[]
	 */
	public static byte[] bitmapToByte(Bitmap bitmap) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, output);
			bitmap.recycle();
			byte[] result = output.toByteArray();
			output.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Bitmap转InputStream
	 * 
	 * @param bm
	 *            图片
	 * @return InputStream
	 */
	public static InputStream bitmapToInputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 从view得到图片
	 * 
	 * @param view
	 *            当前控件
	 * @return Bitmap
	 */
	public static Bitmap getViewBitmap(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * 获取最大加载进内存的缩略图对象
	 * 
	 * @param context
	 * @param imagePath
	 *            图片路径
	 * @return bitmap
	 */
	public static Bitmap getSuitableThumbBitmap(Context context,
												String imagePath) {
		if (StringUtil.isEmpty(imagePath) || !new File(imagePath).exists()
				|| new File(imagePath).isDirectory()) {
			return null;
		}
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int maxNumOfPixels = displayMetrics.widthPixels
				* displayMetrics.heightPixels / 4;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		opts.inJustDecodeBounds = false;
		try {
			return BitmapFactory.decodeFile(imagePath, opts);
		} catch (OutOfMemoryError err) {
		}
		return null;
	}

	/**
	 * 获取最大加载进内存的大图对象
	 * 
	 * @param context
	 * @param imagePath
	 *            绝对路径
	 * @return bitmap
	 */
	public static Bitmap getSuitableBigBitmap(Context context, String imagePath) {
		if (StringUtil.isEmpty(imagePath) || !new File(imagePath).exists()
				|| new File(imagePath).isDirectory()) {
			return null;
		}
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int maxNumOfPixels = Math.min(MAX_SIZE_LOADINMEMORY,
				displayMetrics.widthPixels * displayMetrics.heightPixels);
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		opts.inJustDecodeBounds = false;
		try {
			return BitmapFactory.decodeFile(imagePath, opts);
		} catch (OutOfMemoryError err) {
			return null;
		}
	}

	/**
	 * 获取相册最大加载进内存的大图对象(对应原BitmapUtil.getBitmapFromUri())
	 * 
	 * @param context
	 * @param imageUri
	 *            相对路径
	 * @return bitmap
	 */
	public static Bitmap getAlbumsSuitableBigBitmap(Context context,
													Uri imageUri) {
		if (StringUtil.isEmpty(imageUri.toString())) {
			return null;
		}
		InputStream in = FileUtil.getInputStreamFromUri(context, imageUri);
		if (in != null) {
			DisplayMetrics displayMetrics = context.getResources()
					.getDisplayMetrics();
			int maxNumOfPixels = Math.min(MAX_SIZE_LOADINMEMORY,
					displayMetrics.widthPixels * displayMetrics.heightPixels);
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
			opts.inJustDecodeBounds = false;
			try {
				in = FileUtil.getInputStreamFromUri(context, imageUri);
				return BitmapFactory.decodeStream(in, null, opts);
			} catch (OutOfMemoryError err) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取图片(未压缩)
	 * 
	 * @param context
	 * @param imageUri
	 *            相册路径
	 * @return bitmap
	 */
	public static Bitmap getBitmapByUri(Context context, Uri imageUri) {
		if (StringUtil.isEmpty(imageUri.toString())) {
			return null;
		}
		InputStream in = FileUtil.getInputStreamFromUri(context, imageUri);
		if (in != null) {
			try {
				return BitmapFactory.decodeStream(in);
			} catch (OutOfMemoryError err) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取相册最大加载进内存的大图对象
	 * 
	 * @param context
	 * @param photoString
	 *            图片String
	 * @return bitmap
	 */
	public static Bitmap getAlbumsSuitableBigStringToBitmap(Context context,
															String photoString) {
		if (StringUtil.isEmpty(photoString)) {
			return null;
		}
		InputStream in = new ByteArrayInputStream(Base64.decode(photoString,
				Base64.DEFAULT));
		if (in != null) {
			DisplayMetrics displayMetrics = context.getResources()
					.getDisplayMetrics();
			int maxNumOfPixels = Math.min(MAX_SIZE_LOADINMEMORY,
					displayMetrics.widthPixels * displayMetrics.heightPixels);
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
			opts.inJustDecodeBounds = false;
			try {
				in = new ByteArrayInputStream(Base64.decode(photoString,
						Base64.DEFAULT));
				return BitmapFactory.decodeStream(in, null, opts);
			} catch (OutOfMemoryError err) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取图片本地路径
	 * 
	 * @param context
	 * @param pathName
	 *            图片名称
	 * @return 图片路径
	 */
	public static String getImageLocalPath(Context context, String pathName) {
		if (pathName.startsWith(ContentResolver.SCHEME_CONTENT)) {
			String[] proj = { MediaColumns.DATA };
			Uri pUri = Uri.parse(pathName);
			Cursor actualimagecursor = context.getContentResolver().query(pUri,
					proj, null, null, null);
			int actual_image_column_index = actualimagecursor
					.getColumnIndexOrThrow(MediaColumns.DATA);
			actualimagecursor.moveToFirst();
			pathName = actualimagecursor.getString(actual_image_column_index);
		} else if (pathName.startsWith(ContentResolver.SCHEME_FILE)) {
			try {
				pathName = new File(new URI(pathName)).getAbsolutePath();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return pathName;
	}

	/**
	 * 根据图片宽高和最长边的值计算解码采样值
	 * 
	 * @param bitmapW
	 *            图片宽
	 * @param bitmapH
	 *            图片高
	 * @param maxwh
	 *            最大宽高
	 * @return 解码采样值
	 */
	public static int getScaleDecodeFile(int bitmapW, int bitmapH, int maxwh) {
		int scale;
		if (bitmapW > bitmapH) {
			scale = bitmapH / maxwh;
		} else {
			scale = bitmapW / maxwh;
		}
		if (scale < 2) {
			return 1;
		} else if (scale < 4) {
			return 2;
		} else if (scale < 8) {
			return 4;
		} else {
			return 8;
		}
	}

	/**
	 * 获取压缩值
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 计算初始采样大小
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 圆角化图片处理，原图bitmap不会被释放，外部调用注意处理bitmap
	 * 
	 * @param bitmap
	 *            图片
	 * @param pixels
	 *            圆角值
	 * @return 圆角化后的bitmap
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final android.graphics.Paint paint = new android.graphics.Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final android.graphics.RectF rectF = new android.graphics.RectF(
					rect);
			final float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new android.graphics.PorterDuffXfermode(
					Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
			// Log.e("", "", e);
		}
		return output;
	}

	/**
	 * 裁剪缩略图中间的resultWidth*resultHeight区域，得到为方形 bmp原图会被释放
	 * 
	 * @param bmp
	 *            原图
	 *            结果图的宽
	 * @param resultHeight
	 *            结果图的高
	 * @return
	 */
	public static Bitmap getCutBitmap(Bitmap bmp, int resultWidth,
									  int resultHeight) {
		if (bmp == null) {
			return null;
		}
		Bitmap result;
		int width = bmp.getWidth();// 输入长方形宽
		int height = bmp.getHeight();// 输入长方形高
		if (width < resultWidth || height < resultHeight
				|| (width == resultWidth && height == resultHeight)) {
			return bmp;
		}
		result = Bitmap.createBitmap(bmp, (width - resultWidth) / 2,
				(height - resultHeight) / 2, resultWidth, resultHeight);
		bmp.recycle();
		return result;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 设置图片文件Exif角度信息
	 * 
	 * @param filepath
	 *            文件路径
	 * @param orientation
	 *            角度 为正数 90 * i, i >= 0
	 */
	public static void savePictureDegree(String filepath, int orientation) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
			switch (orientation) {
			case 0: {
				exif.setAttribute(ExifInterface.TAG_ORIENTATION,
						String.valueOf(ExifInterface.ORIENTATION_NORMAL));
			}
			break;
			case 90: {
				exif.setAttribute(ExifInterface.TAG_ORIENTATION,
						String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
			}
			break;
			case 180: {
				exif.setAttribute(ExifInterface.TAG_ORIENTATION,
						String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
			}
			break;
			case 270: {
				exif.setAttribute(ExifInterface.TAG_ORIENTATION,
						String.valueOf(ExifInterface.ORIENTATION_ROTATE_270));
			}
			break;
			default: {
				exif.setAttribute(ExifInterface.TAG_ORIENTATION,
						String.valueOf(ExifInterface.ORIENTATION_NORMAL));
			}
			}
			exif.saveAttributes();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 图片旋转 原图b会被释放
	 * 
	 * @param b
	 *            图片
	 * @param degrees
	 *            旋转度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateImage(int degrees, Bitmap b) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();

			if (degrees == 180)// 某些系统中旋转180无效,通过两次旋转90度达到180效果
			{
				for (int i = 0; i < 2; i++) {
					m.setRotate(90, (float) b.getWidth() / 2,
							(float) b.getHeight() / 2);
					try {
						Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
								b.getHeight(), m, true);
						if (b != b2) {
							b.recycle();
							b = b2;
						}
					} catch (OutOfMemoryError ex) {
						return b;
					}
				}
			} else {
				m.setRotate(degrees, (float) b.getWidth() / 2,
						(float) b.getHeight() / 2);
				try {
					Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
							b.getHeight(), m, true);
					if (b != b2) {
						b.recycle();
						b = b2;
					}
				} catch (OutOfMemoryError ex) {
					return b;
				}
			}
		}
		return b;
	}

	// public static String getSuitablePath(String imagePath, int reqWith, int
	// reqHeight) {
	// if (SharedMothed.isEmpty(imagePath) || !new File(imagePath).exists() ||
	// new File(imagePath).isDirectory()) {
	// return null;
	// }
	// String fileName = FileUtils.getMD5Hash(imagePath) + "_" + reqWith + "X" +
	// reqHeight;
	// String cacheFilePath = FileUtils.IMAGE_PATH + fileName;
	// File cacheFile = new File(cacheFilePath);
	// if(cacheFile.exists()){
	// return cacheFilePath;
	// }
	// int maxNumOfPixels = reqWith*reqHeight;
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(imagePath, opts);
	// opts.inSampleSize = computeInitialSampleSize(opts, -1, maxNumOfPixels);
	// opts.inJustDecodeBounds = false;
	// try {
	// Bitmap bm = BitmapFactory.decodeFile(imagePath, opts);
	// int degree = ImageUtils.readPictureDegree(imagePath);
	// if(degree>0){
	// bm = ImageUtils.rotate(degree, bm);
	// }
	// Utils.saveLocalPic(fileName,90, bm);
	// bm.recycle();
	// return cacheFilePath;
	// } catch (OutOfMemoryError err) {
	// }
	// return null;
	// }

	// public static String getSuitablePath(String imagePath) {
	// return getSuitablePath(imagePath,800,600);
	// }

	// public static String moveToMyAlbum(Context context, String imagePath) {
	// File toCamera = new File(Environment.getExternalStorageDirectory()
	// + File.separator + "DCIM" + File.separator + "Camera"
	// + File.separator, Utils.getPhotoFileName());
	// File sourceFile = new File(imagePath);
	// try {
	//
	// FileUtil.moveFile(sourceFile, toCamera);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	// Uri uri = Uri.fromFile(toCamera);
	// intent.setData(uri);
	// context.sendBroadcast(intent);
	// return toCamera.getAbsolutePath();
	// }

	/**
	 * 移动图片到相册
	 * 
	 * @param context
	 * @param imagePath
	 *            图片路径
	 * @return 相册图片路径
	 */
	public static String moveToAlbum(Context context, String imagePath) {
		File toCamera = new File(Environment.getExternalStorageDirectory()
				+ File.separator + IMConstant.HHXH_IMGDIR, tempPhoto);
		File sourceFile = new File(imagePath);
		sourceFile.renameTo(toCamera);
		ContentValues values = new ContentValues(8);
		values.put(MediaColumns.MIME_TYPE, "image/jpeg");
		values.put(MediaColumns.DATA, toCamera.getAbsolutePath());
		values.put(Images.Media.ORIENTATION,
				ImageUtil.readPictureDegree(toCamera.getAbsolutePath()));
		context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,
				values);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
				.parse("file://" + Environment.getExternalStorageDirectory())));
		return toCamera.getAbsolutePath();
	}

	/**
	 * 复制图片到相册
	 * 
	 * @param context
	 * @param imagePath
	 *            图片路径
	 * @return 相册图片路径
	 */
	public static String copyToAlbum(Context context, String imagePath) {
		String albumPath = Environment.getExternalStorageDirectory()
				+ File.separator + IMConstant.HHXH_IMGDIR;
		FileUtil.copyFile(imagePath, albumPath);
		ContentValues values = new ContentValues(8);
		values.put(MediaColumns.MIME_TYPE, "image/jpeg");
		values.put(MediaColumns.DATA, new File(albumPath).getAbsolutePath());
		values.put(Images.Media.ORIENTATION, ImageUtil
				.readPictureDegree(new File(albumPath).getAbsolutePath()));
		context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,
				values);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
				.parse("file://" + Environment.getExternalStorageDirectory())));
		return new File(albumPath).getAbsolutePath();
	}

	/**
	 * inputStream转bitmap
	 * 
	 * @param is
	 *            InputStream
	 * @param size
	 *            采样值
	 * @return bitmap
	 */
	public static Bitmap decodeStream(InputStream is, int size) {
		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = size;
		return BitmapFactory.decodeStream(is, null, options);
	}

	/**
	 * 获取bitmap，节省内存，替代decodeFile或decodeStream方法
	 * 
	 * @param sourcePath
	 *            图片路径
	 * @param options
	 * @return
	 */
	public static Bitmap getBitmapByOptions(String sourcePath, Options options) {
		FileInputStream fs = null;
		Bitmap bm = null;
		try {
			fs = new FileInputStream(sourcePath);
		} catch (FileNotFoundException fnfException) {
			fnfException.printStackTrace();
		}
		if (fs != null) {
			try {
				bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
						options);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} finally {
				try {
					fs.close();
					fs = null;
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		MyLog.i("ImageUtils", bm != null ? "not null" : "null");
		return bm;
	}

	/**
	 * 保存图片到本地
	 * 
	 * @param bitmap
	 *            图片
	 * @param name
	 *            图片名称
	 * @return 保存路径,为null则保存失败
	 */
	public static String saveBitmapToLocal(Bitmap bitmap, String name) {
		if (bitmap == null || name == null || name.equals("")) {
			return null;
		}
		boolean result = false;
		String path = FileUtil.getSDCardPath() + IMConstant.HHXH_IMGDIR + name;
		FileOutputStream outputStream = null;
		try {
			File file = new File(path);
			outputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result) {
			return path;
		} else {
			return null;
		}
	}

	/**
	 * 保存图片到指定路径
	 * 
	 * @param wifi
	 *            当前网络是否是wifi
	 * @param bitmap
	 *            图片
	 * @param angle
	 *            角度
	 * @param path
	 *            路径
	 * @return 是否保存成功
	 */
	public static boolean saveImageBitmap(boolean wifi, Bitmap bitmap,
			int angle, String path) {
		boolean result = false;
		try {
			File file = new File(path);
			FileOutputStream outputStream = new FileOutputStream(file);
			if (bitmap != null) {
				if (bitmap.compress(CompressFormat.JPEG,
						wifi ? MAX_QUALITY_IN_WIFI : MAX_QUALITY_IN_2G,
								outputStream)) {
					savePictureDegree(path, angle);
					result = true;
				}
			}
			outputStream.flush();
			outputStream.close();
		} catch (OutOfMemoryError err) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return result;
	}

	/**
	 * 创建视频缩略图
	 * 
	 * @param filePath
	 *            视频文件路径
	 * @param kind
	 *            可以为MINI_KIND或MICRO_KIND
	 * @return 缩略图
	 */
	public static Bitmap createVideoThumbnail(String filePath, int kind) {
		Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, kind);
		if (thumb != null) {
			thumb = getCutBitmap(thumb,
					Math.min(thumb.getWidth(), thumb.getHeight()),
					Math.min(thumb.getWidth(), thumb.getHeight()));
		}
		return thumb;
	}

//	/**
//	 * 压缩处理名片扫描图片
//	 */
//	public static String dealNameCardPic() {
//		String newPath=null;
//		try{
//			Options opts = new Options();
//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(MainActivity.NAMECARD_FILE_PATH, opts);
//			opts.inSampleSize=computeInitialSampleSize(opts, -1, 1400*1400);
//			opts.inJustDecodeBounds = false;
//			Bitmap bitmap= BitmapFactory.decodeFile(MainActivity.NAMECARD_FILE_PATH, opts);
//			int  width=bitmap.getWidth();
//			int  height=bitmap.getHeight();
//			int  max=(width>height)?width:height;
//			String name= System.currentTimeMillis()+".jpg";
//			if(max<1501){
//				newPath=saveBitmapToLocal(bitmap, name);
//			}else{
//				float dest=1500.0f;
//				float scale;
//				if(width>height){
//					scale=dest/width;
//				}else{
//					scale=dest/height;
//				}
//				int dstWidth=(int)(width*scale);
//				int dstHeight=(int)(height*scale);
//				Bitmap newBitmap= Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false);
//				newPath=saveBitmapToLocal(newBitmap, name);
//				newBitmap.recycle();
//				newBitmap=null;
//			}
//			bitmap.recycle();
//			bitmap=null;
//			return newPath;
//
//		}catch(Exception e){
//			e.printStackTrace();
//			return null;
//		}
//	}
}
