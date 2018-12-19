package com.letters7.wuchen.springboot2.utils.id;


import com.letters7.wuchen.springboot2.utils.id.workid.WorkerID;
import com.letters7.wuchen.springboot2.utils.string.UtilString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * ID生成器 
 */
public class UtilId {

	/**
	 * 生成UUID
	 */
	public static synchronized String genUuid(){
		UUID uuid =UUID.randomUUID();   
        String uuidStr = uuid.toString();  
		uuidStr = UtilString.remove(uuidStr, '-');
        return uuidStr;
	}
	

	/**
	 * 生成25位长ID(时间戳+随机数)
	 */
	public static synchronized String genTimeseqId(){
		StringBuffer buf = new StringBuffer();  
		//取得时间戳 
        buf.append(getTimeStamp());            
        Random r = new Random(); 
        //增加一个随机数   
        for(int i=0;i<7;i++){
            buf.append(r.nextInt(10));             
        }  
        return buf.toString();
	}
	private static String getTimeStamp(){  
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        return sdf.format(new Date());  
    }
	

	/**
	 * 生成工作主键
	 */
	private static WorkerID worker = new WorkerID(2);
	public static Long genLongWorkerId(){
        return worker.nextId();
	}
	public static String genWorkerId(){
        return genLongWorkerId()+"";
	}
	

	public static void main(String[] args){
		System.out.println(UtilId.genWorkerId());
	}
	
	
}
