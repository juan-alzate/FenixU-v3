package com.fenixu.logica_negocio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmasBase {


    private SQLiteDatabase alarmasDataBase;
    private AlarmasBase(Context context){ alarmasDataBase = new AdminSQLiteOpenHelper(context).getWritableDatabase(); }

    private static AlarmasBase alarmasBase;
    public static AlarmasBase get(Context context){
        if(alarmasBase == null)
            alarmasBase = new AlarmasBase(context);
        return alarmasBase;
    }

    //Retorna todas las alarmas de la base de datos
    public List<Alarma> getAlarmas(){

        Cursor cursor = alarmasDataBase.query("alarma", null,
                null, null, null, null,
                null);

        List<Alarma> alarmas = new ArrayList<>();
        try{
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    int idAlarma = Integer.parseInt(cursor.getString(cursor.getColumnIndex("idAlarma")));

                    String tituloAlarma = cursor.getString(cursor.getColumnIndex("tituloAlarma"));
                    String fechaAlarma = cursor.getString(cursor.getColumnIndex("fechaAlarma"));
                    String horaAlarma = cursor.getString(cursor.getColumnIndex("horaAlarma"));

                    Alarma alarma = new Alarma();
                    alarma.setId(idAlarma);
                    alarma.setTitulo(tituloAlarma);
                    alarma.setFecha(fechaAlarma);
                    alarma.setHora(horaAlarma);
                    alarmas.add(alarma);

                    cursor.moveToNext();
                }
            }
        }finally { cursor.close(); }
        return alarmas;
    }

    //Envia los datos a actualizar en alarma con @param id
    public void actualizarAlarma(Alarma alarma){
        alarmasDataBase.update("alarma", getContentValues(alarma),
                "idAlarma = "+(alarma.getId()) ,null);
    }

    //Envia materia a la base de datos
    public void guardarAlarma(Alarma alarma){
        int idNuevo = getNewId();
        alarma.setId(idNuevo);
        alarmasDataBase.insert("alarma", null,
                getContentValues(alarma));
    }

    public void eliminarAlarma(Alarma alarma){
        alarmasDataBase.delete("alarma", "idAlarma = ?", new String[]{Integer.toString(alarma.getId())});
    }

    //Retorna un objeto ContentValues con los valores de un objeto de la clase alarma
    private ContentValues getContentValues(Alarma alarma){
        ContentValues contentValues = new ContentValues();
        contentValues.put("idAlarma", alarma.getId());
        contentValues.put("tituloAlarma", alarma.getTitulo());
        contentValues.put("fechaAlarma", alarma.getFecha());
        contentValues.put("horaAlarma", alarma.getHora());
        return contentValues;
    }

    //Crea un nuevo id compatible en base al maximo actual en la base de datos
    private int getNewId(){
        Cursor cursor = alarmasDataBase.rawQuery("SELECT * FROM alarma WHERE idAlarma = " +
                "(SELECT MAX(idAlarma) FROM alarma)",  null);

        try{ return (cursor.moveToFirst())?  Integer.parseInt(cursor.getString(cursor.getColumnIndex("idAlarma")))+1:0;
        }finally { cursor.close(); }
    }

}


