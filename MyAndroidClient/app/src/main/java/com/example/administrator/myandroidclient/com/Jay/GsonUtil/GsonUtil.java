package com.example.administrator.myandroidclient.com.Jay.GsonUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {}

    public static String bean2Json(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    public static <T> T gson2Bean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    public static <T> List<T> jsonList2BeanList(String GsonList, Class<T> cls) {
        List<T> list = null;
        JsonArray arry = null;

        if (gson != null) {
            list = new ArrayList<>();
            arry = new JsonParser().parse(GsonList).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        }
        return list;
    }

    public static <T> String beanList2JsonList(List<T> list){
        String GsonList = null;
        if (gson != null){
            GsonList = gson.toJson(list);
        }
        return GsonList;
    }
}
