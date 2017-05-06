package com.example.administrator.myandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/5.
 */

public class AtyMultiFileUpload extends AppCompatActivity {
    private TextView tvFilePath;
    private Button btnMultiFileUpload;

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, AtyMultiFileUpload.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.aty_multi_file_upload);
        tvFilePath = (TextView) findViewById(R.id.tv_filePath);
        btnMultiFileUpload = (Button) findViewById(R.id.btn_multiFileUpload);

        btnMultiFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiFileUpload();
            }
        });
    }

    private void multiFileUpload() {

        File file1 = new File(Environment.getExternalStorageDirectory(), "/183/11.txt");
        File file2 = new File(Environment.getExternalStorageDirectory(), "183/12.jpg");
        if (!file1.exists() || !file2.exists()) {
            Toast.makeText(AtyMultiFileUpload.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.43.42:8080/TestMyService/servlet/MultiFileUpload";
        OkHttpUtils.post()//
                .addFile("text", "文本.txt", file1)//
                .addFile("image", "图片.jpg", file2)//
                .url(url)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tvFilePath.setText("服务器异常，请重试！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tvFilePath.setText("上传成功！");
                    }
                });
    }
}
