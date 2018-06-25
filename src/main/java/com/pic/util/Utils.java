package com.pic.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 说明：内部工具类
 */
public class Utils {

	public static String getFormatTime()
	{
		Date date=new Date();
		  SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      String time=sm.format(date);
	      return time;
	}
	/**
	 * 获取properties文件存储的字段
	 */
	public static String getAddress(String filename,String key)
	{
		String value="";
		InputStream in=null;
		try
		{
		   in=new BufferedInputStream(new FileInputStream(filename));
		  Properties p=new Properties();
		  p.load(in);
		  value=p.getProperty(key);
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}finally
		{
			try{
				in.close();
			}catch (Exception e) {
				e.printStackTrace();// TODO: handle exception
			}
		}
		return value;
	}
	
	/**
	 * 计算新文件名
	 * @param fileName
	 * @return
	 */
	public static String changeFileName(String fileName)
	{
		DateFormat datefor=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=datefor.format(new Date());
		int random = new Random().nextInt(100);
        int position = fileName.lastIndexOf(".");
        String extension = fileName.substring(position);
        return date+random+extension;
	}
	
	/**
	 * 分页
	 * @param count
	 * @param index
	 * @return
	 */
	public static List<Integer> pageInfo(int count, String index) {
		
		List<Integer> result = new ArrayList<Integer>();
		int pagesize=20; // 每页三条数据
		int totalpage=(count%pagesize==0)?(count/pagesize):(count/pagesize+1); // 总页数
		String currentpagestr=index; // 获得当前页数
		if(currentpagestr==null) {
			currentpagestr="1";
		}
		int pageindex=Integer.parseInt(currentpagestr);
		if(pageindex<1){
			pageindex=1;
		}else if(pageindex>totalpage){
			pageindex=totalpage;
		}
		int endIndex = pagesize*pageindex <=count ? pagesize*pageindex : count;
		result.add(pageindex);
		result.add(pagesize);
		result.add(endIndex);
		result.add(totalpage);
		
		return result;
	}
	
	/**
	 * 空值校验
	 * @param str
	 * @return
	 * @since yyzhang 2017年8月23日19:14:43
	 */
	public static boolean isEmpty(String str){
		if(str == null || "".equals(str.trim()))
			return true;
		return false;
	}
	
	/**
	 * 参数校验
	 * @param name
	 * @param value
	 */
	public static void checkValue(String name, String value){
		if(isEmpty(value)) {
			throw new IllegalArgumentException("参数【"+name+"】不能为空！");
		}
	}
	
	/**
	 * 分解字符串
	 * @param value
	 * @param symbol
	 * @param threads
	 * @return
	 */
	public static String[] decomposeData(String value, String symbol, int threads) {
		String[] datas = null;
		String[] values = value.split(symbol);
		int decompose = values.length / threads;
		
		if ((decompose = decompose == 0 ? 1 : decompose) < threads) {
			datas = new String[values.length > 0 ? decompose + 1 : decompose];
			dealData(threads, symbol, values, datas);
		} else {
			datas = new String[values.length % threads == 0 ? threads : threads + 1];
			dealData(decompose, symbol, values, datas);
		}
		return datas;
	}
	
	private static void dealData(int decompose, String symbol, String[] values, String[] datas) {
		for (int i = 1; i <= datas.length; i++) {
			String data = "";
			for (int j = decompose * (i-1); j < decompose * i; j++) {
				if (j >= values.length)
					break;
				data += values[j] + symbol;
			}
			datas[i - 1] = data.substring(0, data.length() - 1);
		}
	}
}
