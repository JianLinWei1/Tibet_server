package com.mj.mainservice.controller;

import com.jian.common.util.FileUtils;
import com.jian.common.util.ResultUtil;
import com.jian.common.util.SnowFlake;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Log4j2
@RestController
public class DownloadController {
       private   SnowFlake snowFlake = new SnowFlake(2 ,2 );

    @GetMapping("/download")
    public   void download(String filename , HttpServletResponse response){
        try {
            InputStream  inputStream = new FileInputStream(FileUtils.getInstance().createFile(filename));
            OutputStream  outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename="+filename);

            byte[] bytes = new byte[1024];
           // BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int i = inputStream.read(bytes);
            while (i != -1){
                outputStream.write(bytes,0,i);
                i =inputStream.read(bytes);
            }

            outputStream.flush();

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }


    }

    @PostMapping("/upload")
    public ResultUtil  upload(MultipartFile  file){
        try{
            String  filename = snowFlake.nextId() +"_"+ file.getOriginalFilename();
            byte[]  fileBin  = file.getBytes();
            File  files  = new File("upload"+File.separator+"img"+File.separator+filename);
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
        }catch (Exception e){
            log.error(e);
            return   new ResultUtil(-1 , e.getMessage());
        }

    }
}
