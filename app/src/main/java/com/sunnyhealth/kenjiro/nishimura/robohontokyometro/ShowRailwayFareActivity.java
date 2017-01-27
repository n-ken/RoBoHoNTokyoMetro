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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.sharp.android.voiceui.VoiceUIManager;
import jp.co.sharp.android.voiceui.VoiceUIVariable;

public class ShowRailwayFareActivity extends Activity implements MainActivityVoiceUIListener.MainActivityScenarioCallback {

    public static final String TAG = ShowTrainInformationActivity.class.getSimpleName();
    // 音声UIイベントリスナー.
    private MainActivityVoiceUIListener mMainActivityVoiceUIListener = null;
    // 音声UIの再起動イベント検知.
    private VoiceUIStartReceiver mVoiceUIStartReceiver = null;
    // ホームボタンイベント検知.
    private HomeEventReceiver mHomeEventReceiver;
    VoiceUIManager mVoiceUIManager = null;
    String mFromRailway;
    String mFromStation;
    String mToRailway;
    String mToStation;
    String mFromRailwayProperty;
    String mFromStationProperty;
    String mToRailwayProperty;
    String mToStationProperty;
    // 路線名をプロパティに変換するMap
    Map<String, String> railwayMap = new HashMap<String, String>() {
        {put("千代田線", "Chiyoda");}
        {put("日比谷線", "Hibiya");}
        {put("東西線", "Tozai");}
        {put("半蔵門線", "Hanzomon");}
        {put("副都心線", "Fukutoshin");}
        {put("丸ノ内線", "Marunouchi");}
        {put("有楽町線", "Yurakucho");}
        {put("南北線", "Namboku");}
        {put("銀座線", "Ginza");}
    };
    // 駅名をプロパティに変換するMap
    Map<String, String> stationMap = new HashMap<String, String>() {
        {put("表参道", "OmoteSando");}
        {put("中野坂上", "NakanoSakaue");}
        {put("中野富士見町", "NakanoFujimicho");}
        {put("新大塚", "ShinOtsuka");}
        {put("築地", "Tsukiji");}
        {put("東大前", "Todaimae");}
        {put("早稲田", "Waseda");}
        {put("西日暮里", "NishiNippori");}
        {put("南阿佐ケ谷", "MinamiAsagaya");}
        {put("人形町", "Ningyocho");}
        {put("西ケ原", "Nishigahara");}
        {put("新宿三丁目", "ShinjukuSanchome");}
        {put("原木中山", "BarakiNakayama");}
        {put("半蔵門", "Hanzomon");}
        {put("銀座一丁目", "GinzaItchome");}
        {put("乃木坂", "Nogizaka");}
        {put("押上<スカイツリー前>", "Oshiage");}
        {put("地下鉄成増", "ChikatetsuNarimasu");}
        {put("志茂", "Shimo");}
        {put("小伝馬町", "Kodemmacho");}
        {put("神谷町", "Kamiyacho");}
        {put("西船橋", "NishiFunabashi");}
        {put("上野", "Ueno");}
        {put("新高円寺", "ShinKoenji");}
        {put("四谷三丁目", "YotsuyaSanchome");}
        {put("清澄白河", "KiyosumiShirakawa");}
        {put("和光市", "Wakoshi");}
        {put("落合", "Ochiai");}
        {put("上野広小路", "UenoHirokoji");}
        {put("秋葉原", "Akihabara");}
        {put("氷川台", "Hikawadai");}
        {put("千駄木", "Sendagi");}
        {put("東京", "Tokyo");}
        {put("外苑前", "Gaiemmae");}
        {put("国会議事堂前", "KokkaiGijidomae");}
        {put("東高円寺", "HigashiKoenji");}
        {put("住吉", "Sumiyoshi");}
        {put("白金高輪", "ShirokaneTakanawa");}
        {put("四ツ谷", "Yotsuya");}
        {put("新木場", "ShinKiba");}
        {put("門前仲町", "MonzenNakacho");}
        {put("日比谷", "Hibiya");}
        {put("西早稲田", "NishiWaseda");}
        {put("仲御徒町", "NakaOkachimachi");}
        {put("赤羽岩淵", "AkabaneIwabuchi");}
        {put("王子", "Oji");}
        {put("代々木公園", "YoyogiKoen");}
        {put("錦糸町", "Kinshicho");}
        {put("千川", "Senkawa");}
        {put("御茶ノ水", "Ochanomizu");}
        {put("目黒", "Meguro");}
        {put("淡路町", "Awajicho");}
        {put("新中野", "ShinNakano");}
        {put("大手町", "Otemachi");}
        {put("末広町", "Suehirocho");}
        {put("恵比寿", "Ebisu");}
        {put("桜田門", "Sakuradamon");}
        {put("西葛西", "NishiKasai");}
        {put("新宿御苑前", "ShinjukuGyoemmae");}
        {put("新御茶ノ水", "ShinOchanomizu");}
        {put("銀座", "Ginza");}
        {put("南千住", "MinamiSenju");}
        {put("中野新橋", "NakanoShimbashi");}
        {put("高田馬場", "Takadanobaba");}
        {put("方南町", "Honancho");}
        {put("稲荷町", "Inaricho");}
        {put("中目黒", "NakaMeguro");}
        {put("湯島", "Yushima");}
        {put("王子神谷", "OjiKamiya");}
        {put("平和台", "Heiwadai");}
        {put("市ケ谷", "Ichigaya");}
        {put("竹橋", "Takebashi");}
        {put("浦安", "Urayasu");}
        {put("東銀座", "HigashiGinza");}
        {put("白金台", "Shirokanedai");}
        {put("江戸川橋", "Edogawabashi");}
        {put("東池袋", "HigashiIkebukuro");}
        {put("根津", "Nezu");}
        {put("駒込", "Komagome");}
        {put("飯田橋", "Iidabashi");}
        {put("麴町", "Kojimachi");}
        {put("南砂町", "MinamiSunamachi");}
        {put("虎ノ門", "Toranomon");}
        {put("小竹向原", "KotakeMukaihara");}
        {put("地下鉄赤塚", "ChikatetsuAkatsuka");}
        {put("二重橋前", "Nijubashimae");}
        {put("西新宿", "NishiShinjuku");}
        {put("豊洲", "Toyosu");}
        {put("広尾", "HiroO");}
        {put("渋谷", "Shibuya");}
        {put("北綾瀬", "KitaAyase");}
        {put("青山一丁目", "AoyamaItchome");}
        {put("入谷", "Iriya");}
        {put("茗荷谷", "Myogadani");}
        {put("新富町", "Shintomicho");}
        {put("溜池山王", "TameikeSanno");}
        {put("三越前", "Mitsukoshimae");}
        {put("本郷三丁目", "HongoSanchome");}
        {put("三ノ輪", "Minowa");}
        {put("本駒込", "HonKomagome");}
        {put("九段下", "Kudanshita");}
        {put("八丁堀", "Hatchobori");}
        {put("北参道", "KitaSando");}
        {put("荻窪", "Ogikubo");}
        {put("水天宮前", "Suitengumae");}
        {put("辰巳", "Tatsumi");}
        {put("南行徳", "MinamiGyotoku");}
        {put("後楽園", "Korakuen");}
        {put("東新宿", "HigashiShinjuku");}
        {put("代々木上原", "YoyogiUehara");}
        {put("神田", "Kanda");}
        {put("池袋", "Ikebukuro");}
        {put("葛西", "Kasai");}
        {put("京橋", "Kyobashi");}
        {put("赤坂", "Akasaka");}
        {put("行徳", "Gyotoku");}
        {put("永田町", "Nagatacho");}
        {put("赤坂見附", "AkasakaMitsuke");}
        {put("六本木", "Roppongi");}
        {put("木場", "Kiba");}
        {put("町屋", "Machiya");}
        {put("雑司が谷", "Zoshigaya");}
        {put("要町", "Kanamecho");}
        {put("妙典", "Myoden");}
        {put("有楽町", "Yurakucho");}
        {put("明治神宮前<原宿>", "MeijiJingumae");}
        {put("東陽町", "Toyocho");}
        {put("中野", "Nakano");}
        {put("護国寺", "Gokokuji");}
        {put("新橋", "Shimbashi");}
        {put("霞ケ関", "Kasumigaseki");}
        {put("月島", "Tsukishima");}
        {put("綾瀬", "Ayase");}
        {put("新宿", "Shinjuku");}
        {put("神楽坂", "Kagurazaka");}
        {put("田原町", "Tawaramachi");}
        {put("茅場町", "Kayabacho");}
        {put("浅草", "Asakusa");}
        {put("六本木一丁目", "RoppongiItchome");}
        {put("麻布十番", "AzabuJuban");}
        {put("北千住", "KitaSenju");}
        {put("日本橋", "Nihombashi");}
        {put("神保町", "Jimbocho");}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_railway_fare);
        Log.d("ANDROID", "RailwayFare.");

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
        VoiceUIManagerUtil.enableScene(mVoiceUIManager, ScenarioDefinitions.SCENE_FARE);

