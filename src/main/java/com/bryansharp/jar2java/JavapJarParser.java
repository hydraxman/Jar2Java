package com.bryansharp.jar2java;

import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by bushaopeng on 18/1/8.
 */
public class JavapJarParser {
    public boolean parse(String path) {
        try {
            File file = new File(path);
            String name = file.getName().split("\\.")[0];
            String absolutePath = file.getParentFile().getAbsolutePath();
            final String destDirPath = absolutePath + "/parseOutput/" + name;
            File destDir = new File(destDirPath);
            if (destDir.exists()) {
                boolean delete = destDir.delete();
                System.out.println("删除文件：" + delete);
            }
            decompress(path, destDirPath);
            final Runtime runtime = Runtime.getRuntime();
            processSubFiles(destDir, new ProcessSubClassFileCallback() {
                @Override
                public void processFile(File subFile) {
                    String className = subFile.getName().split("\\.")[0];
                    File parentFile = subFile.getParentFile();
                    String command = "javap -v -p -l " + className;
                    System.out.println("正在处理: " + subFile.getAbsolutePath() + "，命令" + command);
                    try {
                        Process process = runtime.exec(command, null, parentFile);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line = null;
                        StringBuilder builder = new StringBuilder();
                        int lineNum = 1;
                        while ((line = bufferedReader.readLine()) != null) {
                            builder.append(line).append('\n');
                            filterProcessLine(subFile, line, lineNum);
                            lineNum++;
                        }
                        File javapParser = new File(parentFile, className + ".javap.txt");
                        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(javapParser));
                        outputStream.write(builder.toString().getBytes());
                        outputStream.close();

                        System.out.println("生成文件：" + javapParser.getAbsolutePath());

                        process.destroy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    boolean delete = subFile.delete();
                    System.out.println("删除:" + delete);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void filterProcessLine(File subFile, String line, int lineNum) {
        if (line == null) {
            return;
        }
        line = line.trim();
        if (line.startsWith("#")) {
            return;
        }
        if (line.toLowerCase().contains("android/view")) {
            return;
        }
        if (line.toLowerCase().contains("android/widget")) {
            return;
        }
        if (line.toLowerCase().contains("android/util")) {
            return;
        }
        if (line.toLowerCase().contains("android/graphics")) {
            return;
        }
        if (line.contains("android/text/TextUtils")) {
            return;
        }
        if (line.contains("Method android")&&line.contains("invoke")) {
            System.out.println("包含安卓系统调用：" + subFile.getName() + "文件第" + lineNum + "行: " + line);
        }
    }

    private void processSubFiles(File file, ProcessSubClassFileCallback callback) {
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

    public static void decompress(String srcPath, String dest) throws Exception {
        File file = new File(srcPath);
        if (!file.exists()) {
            throw new RuntimeException(srcPath + "所指文件不存在");
        }
        ZipFile zf = new ZipFile(file);
        Enumeration entries = zf.entries();
        ZipEntry entry = null;
        while (entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();
            System.out.println("解压" + entry.getName());
            if (entry.isDirectory()) {
                String dirPath = dest + File.separator + entry.getName();
                File dir = new File(dirPath);
                dir.mkdirs();
            } else {
                // 表示文件
                File f = new File(dest + File.separator + entry.getName());
                if (!f.exists()) {
                    String dirs = f.getParentFile().getAbsolutePath();
                    File parentDir = new File(dirs);
                    parentDir.mkdirs();
                }
                f.createNewFile();
                // 将压缩文件内容写入到这个文件中
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = zf.getInputStream(entry);
                    fos = new FileOutputStream(f);
                    int count;
                    byte[] buf = new byte[8192];
                    while ((count = is.read(buf)) != -1) {
                        fos.write(buf, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(fos);
                }

            }
        }

    }

    public interface ProcessSubFileCallback {
        boolean matchFile(File subFile);

        void processFile(File subFile);
    }

    public abstract class ProcessSubClassFileCallback implements ProcessSubFileCallback {
        @Override
        public boolean matchFile(File subFile) {
            return subFile.getName().endsWith(".class");
        }
    }
}
