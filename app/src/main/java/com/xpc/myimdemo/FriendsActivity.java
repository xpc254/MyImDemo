package com.xpc.myimdemo;

import android.os.Bundle;
import android.widget.ListView;

import com.xpc.myimdemo.adapter.PersonListAdapter;
import com.xpc.myimdemo.app.MyApplication;
import com.xpc.myimdemo.base.BaseHttpActivity;
import com.xpc.myimdemo.config.ActionConfigs;
import com.xpc.myimdemo.data.UserPrefs;
import com.xpc.myimdemo.http.KeyValuePair;
import com.xpc.myimdemo.http.OnHttpListener;
import com.xpc.myimdemo.im.manager.SocketConnectionManager;
import com.xpc.myimdemo.model.PersonItem;
import com.xpc.myimdemo.util.JsonUtils;
import com.xpc.myimdemo.util.MyLog;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsActivity extends BaseHttpActivity {
    private String parentId = "7b43f905-5c67-11e5-b9b4-00163e00172b";

    @BindView(R.id.personListView)
    ListView personListView;
    List<PersonItem> personItemList = new ArrayList<>();
    private PersonListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        getListData();
        initView();
    }
   private void initView(){
       adapter = new PersonListAdapter(this,personItemList);
       personListView.setAdapter(adapter);
   }
    private void getListData() {
        List<KeyValuePair> params = new ArrayList<KeyValuePair>();
        params.add(new KeyValuePair("operatetype", "getChildrenAdminOrgUnitList"));
        params.add(new KeyValuePair("token", UserPrefs.getToken()));
        params.add(new KeyValuePair("id", parentId));
        httpPostAsync(HTTP_WHAT_ONE,ActionConfigs.GET_ORGANIZATIONAL_STRUCTURE_GROUP,params,onHttpListener);
    }

    OnHttpListener onHttpListener = new OnHttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
               switch (what){
                   case HTTP_WHAT_ONE:
                       parseHttpData(response.get());
                       break;
               }
        }
        @Override
        public void onFailed(int what, String url, Object tag, CharSequence error, int resCode, long ms) {
              MyLog.i("---请求失败--"+ error);
        }
    };
    /**解析网络数据**/
    private void parseHttpData(String responseBody){
        try {
               JSONObject obj = new JSONObject(responseBody);
               if (JsonUtils.isExistObj(obj,"rows")){
                    JSONArray infoArray = obj.getJSONArray("rows");
                   for (int i = 0; i < infoArray.length(); i++) {
                       PersonItem item = new PersonItem(infoArray.optJSONObject(i));
                       String isUser = item.getIsUser();
                       if (isUser.equals("1")) {// 用户
                           personItemList.add(item);
                       } else {
                          // departmentItem.getList().add(item);
                       }
                   }
                   adapter.notifyDataSetChanged();
               }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().stopService(); //把重连服务关掉
        SocketConnectionManager.getInstance().disconnect(); //关掉socket连接
        MyLog.i("---断开消息连接---");
    }
}
