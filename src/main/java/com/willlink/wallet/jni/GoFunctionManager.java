package com.willlink.wallet.jni;

import com.sun.jna.Native;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GoFunctionManager {

    private volatile static GoFunction goLib;

    private GoFunctionManager() {
    }

    public static GoFunction newInstance() {
        if (goLib == null) {
            synchronized (GoFunction.class) {
                if (goLib == null) {
                    String plugPath = loadPublicInterFile();
                    goLib = (GoFunction) Native.loadLibrary(plugPath, GoFunction.class);
                }
            }
        }
        return goLib;
    }



    private static String loadPublicInterFile(){
        String fileName = "publicInter.so";
        if(System.getProperty("os.name").indexOf("Windows") > -1) {
            fileName = "publicInter.dll";
        }
        File publicInterFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        if(!publicInterFile.exists() && !publicInterFile.isDirectory()){
            //create file
            try (InputStream is = GoFunctionManager.class.getClassLoader().getResourceAsStream(fileName)) {
                if (is == null) {
                    throw new IllegalArgumentException("resource with name=" + fileName + " not found!");
                }
                writeToLocalFile(publicInterFile.getPath(), is);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return publicInterFile.getPath();
    }


    private static void writeToLocalFile(String destination, InputStream input) throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, index);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        input.close();
    }

}
