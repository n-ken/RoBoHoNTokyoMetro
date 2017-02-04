package com.sunnyhealth.kenjiro.nishimura.robohontokyometro.customize;

/**
 * シナリオファイルで使用する定数の定義クラス.<br>
 * <p>
 * <p>
 * controlタグのtargetにはPackage名を設定すること<br>
 * scene、memory_p(長期記憶の変数名)、resolve variable(アプリ変数解決の変数名)、accostのwordはPackage名を含むこと<br>
 * </p>
 */
public class ScenarioDefinitions {

     // sceneタグを指定する文字列
    public static final String TAG_SCENE = "scene";
    /**
     * accostタグを指定する文字列
     */
    public static final String TAG_ACCOST = "accost";
    /**
     * target属性を指定する文字列
     */
    public static final String ATTR_TARGET = "target";
    /**
     * function属性を指定する文字列
     */
    public static final String ATTR_FUNCTION = "function";
    // memory_pを指定するタグ
    public static final String TAG_MEMORY_PERMANENT = "memory_p:";
    // function：アプリ終了を通知する.
    public static final String FUNC_END_APP = "end_app";
    // function: 音声認識結果を出力する.
    public static final String FUNC_RECOG_TALK = "func_recog_talk";
    // function: 機能選択シナリオを起動する.
    public static final String FUNC_SELECT_FEATURE = "func_select_feature";
    // function: 運行情報アプリを起動する.
    public static final String FUNC_START_INFORMATION = "func_start_information";
    // function: 運賃アプリを起動する.
    public static final String FUNC_START_FARE = "func_start_fare";
    // function: 運賃検索を開始する.
    public static final String FUNC_SEARCH_FARE = "func_search_railwayfare";
    /**
     * Package名.
     */
    public static final String PACKAGE = "com.sunnyhealth.kenjiro.nishimura.robohontokyometro";
    /**
     * シナリオ共通: controlタグで指定するターゲット名.
     */
    public static final String TARGET = PACKAGE;
     // scene名: アプリ共通シーン
    public static final String SCENE_COMMON = PACKAGE + ".scene_common";
    /**
     * scene名: 特定シーン
     */
    public static final String SCENE01 = PACKAGE + ".scene01";
    // scene名: 運行情報
    public static final String SCENE_INFORMATION = PACKAGE + ".scene_information";
    // scene名: 運賃検索
    public static final String SCENE_FARE = PACKAGE + ".scene_fare";
    // accost名：こんにちは発話実行.
    public static final String ACC_HELLO = ScenarioDefinitions.PACKAGE + ".hello.say";
    // accost名：アプリ終了発話実行.
    public static final String ACC_END_APP = ScenarioDefinitions.PACKAGE + ".app_end.execute";
    // accost名: アプリ初回起動発話実行.
    public static final String ACC_TUTORIAL = ScenarioDefinitions.PACKAGE + ".tutorial";
    // accost名: 機能選択発話実行.
    public static final String ACC_SELECT_FEATURE = ScenarioDefinitions.PACKAGE + ".select.feature";
    // accost名: 運行情報確認結果発話実行.
    public static final String ACC_TRAININFORMATION = ScenarioDefinitions.PACKAGE + ".train_information";
    // accost名: 運賃検索ヒアリング発話実行.
    public static final String ACC_RAILWAYFARE = ScenarioDefinitions.PACKAGE + ".railway_fare";
    // accost名: 運賃検索ヒアリング結果発話実行.
    public static final String ACC_SPEAK_RAILWAYFARE = ScenarioDefinitions.PACKAGE + ".speak_railway_fare";
    // data key: 音声認識結果.
    public static final String KEY_LVCSR_BASIC = "lvcsr_basic";
    // data key: 発車駅路線発話.
    public static final String KEY_FROM_RAILWAY = "data_from_railway";
    // data key: 発車駅名発話.
    public static final String KEY_FROM_STATION = "data_from_station";
    // data key: 到着駅路線発話.
    public static final String KEY_TO_RAILWAY = "data_to_railway";
    // data key: 到着駅名発話.
    public static final String KEY_TO_STATION = "data_to_station";
    // memory_p: 出発路線名発話.
    public static final String MEM_P_FROM_RAILWAY =
            ScenarioDefinitions.TAG_MEMORY_PERMANENT + ScenarioDefinitions.PACKAGE +
            ".data_from_railway";
    // memory_p: 出発駅名発話.
    public static final String MEM_P_FROM_STATION =
            ScenarioDefinitions.TAG_MEMORY_PERMANENT + ScenarioDefinitions.PACKAGE +
                    ".data_from_station";
    // memory_p: 到着路線名発話.
    public static final String MEM_P_TO_RAILWAY =
            ScenarioDefinitions.TAG_MEMORY_PERMANENT + ScenarioDefinitions.PACKAGE +
                    ".data_to_railway";
    // memory_p: 到着駅名発話.
    public static final String MEM_P_TO_STATION =
            ScenarioDefinitions.TAG_MEMORY_PERMANENT + ScenarioDefinitions.PACKAGE +
                    ".data_to_station";
    /**
     * static クラスとして使用する.
     */
    private ScenarioDefinitions() {
    }
}
