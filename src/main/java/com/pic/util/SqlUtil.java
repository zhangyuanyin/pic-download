package com.pic.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SQL工具处理类
 * @author yyzhang
 * @since 2018年1月18日11:04:18
 */
public class SqlUtil {
	private static String sqlCommon = "";
	
	/**
	 * 通过主键生成SQL,查询表内满足条件的所有字段值
	 * @param table
	 * @param primaryKey
	 * @return
	 */
	public static String searchAllByPrimaryKey(String table, String primaryKey) {
		Utils.checkValue("table", table);
		Utils.checkValue("primaryKey", primaryKey);
		sqlCommon = "SELECT * FROM " + table +" WHERE " + primaryKey +" = ?";
		return sqlCommon.toUpperCase();
	}
	
	/**
	 * 通过list字段集合组装SQL
	 * @param table
	 * @param columnMap
	 * @param size 
	 * @return
	 */
	public synchronized static String searchAllByLists(String table, Map<String, Integer> columnMap){
		int count = 1;
		Utils.checkValue("table", table);
		if(columnMap == null || columnMap.size() == 0) {
			throw new IllegalArgumentException("参数 columns 不合法：" + columnMap);
		}
		
		sqlCommon = "SELECT * FROM " + table +" WHERE ";
		for (String key : columnMap.keySet()) {
			int size = columnMap.get(key);
			sqlCommon += key + " IN ( ";
			for(int i = 1; i <= size; i++) {
				if(i == size) {
					if(count == columnMap.size()) {
						sqlCommon += "? ) ";
					} else {
						sqlCommon += "? ) AND ";
					}
				} else {
					sqlCommon += "?, ";
				}
			}
			count++;
		}
		System.out.println("执行 SQL：" + sqlCommon);
		return sqlCommon.toUpperCase();
	}
	
	/**
	 * 处理查询字段的值
	 * @param value
	 * @return
	 */
	public static String dealListValue(List<String> values){
		String value = "";
		if(values == null || values.size() == 0) {
			throw new IllegalArgumentException("参数 value 不合法：" + values);
		}
		for (int i = 0; i < values.size(); i++) {
			if(i == values.size() - 1)
				value += "'" + values.get(i) + "'";
			else 
				value += "'" + values.get(i) + "', ";
		}
		value = value.substring(1);
		
		return value.substring(0, value.length()-1);
	}

	/**
	 * 初始化值
	 * @param ps
	 * @param appNos
	 * @param position 
	 */
	public static void dealListValue(PreparedStatement ps, List<String> columns, int position) {
		for (int i = 0; i < columns.size(); i ++) {
			try {
				ps.setString(i+position+1, columns.get(i));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
}
