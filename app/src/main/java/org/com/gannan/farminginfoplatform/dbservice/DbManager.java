package org.com.gannan.farminginfoplatform.dbservice;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;
import java.util.Map;

public class DbManager  {
	private Context context;
	public DbManager(Context context){
		this.context=context;
	}

	public boolean dbinsert(ContentValues values,String tableName){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		boolean flag=dao.addInfo(tableName, values);
		return flag;
	}
	public boolean dbDel(String[] whereArgs,String tableName){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		return dao.delInfo(tableName, "id=?", whereArgs);
	}
	public boolean dbDelObj(String tableName,String whereClause,String[] whereArgs){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		return dao.delInfo(tableName, whereClause, whereArgs);
	}
	public boolean dbClearTable(String tableName){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		return dao.delInfo(tableName, null,null);
	}
	public void dbUpdate(String tableName,ContentValues values,String whereClause,String[] whereArgs){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		dao.updateInfo(tableName,values, whereClause, whereArgs);
	}
	public Map<String, Object>  dbmapInfo(String tableName,String[] selectionArgs){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		return dao.mapInfo(tableName, "id=?", selectionArgs);
	}
	public List<Map<String, Object>> dblistInfo(String tableName, String selection,
			String[] selectionArgs,String orderBy, String limit){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		return dao.listInfo(tableName,selection,selectionArgs,orderBy, limit);
	}
	public List<Map<String, Object>> dbSelectionListInfo(String tableName,String selection,String[] selectionArgs,String orderBy, String limit){
		DbSqliteHelperDao dao=new DbSqliteHelperDao(context);
		return dao.listInfo(tableName, selection, selectionArgs, orderBy, limit);
	}
}
