package com.jiavan.weibo.crawler.util;

import com.jiavan.weibo.crawler.jdbc.ConnectionFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by Jiavan on 2017/4/3.
 * File operation utils
 */
public class FileHandle {
    /**
     * Write file
     * @param fileName file name and path
     * @param content file content
     */
    public static void write(String fileName, String content) throws IOException {
        File fileHandle = new File(fileName);
        fileHandle.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        bufferedWriter.write(content);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    /**
     * Read file content by path
     * @param path file path
     * @return file content
     */
    public static String read(String path) throws IOException {
        StringBuilder result = new StringBuilder();
        File fileHandle = new File(path);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileHandle));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        inputStreamReader.close();
        bufferedReader.close();
        return result.toString();
    }

    /**
     * Load properties file
     * @param propName properties filename
     * @throws Exception IOException
     */
    public static Properties loadProp(String propName) throws IOException {
        Properties prop = new Properties();
        InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("config.properties");
        prop.load(in);
        in.close();

        return prop;
    }
}
