package com.pic.action;

import java.util.ArrayList;
import java.util.List;

import com.pic.model.Picture;
import com.pic.service.PictureService;
import com.pic.util.Utils;

/**
 * <dt>文件下载入口</dt>
 * @author yyzhang
 * @since 2018年1月18日10:23:49
 */
public class DownloadAction implements Runnable{
	
	public void run() {
		List<List<String>> paramsList = new ArrayList<List<String>>();
		// 参数校验
		if(Utils.isEmpty(appNo))
			throw new IllegalArgumentException("【影像系统-下载】参数 appNo 不能为空！！！");
		
		// 参数转换
		this.convertParams(appNo, paramsList);
		this.convertParams(subclassSort, paramsList);
		
		// 依据picture表中的 pic_flag 判断接口走向：1 老接口，0 新接口
		List<Picture> pictureList = PictureService.getPictures(paramsList.get(0), paramsList.get(1));
		
		// 依据查询结果，判断数据获取来源
		if(pictureList != null && pictureList.size() > 0){
			PictureService.download(pictureList, filePath);
		}else {
			System.out.println("【影像系统-下载】无满足条件的数据, 条件app_no="+appNo+", subclass_sort="+subclassSort+"！！！");
		}
	}
	
	/**
	 * 参数转换
	 * @param param
	 * @param paramsList
	 */
	private void convertParams(String param, List<List<String>> paramsList) {
		List<String> valueList = new ArrayList<String>();
		if(!Utils.isEmpty(param)){
			String[] values = param.split(";");
			for (String value : values) {
				valueList.add(value);
			}
		}
		paramsList.add(valueList);
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public void setSubclassSort(String subclassSort) {
		this.subclassSort = subclassSort;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	private String appNo;			// 进件号
	private String subclassSort;	// 图片类型
	private String filePath;		// 文件保存路径
}
