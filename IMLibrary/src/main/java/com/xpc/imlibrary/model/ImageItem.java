package com.xpc.imlibrary.model;

import android.graphics.Bitmap;

import com.xpc.imlibrary.util.ImageUtil;

import java.io.Serializable;

/**
 * 图片item
 * @author qiaocbao
 * @time 2014-10-8 下午7:35:53
 */
public class ImageItem implements BaseItem, Serializable {

	/***/
	private static final long serialVersionUID = 1L;
	/** 图片 */
	private Bitmap bitmap;
	/** 图片url */
	private String imgURL;
	/** 图片名称 */
	private String photoName;
	/** 图片描述 */
	private String photoDesc;
	/** 旋转角度 */
	private int getRotateDegree;

	public ImageItem() {
	};

	public ImageItem(Bitmap bitmap, String imgURL) {
		this.bitmap = bitmap;
		this.imgURL = imgURL;
		this.photoName= ImageUtil.getPhotoName();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoDesc() {
		return photoDesc;
	}

	public void setPhotoDesc(String photoDesc) {
		this.photoDesc = photoDesc;
	}

	public int getGetRotateDegree() {
		return getRotateDegree;
	}

	public void setGetRotateDegree(int getRotateDegree) {
		this.getRotateDegree = getRotateDegree;
	}
}
