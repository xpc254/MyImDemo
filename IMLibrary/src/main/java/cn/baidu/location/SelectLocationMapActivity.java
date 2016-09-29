package cn.baidu.location;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xpc.imlibrary.BaseHttpActivity;
import com.xpc.imlibrary.ChatActivity;
import com.xpc.imlibrary.R;
import com.xpc.imlibrary.config.ActionConfigs;
import com.xpc.imlibrary.data.SavePicture;
import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.imlibrary.http.KeyValuePair;
import com.xpc.imlibrary.http.OnHttpListener;
import com.xpc.imlibrary.util.JsonUtils;
import com.xpc.imlibrary.util.MyLog;
import com.xpc.imlibrary.util.StatusBarCompat;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.baidu.adapter.LocationMapAdapter;
import cn.baidu.model.LocationMapItem;

/**
 * 地图定位
 *
 * @author qiaocbao
 * @time 2015-2-3 上午10:05:36
 */
public class SelectLocationMapActivity extends BaseHttpActivity {
    // 定位相关
    public LocationClient mLocClient = null;
    public MyLocationData locData;
    private MyLocationListenner myListener = new MyLocationListenner();
    /**
     * 签到地图
     */
    private MapView mapView;

    /**
     * 百度地图
     */
    private BaiduMap mBaiduMap;

    /**
     * 地图显示变化
     */
    private MapStatusUpdate msu;

    /**
     * 是否是首次定位
     */
    private boolean isFirstLoc = true;

    /**
     * 显示模式（普通、跟随、罗盘）
     */
    private LocationMode mCurrentMode;
    /**
     * 定位图标
     */
    private BitmapDescriptor mCurrentMarker;

    /**
     * 位置列表
     */
    private ListView addressList;