        // 検索条件ヒアリングシナリオの起動
        mFromRailway = "有楽町線";
        mFromStation = "新富町";
        mToRailway = "日比谷線";
        mToStation = "上野";
        mFromRailwayProperty = railwayMap.get(mFromRailway);
        mToRailwayProperty = railwayMap.get(mToRailway);
        mFromStationProperty = stationMap.get(mFromStation);
        mToStationProperty = stationMap.get(mToStation);
        String mUrl = "&odpt:toStation=odpt.Station:TokyoMetro." + mToRailwayProperty + "." + mToStationProperty
                + "&odpt:fromStation=odpt.Station:TokyoMetro." + mFromRailwayProperty + "." + mFromStationProperty;

        // データの取得(実際にはシナリオ起動後のコントロール下で動作することとなる)
        AsyncTokyoMetro mAsyncTokyoMetro = new AsyncTokyoMetro(this, mVoiceUIManager);
        mAsyncTokyoMetro.setOnCallBack(new AsyncTokyoMetro.CallBackTask() {
            @Override
            public void CallBack(Map<String, String> data) {
                super.CallBack(data);
                // データ取得確認
                String msg;
                msg = mFromStation + "(" + data.get("fromStation") + ")から" +
                        mToStation + "(" + data.get("toStation") + ")までの運賃は、"
                        + data.get("ticketFare") + "円だよ。子供だと" + data.get("childTicketFare")
                        + "円だよ。ICカードを使うと" + data.get("icCardFare") + "円、子供だと"
                        + data.get("childIcCardFare") + "円になるよ。";
                Log.d("ANDROID", msg);

                // 一時的にmsgを変更
                msg = "ごめんね、運賃検索機能は今工事中だよ。";
                int ret = VoiceUIVariableUtil.setVariableData(mVoiceUIManager,
                        ScenarioDefinitions.TAG_MEMORY_PERMANENT + ScenarioDefinitions.PACKAGE + ".message_railwayfare",
                        msg);

                // シナリオの起動
                VoiceUIVariableUtil.VoiceUIVariableListHelper helper = new VoiceUIVariableUtil.VoiceUIVariableListHelper().addAccost(ScenarioDefinitions.ACC_RAILWAYFARE);
                VoiceUIManagerUtil.updateAppInfo(mVoiceUIManager, helper.getVariableList(), true);
            }
        });
        mAsyncTokyoMetro.execute("railwayFare", mUrl);
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
                intent = new Intent(ShowRailwayFareActivity.this, ShowRailwayFareActivity.class);
                startActivity(intent);
                break;
            case ScenarioDefinitions.FUNC_START_INFORMATION:
                // 運行情報確認画面に遷移する
                Log.d("ANDROID", "Now, Start ActivityTransition.");
                intent = new Intent(ShowRailwayFareActivity.this, ShowTrainInformationActivity.class);
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

        // 終了
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
        setTitle("運賃検索");
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
