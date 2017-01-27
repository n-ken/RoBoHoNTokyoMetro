package com.sunnyhealth.kenjiro.nishimura.robohontokyometro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.sunnyhealth.kenjiro.nishimura.robohontokyometro.customize.ScenarioDefinitions;
import com.sunnyhealth.kenjiro.nishimura.robohontokyometro.util.VoiceUIManagerUtil;
import com.sunnyhealth.kenjiro.nishimura.robohontokyometro.util.VoiceUIVariableUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.sharp.android.voiceui.VoiceUIManager;
import jp.co.sharp.android.voiceui.VoiceUIVariable;

import static com.sunnyhealth.kenjiro.nishimura.robohontokyometro.util.VoiceUIManagerUtil.TAG;

public class ShowTrainInformationActivity extends Activity implements MainActivityVoiceUIListener.MainActivityScenarioCallback {

    public static final String TAG = ShowTrainInformationActivity.class.getSimpleName();
    // 音声UIイベントリスナー.
    private MainActivityVoiceUIListener mMainActivityVoiceUIListener = null;
    // 音声UIの再起動イベント検知.
    private VoiceUIStartReceiver mVoiceUIStartReceiver = null;
    // ホームボタンイベント検知.
    private HomeEventReceiver mHomeEventReceiver;
    VoiceUIManager mVoiceUIManager = null;
    // プロパティと名称の対応付け
    Map<String, String> railwayMap = new HashMap<String, String>(){
        {put("odpt.Railway:TokyoMetro.Chiyoda", "千代田線");}
        {put("odpt.Railway:TokyoMetro.Hibiya", "日比谷線");}
        {put("odpt.Railway:TokyoMetro.Tozai", "東西線");}
        {put("odpt.Railway:TokyoMetro.Hanzomon", "半蔵門線");}
        {put("odpt.Railway:TokyoMetro.Fukutoshin", "副都心線");}
        {put("odpt.Railway:TokyoMetro.Marunouchi", "丸ノ内線");}
        {put("odpt.Railway:TokyoMetro.Yurakucho", "有楽町線");}
        {put("odpt.Railway:TokyoMetro.Namboku", "南北線");}
        {put("odpt.Railway:TokyoMetro.Ginza", "銀座線");}
    };
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_train_information);
        Log.d("ANDROID", "TrainInformation.");

        // タイトルの設定
        setupTitleBar();

    }

    public void onResume() {
        super.onResume();

        //ホームボタンの検知登録.
        mHomeEventReceiver = new HomeEventReceiver();
        IntentFilter filterHome = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeEventReceiver, filterHome);

        //VoiceUI再起動の検知登録.
        mVoiceUIStartReceiver = new VoiceUIStartReceiver();
        IntentFilter filter = new IntentFilter(VoiceUIManager.ACTION_VOICEUI_SERVICE_STARTED);
        registerReceiver(mVoiceUIStartReceiver, filter);

        //VoiceUIManagerのインスタンス取得.
        if (mVoiceUIManager == null) {
            mVoiceUIManager = VoiceUIManager.getService(getApplicationContext());
        }

        //MainActivityVoiceUIListener生成.
        if (mMainActivityVoiceUIListener == null) {
            mMainActivityVoiceUIListener = new MainActivityVoiceUIListener(this);
        }

        //VoiceUIListenerの登録
        VoiceUIManagerUtil.registerVoiceUIListener(mVoiceUIManager, mMainActivityVoiceUIListener);

        // シーンの有効化
        VoiceUIManagerUtil.enableScene(mVoiceUIManager, ScenarioDefinitions.SCENE_COMMON);
        VoiceUIManagerUtil.enableScene(mVoiceUIManager, ScenarioDefinitions.SCENE_INFORMATION);

        // データの取得
        AsyncTokyoMetro mAsyncTokyoMetro = new AsyncTokyoMetro(this, mVoiceUIManager);
        mAsyncTokyoMetro.setOnCallBack(new AsyncTokyoMetro.CallBackTask() {
            @Override
            public void CallBack(Map<String, String> data) {
                super.CallBack(data);
                // トラブルの有無に応じてメッセージを生成
                if (data.get("trouble").equals("true")) {
                    msg = "現在、一部の路線でトラブルが発生しているよ。東京メトロからのアナウンスを読み上げるね。";
                    for (Map.Entry<String, String> e: data.entrySet()) {
                        if (e.getKey().equals("trouble")) continue;
                        msg += railwayMap.get(e.getKey()) + "は、" + e.getValue();
                    }
                } else {
                    msg = "現在、東京メトロの全路線は平常通りに運行しているよ。";
                }

                int ret = VoiceUIVariableUtil.setVariableData(mVoiceUIManager,
                ScenarioDefinitions.TAG_MEMORY_PERMANENT + ScenarioDefinitions.PACKAGE + ".message_traininformation",
                msg);

                // シナリオの起動
                VoiceUIVariableUtil.VoiceUIVariableListHelper helper = new VoiceUIVariableUtil.VoiceUIVariableListHelper().addAccost(ScenarioDefinitions.ACC_TRAININFORMATION);
                VoiceUIManagerUtil.updateAppInfo(mVoiceUIManager, helper.getVariableList(), true);
            }
        });
        mAsyncTokyoMetro.execute("trainInformation");
    }

    @Override
    public void onExecCommand(String command, List<VoiceUIVariable> variables) {
        Log.v(TAG, "onExecCommand() : " + command);

        VoiceUIVariableUtil.VoiceUIVariableListHelper helper;
        Intent intent;

        switch (command) {
            case ScenarioDefinitions.FUNC_END_APP:
                finish();
                break;
            case ScenarioDefinitions.FUNC_START_FARE:
                // 運賃検索画面に遷移する
                Log.d("ANDROID", "Now, Start ActivityTransition.");
                intent = new Intent(ShowTrainInformationActivity.this, ShowRailwayFareActivity.class);
                startActivity(intent);
                break;
            case ScenarioDefinitions.FUNC_START_INFORMATION:
                // 運行情報確認画面に遷移する
                Log.d("ANDROID", "Now, Start ActivityTransition.");
                intent = new Intent(ShowTrainInformationActivity.this, ShowTrainInformationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()");

        //バックに回ったら発話を中止する.
        VoiceUIManagerUtil.stopSpeech();

        //VoiceUIListenerの解除.
        VoiceUIManagerUtil.unregisterVoiceUIListener(mVoiceUIManager, mMainActivityVoiceUIListener);

        //Scene無効化.
        VoiceUIManagerUtil.disableScene(mVoiceUIManager, ScenarioDefinitions.SCENE_COMMON);
        VoiceUIManagerUtil.disableScene(mVoiceUIManager, ScenarioDefinitions.SCENE_INFORMATION);

        // アクティビティを終了
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()");

        //ホームボタンの検知破棄.
        this.unregisterReceiver(mHomeEventReceiver);

        //VoiceUI再起動の検知破棄.
        this.unregisterReceiver(mVoiceUIStartReceiver);

        //TODO プロジェクタイベントの検知破棄(プロジェクター利用時のみ).
        //this.unregisterReceiver(mProjectorEventReceiver);

        //インスタンスのごみ掃除.
        mVoiceUIManager = null;
        mMainActivityVoiceUIListener = null;
    }

    private void setupTitleBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        setTitle("運行情報");
    }

    /**
     * 音声UI再起動イベントを受け取るためのBroadcastレシーバークラス.<br>
     * <p/>
     * 稀に音声UIのServiceが再起動することがあり、その場合アプリはVoiceUIの再取得とListenerの再登録をする.
     */
    private class VoiceUIStartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (VoiceUIManager.ACTION_VOICEUI_SERVICE_STARTED.equals(action)) {
                Log.d(TAG, "VoiceUIStartReceiver#onReceive():VOICEUI_SERVICE_STARTED");
                //VoiceUIManagerのインスタンス取得.
                mVoiceUIManager = VoiceUIManager.getService(getApplicationContext());
                if (mMainActivityVoiceUIListener == null) {
                    mMainActivityVoiceUIListener = new MainActivityVoiceUIListener(getApplicationContext());
                }
                //VoiceUIListenerの登録.
                VoiceUIManagerUtil.registerVoiceUIListener(mVoiceUIManager, mMainActivityVoiceUIListener);
            }
        }
    }

    private class HomeEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Receive Home button pressed");
            // ホームボタン押下でアプリ終了する.
            finish();
        }
    }
}
