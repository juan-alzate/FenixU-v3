package com.fenixu.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorMateriasNotas;
import com.fenixu.logica_negocio.DialogoCrearMateria;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Notas extends AppCompatActivity implements DialogoCrearMateria.DialogoCrearMateriasListener {

    private Toolbar toolbar;
    private ListView lista;

    int maxId;
    int cont;
    float notaAct;
    float notaNece;
    int acumulador;

    //nombre, porcentaje, nota actual, nota necesaria, creditos
    List<List<String>> itemMateria = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        //Menu superior
        toolbar = (Toolbar)findViewById(R.id.toolbarNotas);
        toolbar.setTitle("NOTAS");
        setSupportActionBar(toolbar);

        lista = (ListView)findViewById(R.id.listViewNotas);

        //creamos la cantidad de sub-listas necesarios segun las variables.
        if(itemMateria.size()==0){
        for(int i = 0; i < 5; i++) {
            itemMateria.add(new ArrayList<String>());
        }}

       //Abrimos la base de datos para agregar todos los datos de la db a la lista multidimensional.
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminMaterias", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor maxima = db.rawQuery("select max(idMateria) from materias", null);
        if(maxima.moveToFirst()){
            maxId= maxima.getInt(0);
        }
        Cursor limiteFor = db.rawQuery("select count(idMateria) from materias", null);
        if(limiteFor.moveToFirst()){
            cont = limiteFor.getInt(0);
            for(int i = 0; i <= maxId; i++){
                Cursor materia = db.rawQuery("select * from materias where idMateria="+i, null);
                if(materia.moveToFirst()){
                    itemMateria.get(0).add(materia.getString(1));
                    itemMateria.get(4).add(materia.getString(3));
                }
            }
        }
        db.close();

        AdminSQLiteOpenHelper admin2 = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
        SQLiteDatabase db2 = admin2.getWritableDatabase();

        for(int i = 0; i <=maxId; i++){
            Cursor mayor = db2.rawQuery("select max(idNota) from notas where idMateria="+i,null);
            if(mayor.moveToFirst()){
                int m = mayor.getInt(0);
                Cursor notaActual = db2.rawQuery("select notaActual from notas where idNota="+m +" and idMateria ="+i,null);
                float n = 0;
                if(notaActual.moveToFirst()){
                    n =notaActual.getFloat(0)/100;
                }
                itemMateria.get(2).add(String.valueOf((double) Math.round(n * 100d) / 100d));
                acumulador =0;
                notaAct = 0;
                notaNece = 0;
                int p=0;
                for(int j=0; j<=m; j++){
                    Cursor porcentaje = db2.rawQuery("select porcentaje from notas where idNota=" + j + " and idMateria =" + i, null);
                    if(porcentaje.moveToFirst()){
                        p = porcentaje.getInt(0);
                        acumulador += p;
                    }
                    if(acumulador>0 && acumulador<100){
                        Cursor notas = db2.rawQuery("select nota from notas where idNota=" + j + " and idMateria=" + i, null);
                        if(notas.moveToFirst()){
                            float nta = notas.getFloat(0);
                            float p100= Float.parseFloat("100");
                            notaAct += (nta * p)/p100;
                            notaNece = (3 - notaAct) / (1 -(acumulador)/p100);
                        }
                    }
                }
                itemMateria.get(1).add(acumulador+"%");

                String nn = " ";
                if(acumulador<100 && notaNece>0) {
                    nn = String.valueOf((double) Math.round(notaNece * 100d) / 100d);
                }
                itemMateria.get(3).add(nn);
            }
        }
        db2.close();

        //eliminamos la materia en la base de datos al darle al boton eliminar
        eliminarMateria();

       //pintamos la lista en la activity solo si la listas es mayor a 0
        if(itemMateria.get(0).size()>0) {
            lista.setAdapter(new AdaptadorMateriasNotas(this, itemMateria));
        }

        //Evitamos que el programa se voltee horizontalmente
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //Items del menu superior(agregar, eliminar)
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //Metodo que se ejecuta al dar click a los items del menu superior
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.agregar){
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    //Metodo para abrir la ventana donde se agrega la materia
    public void openDialog(){
        DialogoCrearMateria dcm =  new DialogoCrearMateria();
        dcm.show(getSupportFragmentManager(),"Agregar Materia");
    }

    //metodo en el cual insertamos los datos de materia y creditos tanto a la lista como a la db.
    @Override
    public void applyTexts(String materia, String creditos){
        itemMateria.get(0).add(materia);
        itemMateria.get(1).add("0");
        itemMateria.get(2).add("0");
        itemMateria.get(3).add("0");
        itemMateria.get(4).add(creditos);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminMaterias", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor max = db.rawQuery("select max(idMateria) from materias", null);
        if(max.moveToFirst() && cont>0){
            cont = 1+max.getInt(0);
        }

        String tituloBD = itemMateria.get(0).get(itemMateria.get(0).size()-1);
        String creditosBD = itemMateria.get(4).get(itemMateria.get(0).size()-1);
        int idMateria = cont;

        ContentValues registro = new ContentValues();

        registro.put("titulo", tituloBD);
        registro.put("creditos", creditosBD);
        registro.put("idMateria", idMateria);

        db.insert("materias",null,registro);
        db.close();

        lista.setAdapter(new AdaptadorMateriasNotas(this, itemMateria));
        cont++;
    }

    public void eliminarMateria(){
        int posicion;
        Intent intent = getIntent();
        Bundle a = intent.getExtras();

        if(a!=null){
            posicion = a.getInt("posicionEliminar");

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminMaterias", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            db.execSQL("delete from materias where idMateria="+(posicion-1000));
            db.close();

            AdminSQLiteOpenHelper admin2 = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
            SQLiteDatabase db2 = admin2.getWritableDatabase();
            db2.execSQL("delete from notas where idMateria="+(posicion-1000));
            db2.close();

           Intent notas= new Intent(this, Notas.class);
           startActivity(notas);
        }
    }

    //Boton del celular para regresar
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
