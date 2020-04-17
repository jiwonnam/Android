package com.example.test1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemberInfoHelper extends SQLiteOpenHelper {

    public MemberInfoHelper(Context context){
        super(context, "MemberInfo.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE member (" +
                "id varchar(50) not null primary key," +
                "pwd varchar(50) not null," +
                "name varchar(50) not null," +
                "age int not null" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE member");
        onCreate(db);
    }
}
