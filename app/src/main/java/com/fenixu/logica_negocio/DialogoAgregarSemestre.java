package com.fenixu.logica_negocio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.fenixu.R;

public class DialogoAgregarSemestre extends AppCompatDialogFragment{
    private EditText semestre;
    private EditText promedio;
    private EditText creditos;
    private DialogoAgregarSemestresListener dcm3;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_agregar_semestre, null);

        semestre = view.findViewById(R.id.nombreAgregarSemestre);
        promedio = view.findViewById(R.id.nombrePromedioSemestre);
        creditos = view.findViewById(R.id.nombreAgregarCreditos);

        builder.setView(view)
                .setTitle("Agregar nota")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String semestreR = semestre.getText().toString();
                        String promedioR = promedio.getText().toString();
                        String creditoR = creditos.getText().toString();
                           // dcm3.applyTexts3(semestreR, promedioR, creditoR);
                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(Context contexto){
        super.onAttach(contexto);

        try {
            dcm3 =(DialogoAgregarSemestresListener) contexto;
        } catch (ClassCastException e) {
            throw new ClassCastException(contexto.toString() + "Error");
        }
    }

    public interface DialogoAgregarSemestresListener{
        void applyTexts3(String semestre, String promedio, String credito);
    }
}

