package com.xpc.imlibrary.hold;

import android.graphics.Bitmap;

import com.xpc.imlibrary.model.ImageItem;
import com.xpc.imlibrary.photo.AlbumInfo;

import java.util.List;


/**
 * 保存图片
 * @author qiaocbao
 * @time 2014-12-3 下午8:04:20
 */
public class SavePicture {
	/** 图片 */
	public static Bitmap mBitmap = null;
	/** 图片 List*/
	public static List<Bitmap> bmpList = null;
	/** 图片 Image*/
	public static ImageItem mImage = null;
	/** 图片 List 带描述*/	
	public static List<ImageItem> imgList = null;
	/** 图片截图专用 ，在原来有查看大图的界面截图跳转到建议界面后，若用同一个bitmap，会有冲突*/
	public static Bitmap screenBitmap = null;
	/** 图片 List截图专用，在原来有查看大图的界面截图跳转到建议界面后，若用同一个bitmap，会有冲突*/
	public static List<Bitmap> screenBmpList = null;
	/** 相册图片 */
	public static List<AlbumInfo> galleryImageList = null;
	/**用于多个相册文件夹选择图片的List*/
	public static List<ImageItem> picImgList = null;
}
