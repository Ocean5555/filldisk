package com.ocean.utils.filedisk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FillDiskApplication {

    public static void main(String[] args){
        if (args.length == 0) {
            fillDisk("D");
        }else {
            System.out.println("opts:"+ Arrays.toString(args));
            fillDisk(args[0]);
        }
    }

    private static Integer count = 1;

    public static void fillDisk(String partition) {
        File rootDir;
        if (isWindows()) {
            rootDir = new File(partition + ":\\");
        } else {
            rootDir = new File("/");
        }
        System.out.println("fill disk : " + rootDir.getAbsolutePath());
        File tempDir = new File(rootDir.getAbsoluteFile() + "fillDiskDir");
        tempDir.mkdir();
        System.out.println("fill dir : " + tempDir.getAbsolutePath());
        System.out.println("start fill disk");
        try {
            byte[] fillData = new byte[1024 * 1024];
            while (true) {
                writeFillFile(tempDir.getAbsolutePath(), fillData);
            }
        } catch (IOException e1) {
            System.out.println("finished fill disk, start delete temp file...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File[] files = tempDir.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    file.delete();
                }
                tempDir.delete();
            }
            System.out.println("done, program will exit.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeFillFile(String fillDir, byte[] fillData) throws Exception {
        File file = new File(fillDir + File.separator + System.currentTimeMillis());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        try {
            while (file.length() < 1024 * 1024 * 1024) {
                try {
                    fos.write(fillData);
                } catch (IOException e) {
                    while (true) {
                        byte[] fillDataMini = new byte[10];
                        fos.write(fillDataMini);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            fos.flush();
            fos.close();
            System.out.println("write file finished:" + count++);
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

}
