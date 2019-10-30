package com.fenixu.recursos_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(@Nullable Context context){
        super(context, "admin", null, 1);
    }

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table materias(idMateria int primary key, titulo varchar, porcentaje int, creditos int)");
        db.execSQL("create table notas(idNota int primary key, nota float(2), porcentaje float(2), idMateria int, notaActual float(2), notaNecesaria float(2))");
        db.execSQL("create table semestres(idSemestre int primary key, titulo varchar, promedio float(2), creditos int)");
        db.execSQL("create table alarma(idAlarma int primary key, tituloAlarma varchar, fechaAlarma varchar, horaAlarma varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
