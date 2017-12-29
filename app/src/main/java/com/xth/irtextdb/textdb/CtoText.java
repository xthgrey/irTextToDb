package com.xth.irtextdb.textdb;

import android.content.Context;
import android.util.Log;

import com.xth.irtextdb.util.Constants;
import com.xth.irtextdb.util.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by XTH on 2017/12/19.
 * 注：TV_stb_DVD_arc_data_table_V8.7.c 修改
 * 1、删除备注含有 g_remote_arc_info 的那一行
 * 2、删除43037整行备注
 * 3、45888,45890,45892,45962,45964,45966,45968，46086,行备注在@前加一个')'
 * 4、46080行添加英文标注YORK
 * 5、大金和INVERTER备注"/*"后面加空格
 * 6、43522，43524，43526,44138,44140,45208,45598备注格式改成和其他备注一样
 * 7、修改remote_fan_2_info,remote_IPTV_2_info,remote_pjt_2_info,remote_stb_2_info,remote_tv_2_info 中的备注
 * 8、修改remote_stb_2_info中D-BOX为DBOX, 凯擘大宽屏(KBRO)-L-3/160545-kbro@2 为 凯擘大宽屏(kbro)-KBRO-L-3/160545@2，Shenzhen Print-Rite为Shenzhen Print Rite
 * 9、remote_tv_info改为 remote_tv_2_info
 * 10、remote_Audio_2_info改修缩进
 *
 * brand_tv_one_key文本中12870行少了“",”7663加英文(TOS)，12795行重新修改备注，和其他备注格式一样，用tab空格
 * brand_dvd_one_key 9703,13266行去掉一个（,13080，13101,7499,7600行加一个），13170加一个空格,2601品牌加(AIZI)
 *
 */

public class CtoText {
    private Context mContext;
    private BufferedWriter writer;
    private BufferedReader bufferedReader;
    private FileOutputStream out;
    private InputStreamReader inputStreamReader;

    public CtoText(Context context) {
        mContext = context;
    }

    private String searchFileName(String line,String createTextName) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 11; j++){
                if(line.contains(Constants.fileName[i][j])){
                    return Constants.fileName[i][j];
                }
            }
        }
        return createTextName;
    }


    public void readCFile(String fileName) {
        try {
            out = null;
            writer = null;
            inputStreamReader = new InputStreamReader(mContext.getAssets().open(fileName));
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            String createTextName = "";
            while ((line = bufferedReader.readLine()) != null) {
                createTextName = searchFileName(line,createTextName);
                if (!createTextName.equals("")) {
                    if (out == null && writer == null) {
                        out = mContext.openFileOutput(createTextName, Context.MODE_PRIVATE);
                        writer = new BufferedWriter(new OutputStreamWriter(out));
                    }
                    if (line.contains(Constants.text_end)) {//检测到末尾
                        writer.write(line + "\n");
                        createTextName = "";
//                        LogUtil.v(fileName+":createTextName:"+createTextName+":"+line);
                        if (writer != null) {
                            try {
                                writer.close();
                                writer = null;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                                out = null;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        writer.write(line + "\n");
//                        LogUtil.v(fileName+":createTextName:"+createTextName+":"+line);
                    }
                }

            }
            LogUtil.d("readCFile:"+fileName +" over");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                    writer = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
