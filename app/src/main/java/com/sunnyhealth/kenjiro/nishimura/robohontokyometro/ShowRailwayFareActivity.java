package com.sunnyhealth.kenjiro.nishimura.robohontokyometro;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

public class ShowRailwayFareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_railway_fare);
        Log.d("ANDROID", "RailwayFare.");

        // タイトルの設定
        setupTitleBar();
    }

    private void setupTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        setTitle("運賃検索");
    }
}
