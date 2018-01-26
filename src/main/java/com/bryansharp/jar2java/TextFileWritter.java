package com.bryansharp.jar2java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bushaopeng on 18/1/19.
 */
public class TextFileWritter {

    static final Map<String, TextFileWritter> writterMap = new HashMap<>();

    static {
    }

    private final String name;
    private final File file;
    private BufferedWriter fileWritter;

    public TextFileWritter(String name) {
        this.name = name;
        boolean needDate = false;
        if (needDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddHHmmss");
            this.file = new File(name + "." + dateFormat.format(new Date()) + ".txt");
        } else {
            this.file = new File(name + ".txt");
        }
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Utils.log("文件路径：" + file.getAbsolutePath());
        try {
            this.fileWritter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TextFileWritter getWritter(String name) {
        if (writterMap.get(name) == null) {
            writterMap.put(name, new TextFileWritter(name));
        }
        return writterMap.get(name);
    }

    public void println(Object msg) {
        Utils.log(msg);
        try {
            if (msg == null) {
                this.fileWritter.write("null");
            } else {
                this.fileWritter.write(msg.toString());
            }
            this.fileWritter.newLine();
            this.fileWritter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.fileWritter.flush();
            this.fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TextFileWritter getDefaultWritter() {
        return getWritter("default");
    }
}
