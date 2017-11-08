package com.dev.mendes.android_mytasks.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tainaviriato on 21/06/17.
 */

public class DataBaseTask extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "banco.db";
    public static final String TABELA = "task";
    public static final String ID = "_id";
    public static final String NOME = "taskName";
    public static final String DATE = "taskDate";
    public static final String PLACE = "taskPlace";
    public static final String COORDINATES = "taskCoordinates";
    public static final String NOTE = "taskNote";
    public static final String CHECK = "chekBox";
    public static final int VERSAO = 4;


    public DataBaseTask(Context context) {
        super(context, NOME_BANCO, null, VERSAO);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + " ("
                + ID + " integer primary key autoincrement, "
                + NOME + " text, "
                + DATE + " text, "
                + PLACE + " text, "
                + COORDINATES + " text, "
                + CHECK + " INTEGER DEFAULT 0, "
                + NOTE + " text "
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}
