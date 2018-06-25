package com.pic.service;

import java.util.List;

import com.pic.dao.PictureDao;
import com.pic.model.Picture;
import com.pic.util.Utils;

/**
 * <dt>图片逻辑服务类</dt> 
 * @author yyzhang
 * @since 2018年1月18日10:18:45
 */
public class PictureService {
	
	/**
	 * 获取满足条件的图片信息
	 * @param appNos
	 * @param subclassSorts
	 * @return
	 */
	public static List<Picture> getPictures(List<String> appNos, List<String> subclassSorts){
		return PictureDao.getPictures(appNos, subclassSorts);
	}
	
	/**
	 * 下载文件入口
	 * @param dataList
	 * @param filePath
	 */
	public static void download(List<Picture> dataList, String filePath){
		for (Picture picture : dataList) {
			// 文件类型 与 文件服务器唯一标识符不为空时，调用下载服务接口
			if(!Utils.isEmpty(picture.getSubclassSort())){
				HttpFileService.download(picture, filePath);
			}
		}
	}
}
