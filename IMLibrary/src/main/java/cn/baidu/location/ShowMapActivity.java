package cn.baidu.location;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.xpc.imlibrary.BaseActivity;
import com.xpc.imlibrary.R;

/**
 * 显示地图
 *
 * @author qiaocbao
 * @time 2015-8-9 上午11:28:40
 */
public class ShowMapActivity extends BaseActivity {
    /**
     * 百度地图显示
     */
    private MapView showMapView;
    /**
     * 百度地图
     */
    private BaiduMap mBaiduMap;
    /**
     * 纬度
     */
    private double lat;
    /**
     * 经度
     */
    private double lon;

    /**
     * 定位图标
     */
    private BitmapDescriptor mCurrentMarker;

    /**
     * 地图显示变化
     */
    private MapStatusUpdate msu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        initTitle("查看地图");
        showMapView = (MapView) findViewById(R.id.showMapView);
        mBaiduMap = showMapView.getMap();
        // 修改为自定义marker图标
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_gcoding);
        if (lat != 0 && lon != 0) {
            LatLng ll = new LatLng(lat, lon);
            msu = MapStatusUpdateFactory.newLatLngZoom(ll, 15);
            mBaiduMap.animateMapStatus(msu);
            addMark(ll);
        }
    }

    /**
     * 添加覆盖物
     */
    private void addMark(LatLng latLng) {
        OverlayOptions option = new MarkerOptions().position(latLng).icon(mCurrentMarker);
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onPause() {
        showMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        showMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        showMapView.onDestroy();
        showMapView = null;
        super.onDestroy();
    }

}
