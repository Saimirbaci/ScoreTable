package com.saimirbaci.PesKatshTabel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by saimirbaci on 25/10/14.
 */
public class FileIO {

    public static int saveToFile(File filePath, String fileName, String stringToWrite){
        File file ;

        file = new File(filePath, fileName);
        FileOutputStream outStream;
        try{
            outStream = new FileOutputStream(file);
            outStream.write(stringToWrite.getBytes());
            outStream.close();
        }
        catch(Exception e){
            return Constants.CANT_WRITE_TO_FILE;
        }
        return Constants.FILE_STATUS_OK;
    }

    public static String[] openFile(File filePath, String fileName){
        ArrayList<String> lines = new ArrayList<String>();

        BufferedReader br ;
        try {
            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
        }
        catch (Exception e){
            System.out.print(e.getMessage());
            return null;
        }
        String line;
        try{
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }
        return lines.toArray(new String[lines.size()]);
    }
}
