package com.xth.irtextdb.textdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.xth.irtextdb.util.ConstantUtil;
import com.xth.irtextdb.util.Constants;
import com.xth.irtextdb.util.LogUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by XTH on 2017/12/20.
 */

public class TextToDb {
    private static final String DbName = "irlibaray.db";
    private static final String PacketName = "com.xth.irtextdb";
    private static final String DbPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PacketName;
    private static final String DbPathName = DbPath + "/" + DbName;
    private Context mContext;
    private SQLiteDatabase database;
    private int dbType;

    private int startCursor;//起始游标，开始为负数，到需要计算的码库字节为0 大于0表示开始运算
    private int endCursor;//末尾游标，大于0表示运算结束
    private int[] hexArray = new int[5];//码库字节数据暂存
    private byte[] codeByteArray = new byte[230];//码库所有数据缓存
    private char[] codeCharArray = new char[100];//除码库外所有数据缓存
    private String infoStringBuffer,infoCodeString;//info文本中码库缓存
    private int hexLength;//table中码库数据长度,在2_info中用于保存码库index,在info中用于‘/**/’运算
    private int startPlace;//开始运算和存储的文本位置，主要用于左大括号{计数
    private int serial;//码库index

    private String brandCn;
    private String brandEn;
    private String model;
    private boolean isEdit;
    private int slashCounter;

    private void resetValue() {
        startCursor = -2;
        endCursor = 0;
        hexArray = new int[5];
        codeByteArray = new byte[230];
        codeCharArray = new char[100];
        hexLength = 0;
        startPlace = 0;
        serial = 0;
        brandCn = "";
        brandEn = "";
        model = "";
        isEdit = false;
        infoCodeString = "";
        infoStringBuffer = "";
        slashCounter = 0;
    }

    public TextToDb(Context context) {
        mContext = context;
        database = SQLiteDatabase.openOrCreateDatabase(DbPathName, null);
    }

    public void closeDatabase() {
        if (database != null) {
            database.close();
            database = null;
        }
        LogUtil.v("closeDatabase: ");
    }

