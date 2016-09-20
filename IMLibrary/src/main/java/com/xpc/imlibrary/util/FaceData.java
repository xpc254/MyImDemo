package com.xpc.imlibrary.util;

import android.content.Context;

import com.xpc.imlibrary.R;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表情数据
 * @author qiaocbao
 * @time 2014-5-7 上午11:16:51
 */
public class FaceData {
	public static final int NUM_PAGE = 4;// 总共有多少页
	public static int NUM = 20;// 每页20个表情,还有最后一个删除button
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	private Context context;
	private static FaceData mFaceData = new FaceData();

	public static FaceData getMFaceData() {
		return mFaceData;
	}

	private FaceData() {
		initFaceMap();
	}

	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

	private void initFaceMap() {
		mFaceMap.put("[OK]", R.drawable.f_static_000);
		mFaceMap.put("[不好]", R.drawable.f_static_001);
		mFaceMap.put("[赞]", R.drawable.f_static_002);
		mFaceMap.put("[拍手]", R.drawable.f_static_003);
		mFaceMap.put("[耶]", R.drawable.f_static_004);
		mFaceMap.put("[握手]", R.drawable.f_static_005);
		mFaceMap.put("[拜托]", R.drawable.f_static_006);
		mFaceMap.put("[微笑]", R.drawable.f_static_007);
		mFaceMap.put("[大笑]", R.drawable.f_static_008);
		mFaceMap.put("[偷笑]", R.drawable.f_static_009);
		mFaceMap.put("[疑问]", R.drawable.f_static_010);
		mFaceMap.put("[惊讶]", R.drawable.f_static_011);
		mFaceMap.put("[擦汗]", R.drawable.f_static_012); 
		mFaceMap.put("[晕]", R.drawable.f_static_013);
		mFaceMap.put("[发怒]", R.drawable.f_static_014);
		mFaceMap.put("[难过]", R.drawable.f_static_015);
		mFaceMap.put("[委屈]", R.drawable.f_static_016);
		mFaceMap.put("[流泪]", R.drawable.f_static_017);
		mFaceMap.put("[奋斗]", R.drawable.f_static_018);
		mFaceMap.put("[抓狂]", R.drawable.f_static_019);
		mFaceMap.put("[害羞]", R.drawable.f_static_020);
		mFaceMap.put("[得意]", R.drawable.f_static_021);
		mFaceMap.put("[睡]", R.drawable.f_static_022);
		mFaceMap.put("[傲慢]", R.drawable.f_static_023);
		mFaceMap.put("[借]", R.drawable.f_static_024);
		mFaceMap.put("[困]", R.drawable.f_static_025);
		mFaceMap.put("[惊恐]", R.drawable.f_static_026);
		mFaceMap.put("[流汗]", R.drawable.f_static_027);
		mFaceMap.put("[憨笑]", R.drawable.f_static_028);
		mFaceMap.put("[悠闲]", R.drawable.f_static_029);
		mFaceMap.put("[吐]", R.drawable.f_static_030);
		mFaceMap.put("[咒骂]", R.drawable.f_static_031);
		mFaceMap.put("[愉快]", R.drawable.f_static_032);
		mFaceMap.put("[嘘]", R.drawable.f_static_033);
		mFaceMap.put("[白眼]", R.drawable.f_static_034);
		mFaceMap.put("[疯了]", R.drawable.f_static_035);
		mFaceMap.put("[衰]", R.drawable.f_static_036);
		mFaceMap.put("[骷髅]", R.drawable.f_static_037);
		mFaceMap.put("[敲打]", R.drawable.f_static_038);
		mFaceMap.put("[再见]", R.drawable.f_static_039);
		mFaceMap.put("[调皮]", R.drawable.f_static_040);
		mFaceMap.put("[扣鼻]", R.drawable.f_static_041);
		mFaceMap.put("[鼓掌]", R.drawable.f_static_042);
		mFaceMap.put("[糗大了]", R.drawable.f_static_043);
		mFaceMap.put("[坏笑]", R.drawable.f_static_044);
		mFaceMap.put("[左哼]", R.drawable.f_static_045);
		mFaceMap.put("[右哼]", R.drawable.f_static_046);
		mFaceMap.put("[哈欠]", R.drawable.f_static_047);
		mFaceMap.put("[鄙视]", R.drawable.f_static_048);
		mFaceMap.put("[撇嘴]", R.drawable.f_static_049);
		mFaceMap.put("[快哭了]", R.drawable.f_static_050);
		mFaceMap.put("[淫笑]", R.drawable.f_static_051);
		mFaceMap.put("[亲亲]", R.drawable.f_static_052);
		mFaceMap.put("[吓]", R.drawable.f_static_053);
		mFaceMap.put("[可怜]", R.drawable.f_static_054);
		mFaceMap.put("[闭嘴]", R.drawable.f_static_055);
		mFaceMap.put("[大哭]", R.drawable.f_static_056);
		mFaceMap.put("[发呆]", R.drawable.f_static_057);
		mFaceMap.put("[尴尬]", R.drawable.f_static_058);
		mFaceMap.put("[酷]", R.drawable.f_static_059);
		mFaceMap.put("[冷汗]", R.drawable.f_static_060);
		mFaceMap.put("[色]", R.drawable.f_static_061);
		mFaceMap.put("[大便]", R.drawable.f_static_062);
	}
}
