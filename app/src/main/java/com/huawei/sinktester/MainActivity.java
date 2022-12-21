package com.huawei.sinktester;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SinkTesterMainActivity";
    private SwitchLayout mMainSwitchLayout;
    private SwitchLayout mEncryptionSwitchLayout;
    private SwitchLayout mPasswordLayout;
    private SwitchLayout mFpsLayout;
    private SwitchLayout mRotateLayout;

    private TextView mWifiNameTextView;
    private TextView mTvNameTextView;
    private boolean mIsDiscoverable;
    private boolean mAuthMode;
    private String mPassword;
    private int mFps;
    private WifiManager mWifi;
    private BluetoothAdapter myDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //去掉信息栏 Remove the ActionBar，
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, SinkTesterService.class);
        startService(intent);
        mMainSwitchLayout = findViewById(R.id.main_switch_layout);
        mEncryptionSwitchLayout = findViewById(R.id.encryption_switch_layout);
        mPasswordLayout = findViewById(R.id.password_layout);
        mWifiNameTextView = findViewById(R.id.wifi_name_textview);
        mTvNameTextView = findViewById(R.id.tv_name_text_view);
        mFpsLayout=findViewById(R.id.fps_layout);
        mRotateLayout=findViewById(R.id.rotate_layout);
        mMainSwitchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsDiscoverable = SharedPreferenceUtil.getDiscoverable(MainActivity.this);
                mIsDiscoverable = !mIsDiscoverable;
                Log.d(TAG, "mMainSwitchLayout onClick(), mIsDiscoverable: " + mIsDiscoverable);
                SharedPreferenceUtil.setDiscoverable(MainActivity.this, mIsDiscoverable);
                Intent setDiscoverableIntent = new Intent();
                setDiscoverableIntent.setAction(SinkTesterService.BROADCAST_ACTION_SET_DISCOVERABLE);
                setDiscoverableIntent.putExtra("discoverable", mIsDiscoverable);
                sendBroadcast(setDiscoverableIntent);
                mMainSwitchLayout.setCheck(mIsDiscoverable);

            }
        });
        mEncryptionSwitchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mEncryptionSwitchLayout onClick().");
                mAuthMode = SharedPreferenceUtil.getAuthMode(MainActivity.this);
                mAuthMode = !mAuthMode;
                Log.d(TAG, "mEncryptionSwitchLayout onClick(), mAuthMode: " + mAuthMode);
                SharedPreferenceUtil.setAuthMode(MainActivity.this, mAuthMode);
                Intent setAuthModeIntent = new Intent();
                setAuthModeIntent.setAction(SinkTesterService.BROADCAST_ACTION_SET_AUTH_MODE);
                setAuthModeIntent.putExtra("needpassword", mAuthMode);
                setAuthModeIntent.putExtra("password", mPassword);
                setAuthModeIntent.putExtra("ispasswordnew", false);
                sendBroadcast(setAuthModeIntent);
                mEncryptionSwitchLayout.setCheck(mAuthMode);
                mPasswordLayout.setEnabled(mAuthMode);
                setPassWordVisible(mAuthMode);
            }
        });
        mPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });


        mFpsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle("选择fps");
                final String[] fps = {"30","60"};
                builder.setItems(fps, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which==0){
                            SharedPreferenceUtil.setFps(getApplicationContext(),30);
                        }
                        else if (which ==1 ){
                            SharedPreferenceUtil.setFps(getApplicationContext(),60);
                        }
                        onResume();
                    }
                });
                builder.show();
            }
        });
        mRotateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择方向");
                final String[] rotate = {"默认方向","反向"};
                builder.setItems(rotate, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which==0){
                            SharedPreferenceUtil.setRotation(getApplicationContext(),true);
                        }
                        else if (which ==1 ){
                            SharedPreferenceUtil.setRotation(getApplicationContext(),false);
                        }
                        onResume();
                        Toast.makeText(MainActivity.this, "重启程序生效", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });
    }
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called.");
        super.onResume();
        mIsDiscoverable = SharedPreferenceUtil.getDiscoverable(this);
        Intent setDiscoverableIntent = new Intent();
        setDiscoverableIntent.setAction(SinkTesterService.BROADCAST_ACTION_SET_DISCOVERABLE);
        setDiscoverableIntent.putExtra("discoverable", mIsDiscoverable);
        sendBroadcast(setDiscoverableIntent);
        mAuthMode = SharedPreferenceUtil.getAuthMode(this);
        mPassword = SharedPreferenceUtil.getPassword(MainActivity.this);
        mFps =SharedPreferenceUtil.getFps(this);
        Log.d(TAG, "onResume(), mIsDiscoverable: " + mIsDiscoverable + " mAuthMode: " + mAuthMode + " mPassword: " + mPassword);
        mMainSwitchLayout.setCheck(mIsDiscoverable);
        mEncryptionSwitchLayout.setCheck(mAuthMode);
        mPasswordLayout.setPwd(mPassword);
        mFpsLayout.setPwd(String.valueOf(mFps));
        setPassWordVisible(mAuthMode);


        WindowManager windowManager = getWindow().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        SharedPreferenceUtil.setWidth(this,width);
        SharedPreferenceUtil.setHeight(this,height);

        Intent setAuthModeIntent = new Intent();
        setAuthModeIntent.setAction(SinkTesterService.BROADCAST_ACTION_SET_AUTH_MODE);
        setAuthModeIntent.putExtra("needpassword", mAuthMode);
        setAuthModeIntent.putExtra("password", mPassword);
        setAuthModeIntent.putExtra("ispasswordnew", false);
        sendBroadcast(setAuthModeIntent);
        mPasswordLayout.setEnabled(mEncryptionSwitchLayout.getCheck());
        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        registerReceiver(mBroadcastReceiver, broadcastFilter);
        myDevice = BluetoothAdapter.getDefaultAdapter();
        mTvNameTextView.setText(Build.DEVICE);
        if (myDevice != null && myDevice.getName() != null) {
            mTvNameTextView.setText("电视名称：" + myDevice.getName());
        } else {
            mTvNameTextView.setText("电视名称：" + "CastPlusTestDevice");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
        mWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifi != null) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            if (wifiInfo != null) {
                String ssid = wifiInfo.getSSID();
                if (!TextUtils.isEmpty(ssid) && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    mWifiNameTextView.setText("网络名称：" + ssid.substring(1, ssid.length() - 1));
                } else {
                    mWifiNameTextView.setText("网络名称：" + ssid);
                }
            }
        }
    }
    private void setPassWordVisible(boolean mAuthMode) {
        if (mAuthMode) {
            mPasswordLayout.setVisibility(View.VISIBLE);
            mPasswordLayout.setVisibility(View.VISIBLE);
        } else {
            mPasswordLayout.setVisibility(View.INVISIBLE);
            mPasswordLayout.setVisibility(View.INVISIBLE);
        }
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Broadcast received, action: " + action);
            if (BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action)) {
                mTvNameTextView.setText("电视名称：" + myDevice.getName());
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called.");
        unregisterReceiver(mBroadcastReceiver);
        Intent intent = new Intent(MainActivity.this, SinkTesterService.class);
        stopService(intent);
    }
}