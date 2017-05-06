package com.example.administrator.myandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/5/6.
 */

public class DownloadFile extends AppCompatActivity {
    private Button btnDownloadFile;
    private TextView tvDownlaodPrompt, tvDownloadPath;
    private ProgressBar pbDownloadPb;

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, DownloadFile.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.aty_download_file);
        btnDownloadFile = (Button) findViewById(R.id.btn_download);
        tvDownlaodPrompt = (TextView) findViewById(R.id.tv_downloadPrompt);
        tvDownloadPath = (TextView) findViewById(R.id.tv_downloadPath);
        pbDownloadPb = (ProgressBar) findViewById(R.id.pb_downloadPb);

        btnDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/183/", "英雄时刻.avi");
                if (file.exists()) {
                    Toast.makeText(DownloadFile.this, "文件已存在！", Toast.LENGTH_SHORT).show();
                    tvDownloadPath.setText("文件已存在！");
                    return;
                }
                downloadFile();
            }
        });
    }

    public void downloadFile() {
        String url = "http://192.168.43.42:8080/TestMyService/servlet/DownLoadServlet";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath() + "/183/", "英雄时刻.avi") {
                    private boolean isDownloadOk = false;

                    @Override
                    public void onBefore(Request request, int id) {
                        pbDownloadPb.setVisibility(View.VISIBLE);
                        tvDownlaodPrompt.setVisibility(View.VISIBLE);
                    }


                    @Override
                    public void inProgress(float progress, long total, int id) {
                        tvDownloadPath.setText("正在下载.....");
                        pbDownloadPb.setProgress(Math.abs((int) progress / 100000));
                        Log.e("------>进度", "inProgress :" + Math.abs((int) progress / 100000));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tvDownloadPath.setText("服务器异常！");
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        isDownloadOk = true;
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isDownloadOk) {
                            tvDownloadPath.setText("下载完成！");
                        } else {
                            tvDownloadPath.setText("下载失败！");
                        }
                        pbDownloadPb.setVisibility(View.INVISIBLE);
                        tvDownlaodPrompt.setVisibility(View.INVISIBLE);
                    }

                });
    }
}
