package com.jian.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
/**
 * 
 * @ClassName:  FileUtil   
 * @Description:TODO   
 * @author: JianLinWei
 * @email: jianlinwei_dream@163.com
 * @date:   2019年1月30日 下午2:44:07   
 *
 */
public class FileUtil {
	
	private static FileUtil  fileUtil;
	
	private FileUtil(){
		
	}
	
	public  static synchronized FileUtil getInstance(){
		if(fileUtil == null)
			return  new FileUtil();
		return fileUtil;
	}
	
	
	public  String getMidkir(String  filePath ){
		File  file  = new File(System.getProperty("catalina.home")+"/img/upload/" +filePath);
		if(!file.exists())
			file.mkdirs();
		return file.getPath();
	}

	public  String getMidkir2(String  filePath ){
		File  file  = new File(System.getProperty("catalina.home") +filePath);
		if(!file.exists())
			file.mkdirs();
		return file.getPath();
	}
	/**
	 * @throws IOException 
	 * 从路径下获取图片并转化为byte[]
	 * @Title: getByteByPath   
	 * @Description: TODO   
	 * @param: @param filePath
	 * @param: @return 
	 * @author: JianLinWei     
	 * @return: byte[]      
	 * @throws
	 */
	public   byte[]  getByteByPath(String filePath) throws IOException{
		File file = new File(System.getProperty("catalina.home")+filePath);
		InputStream  inputStream  = new FileInputStream(file);
		byte[]  bs = new byte[inputStream.available()];
		inputStream.read(bs);
		inputStream.close();
		
		return bs;
	}
	

	
	/**
	 * @throws IOException 
	 * 添加图片到目录
	 * images\\(存放现场注册人脸照片)
	 * records\\(存放记录照片)
	 * card\\(存放证件照片)
	 * @Title: addByteToPath   
	 * @Description: TODO   
	 * @param: @param pic
	 * @param: @param path
	 * @param: @return 
	 * @author: JianLinWei     
	 * @return: String      
	 * @throws
	 */
	public  String  addByteToPath(byte[]  pic , String  name , String path) throws IOException{
		name += "_"+UUID.randomUUID().toString()+".jpg";
		File  file  = new File(getMidkir(path)+"/"+name);
		if(!file.exists())
			file.createNewFile();
		OutputStream outputStream = new FileOutputStream(file);
		BufferedOutputStream bufferedOutputStream  = new BufferedOutputStream(outputStream);
		bufferedOutputStream.write(pic, 0, pic.length);
		/*FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(pic, 0, pic.length);
		fileOutputStream.flush();
		fileOutputStream.close();*/
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
		outputStream.flush();
		outputStream.close();
		return file.toURI().toString().substring(file.toURI().toString().indexOf("img")-1);
		
	}
	
	/**
	 * @throws IOException 
	 * 获取一个图片的路径
	 * @Title: getByteToPath   
	 * @Description: TODO   
	 * @param: @param pic
	 * @param: @param name
	 * @param: @param path
	 * @param: @return 
	 * @author: JianLinWei     
	 * @return: String      
	 * @throws
	 */
	public    String getByteToPath(byte[] pic ,String name , String path) throws IOException{
		String file_path = getMidkir(path)+"/"+name+".jpg";
		File  file  = new File(file_path);
		if(!file.exists())
			file.createNewFile();
		OutputStream outputStream = new FileOutputStream(file);
		BufferedOutputStream bufferedOutputStream  = new BufferedOutputStream(outputStream);
		bufferedOutputStream.write(pic);
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
		return file_path ;
	}
	
	/**
	 * @throws IOException 
	 * 
	 * @Title: modifyPic   
	 * @Description: TODO   
	 * @param: @param pic
	 * @param: @param name
	 * @param: @param path
	 * @param: @return 
	 * @author: JianLinWei     
	 * @return: String      
	 * @throws
	 */
	public   String   modifyPic(byte[]  pic , String  name , String path) throws IOException{
		File  file  = new File(getMidkir(path)+"/"+name+".jpg");
		if(!file.exists())
			file.createNewFile();
		OutputStream outputStream = new FileOutputStream(file);
		BufferedOutputStream bufferedOutputStream  = new BufferedOutputStream(outputStream);
		bufferedOutputStream.write(pic);
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
		return file.toURI().toString().substring(file.toURI().toString().indexOf("img")-1);
	}
	
	/**
	 * 删除文件
	 * @Description:
	 * @auther:JianLinwei
	 * @date:2019年10月29日上午11:01:22
	 * @param path
	 * @return
	 */
	public boolean delImgFile(String path){
		File  file  = new File(getMidkir2(path));
		return file.delete();
		
	}
	public   void  saveM3u8(byte[]  stream , String name) throws IOException{
		File  file  = new File(getMidkir("")+"/"+name+".m3u8");
		if(!file.exists())
			file.createNewFile();
		FileOutputStream fe=new FileOutputStream(file,true);
		fe.write(stream);
		fe.flush();
		fe.close();
	}
	
	
	
}
