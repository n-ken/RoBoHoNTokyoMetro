package com.sunnyhealth.kenjiro.nishimura.robohontokyometro;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import jp.co.sharp.android.voiceui.VoiceUIManager;

public class AsyncTokyoMetro extends AsyncTask<String, Void, String> {

    // メンバ変数
    private static String ENDPOINT_RAILWAYFARE = "https://api.tokyometroapp.jp/api/v2/datapoints?rdf:type=odpt:RailwayFare";
    private static String ENDPOINT_TRAININFORMATION = "https://api.tokyometroapp.jp/api/v2/datapoints?rdf:type=odpt:TrainInformation";
    private String mUrl;
    private Activity mActivity;
    private VoiceUIManager mVoiceUIManager = null;
    String mAccessToken;
    private String targetFeature;
    private JSONArray rootJSON;
    private CallBackTask callbacktask;

    // コンストラクタ
    public AsyncTokyoMetro(Activity activity, VoiceUIManager mVoiceUIManager) {
        super();
        this.mActivity = activity;
        this.mVoiceUIManager = mVoiceUIManager;

    }

    // 非同期処理前に実行
    @Override
    public void onPreExecute() {

        // アクセストークンの読み込み
        InputStream is = null;
        BufferedReader br = null;

        try {
            try {
                is = mActivity.getAssets().open("access_token.txt");
                br = new BufferedReader(new InputStreamReader(is));
                mAccessToken = br.readLine();
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e) {
        }
    }

    // バックグラウンド処理
    @Override
    public String doInBackground(String... params) {

        // 対象機能を取得
        targetFeature = params[0];

        // URLを生成
        switch (targetFeature) {
            case "railwayFare":
                mUrl = ENDPOINT_RAILWAYFARE + params[1] + "&acl:consumerKey=" + mAccessToken;
                break;
            case "trainInformation":
                mUrl = ENDPOINT_TRAININFORMATION + "&acl:consumerKey=" + mAccessToken;
                break;
            default:
                break;
        }
        Log.d("ANDROID", mUrl);

        // APIからデータを取得
        HttpsURLConnection con = null;
        InputStream is = null;
        String result = "";

        try {
            URL url = new URL(mUrl);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            is = con.getInputStream();
            result = is2String(is);
        } catch(MalformedURLException ex) {
        } catch(IOException ex) {
        } finally {
            if (con != null) con.disconnect();
            if (is != null) {
                try {
                    is.close();
                } catch(IOException ex) {
                }
            }
        }
        return result;
    }

    // 非同期後処理
    @Override
    public void onPostExecute(String result) {

        // JSONArrayに変換
        try {
            rootJSON = new JSONArray(result);
        } catch(JSONException ex) {
            Log.d("ANDROID", "JSONException");
        }

        // 機能に応じて処理をスイッチ
        switch (targetFeature) {
            case "railwayFare":
                // 運賃検索データの加工処理
                aggregateRailwayFare(rootJSON);
                break;
            case "trainInformation":
                // 運行情報データの加工処理
                aggregateTrainInformation(rootJSON);
                break;
        }
    }

    // 運賃検索データの加工処理
    private void aggregateRailwayFare(JSONArray rootJSON) {

        // 始点駅 / 終点駅 / 運賃の取得
        Map<String, String> data = new HashMap<String, String>();
        try {
            JSONObject rootJSONObject = rootJSON.getJSONObject(0);
            data.put("fromStation", rootJSONObject.getString("odpt:fromStation"));
            data.put("toStation", rootJSONObject.getString("odpt:toStation"));
            data.put("ticketFare", rootJSONObject.getString("odpt:ticketFare"));
            data.put("childTicketFare", rootJSONObject.getString("odpt:childTicketFare"));
            data.put("icCardFare", rootJSONObject.getString("odpt:icCardFare"));
            data.put("childIcCardFare", rootJSONObject.getString("odpt:childIcCardFare"));
        } catch(JSONException ex) {
        }

        callbacktask.CallBack(data);
    }

    // 運行情報データの加工処理
    private void aggregateTrainInformation(JSONArray rootJSON) {

        // 路線毎の運行情報を取得
        Map<String, String> data = new HashMap<String, String>();
        Map<String, String> dataTrouble = new HashMap<String, String>();
        JSONObject currentData;
        String msg;
        int counter = 0;
        for (int i = 0; rootJSON.length() > i; i++) {
            try {
                currentData = rootJSON.getJSONObject(i);
                msg = currentData.getString("odpt:trainInformationText");
                data.put(currentData.getString("odpt:railway"), msg);

                // 異常がある場合は加算
                if (!msg.equals("現在、平常どおり運転しています。")) {
                    counter += 1;
                    dataTrouble.put(currentData.getString("odpt:railway"), msg);
                }

            } catch(JSONException ex) {
            }
        }

        // 異常がある場合、"true"とし、それ以外を"false"とする
        if (counter > 0) {
            data = dataTrouble;
            data.put("trouble", "true");
        } else {
            data.put("trouble", "false");
        }

        callbacktask.CallBack(data);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(Map<String, String> data) {
        }
    }

    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while (0 <= (line = reader.read(b))) {
            sb.append(b, 0, line);
        }
        return sb.toString();
    }
}
