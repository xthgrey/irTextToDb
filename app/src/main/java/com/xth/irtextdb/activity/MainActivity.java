package com.xth.irtextdb.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xth.irtextdb.textdb.CtoText;
import com.xth.irtextdb.R;
import com.xth.irtextdb.textdb.DbToXml;
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
        for(int i = 0;i < 4;i ++){
            for(int j = 0; j < 11; j ++){
                textToDb.readTextFile(Constants.fileName[i][j]);
                LogUtil.d("read:"+ Constants.fileName[i][j] + " over!");
            }
        }
        DbToXml dbToXml = new DbToXml(this);
        for(int i = 1;i < 3;i++){
            for(int j = 0;j< 11;j++){
                LogUtil.d("readDbToXml:"+ Constants.fileName[i][j] + " start!");
                dbToXml.readDbToXml(Constants.fileName[i][j]);
                LogUtil.d("readDbToXml:"+ Constants.fileName[i][j] + " over!");
            }
        }
    }
}
