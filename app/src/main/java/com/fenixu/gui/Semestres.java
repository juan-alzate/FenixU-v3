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
import com.fenixu.logica_negocio.DialogoAgregarSemestre;
import com.fenixu.logica_negocio.DialogoCrearMateria;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Semestres extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView lista;

    List<List<String>> itemSemestre = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semestres);

        //Menu superior
        toolbar = (Toolbar)findViewById(R.id.toolbarNotas);
        toolbar.setTitle("PLAN ACADÉMICO");
        setSupportActionBar(toolbar);

        lista = (ListView)findViewById(R.id.listViewSemestres);

        //creamos la cantidad de sub-listas necesarios segun las variables.
        if(itemSemestre.size()==0){
            for(int i = 0; i < 2; i++) {
                itemSemestre.add(new ArrayList<String>());
            }}

        //Evitamos que el programa se voltee horizontalmente
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //Metodo para abrir la ventana donde se agrega la materia
    public void openDialog(){
        DialogoAgregarSemestre dcm =  new DialogoAgregarSemestre();
        dcm.show(getSupportFragmentManager(),"Agregar Semestre");
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

    //Boton del celular para regresar
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
