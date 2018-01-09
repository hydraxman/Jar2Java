package com.bryansharp.jar2java;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by bushaopeng on 18/1/8.
 */
public class AidlProcessor {
    public boolean process(String path) {
        try {
            File file = new File(path);
            final Runtime runtime = Runtime.getRuntime();
            final File parentFile = new File("/Users/bushaopeng/Desktop/androidSDK/build-tools/20.0.0");
            processSubFiles(file, new ProcessSubAIDLFileCallback() {
                @Override
                public void processFile(File subFile) {

                    String command = "./aidl -I/Users/bushaopeng/Desktop/myGit/JDroid/src/main/java "
                            + subFile.getAbsolutePath();
                    System.out.println("正在处理: " + subFile.getAbsolutePath() + "，命令" + command);
                    try {
                        Process process = runtime.exec(command, null, parentFile);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line = null;
                        StringBuilder builder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            builder.append(line).append('\n');
                        }
                        System.out.println(builder.toString());
                        process.destroy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private void processSubFiles(File file, ProcessSubAIDLFileCallback callback) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    processSubFiles(f, callback);
                }
            }
        } else {
            if (callback.matchFile(file)) {
                callback.processFile(file);
            }
        }
    }

    public interface ProcessSubFileCallback {
        boolean matchFile(File subFile);

        void processFile(File subFile);
    }

    public abstract class ProcessSubAIDLFileCallback implements ProcessSubFileCallback {
        @Override
        public boolean matchFile(File subFile) {
            return subFile.getName().endsWith(".aidl");
        }
    }
}
