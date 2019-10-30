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
import android.widget.Toast;

import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorAgregarNotas;
import com.fenixu.logica_negocio.AdaptadorMateriasNotas;
import com.fenixu.logica_negocio.DialogoAgregarNota;
import com.fenixu.logica_negocio.MateriasNotas;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CrearNotasMaterias extends AppCompatActivity implements DialogoAgregarNota.DialogoCrearNotasListener {

    private Toolbar toolbar;
    private ListView lista;

    Bundle b;

    int maxId;
    int cont;
    int posicionfk;
    int limite;
    int limiteFk;

    //idNota, nota, porcentaje, idMateria, actual, necesaria
    List<List<String>> itemNotas = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_notas_materias);

        toolbar = (Toolbar)findViewById(R.id.toolbarAgregarNotas);
        setSupportActionBar(toolbar);

        lista = (ListView)findViewById(R.id.listViewAgregarNotas);

        //creamos la cantidad de sublistas necesarios segun las variables.
        if(itemNotas.size()==0){
            for(int i = 0; i < 6; i++) {
                itemNotas.add(new ArrayList<String>());
            }}

        Intent intent = getIntent();
        b = intent.getExtras();

        if(b!=null){
            posicionfk = b.getInt("posicion");
        }

        //Abrimos la base de datos para agregar todos los datos de la db a la lista multidimensional.
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor limiteFkDb = db.rawQuery("select count(idNota) from notas where idMateria!="+posicionfk, null);
        if(limiteFkDb.moveToFirst()){
            limiteFk = limiteFkDb.getInt(0);
        }

        Cursor maximo = db.rawQuery("select max(idNota) from notas", null);
        if(maximo.moveToFirst()){
            maxId= maximo.getInt(0);
        }

        Cursor limiteFor = db.rawQuery("select count(idNota) from notas", null);
        if(limiteFor.moveToFirst()){
            limite = limiteFor.getInt(0);
            cont=limite;
            for(int i = 0; i <= maxId; i++) {
                Cursor nota = db.rawQuery("select * from notas where idMateria="+posicionfk+" and idNota="+i , null);
                if(nota.moveToFirst()) {
                    itemNotas.get(0).add(nota.getString(0));
                    itemNotas.get(1).add(nota.getString(1));
                    itemNotas.get(2).add(nota.getString(2));
                    itemNotas.get(3).add(nota.getString(3));
                    itemNotas.get(4).add(nota.getString(4));
                    itemNotas.get(5).add(nota.getString(5));
                }
            }
        }
        db.close();

        //eliminamos la nota en la base de datos al darle al boton eliminar
        eliminarNota();

        //pintamos la lista en la activity solo si la listas es mayor a 0
        if(itemNotas.get(0).size()>0) {
            lista.setAdapter(new AdaptadorAgregarNotas(this, itemNotas));
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
        DialogoAgregarNota dcm =  new DialogoAgregarNota();
        dcm.show(getSupportFragmentManager(),"Agregar nota");
    }

    @Override
    public void applyTexts2(String nota, String porcentaje) {


        Log.d("CrearNotasMaterias","eso:"+((itemNotas.get(1).size()-1)));
        if(b!=null){
            itemNotas.get(0).add(" ");
            itemNotas.get(1).add(nota);
            itemNotas.get(2).add(porcentaje);
            itemNotas.get(3).add(String.valueOf(posicionfk));
            itemNotas.get(4).add(" ");
            itemNotas.get(5).add(" ");

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            int idNotaBD;
            Cursor max = db.rawQuery("select max(idNota) from notas", null);
            if(max.moveToFirst() && cont>0){
                idNotaBD = 1+max.getInt(0);
            }else{
                idNotaBD = cont;
            }
            Log.d("CrearNotasMaterias","cont:"+cont);
            Log.d("CrearNotasMaterias","limiteFk:"+limiteFk);
            String notaBD = itemNotas.get(1).get((itemNotas.get(1).size()-1));
            String porcentajeBD = itemNotas.get(2).get((itemNotas.get(2).size()-1));
            String fk = itemNotas.get(3).get((itemNotas.get(3).size()-1));

            //mandar a calcularNota() la ultima nota y el ultimo porcentaje
            MateriasNotas mn = new MateriasNotas();
            float notaActual = mn.calcularNota(posicionfk, this, itemNotas.get(1).get(itemNotas.get(1).size()-1),
                    itemNotas.get(2).get(itemNotas.get(2).size()-1));

            ContentValues registro = new ContentValues();

            registro.put("notaActual", notaActual);
            registro.put("idNota", idNotaBD);
            registro.put("nota", notaBD);
            registro.put("porcentaje", porcentajeBD);
            registro.put("idMateria", fk);

            db.insert("notas",null,registro);
            db.close();
        }
        lista.setAdapter(new AdaptadorAgregarNotas(this, itemNotas));
        cont++;
    }

    public void eliminarNota(){
        int posicionNota;
        Intent intent = getIntent();
        Bundle a = intent.getExtras();

        if(a!=null){
            posicionNota = a.getInt("posicionEliminarNota");
            if(posicionNota>0){
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "adminNotas", null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();
                db.execSQL("delete from notas where idNota=" + (posicionNota-1000));
                db.close();

                Intent notasMaterias = new Intent(this, CrearNotasMaterias.class);
                startActivity(notasMaterias);
            }
        }
    }

    //Boton para regresar del celular
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Notas.class);
        startActivity(intent);
        finish();
    }
}