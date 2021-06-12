package com.zpj.minote.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lxj.xpermission.PermissionConstants;
import com.lxj.xpermission.XPermission;
import com.zpj.minote.R;
import com.zpj.utils.PrefsHelper;
import com.zpj.utils.StatusBarUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparentStatusBar(this);

        setContentView(R.layout.activity_splash);
        StatusBarUtils.setDarkMode(getWindow());

        showRequestPermissionPopup();

    }

    private void showRequestPermissionPopup() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("权限申请")
                    .setMessage("本软件需要存储权限用于文件的下载与查看，是否申请存储权限？")
                    .setPositiveButton("去申请", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermission();
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create().show();
        }
    }

    private void requestPermission() {
        XPermission.create(getApplicationContext(), PermissionConstants.STORAGE)
                .callback(new XPermission.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        getWindow().getDecorView().postDelayed(() -> {
                            Intent intent;
                            if (TextUtils.isEmpty(PrefsHelper.with().getString("cookie", ""))) {
                                intent = new Intent(SplashActivity.this, LoginActivity.class);
                            } else {
                                intent = new Intent(SplashActivity.this, MainActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        }, 1000);
                    }

                    @Override
                    public void onDenied() {
                        showRequestPermissionPopup();
                    }
                }).request();
    }

}
