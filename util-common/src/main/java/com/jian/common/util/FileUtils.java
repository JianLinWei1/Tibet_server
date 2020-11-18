package com.jian.common.util;

import java.io.*;

/**
 * Jian
 */
public class FileUtils {

    private   SnowFlake snowFlake = new SnowFlake(3,2 );
    private static FileUtils fileUtils;
    private  FileUtils(){

    }

    public  static  synchronized  FileUtils getInstance(){
        if (fileUtils != null)
            return  fileUtils;
        return new FileUtils();
    }


    /**
     * 创建文件
     * @param name
     * @return
     */
    public File  createFile(String name){
        return  new File("upload//"+name);
    }



    public ResultUtil saveCarImg(byte[] fileBin) throws IOException {
        File  files  = new File("upload"+File.separator+"img"+File.separator +"car"+File.separator+ snowFlake.nextId()+".jpg");
        if(!files.getParentFile().exists())
            files.getParentFile().mkdirs();
        if(!files.exists())
            files.createNewFile();
        OutputStream outputStream = new FileOutputStream(files);
        BufferedOutputStream bufferedOutputStream  = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(fileBin, 0, fileBin.length);

        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        outputStream.flush();
        outputStream.close();


        return new ResultUtil(0 , files.getPath() ,"");
    }
}
