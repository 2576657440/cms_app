package org.com.gannan.farminginfoplatform.dbservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbSqliteHelperDao implements DbSqliteHelperService {
	
	private UseSqlite sqlite=null;
	public DbSqliteHelperDao(Context context) {
		sqlite=new UseSqlite(context);
	}

	@Override
	public boolean addInfo(String tableName,ContentValues values) {
		// TODO Auto-generated method stub
		boolean flag=false;
		long backFlag=0;
		SQLiteDatabase database=null;
		try {
			database=sqlite.getWritableDatabase();
			backFlag=database.insert(tableName, null, values);
			
			flag=backFlag==-1?false:true;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(database!=null)
				database.close();
		}
		return flag;
	}

	@Override
	public boolean delInfo(String tableName,
			String whereClause,String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean flag=false;
		int backFlag=0;
		SQLiteDatabase database=null;
		try {
			database=sqlite.getWritableDatabase();
			backFlag=database.delete(tableName, whereClause, whereArgs);
			flag=backFlag==0?false:true;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(database!=null)
				database.close();
		}
		return flag;
	}

	@Override
	public boolean updateInfo(String tableName,ContentValues values,
			String whereClause,String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean flag=false;
		int backFlag=0;
		SQLiteDatabase database=null;
		try {
			database=sqlite.getWritableDatabase();
			backFlag=database.update(tableName, values, whereClause, whereArgs);
			
			flag=backFlag==0?false:true;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(database!=null)
				database.close();
		}
		return flag;
	}

	@Override
	public Map<String, Object> mapInfo(String tableName, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Map<String, Object> map=new HashMap<String, Object>();
		SQLiteDatabase database=null;
		try {
			database=sqlite.getReadableDatabase();
			Cursor cursor=database.query(tableName, null, selection,
					selectionArgs, null, null, null, null);
			int length=cursor.getColumnCount();
			while(cursor.moveToNext()){
				for(int i=0; i<length;i++){
					String colum_name= cursor.getColumnName(i);
					Object colum_value= cursor.getString(cursor.getColumnIndex(colum_name));
					map.put(colum_name, colum_value);
				}
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(database!=null)
				database.close();
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> listInfo(String tableName, String selection,
			String[] selectionArgs,String orderBy, String limit) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		SQLiteDatabase database=null;
		try {
			database=sqlite.getReadableDatabase();
			Cursor cursor=database.query(tableName, null, selection,
					selectionArgs, null, null, orderBy, limit);
			int length=cursor.getColumnCount();
			while(cursor.moveToNext()){
				Map<String, Object> map=new HashMap<String, Object>();
				for(int i=0; i<length;i++){
					String colum_name= cursor.getColumnName(i);
					Object colum_value= cursor.getString(cursor.getColumnIndex(colum_name));
					map.put(colum_name, colum_value);
				}
				list.add(map);
			}
			cursor.close();
			database.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		return list;
	}
	
	
}
