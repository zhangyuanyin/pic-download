package com.pic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pic.factory.ConnectionFactory;
import com.pic.model.Picture;
import com.pic.util.SqlUtil;

/**
 * <dt>图片数据处理类</dt>
 * @author yyzhang
 * @since 2018年1月18日10:19:38
 */
public class PictureDao {
	
	/**
	 * 根据进件号，文件类型查询图片信息
	 * @param appNos
	 * @param subclassSorts
	 * @return
	 */
	public static List<Picture> getPictures(List<String> appNos, List<String> subclassSorts){
		List<Picture> pictures = new ArrayList<Picture>();
		Map<String, Integer> columnMap = new HashMap<String, Integer>(); 
		boolean flag = false;
		if(subclassSorts == null || subclassSorts.size() == 0) {
			flag = true;
		}
		
		columnMap.put("APP_NO", appNos.size());
		if(!flag){
			columnMap.put("SUBCLASS_SORT", subclassSorts.size());
		}
		String sqlCommon = SqlUtil.searchAllByLists("PICTURE", columnMap);
		Connection connection = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sqlCommon);
			SqlUtil.dealListValue(ps, appNos, 0);
			if(!flag){ 
				SqlUtil.dealListValue(ps, subclassSorts, appNos.size());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				assembPictureData(rs, pictures);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.close(rs, ps, connection);
		}
		return pictures;
	}

	/**
	 * 组装picture数据
	 * @param rs
	 * @param pictures
	 * @throws SQLException 
	 */
	private static void assembPictureData(ResultSet rs, List<Picture> pictures) throws SQLException {
		Picture picture = new Picture();
		picture.setId(rs.getLong("ID"));
		picture.setOrg(rs.getString("ORG"));
		picture.setOenner(rs.getString("OENNER"));
		picture.setIdTpye(rs.getString("ID_TPYE"));
		picture.setIdNo(rs.getString("ID_NO"));
		picture.setOutNo(rs.getString("OUT_NO"));
		picture.setBranchid(rs.getString("BRANCHID"));
		picture.setImgname(rs.getString("IMGNAME"));
		picture.setSavename(rs.getString("SAVENAME"));
		picture.setSortsid(rs.getLong("SORTSID"));
		picture.setGeneeicSort(rs.getString("GENEEIC_SORT"));
		picture.setMediumSort(rs.getString("MEDIUM_SORT"));
		picture.setSubclassSort(rs.getString("SUBCLASS_SORT"));
		picture.setUptime(rs.getDate("UPTIME"));
		picture.setAppNo(rs.getString("APP_NO"));
		picture.setRemark(rs.getString("REMARK"));
		picture.setSysName(rs.getString("SYS_NAME"));
		picture.setIfPatchBolt(rs.getString("IF_PATCH_BOLT"));
		picture.setIfWaste(rs.getString("IF_WASTE"));
		picture.setIsSmall(rs.getString("IS_SMALL"));
		picture.setPicFlag(rs.getString("PIC_FLAG"));
		picture.setkId(rs.getString("K_ID"));
		pictures.add(picture);
	}
}
