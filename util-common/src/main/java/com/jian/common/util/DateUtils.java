package com.jian.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: DateUtils
 * @Description:TODO
 * @author: JianLinWei
 * @email: jianlinwei_dream@163.com
 * @date: 2019年1月14日 上午10:58:32
 *
 */
public class DateUtils {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	

	public static String formatDate(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	
	
	public static String   formatDate(LocalDateTime  date){
		
		return DATE_TIME_FORMATTER.format(date);
		
	}
	
	public static  LocalDateTime  parse(String  date){
		
		return LocalDateTime.parse(date);
	}
	
	
    public  static  Date  LocalDateTime2Date(LocalDateTime localDateTime){
    	ZoneId offset = ZoneId.systemDefault();
    	return Date.from(localDateTime.atZone(offset).toInstant());
    }
    
    public static  Date  parse1(String  date){
    	if(StringUtils.isEmpty(date))
    		return null;
    	return  LocalDateTime2Date(LocalDateTime.parse(date, DATE_TIME_FORMATTER));
    }
    
    /**
     * localDate
     * @Description:
     * @auther:JianLinwei
     * @date:2019年7月30日下午12:32:35
     * @param date
     * @param format
     * @return
     */
    public static Date parse2(String date , String format){
    	ZoneId offset = ZoneId.systemDefault();
    	return Date.from( LocalDate.parse(date, DateTimeFormatter.ofPattern(format)).atStartOfDay(offset).toInstant());
    }
    
    public static Date parse3(Date  date , String format){
    	return parse2(formatDate(date, format), format);
    }
    
    public static synchronized Date getDate(){
    	Date date = new Date();
    	return date;
    }
 
    
}
