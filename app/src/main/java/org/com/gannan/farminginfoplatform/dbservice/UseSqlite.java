package org.com.gannan.farminginfoplatform.dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UseSqlite extends SQLiteOpenHelper {

    private static String name = "mydbase.db";//表示数据的名称
    private static int version = 1;//数据库的版本号
    public static String SEARCH_RECORD_TABLE_NAME = "search_record";
    private String msgInfoSql;

    public UseSqlite(Context context) {
        super(context, name, null, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        //列表，搜索关键字记录   /* 创建一个表，支持的数据类型
        msgInfoSql = "create table " + SEARCH_RECORD_TABLE_NAME +
                " (id integer primary key,the_key varchar(100),create_time varchar(30))";
        db.execSQL(msgInfoSql);//帮助完成数据库的创建

    }

    //version 修改表结构要更改版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

}
