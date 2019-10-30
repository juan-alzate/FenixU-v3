package com.fenixu.gui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fenixu.R;
import com.fenixu.logica_negocio.AdaptadorAlarmas;
import com.fenixu.logica_negocio.AlarmReceiver;
import com.fenixu.logica_negocio.Alarma;
import com.fenixu.logica_negocio.AlarmasBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class CronogramaActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;

    private List<Alarma> alarmas;

    private AdaptadorAlarmas adaptadorAlarmas;

    private String extraout = ""; //Auxiliar para identificar Activity que se debe cerrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cierra Activity auxiliar
        String msg = (getIntent().getStringExtra("valor") == null)? "": getIntent().getStringExtra("valor");
        if(!msg.isEmpty()) {extraout = msg; finish();}

        //Busca vista de la actividad
        setContentView(R.layout.activity_cronograma);

        //Pone valores al listview
        listView = findViewById(R.id.lvAlarmas);
        if(alarmas != null) if(alarmas.size()>0) alarmas.clear();
        alarmas = AlarmasBase.get(getApplicationContext()).getAlarmas();
        adaptadorAlarmas = new AdaptadorAlarmas(getApplicationContext(), alarmas);
        listView.setAdapter(adaptadorAlarmas);

        //Incializa FloatingActionButtom
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //Fija orientaciond de pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Envia alarmas al sistema
        if(alarmas.size()>0) {
            for (Alarma alarmain : alarmas){

                String fechaActual = Calendar.getInstance().getTime().getDate()+"/"+
                        Calendar.getInstance().getTime().getMonth()+"/"+(1900+
                        Calendar.getInstance().getTime().getYear())+"/"+
                        Calendar.getInstance().getTime().getHours()+"/"+
                        Calendar.getInstance().getTime().getMinutes();

                String[] h = alarmain.getHora().split(":");
                String fechaAlarma = alarmain.getFecha()+"/"+h[0]+"/"+h[1];


                //Evita poner alarmas inmediatas
                if(!fechaAnteriorAB(fechaAlarma, fechaActual)) {

                    //Intent
                    Intent intentar = new Intent(CronogramaActivity.this, AlarmReceiver.class);
                    intentar.putExtra("idAlarma", alarmain.getId());
                    intentar.putExtra("tituloAlarma", alarmain.getTitulo());
                    intentar.setAction(Long.toString(System.currentTimeMillis()));

                    //Pending intent
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(CronogramaActivity.this, 0, intentar, PendingIntent.FLAG_ONE_SHOT);

                    //AlarmManager
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(this.ALARM_SERVICE);

                    //Calendar
                    Calendar calendar = Calendar.getInstance();
                    String[] t1 = alarmain.getHora().split(":");
                    String[] t2 = alarmain.getFecha().split("/");

                    calendar.set(Calendar.MONTH, Integer.parseInt(t2[1]));
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(t2[0]));
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t1[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(t1[1]));
                    calendar.set(Calendar.SECOND, 0);

                    //Set Pending Intent to alarmManager at Calendar time
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CronogramaActivity.this);

        //Var auxiliar para usar en onclick
        final View viewin = view;

        final View mView = getLayoutInflater().inflate(R.layout.dialogo_agregar_alarma, null);
        final EditText mTituloAlarma = mView.findViewById(R.id.etTituloAlarma);
        Button mCrear = mView.findViewById(R.id.btnCrearAlarma);
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();

        mCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTituloAlarma.getText().toString().isEmpty()){

                    Alarma alarma = new Alarma();
                    alarma.setTitulo(mTituloAlarma.getText().toString());

                    AlarmasBase.get(viewin.getContext()).guardarAlarma(alarma);

                    dialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), CronogramaActivity.class);
                    startActivity(intent);
                    terminar();
                } else {
                    Toast.makeText(CronogramaActivity.this,
                            "Pon un titulo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        if(extraout.equals("")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void terminar(){
        finish();
    }

    //true fechaA es anterior a fechaB, sino false
    private boolean fechaAnteriorAB(String fechaA, String fechaB){
        String[] a = fechaA.split("/");
        String[] b = fechaB.split("/");
        //año
        if(Integer.parseInt(a[2]) < Integer.parseInt(b[2])) return true;
        if(Integer.parseInt(a[2]) > Integer.parseInt(b[2])) return false;
        if(Integer.parseInt(a[2]) == Integer.parseInt(b[2])){
            //mes
            if(Integer.parseInt(a[1]) < Integer.parseInt(b[1])) return true;
            if(Integer.parseInt(a[1]) > Integer.parseInt(b[1])) return false;
            if(Integer.parseInt(a[1]) == Integer.parseInt(b[1])){
                //dia
                if(Integer.parseInt(a[0]) < Integer.parseInt(b[0])) return true;
                if(Integer.parseInt(a[0]) > Integer.parseInt(b[0])) return false;
                if(Integer.parseInt(a[0]) == Integer.parseInt(b[0])){
                    //hora
                    if(Integer.parseInt(a[3]) < Integer.parseInt(b[3])) return true;
                    if(Integer.parseInt(a[3]) > Integer.parseInt(b[3])) return false;
                    if(Integer.parseInt(a[3]) == Integer.parseInt(b[3])){
                        //minuto
                        if(Integer.parseInt(a[4]) < Integer.parseInt(b[4])) return true;
                        if(Integer.parseInt(a[4]) > Integer.parseInt(b[4])) return false;
                        if(Integer.parseInt(a[4]) == Integer.parseInt(b[4])) return true;
                    }
                }
            }
        }
        return false;
    }

}
