package com.example.administrator.myandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myandroidclient.com.Jay.entity.Student;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/5/5.
 */

public class PostData2MyService extends AppCompatActivity {
    private TextView tvResponseData;
    private EditText etPostStringData;
    private Button btnPostStringData;


    public static void actionStart(Context context){
        context.startActivity(new Intent(context, PostData2MyService.class));
    }

    private Student parseJsonData(String responseData) {
        Gson gson = new Gson();
        Log.e("responseData:", responseData);
        Student student = gson.fromJson(responseData, Student.class);
        return student;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.aty_post_data2my_service);
        tvResponseData = (TextView) findViewById(R.id.tv_responseData);
        etPostStringData = (EditText) findViewById(R.id.et_stringData);
        btnPostStringData = (Button) findViewById(R.id.btn_postStringData);

        btnPostStringData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postStringData();
            }
        });
    }

    private void postStringData() {
        if (etPostStringData.getText().toString().isEmpty()){
            Toast.makeText(this, "尚未输入", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://192.168.43.42:8080/TestMyService/servlet/ResponseData";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", etPostStringData.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        tvResponseData.setText("服务器异常，未得到数据！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if ("false".equals(response)){
                            tvResponseData.setText("未查询到！");
                        }else {
                            Student student = parseJsonData(response);
                            tvResponseData.setText("id:" + student.getId() + ", name:" + student.getName() + ", score:" + student.getScore());
                        }
                    }
                });
    }
}
