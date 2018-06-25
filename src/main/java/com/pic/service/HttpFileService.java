package com.pic.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.pic.model.Picture;

public class HttpFileService {
	private static final String FILE_NAME = "file_name"; // 文件名称
	private static final String URL = "http://10.143.92.200/fastdfs-gateway/";
	private static final String SYSNAME = "ffce";
	private static final String CURSYSNO = "0601";

	public static void download(Picture picture, String filePath) {

    	List<String> fileNameList = new ArrayList<String>();
    	InputStream in = null;
    	FileOutputStream out = null;
    	StringBuffer url = null;
    	if("0".equals(picture.getPicFlag())) {
    		url = new StringBuffer(URL);
    		url.append(SYSNAME).append("/download")				// 请求文件下载服务的系统名称
    		.append("?").append("curSysNo=").append(CURSYSNO)	// 请求文件下载服务的系统编码
    		.append("&").append("key=").append(picture.getkId())		// 上传文件时返回的文件唯一标识key
    		.append("&").append("type=").append(1);					// 操作类型 1:下载, 0:查看
    		System.out.println("【影像系统-下载】新文件服务接口地址：" + url);
    	} else {
    		url = new StringBuffer();
			url = url.append(getUrl(picture.getSysName(), picture.getAppNo(), picture.getSubclassSort(), picture.getSavename()));
			System.out.println(url);
		}
 
        
        try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url.toString());
			HttpResponse response = httpClient.execute(get);
			
			// 获取并验证执行结果
			int code = response.getStatusLine().getStatusCode();
			if(code == HttpStatus.SC_OK){
				String fileName = "";
				in = response.getEntity().getContent();
				Header[] headers = response.getAllHeaders();
				for (Header header : headers) {
					if(FILE_NAME.equals(header.getName())){
						fileName = URLDecoder.decode(header.getValue(), "UTF-8");
						break;
					}
				}
				fileNameList.add(fileName);
				System.out.println("获取参数信息："+fileName+" -- "+in);
				
				// 判断文件路径是否存在，不存在创建对应路径
				filePath += picture.getAppNo() + "/";
				File file = new File(filePath);
				if(!file.isDirectory()){
					file.mkdirs();
				}
				
				// 判断文件是否存在，不存在创建新文件
				if("".equals(fileName)) 
					fileName = picture.getSavename();
				file = new File(filePath + fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				// 将输入流写入到新文件中
				out = new FileOutputStream(file);
				int i = 0;
				while(!((i = in.read()) == -1)){
					out.write(i);
				}
				out.flush();
			}else {
				System.out.println("【影像系统-下载】失败！！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null)
					out.close();
				if(in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getUrl(String sysNo, String appNo, String type, String filename) {
		return "http://10.143.92.200/pic-app/file/"+sysNo+"/"+appNo+"/"+type+"/"+filename;
	}
}
