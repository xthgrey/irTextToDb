package com.xth.irtextdb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xth.irtextdb.textdb.CtoText;
import com.xth.irtextdb.R;
import com.xth.irtextdb.textdb.TextToDb;
import com.xth.irtextdb.util.Constants;
import com.xth.irtextdb.util.LogUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CtoText ctoText = new CtoText(this);
        ctoText.readCFile(Constants.irCFileName);
        for (int i = 0; i < 11; i++) {
            ctoText.readCFile(Constants.fileName[3][i]);
        }
        TextToDb textToDb = new TextToDb(this);
//        textToDb.readTextFile(Constants.arc_table);
//        textToDb.readTextFile(Constants.dvd_data_table);
//        textToDb.readTextFile(Constants.remote_air_purifier_table);
//        textToDb.readTextFile(Constants.remote_Audio_table);
//        textToDb.readTextFile(Constants.remote_IPTV_table);
//        textToDb.readTextFile(Constants.remote_SLR_table);
//        textToDb.readTextFile(Constants.remote_Water_Heater_table);
//        textToDb.readTextFile(Constants.stb_data_table);
//        textToDb.readTextFile(Constants.stb_fan_table);
//        textToDb.readTextFile(Constants.stb_pjt_table);
//        textToDb.readTextFile(Constants.tv_table);

//        textToDb.readTextFile(Constants.g_remote_arc_2_info);
//        textToDb.readTextFile(Constants.remote_dvd_2_info);
//        textToDb.readTextFile(Constants.remote_air_purifier_2_info);
//        textToDb.readTextFile(Constants.remote_Audio_2_info);
//        textToDb.readTextFile(Constants.remote_IPTV_2_info);
//        textToDb.readTextFile(Constants.remote_SLR_2_info);
//        textToDb.readTextFile(Constants.remote_Water_Heater_2_info);
//        textToDb.readTextFile(Constants.remote_stb_2_info);
//        textToDb.readTextFile(Constants.remote_fan_2_info);
//        textToDb.readTextFile(Constants.remote_pjt_2_info);
//        textToDb.readTextFile(Constants.remote_tv_2_info);

//        textToDb.readTextFile(Constants.g_remote_arc_info);
//        textToDb.readTextFile(Constants.remote_dvd_info);
//        textToDb.readTextFile(Constants.remote_air_purifier_info);
//        textToDb.readTextFile(Constants.remote_Audio_info);
//        textToDb.readTextFile(Constants.remote_IPTV_info);
//        textToDb.readTextFile(Constants.remote_SLR_info);
//        textToDb.readTextFile(Constants.remote_Water_Heater_info);
//        textToDb.readTextFile(Constants.remote_stb_info);
//        textToDb.readTextFile(Constants.remote_fan_info);
//        textToDb.readTextFile(Constants.remote_pjt_info);
//        textToDb.readTextFile(Constants.TV_info);

//        textToDb.readTextFile("all_air_purifier_one_key");
//        textToDb.readTextFile("all_arc_one_key");
//        textToDb.readTextFile("all_fan_one_key");
//        textToDb.readTextFile("all_IPTV_one_key");
//        textToDb.readTextFile("all_pjt_one_key");
//        textToDb.readTextFile("all_SLR_one_key");
//        textToDb.readTextFile("all_Water_Heater_one_key");
//        textToDb.readTextFile("brand_Audio_one_key");
//        textToDb.readTextFile("brand_dvd_one_key");
//        textToDb.readTextFile("brand_stb_one_key");
//        textToDb.readTextFile("brand_tv_one_key");
            for(int i = 0;i < 4;i ++){
                for(int j = 0; j < 11; j ++){
                    textToDb.readTextFile(Constants.fileName[i][j]);
                    LogUtil.d("read:"+ Constants.fileName[i][j] + " over!");
                }
            }
    }
}