    /**
     * 地理编码查询
     */
    private GeoCoder mGeoCoder;
    /**
     * 附近位置adapter
     */
    private LocationMapAdapter locationAdapter;
    /**
     * 当前地理位置数据
     */
    private List<LocationMapItem> locationLists;
    /**
     * 是否是考勤签到
     */
    private boolean isAttendance;
    /**
     * 定位地图activity
     */
    public static SelectLocationMapActivity locationMapActivity;
    /**
     * 服务器时间
     */
    private String serverTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);
        StatusBarCompat.compat(this,getResources().getColor(R.color.title_bar));
        locationMapActivity = this;
        mContext = this;
        initTitle(getString(R.string.my_location));
        initView();
        initLocationMap();
    }

    @Override
    protected void initTitle(String str) {
        super.initTitle(str);
        titleRightText.setVisibility(View.VISIBLE);
        titleRightText.setText(getResources().getString(R.string.confirm));
        titleRightText.setOnClickListener(onClickListener);
    }

    /**
     * 初始化View
     */
    private void initView() {
        isAttendance = getIntent().getBooleanExtra("isAttendance", false);
        mapView = (MapView) findViewById(R.id.locationMap);
        addressList = (ListView) findViewById(R.id.addressList);
        // 修改为自定义marker图标
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_gcoding);
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mGeoCoder = GeoCoder.newInstance();
        if (locationLists == null) {
            locationLists = new ArrayList<LocationMapItem>();
        }
        if (locationLists.size() > 0) {
            locationLists.clear();
        }
        locationAdapter = new LocationMapAdapter(mContext, locationLists);
        addressList.setAdapter(locationAdapter);
        addressList.setOnItemClickListener(onItemClickListener);
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            for (int i = 0; i < locationLists.size(); i++) {
                LocationMapItem locationItem = locationLists.get(i);
                if (i == arg2) {
                    locationItem.setSelected(true);
                } else {
                    locationItem.setSelected(false);
                }
            }
            locationAdapter.notifyDataSetChanged();
            getScreenshots(locationLists.get(arg2));
        }
    };


    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.titleRightText) {
                if (locationLists == null || locationLists.size() == 0) {
                    return;
                } else {
                    for (int i = 0; i < locationLists.size(); i++) {
                        LocationMapItem locationItem = locationLists.get(i);
                        if (locationItem.isSelected()) {
                            getScreenshots(locationItem);
                            break;
                        }
                    }
                }
            }
        }
    };

    /**
     * 初始化定位数据
     */
    private void initLocationMap() {
        // initLocationMode();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // // 普通地图
        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 设置坐标类型,默认值gcj02
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为10s
        // 设置定位精度，默认高精度
        // option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 设置显示定位模式
     */
    private void initLocationMode() {
        switch (mCurrentMode) {
            case NORMAL:// 普通

                break;
            case COMPASS:// 罗盘

                break;
            case FOLLOWING:// 跟随

                break;

            default:
                break;
        }
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) return;
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) return;
            // locData = new MyLocationData.Builder()
            // .accuracy(location.getRadius())
            // // 此处设置开发者获取到的方向信息，顺时针0-360
            // .direction(100).latitude(location.getLatitude())
            // .longitude(location.getLongitude()).build();
            // mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                msu = MapStatusUpdateFactory.newLatLngZoom(ll, 15);
                mBaiduMap.animateMapStatus(msu);
                addMark(ll);
                initNearbySearch(ll);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    /**
     * 添加覆盖物
     */
    private void addMark(LatLng latLng) {
        OverlayOptions option = new MarkerOptions().position(latLng).icon(mCurrentMarker);
        mBaiduMap.addOverlay(option);
    }

    /**
     * 反地理编码查找附近位置
     */
    private void initNearbySearch(LatLng latLng) {
        ReverseGeoCodeOption reOption = new ReverseGeoCodeOption();
        reOption.location(latLng);
        mGeoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        mGeoCoder.reverseGeoCode(reOption);

    }


    /**
     * 获取屏幕截图
     */
    private void getScreenshots(final LocationMapItem locationItem) {
        mBaiduMap.snapshot(new SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap arg0) {
                try {
                    SavePicture.mBitmap = arg0;
                    if (isAttendance) {
                        if (serverTime == null || serverTime.equals("")) {
                            getServerTime();
                        }

                    } else if (getIntent().getBooleanExtra("isChat", false)) {//聊天进来的
                        Intent taskIntent = new Intent();
                        taskIntent.putExtra("mapItem", (Serializable) locationItem);
                        setResult(ChatActivity.FUNCTION_LOCATION, taskIntent);
                        finish();
                    } else {
                        Intent mapIntent = new Intent();
                        mapIntent.putExtra("mapItem", (Serializable) locationItem);
                        setResult(RESULT_OK, mapIntent);
                        finish();
                    }
                } catch (Exception e) {
                    MyLog.e("getScreenshots error:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 查询周边位置监听
     */
    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
            if (arg0 != null) {
                List<PoiInfo> poiList = arg0.getPoiList();
                for (int i = 0; i < poiList.size(); i++) {
                    PoiInfo info = poiList.get(i);
                    LocationMapItem locationItem = new LocationMapItem(info);
                    if (i == 0) {
                        locationItem.setSelected(true);
                    }
                    locationLists.add(locationItem);
                }
                locationAdapter.notifyDataSetChanged();
                if (isAttendance) {
                    getServerTime();
                }
            }

        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {
        }
    };


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }


    /**
     * 获取服务器时间
     */
    private void getServerTime() {
        List<KeyValuePair> params = new ArrayList<KeyValuePair>();
        params.add(new KeyValuePair("token", UserPrefs.getToken()));
        params.add(new KeyValuePair("operatetype", "getSystemTime"));
        httpPostAsync(HTTP_WHAT_ONE, ActionConfigs.getATTENDANCEUrl, params, onHttpListener);
    }

    ;

    private OnHttpListener onHttpListener = new OnHttpListener() {
        @Override
        public void onSucceed(int what, Response response) {
            JSONObject obj;
            try {
                obj = new JSONObject((String) response.get());
                if (JsonUtils.isExistObj(obj, "systemtime")) {
                    serverTime = obj.optString("systemtime");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(int what, Response response) {
            MyLog.i("---获得服务器时间失败---" + response.get());
        }
    };
//
//	/**
//	 * 获取服务器时间的handler
//	 */
//	private AsyncHttpResponseHandler getServerTimeHander = new AsyncHttpResponseHandler() {
//
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				byte[] responseBody) {
//			if (statusCode == 200 && responseBody != null
//					&& responseBody.length > 0) {
//				HhxhLog.i("----------result:" + new String(responseBody));
//				JSONObject obj;
//				try {
//					obj = new JSONObject(new String(responseBody));
//					if (JsonUtils.isExistObj(obj, "systemtime")) {
//						serverTime=obj.optString("systemtime");
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//
//		public void onStart() {
//
//		};
//
//		public void onFinish() {
//
//		};
//
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				byte[] responseBody, Throwable error) {
//			getServerTime();
//		}
//	};

}