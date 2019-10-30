package com.fenixu.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.fenixu.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String msg = (getIntent().getStringExtra("valor")==null)?"":getIntent().getStringExtra("valor");
        if(!msg.isEmpty()) if(msg.equals("prueba")){finish();}

        //Evitamos que el programa se voltee horizontalmente
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void btnSiguienteCronograma(View view){
        Intent intent = new Intent(this, CronogramaActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnSiguienteNots(View view){
        Intent intent = new Intent(this, Notas.class);
        startActivity(intent);
        finish();
    }

    public void btnSiguienteSemestre(View view){
        Intent intent = new Intent(this, Semestres.class);
        startActivity(intent);
        finish();
    }
}
