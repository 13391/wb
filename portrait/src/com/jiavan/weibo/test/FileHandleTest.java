package com.jiavan.weibo.test;

import com.jiavan.weibo.util.FileHandle;
import com.jiavan.weibo.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * File operation util unit test
 * Created by Jiavan on 2017/4/3.
 */
public class FileHandleTest {
    public static void main(String[] args) {
        try {
//            write();
//            read();
//            loadProp("config.properties");
            readProxy("./file/proxy_1100.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read() throws Exception {
        String result = FileHandle.read("test.txt");
        System.out.println(result);
    }

    public static void write() throws Exception{
        FileHandle.write("test.txt", "甲烷");
    }

    public static void loadProp(String propName) throws Exception {
        Properties prop = FileHandle.loadProp(propName);
        System.out.println(prop.getProperty("cookie"));
    }

    public static void readProxy(String path) throws Exception{
        StringBuilder result = new StringBuilder();
        File fileHandle = new File(path);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileHandle));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] proxy = line.split(":");
            Log.i(proxy[0] + "," + proxy[1]);
        }

        inputStreamReader.close();
        bufferedReader.close();
    }
}
