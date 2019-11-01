package com.fenixu.logica_negocio;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.fenixu.R;
import com.fenixu.gui.CronogramaActivity;

import java.util.Calendar;
import java.util.List;

public class AdaptadorAlarmas extends BaseAdapter {

    private Context context;
    private List<Alarma> alarmas;


    public AdaptadorAlarmas(Context context, List<Alarma> alarmas) {
        this.context = context;
        this.alarmas = alarmas;
    }

    @Override
    public int getCount() {
        return alarmas.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        if(view == null) view = View.inflate(context, R.layout.elemento_lista_alarma, null);

        //Seran usados en los onclick
        final View viewin = view;
        final int iin = i;

        Alarma alarma = alarmas.get(i);//Busca la alarma

        //Pone el titulo de la alarma
        TextView tituloAlarma = view.findViewById(R.id.etTituloAlarma);
        tituloAlarma.setText(alarma.getTitulo());


        //Pone la fecha
        TextView fechaAlarma = view.findViewById(R.id.etFechaAlarma);
        String[] fecha = alarma.getFecha().split("/");           //Suma 1 al mes
        fecha[1] = Integer.toString(Integer.parseInt(fecha[1]) + 1);
        fechaAlarma.setText(fecha[0] + "/" + fecha[1] + "/" + fecha[2]);
        fechaAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viw) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //Guarda en bd
                        String fecha = ""+ i2+"/"+i1+"/"+i;
                        Alarma alarma = alarmas.get(iin);
                        alarma.setFecha(fecha);
                        AlarmasBase.get(context).actualizarAlarma(alarma);
                        //Refresca los datos
                        notifyDataSetChanged();
                        refresca();
                    }
                },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                //datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                datePickerDialog.show();
            }
        });

        //Pone la hora
        TextView horaAlarma = view.findViewById(R.id.etHoraAlarma);
        horaAlarma.setText(alarma.getHora());
        horaAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viw) {
                TimePickerDialog datePickerDialog = new TimePickerDialog(
                        context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        //Guarda en bd
                        String hora, h, s;
                        if(i  < 10) h = "0"+i; else h = ""+i;
                        if(i1 < 10) s = "0"+i1; else s = ""+i1;
                        hora = h+":"+s;
                        Alarma alarma = alarmas.get(iin);
                        alarma.setHora(hora);
                        AlarmasBase.get(context).actualizarAlarma(alarma);
                        //Refresca los datos
                        notifyDataSetChanged();
                        refresca();
                    }
                },
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true
                );
                //datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
                datePickerDialog.show();
            }
        });

        //Pone la imagen
        ImageView imgEliminar = view.findViewById(R.id.imgEliminarAlarma);
        imgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarma alarma = alarmas.get(iin);
                AlarmasBase.get(context).eliminarAlarma(alarma);
                alarmas.clear();
                alarmas = AlarmasBase.get(context).getAlarmas();
                //Refresca los datos
                notifyDataSetChanged();
                refresca();
            }
        });

        return view;
    }

    //"Refresca" Cronograma activity
    private void refresca(){
        Bundle b = new Bundle();
        b.putString("valor", "prueba");
        Intent i = new Intent(context, CronogramaActivity.class);
        i.putExtra("valor", "prueba");
        context.startActivity( i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), b);
    }

}
