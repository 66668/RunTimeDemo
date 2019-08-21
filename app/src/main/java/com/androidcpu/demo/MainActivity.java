package com.androidcpu.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity {

    EditText et_input;
    Button btn_search;
    Button btn_search2;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    List<String> cmdList;
    RecyclerView.LayoutManager manager;
    CommonDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMyView();
        initData();
    }

    private void initMyView() {
        recyclerView = findViewById(R.id.recyclerView);
        et_input = findViewById(R.id.et_input);
        btn_search = findViewById(R.id.btn_search);
        btn_search2 = findViewById(R.id.btn_search2);
        cmdList = new ArrayList<>();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmd = et_input.getText().toString();
                if (TextUtils.isEmpty(cmd)) {
                    return;
                }
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... voids) {
                        return RunTimeUtils.runExec(cmd);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        showDialog(result);
                    }
                }.execute();
            }
        });
        btn_search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cmd = et_input.getText().toString();
                if (TextUtils.isEmpty(cmd)) {
                    return;
                }
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... voids) {
                        return RunTimeUtils.runExecCmd(cmd);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        showDialog(result);

                    }
                }.execute();

            }
        });
    }

    /**
     * 展示 adb命令信息
     *
     * @param result
     */
    private void showDialog(String result) {
        dialog = new CommonDialog(this, result, null);
        dialog.show();
    }

    private void initData() {
        cmdList.add("ls sys/devices/virtual/thermal/");//获取所有cpu的thermal信息
        cmdList.add("cat sys/devices/virtual/thermal/thermal_zone0/temp");//获取cpu温度
        cmdList.add("cat sys/devices/virtual/thermal/thermal_zone0/type");//获取cpu的厂商类型
        cmdList.add("netcfg |grep wlan0|busybox awk '{print $5}'");//获取wifi的mac地址
        cmdList.add("cat /sys/class/net/wlan0/address");//获取wifi的mac地址
        cmdList.add("ping -c 3 -W 5 www.baidu.com");//获取ping信息
        cmdList.add("logcat -v time");//获取日志，此处代码不适合使用，io流会一直执行。
        //
        manager = new LinearLayoutManager(this);
        ((LinearLayoutManager) manager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        //
        adapter = new RecyclerAdapter(this);
        adapter.setList(cmdList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String str, int position) {
                et_input.setText(str);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
