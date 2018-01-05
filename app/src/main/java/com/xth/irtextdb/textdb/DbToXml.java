package com.xth.irtextdb.textdb;

import android.content.Context;
import android.database.Cursor;


import com.xth.irtextdb.util.LogUtil;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by XTH on 2018/1/3.
 */

public class DbToXml {
    private Context mContext;
    private DbManage dbManage;

    public DbToXml(Context mContext) {
        this.mContext = mContext;
        dbManage = new DbManage(mContext);
    }

    public void readDbToXml(String fileName) {
        Cursor cursor = dbManage.getDatabase().query(fileName, null, null, null, null, null, null);
        String result = "";
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = mContext.openFileOutput(fileName + ".xml", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (cursor.moveToNext()) {
            result = "";
            int id = cursor.getInt(0);
            int serial = cursor.getInt(1);
            String brandCn = cursor.getString(2);
            String brandEn = cursor.getString(3);
            String model = cursor.getString(4);

            //使用Log查看数据,未在界面展示
            if (fileName.contains("2_info")) {
                result = "<item>" + brandCn + "(" + brandEn + ")" + "-" + model + "</item>\n";
            } else if(fileName.contains("info")){
                result = "<item>" + brandCn + "(" + brandEn + ")" + "</item>\n";
            }
            try {
                writer.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogUtil.i("id:" + id + "---serial:" + serial + "---getBrandCn:" + brandCn + "---getBrandEn:" + brandEn +
                    "---model:" + model);
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
