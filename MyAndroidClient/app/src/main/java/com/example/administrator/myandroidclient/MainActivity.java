package com.example.administrator.myandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myandroidclient.com.Jay.GsonUtil.GsonUtil;
import com.example.administrator.myandroidclient.com.Jay.entity.Student;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTestConnection, tvLoading;
    private ProgressBar pbLoading;
    private Button btnGetJsonData, btnPostStringData2MyService, btnMultiFileUpload, btnDownloadFile;


    public class TestCollectionCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            pbLoading.setVisibility(View.VISIBLE);
            tvLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAfter(int id) {
            //不管连接与否，都会执行。
            pbLoading.setVisibility(View.INVISIBLE);
            tvLoading.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tvTestConnection.setText("服务器异常，连接失败！");
        }

        @Override
        public void onResponse(String response, int id) {
            tvTestConnection.setText("连接成功！");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        testGsonUtil();
    }

    private void testGsonUtil() {
        Student student = new Student("09", "lizijie", 88);
        String jsonString = GsonUtil.bean2Json(student);
        Log.e("------>jsonString:", jsonString);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(new Student("08", "www", 87));
        String jsonList = GsonUtil.beanList2JsonList(students);
        Log.e("------>jsonList:", jsonList);

        Student temp = GsonUtil.gson2Bean(jsonString, Student.class);
        Log.e("------>bean:", temp.getId() + ", " + temp.getName() + ", " + temp.getScore());

        List<Student> temps = GsonUtil.jsonList2BeanList(jsonList, Student.class);
        for (int i=0; i<temps.size(); i++){
            Log.e("------>bean:", temps.get(i).getId() + ", " + temps.get(i).getName() + ", " + temps.get(i).getScore());
        }


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
        btnPostStringData2MyService = (Button) findViewById(R.id.btn_postStringData);
        btnMultiFileUpload = (Button) findViewById(R.id.btn_multiFileUpload);
        btnDownloadFile = (Button) findViewById(R.id.btn_downloadFile);

        tvTestConnection.setOnClickListener(this);
        btnGetJsonData.setOnClickListener(this);
        btnPostStringData2MyService.setOnClickListener(this);
        btnMultiFileUpload.setOnClickListener(this);
        btnDownloadFile.setOnClickListener(this);
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
            case R.id.btn_postStringData:
                PostData2MyService.actionStart(MainActivity.this);
                break;
            case R.id.btn_multiFileUpload:
                AtyMultiFileUpload.actionStart(MainActivity.this);
                break;
            case R.id.btn_downloadFile:
                DownloadFile.actionStart(MainActivity.this);
                break;
        }

    }
}
