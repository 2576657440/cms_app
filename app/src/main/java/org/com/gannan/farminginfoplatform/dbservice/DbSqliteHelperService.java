package org.com.gannan.farminginfoplatform.dbservice;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

public interface DbSqliteHelperService {
	
	public  boolean addInfo(String tableName, ContentValues values);
	
	public boolean delInfo(String tableName, String whereClause, String[] whereArgs);
	
	public boolean updateInfo(String tableName, ContentValues values, String whereClause, String[] whereArgs);
	
	public  Map<String, Object> mapInfo(String tableName, String selection, String[] selectionArgs);
	
	public List<Map<String, Object>> listInfo(String tableName, String selection, String[] selectionArgs, String orderBy, String limit);
	
}