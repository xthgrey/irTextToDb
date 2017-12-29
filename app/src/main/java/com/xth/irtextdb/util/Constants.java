package com.xth.irtextdb.util;

import android.os.Environment;

/**
 * Created by XTH on 2017/12/19.
 */

public class Constants {
    public static final String irCFileName = "TV_stb_DVD_arc_data_table_V8.7.c";
    //  投影仪
    public static final String stb_pjt_table = "stb_pjt_table";
    public static final String remote_pjt_info = "remote_pjt_info";
    public static final String remote_pjt_2_info = "remote_pjt_2_info";
    //风扇
    public static final String stb_fan_table = "stb_fan_table";
    public static final String remote_fan_info = "remote_fan_info";
    public static final String remote_fan_2_info = "remote_fan_2_info";
    //机顶盒
    public static final String stb_data_table = "stb_data_table";
    public static final String remote_stb_info = "remote_stb_info";
    public static final String remote_stb_2_info = "remote_stb_2_info";
    //DVD
    public static final String dvd_data_table = "dvd_data_table";
    public static final String remote_dvd_info = "remote_dvd_info";
    public static final String remote_dvd_2_info = "remote_dvd_2_info";
    //网络机顶盒
    public static final String remote_IPTV_table = "remote_IPTV_table";
    public static final String remote_IPTV_info = "remote_IPTV_info";
    public static final String remote_IPTV_2_info = "remote_IPTV_2_info";
    //电视
    public static final String tv_table = "tv_table";
    public static final String TV_info = "TV_info";
    public static final String remote_tv_2_info = "remote_tv_2_info";
    //空调
    public static final String arc_table = "arc_table";
    public static final String g_remote_arc_info = "g_remote_arc_info";
    public static final String g_remote_arc_2_info = "g_remote_arc_2_info";
    //单反
    public static final String remote_SLR_table = "remote_SLR_table";
    public static final String remote_SLR_info = "remote_SLR_info";
    public static final String remote_SLR_2_info = "remote_SLR_2_info";
    //空气净化器
    public static final String remote_air_purifier_table = "remote_air_purifier_table";
    public static final String remote_air_purifier_info = "remote_air_purifier_info";
    public static final String remote_air_purifier_2_info = "remote_air_purifier_2_info";
    //热水器
    public static final String remote_Water_Heater_table = "remote_Water_Heater_table";
    public static final String remote_Water_Heater_info = "remote_Water_Heater_info";
    public static final String remote_Water_Heater_2_info = "remote_Water_Heater_2_info";
    //音响
    public static final String remote_Audio_table = "remote_Audio_table";
    public static final String remote_Audio_info = "remote_Audio_info";
    public static final String remote_Audio_2_info = "remote_Audio_2_info";

    public static final String brand_stb_one_key = "brand_stb_one_key";


    public static final String text_end = "};";
    public static final String[][] fileName = {
        {"stb_pjt_table",     "stb_fan_table",     "stb_data_table",   "dvd_data_table",   "remote_IPTV_table", "tv_table",         "arc_table",            "remote_SLR_table", "remote_air_purifier_table",  "remote_Water_Heater_table", "remote_Audio_table"},
        {"remote_pjt_info",   "remote_fan_info",  "remote_stb_info",  "remote_dvd_info",  "remote_IPTV_info",   "TV_info",         "g_remote_arc_info",  "remote_SLR_info",   "remote_air_purifier_info",  "remote_Water_Heater_info",  "remote_Audio_info"},
        {"remote_pjt_2_info","remote_fan_2_info","remote_stb_2_info","remote_dvd_2_info","remote_IPTV_2_info","remote_tv_2_info","g_remote_arc_2_info","remote_SLR_2_info","remote_air_purifier_2_info","remote_Water_Heater_2_info","remote_Audio_2_info"},
        {"all_pjt_one_key",   "all_fan_one_key",  "brand_stb_one_key","brand_dvd_one_key","all_IPTV_one_key",  "brand_tv_one_key","all_arc_one_key",     "all_SLR_one_key",  "all_air_purifier_one_key",  "all_Water_Heater_one_key",  "brand_Audio_one_key"}
    };
    public static final int TABLE = 0;
    public static final int INFO = 1;
    public static final int INFO2 = 2;
    public static final int ONEKEY = 3;
}
