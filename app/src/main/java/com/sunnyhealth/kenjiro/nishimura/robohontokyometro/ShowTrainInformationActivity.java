package com.sunnyhealth.kenjiro.nishimura.robohontokyometro;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

public class ShowTrainInformationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_train_information);
        Log.d("ANDROID", "TrainInformation.");

        // タイトルの設定
        setupTitleBar();
    }

    private void setupTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        setTitle("運行情報");
    }
}
