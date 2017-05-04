package com.example.administrator.myandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTestConnection, tvLoading;
    private ProgressBar pbLoading;
    private Button btnGetJsonData;


    public class TestCollectionCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            pbLoading.setVisibility(View.VISIBLE);
            tvLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAfter(int id) {
            pbLoading.setVisibility(View.INVISIBLE);
            tvLoading.setVisibility(View.INVISIBLE);
            tvTestConnection.setText("连接成功！");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tvTestConnection.setText("服务器异常，连接失败！");
        }

        @Override
        public void onResponse(String response, int id) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void testConnection() {
            String url = "http://192.168.43.42:8080/TestMyService/servlet/SendMessageToAndroidClient";
            OkHttpUtils
                    .get()
                    .url(url)
//                    .id(100)
                    .build()
                    .execute(new TestCollectionCallback());
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        tvTestConnection = (TextView) findViewById(R.id.tv_testConnection);
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        btnGetJsonData = (Button) findViewById(R.id.btn_getJsonData);

        tvTestConnection.setOnClickListener(this);
        btnGetJsonData.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_testConnection:
                testConnection();
                break;

            case R.id.btn_getJsonData:
                GetJsonData.anctionStart(MainActivity.this);
                break;
        }

    }
}