    private void createTable(String tableName) {
        boolean flag = false;
        String createTable = "create table " + tableName + " (" +
                "ID integer primary key autoincrement," +
                "SERIAL integer," +
                "BRAND_CN text," +
                "BRAND_EN text," +
                "MODEL text," +
                "CODE blob)";
        Cursor cursor = database.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            LogUtil.v("tableName: " + name);
            if (name.equals(tableName) == true) {
                flag = true;
            }
        }
        if (flag == false) {
            LogUtil.v("createTable: " + createTable);
            database.execSQL(createTable);
        }
    }

    public void insert(String tableName, int serial, byte[] code) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("SERIAL", serial);
        cValue.put("BRAND_CN", "");
        cValue.put("BRAND_EN", "");
        cValue.put("MODEL", "");
        cValue.put("CODE", code);
        database.insert(tableName, null, cValue);
    }

    public void insert(String tableName, int serial, String brandCn, String brandEn, String model, int code) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("SERIAL", serial);
        cValue.put("BRAND_CN", brandCn);
        cValue.put("BRAND_EN", brandEn);
        cValue.put("MODEL", model);
        cValue.put("CODE", code);
        database.insert(tableName, null, cValue);
    }
    public void insert(String tableName, int serial, String brandCn, String brandEn, String code) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("SERIAL", serial);
        cValue.put("BRAND_CN", brandCn);
        cValue.put("BRAND_EN", brandEn);
        cValue.put("MODEL", "");
        cValue.put("CODE", code);
        database.insert(tableName, null, cValue);
    }
    public void insert(String tableName, int serial, String brandCn, String brandEn, byte[] code) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("SERIAL", serial);
        cValue.put("BRAND_CN", brandCn);
        cValue.put("BRAND_EN", brandEn);
        cValue.put("MODEL", "");
        cValue.put("CODE", code);
        database.insert(tableName, null, cValue);
    }

    public void readTextFile(String fileName) {
        FileInputStream in = null;
        BufferedReader reader = null;
        resetValue();//读一个文件前先把数据重置
        if (fileName.contains("table")) {
            dbType = Constants.TABLE;
        } else if (fileName.contains("2_info")) {
            dbType = Constants.INFO2;
        } else if (fileName.contains("info")) {
            dbType = Constants.INFO;
        } else if(fileName.contains("one_key")){
            dbType = Constants.ONEKEY;
        }
        createTable(fileName);
        try {
            in = mContext.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            int line = 0;
            char value = 0;
            try {
                while ((line = reader.read()) != -1) {
                    value = (char) line;
                    switch (dbType) {
                        case Constants.TABLE:
                            tableCopyToDb(fileName, value);
                            break;
                        case Constants.INFO2:
                            info2CopyToDb(fileName, value);
                            break;
                        case Constants.INFO:
                            infoCopyToDb(fileName, value);
                            break;
                        case Constants.ONEKEY:
                            oneKeyCopyToDb(fileName, value);
                            break;
                        default:
                            break;
                    }

//                    LogUtil.v(value + "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void tableCopyToDb(String fileName, char value) {
        switch (value) {
            case '{':
                endCursor = 0;//结束游标清零
                startPlace++;//{，左括号计数器
                startCursor = -2;//开始游标置位
                break;
            case '}':
                switch (fileName) {
                    case Constants.arc_table://在该文件中，第4个{才到码库位置
                        if (startPlace >= 4) {
                            endCursor = 1;//游标位移
                        }
                        break;
                    default:
                        if (startPlace >= 2) {
                            endCursor = 1;//游标位移
                        }
                        break;
                }
                break;
            case ',':
                if (startCursor == 1) {//游标位移
                    startCursor = -2;//游标位移
                    hexArray[1] = hexArray[0];
                    hexArray[0] = 0;
                    codeByteArray[hexLength - 1] = ConstantUtil.intArrayTurnByte(hexArray);//两个字符运算得到最终的16进制
                }
                if (endCursor == 1) {//游标位移
                    endCursor = 2;//游标位移
                    startCursor = -2;//游标位移
                    byte[] tempArray = new byte[hexLength];//最终要存进数据库的码库数组
                    System.arraycopy(codeByteArray, 0, tempArray, 0, hexLength);
                    LogUtil.v("fileName:"+fileName+",serial:" + serial + "--tempArray:" + ConstantUtil.bytes2HexString(tempArray));
                    insert(fileName, serial, tempArray);
                    serial++;//从零开始累加
                    hexLength = 0;//码库长度清零
                }
                break;
            case 'x':
            case 'X':
                if (startCursor == -1) {//游标位移
                    startCursor = 0;///游标位移
                    hexLength++;
                }
                break;
            case '0':
                if (startCursor == -2 && endCursor == 0) {//在{和}之间，已经开始了，还没有结束
                    switch (fileName) {
                        case Constants.arc_table://在该文件中，第4个{才到码库位置
                            if (startPlace >= 4) {
                                startCursor = -1;//游标位移
                            }
                            break;
                        default:
                            if (startPlace >= 2) {
                                startCursor = -1;///游标位移
                            }
                            break;
                    }
                }
            default:
                if (startCursor == 0) {
                    hexArray[0] = ConstantUtil.hexArrayTurnInt(ConstantUtil.bigToSmall(value));//将第一个字符转成对应字符的整数
                    startCursor = 1;//游标位移
                } else if (startCursor == 1) {
                    hexArray[1] = ConstantUtil.hexArrayTurnInt(ConstantUtil.bigToSmall(value));//将第二个字符转成对应字符的整数
                    codeByteArray[hexLength - 1] = ConstantUtil.intArrayTurnByte(hexArray);//两个字符运算得到最终的16进制
//                    LogUtil.v("hexArray[0]:"+hexArray[0]+"hexArray[1]:"+hexArray[1]+"codeByteArray["+(hexLength - 1)+"]"+codeByteArray[hexLength - 1]);
                    startCursor = -2;//游标位移
                }
                break;
        }
    }

    private void info2CopyToDb(String fileName, char value) {
        switch (value) {
            case '{':
                endCursor = 0;//结尾游标置位
                startCursor = -1;//起始游标置位
                startPlace++;//位置计数
                break;
            case '}':
                if (startCursor >= 0) {//游标检测要运算的数据完毕
                    hexLength = ConstantUtil.codeArrayToInt(codeCharArray, startCursor);//品牌的行数
                    startCursor = -5;
                }
                break;
            case ',':
                if (startCursor >= 0) {//游标检测要运算的数据完毕
                    startCursor = -1;
                } else if (startCursor == -5) {
                    startCursor = -4;
                }
                break;
            case '*':
                if (startCursor == -3) {
                    startCursor = -2;
                }
                break;
            case '@':
                int i,tempModel = 0,tempEn = 0,tempCn = 0,count = 0;
                for (i = startCursor; i >= 0; i--) {
                    if(codeCharArray[i] == ')'){
                        count ++;
                    } else if (codeCharArray[i] == '(') {//判断有左括号
                        count --;
                        switch (fileName){
                            case Constants.g_remote_arc_2_info:
                                if(tempModel == 0 && count == 0){
                                    tempModel = i;
                                }
                                break;
                            default:
                                tempEn = i;
                                break;
                        }

                    }else if(codeCharArray[i] == ' '){
                        switch (fileName){
                            case Constants.g_remote_arc_2_info:
                                if(tempEn == 0){
                                    tempEn = i;
                                }
                                break;
                            default:
                                if(tempCn == 0){
                                    tempCn = i;
                                }
                                break;
                        }

                    }else if(codeCharArray[i] == '-'){
                        switch (fileName){
                            case Constants.g_remote_arc_2_info:
                                break;
                            default:
                                tempModel = i;
                                break;
                        }
                    }
                }
                char[] enArray = new char[50];
                switch (fileName){
                    case Constants.g_remote_arc_2_info:
                        model = ConstantUtil.codeArrayToString(codeCharArray, tempModel - 1).trim();//左括号之前的所有东西都是型号
                        System.arraycopy(codeCharArray, tempModel + 1, enArray, 0, tempEn - tempModel - 1);
                        brandEn = ConstantUtil.codeArrayToString(enArray, tempEn - tempModel - 2);
                        break;
                    default:
                        brandCn = ConstantUtil.codeArrayToString(codeCharArray, tempEn - 1).trim();

                        char[] modelArray = new char[50];
                        System.arraycopy(codeCharArray, tempModel + 1, modelArray, 0, startCursor - tempModel);
                        model = ConstantUtil.codeArrayToString(modelArray, startCursor - tempModel - 1).trim();//左括号之前的所有东西都是型号

                        System.arraycopy(codeCharArray, tempEn + 1, enArray, 0, tempModel - tempEn - 2);
                        brandEn = ConstantUtil.codeArrayToString(enArray, tempModel - tempEn - 3);
                        break;
                }
                if (endCursor == 0) {
                    endCursor = 1;
                    startCursor = -2;
                    LogUtil.v("fileName:"+fileName+",serial:" + serial + "--brandCn:" + brandCn + "--brandEn:" + brandEn + "--model:" + model + "--code:" + hexLength);
                    insert(fileName, serial, brandCn, brandEn, model, hexLength);
                    serial++;
                    brandCn = "";
                    brandEn = "";
                    model = "";
                    hexLength = 0;
                }
                break;
            case '\r':
                break;
            case '\n':
                break;
            case '\t':
                break;
            default:
                if(startPlace >= 2){
                    if(value == '/'){
                        if (startCursor == -4) {
                            startCursor = -3;
                        }
                    }
                    if(value == ' '){
                        if (startCursor == -2) {
                            startCursor = -1;
                            isEdit = true;
                        } else if (startCursor >= 0 && isEdit) {
                            switch (fileName){
                                case Constants.g_remote_arc_2_info:
                                    isEdit = false;
                                    brandCn = ConstantUtil.codeArrayToString(codeCharArray, startCursor).trim();
                                    startCursor = -1;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (startCursor >= -1) {//游标检测到第二个{
                        if(startCursor == -1 && value == ' '){

                        }else{
                            startCursor++;//游标累加，最小值是0
                            codeCharArray[startCursor] = value;//开始保存数据，知道检测到',';
                        }
                    }
                }
                break;
        }
    }
    private void infoCopyToDb(String fileName, char value) {
        switch (value){
            case '{':
                endCursor = 0;//结尾游标置位
                startCursor = -1;//起始游标置位
                startPlace++;//位置计数
                infoStringBuffer = "";
                break;
            case '}':
                endCursor = 1;
                startCursor = -4;
                infoCodeString  = infoStringBuffer.replace(" ","");
                infoStringBuffer = "";
                break;
            case '\r':
                break;
            case '\n':
                break;
            case '\t':
                break;
            default:
                if(startPlace >= 2){
                    if(startCursor >= -1){
                        if(endCursor == 0){
                            if(value == '/'){
                                isEdit = !isEdit;//刚开始是false,检测到/后变成true，再检测到/变成false
                                break;
                            }
                        }else{
                            if(value == '('){
                                brandCn = infoStringBuffer.trim();
                                startCursor = -1;
                                infoStringBuffer = "";
                                break;
                            }else if(value == ')'){
                                brandEn = infoStringBuffer;
                                LogUtil.v("fileName:"+fileName+",serial:" + serial + "--brandCn:" + brandCn + "--brandEn:" + brandEn + "--code:" + infoCodeString);
                                insert(fileName,serial,brandCn,brandEn,infoCodeString);
                                infoStringBuffer = "";
                                brandCn = "";
                                brandEn = "";
                                serial++;
                                break;
                            }
                        }
                        if(!isEdit){//ture的情况下，即检测到/**/的情况下不缓存
                            startCursor++;
                            infoStringBuffer += value;
                        }
                    }else{
                        switch(value){
                            case ',':
                                if(startCursor == -4){
                                    startCursor = -3;
                                }
                                break;
                            case '/':
                                if(startCursor == -3){
                                    startCursor = -2;
                                }
                                break;
                            case '*':
                                if(startCursor == -2){
                                    startCursor = -1;
                                }
                                break;
                        }
                    }

                }
                break;
        }
    }

    private void oneKeyCopyToDb(String fileName,char value){
        switch (value){
            case '"':
                endCursor = 0;//结束游标清零
                if(startPlace == 0){//为了和全局一键匹配中的大括号对应读取数据
                    startPlace = 1;
                }
                startPlace++;
                startCursor = -1;//开始游标置位
                if(startPlace % 2 == 1){
                    insert(fileName,serial,brandCn,brandEn,codeByteArray);
                    LogUtil.v("fileName:"+fileName+",serial:" + serial + "--brandCn:" + brandCn + "--brandEn:" + brandEn + "--code:" + ConstantUtil.bytes2HexString(codeByteArray));
                    serial ++;
                    hexLength = 0;
                }
                break;
            case '{':
                endCursor = 0;//结束游标清零
                startPlace++;
                startCursor = -1;//开始游标置位
                break;
            case '}':
                if(endCursor == 0){
                    if(!fileName.equals(Constants.all_arc_one_key)){
                        insert(fileName,serial,codeByteArray);
                        LogUtil.v("fileName:"+fileName+",serial:" + serial + "--brandCn:" + brandCn + "--brandEn:" + brandEn + "--code:" + ConstantUtil.bytes2HexString(codeByteArray));
                        serial++;
                    }
                    hexLength = 0;
                    endCursor = 1;
                }
                break;
            case '/':
                slashCounter++;
                endCursor = 1;
                startCursor = -1;
                if(slashCounter == 1){
                    break;
                }
                if(fileName.equals(Constants.brand_stb_one_key) ){
                    if(isEdit){
                        brandCn = infoStringBuffer.trim();
                        brandEn = "";
                        infoStringBuffer = "";
                    }
                    isEdit = false;
                }
                break;
            case '\r':
                break;
            case '\n':
                break;
            case '\t':
                break;
            default:
                if(startPlace >= 2){
                    if(startCursor >= -1) {
                        if(endCursor == 0){
                            startCursor++;
                            //每两个字节为一个16进制
                            if (startCursor % 2 == 0) {
                                hexArray[0] = ConstantUtil.hexArrayTurnInt(ConstantUtil.bigToSmall(value));//将第一个字符转成对应字符的整数
                            } else {
                                hexLength++;
                                hexArray[1] = ConstantUtil.hexArrayTurnInt(ConstantUtil.bigToSmall(value));//将第二个字符转成对应字符的整数
                                codeByteArray[hexLength - 1] = ConstantUtil.intArrayTurnByte(hexArray);//两个字符运算得到最终的16进制
                            }
                        }
                    }
                }
                if(endCursor == 1){
                    slashCounter = 0;
                    if(fileName.equals(Constants.brand_stb_one_key)){
                        infoStringBuffer += value;//中文名字
                        isEdit = true;
                    }else if(fileName.equals(Constants.all_arc_one_key)){
                        if(value == '*'){
                            isEdit = !isEdit;
                            if(!isEdit){
                                serial = ConstantUtil.codeArrayToInt(codeCharArray,hexLength - 1);
                                insert(fileName,serial,codeByteArray);
                                LogUtil.v("fileName:"+fileName+",serial:" + serial + "--brandCn:" + brandCn + "--brandEn:" + brandEn + "--code:" + ConstantUtil.bytes2HexString(codeByteArray));
                                hexLength = 0;
                            }
                        }else{
                            if(isEdit){
                                codeCharArray[hexLength] = value;
                                hexLength ++;
                            }
                        }

                    }
                    else{
                        if(value == ' ' && !isEdit){
                            infoStringBuffer = "";
                            isEdit = true;
                        }else if(value == '('){
                            brandCn = infoStringBuffer.trim();
                            infoStringBuffer = "";
                        }else if(value == ')'){
                            isEdit = false;
                            brandEn = infoStringBuffer.trim().replace("(","");
                        }
                        infoStringBuffer+= value;
                    }

                }
                break;

        }
    }

}
