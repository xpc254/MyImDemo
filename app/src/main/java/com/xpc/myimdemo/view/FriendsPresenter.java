package com.xpc.myimdemo.view;

import com.xpc.imlibrary.data.UserPrefs;
import com.xpc.myimdemo.base.HttpPresenter;
import com.xpc.myimdemo.base.IHttpView;
import com.xpc.myimdemo.config.ActionConfigs;
import com.xpc.myimdemo.http.KeyValuePair;
import com.xpc.myimdemo.model.PersonItem;
import com.xpc.myimdemo.util.JsonUtils;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiepc on 2016-09-19  下午 3:11
 */
public class FriendsPresenter <V extends IHttpView>extends HttpPresenter {
    private String parentId = "7b43f905-5c67-11e5-b9b4-00163e00172b";
    List<PersonItem> personItemList = new ArrayList<>();
    public FriendsPresenter(V view){
        impView = view;
    }

     public void getFriends(){
         List<KeyValuePair> params = new ArrayList<KeyValuePair>();
         params.add(new KeyValuePair("operatetype", "getChildrenAdminOrgUnitList"));
         params.add(new KeyValuePair("token", UserPrefs.getToken()));
         params.add(new KeyValuePair("id", parentId));
         httpPostAsync(HTTP_WHAT_ONE, ActionConfigs.GET_ORGANIZATIONAL_STRUCTURE_GROUP,params);
     }

    @Override
    protected void parseHttpData(int what, Response responseBody) {
        switch (what) {
            case HTTP_WHAT_ONE:
                try {
                    JSONObject obj = new JSONObject((String)responseBody.get());
                    if (JsonUtils.isExistObj(obj,"rows")){
                        JSONArray infoArray = obj.getJSONArray("rows");
                        if(personItemList.size() > 0){
                            personItemList.clear();
                        }
                        for (int i = 0; i < infoArray.length(); i++) {
                            PersonItem item = new PersonItem(infoArray.optJSONObject(i));
                            String isUser = item.getIsUser();
                            if (isUser.equals("1")) {// 用户
                                personItemList.add(item);
                            } else {
                                // departmentItem.getList().add(item);
                            }
                        }
                        impView.onLoadData(what,personItemList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
