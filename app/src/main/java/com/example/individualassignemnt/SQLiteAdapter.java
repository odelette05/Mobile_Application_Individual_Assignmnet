package com.example.individualassignemnt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import java.util.ArrayList;

public class SQLiteAdapter {

    //SQL Commands
    //Constant variable
    private static final String MYDATABASE_NAME="MY_DATABASE"; //database name

    private static final String DATABASE_TABLE = "MY_TABLE";

    private static final int MYDATABASE_VERSION = 1;  //version

    private static final String KEY_CONTENT = "Friend_Name";
    private static final String KEY_CONTENT_2 = "Title"; //column var name
    private static final String VALUE = "Amount"; //column var name

    //SQL command create the table with the column
    //private static final String SCRIPT_CREATE_DATABASE = "create table "+ DATABASE_TABLE +" ("+ KEY_CONTENT + " text not null);";
    private static final String SCRIPT_CREATE_DATABASE = "create table "+ DATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CONTENT + " text not null, " + KEY_CONTENT_2 + " text, " + VALUE + " double);";

    //variables
    private Context context;
    //declare a var which is SQLiteHelper datatype
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;


    //constructor
    public SQLiteAdapter(Context c) {

        context = c;

    }

    //open the database to insert data/ to write the data
    public SQLiteAdapter openToWrite() throws android.database.SQLException{

        //call sqLiteHelper to do this things
        //
        //the version
        sqLiteHelper = new SQLiteHelper(context,MYDATABASE_NAME,null,MYDATABASE_VERSION);

        //open the database to write
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        return this;
    }

    //open the database to read
    public SQLiteAdapter openToRead () throws android.database.SQLException{

        sqLiteHelper=new SQLiteHelper(context,MYDATABASE_NAME,null,MYDATABASE_VERSION);

        //open to read
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        return this;
    }

    public long insert(String content, String content_2, String content_3){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT,content);
        contentValues.put(KEY_CONTENT_2, String.valueOf(content_2));
        contentValues.put(VALUE,content_3);

        return sqLiteDatabase.insert(DATABASE_TABLE,null,contentValues);
    }

    public String queueAll(){

        String[] columns=new String[] {KEY_CONTENT,KEY_CONTENT_2,VALUE};
        //command and create the cursor to move to the table and which column u pointing at
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE,columns,null,null,null,null,null);
        String result="";
        //get the column index
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_3 = cursor.getColumnIndex(VALUE);


        //go into the data under that particular column
        for(cursor.moveToFirst(); !(cursor.isAfterLast());cursor.moveToNext()) {
            result = result + cursor.getString(index_CONTENT) + " had borrowed RM"
                    + cursor.getString(index_CONTENT_3) + " for "
                    + cursor.getString(index_CONTENT_2) + "\n";
        }

        return result;
    }

    //close the sqLiteHelper to close the database after we executing it
    public void close(){
        sqLiteHelper.close();
    }



    //delete all the content in the table "DATABASE_TABLE", so data will not repeated when adding another data
    public int deleteAll(){
        return sqLiteDatabase.delete(DATABASE_TABLE,null,null);
    }


    //need another special class
    //SQLiteOpenHelper helps to create database
    //superclass of SQLiteOpenHelper
    //override the methods which creates the database
    public static class SQLiteHelper extends SQLiteOpenHelper {

        //construct with 4 parameters
        public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //to create the database ->using sql
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

        //version control
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

    }

}
