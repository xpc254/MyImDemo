package com.xpc.myimdemo.im.manager;

import android.content.ContentValues;
import android.content.Context;

import com.xpc.myimdemo.db.DBManager;
import com.xpc.myimdemo.db.SQLiteTemplate;
import com.xpc.myimdemo.im.model.RecMessageItem;


/**
 * 人员资料管理类
 * 
 * @author qiaocbao
 * @time 2015-9-16 上午10:36:25
 */
public class PersonInfoManager {
	private static PersonInfoManager personInfoManager = null;
	private static DBManager manager = null;

	private PersonInfoManager(Context context) {
		manager = DBManager.getInstance(context);
	}

	public static PersonInfoManager getInstance(Context context) {
		if (personInfoManager == null) {
			personInfoManager = new PersonInfoManager(context);
		}
		return personInfoManager;
	}

	/**
	 * 保存聊天好友资料(如果存在该好友资料则更新，否则新增该资料)
	 * @param msg
	 * @return 受影响行数
	 */
	public long savePersonInfo(RecMessageItem msg) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		long influencesRows = -1;
		ContentValues contentValues = new ContentValues();
		contentValues.put("send_id", msg.getSendId());
		contentValues.put("send_name", msg.getSendNickName());
		contentValues.put("send_url", msg.getSendUserAvatar());
		if (st.isExistsByField("im_person_info", "send_id", msg.getSendId()) != null
				&& st.isExistsByField("im_person_info", "send_id",
						msg.getSendId())) {

			influencesRows = st.update("im_person_info", contentValues,
					"send_id=?", new String[] { msg.getSendId() });
		} else {
			influencesRows = st.insert("im_person_info", contentValues);
		}
		return influencesRows;
	}



	/**
	 * 删除好友资料
	 * @param sendId
	 * @return
	 */
	public int delPersonInfo(String sendId) {
		SQLiteTemplate st = SQLiteTemplate.getInstance(manager, false);
		if (st.isExistsByField("im_person_info", "send_id", sendId) != null
				&& st.isExistsByField("im_person_info", "send_id", sendId)) {
			return st.deleteByField("im_person_info", "send_id", sendId);
		}
		return 0;
	}
}
