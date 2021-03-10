package com.zpj.minote.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.zpj.http.core.IHttp;
import com.zpj.minote.R;
import com.zpj.minote.api.HttpApi;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.recyclerview.EasyViewHolder;
import com.zpj.recyclerview.IEasy;
import com.zpj.utils.PrefsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final List<JSONObject> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String cookie = PrefsHelper.with().getString("cookie");
        Log.d("MainActivity", "cookie=" + cookie);

        EasyRecyclerView<JSONObject> recyclerView = new EasyRecyclerView<>(findViewById(R.id.recycler_view));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        recyclerView.setData(list)
                .setItemRes(R.layout.item_note)
                .setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
                .onBindViewHolder(new IEasy.OnBindViewHolderListener<JSONObject>() {
                    @Override
                    public void onBindViewHolder(EasyViewHolder holder, List<JSONObject> list, int position, List<Object> payloads) {
                        JSONObject object = list.get(position);
                        try {
//                            holder.setText(R.id.tv_title, object.getString(""));
                            holder.setText(R.id.tv_content, object.getString("snippet"));
                            holder.setText(R.id.tv_info, format.format(new Date(Long.parseLong(object.getString("modifyDate")))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();
        recyclerView.showLoading();


        HttpApi.getNodeList(cookie)
                .onSuccess(new IHttp.OnSuccessListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject obj) throws Exception {
                        Log.d("MainActivity", "obj=" + obj);
                        if (obj.has("data")) {
                            JSONArray entries = obj.getJSONObject("data").getJSONArray("entries");
                            for (int i = 0; i < entries.length(); i++) {
                                list.add(entries.getJSONObject(i));
                            }
                            recyclerView.showContent();
                        } else {
                            Toast.makeText(MainActivity.this, "登录已过期，请重新登录！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }

                    }
                })
                .subscribe();

    }
}