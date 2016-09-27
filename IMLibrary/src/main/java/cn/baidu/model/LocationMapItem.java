package cn.baidu.model;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xpc.imlibrary.model.BaseItem;

import java.io.Serializable;

/**
 * 地图定位数据item
 * @author qiaocbao
 * @time 2015-7-23 下午2:31:38
 */
public class LocationMapItem implements BaseItem, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 定位位置 */
	private String name;
	/** 定位地址 */
	private String address;
	/** 经度 */
	private double Longitude;
	/** 纬度 */
	private double Latitude;
	/** 获取详情的uid */
	private String uid;
	/** 是否选中 */
	private boolean isSelected;
	/** 截图地址 */
	private String screenshotUrl;

	public LocationMapItem() {
	}

	public LocationMapItem(PoiInfo info) {
		this.name = info.name;
		this.address = info.address;
		this.Longitude = info.location.longitude;
		this.Latitude = info.location.latitude;
		this.uid = info.uid;
		this.isSelected = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getScreenshotUrl() {
		return screenshotUrl;
	}

	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}
}
