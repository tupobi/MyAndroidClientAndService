package com.example.administrator.myandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myandroidclient.com.Jay.GsonUtil.GsonUtil;
import com.example.administrator.myandroidclient.com.Jay.entity.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/4.
 */

public class GetJsonData extends AppCompatActivity {
    private TextView tvUnparsedJsonData, tvParsedJsonData;
    private Button btnGetJsonData;
    private boolean isConnected = false;

    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tvUnparsedJsonData.setText("拉取数据失败，请检查服务器！");

        }

        @Override
        public void onResponse(String response, int id) {
            tvUnparsedJsonData.setText(response);
            tvParsedJsonData.setText(parseJSONWithGSON(tvUnparsedJsonData.getText().toString()));
        }
    }

    public static void anctionStart(Context context) {
        context.startActivity(new Intent(context, GetJsonData.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.aty_getjsondata);
        tvParsedJsonData = (TextView) findViewById(R.id.tv_parsedJsonData);
        tvUnparsedJsonData = (TextView) findViewById(R.id.tv_unParsedJsonData);
        btnGetJsonData = (Button) findViewById(R.id.btn_getJsonData);

        btnGetJsonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.43.42:8080/TestMyService/servlet/SendMessageToAndroidClient";
                OkHttpUtils
                        .get()
                        .url(url)
                        .build()
                        .execute(new MyStringCallback());
            }
        });
    }

    private String parseJSONWithGSON(String jsonData) {
        Log.e("------------>", "isC = " + isConnected);
        Gson gson = new Gson();
        List<Student> temp = GsonUtil.jsonList2BeanList(jsonData, Student.class);
        StringBuffer sb = new StringBuffer();
        for (Student student : temp) {
            sb.append("id:" + student.getId() + ", name:" + student.getName() + ", score:" + student.getScore() + "\n");
        }
        return new String(sb);
    }
}
