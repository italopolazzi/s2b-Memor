package com.dev.mendes.android_mytasks.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tainaviriato on 21/06/17.
 */

public class DataBaseControl {

    private SQLiteDatabase db;
    private DataBaseTask banco;

    public DataBaseControl(Context context) {
        banco = new DataBaseTask(context);
    }

    public String addTask(String taskName, String taskDate, String taskPlace, String taskCoordinates, String taskNote) {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(DataBaseTask.NOME, taskName);
        valores.put(DataBaseTask.DATE, taskDate);
        valores.put(DataBaseTask.PLACE, taskPlace);
        valores.put(DataBaseTask.COORDINATES, taskCoordinates);
        valores.put(DataBaseTask.NOTE, taskNote);

        resultado = db.insert(DataBaseTask.TABELA, null, valores);
        db.close();

        if (resultado == -1) {
            return "Erro ao adicionar tarefa";
        } else {
            return "Tarefa adicionada com sucesso";
        }

    }

    public String editTask(String taskId, String taskName, String taskDate, String taskPlace, String taskCoordinates, String taskNote) {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(DataBaseTask.ID, taskId);
        valores.put(DataBaseTask.NOME, taskName);
        valores.put(DataBaseTask.DATE, taskDate);
        valores.put(DataBaseTask.PLACE, taskPlace);
        valores.put(DataBaseTask.COORDINATES, taskCoordinates);
        valores.put(DataBaseTask.NOTE, taskNote);

        resultado = db.replace(DataBaseTask.TABELA, null, valores);
        db.close();

        if (resultado == -1) {
            return "Erro ao editar tarefa";
        } else {
            return "Tarefa editada com sucesso";
        }

    }

    public Cursor loadTasks() {
        Cursor cursor;
        db = banco.getReadableDatabase();
        cursor = db.query(DataBaseTask.TABELA, new String[]{}, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }


    public boolean setChecked(String id, boolean isChecked) {
        ContentValues cv = new ContentValues();
        cv.put(DataBaseTask.CHECK, isChecked ? 1 : 0);

        db = banco.getWritableDatabase();
        int rows = db.update(DataBaseTask.TABELA, cv, DataBaseTask.ID + " = " + id, null);

        return rows > 0;
    }

    public void deleteTask(int id) {
        String where = DataBaseTask.ID + " = " + id;
        db = banco.getReadableDatabase();
        db.delete(DataBaseTask.TABELA, where, null);
        db.close();
    }

}
