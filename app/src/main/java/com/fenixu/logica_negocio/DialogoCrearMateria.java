package com.fenixu.logica_negocio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.fenixu.R;

public class DialogoCrearMateria extends AppCompatDialogFragment {

    private EditText materia;
    private EditText creditos;
    private DialogoCrearMateriasListener dcml;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_agregar_materias, null);

        builder.setView(view)
                .setTitle("Agregar materia")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String materiaR = materia.getText().toString();
                        String creditosR = creditos.getText().toString();
                        if(materiaR.length()>0 && creditosR.length()>0){
                            dcml.applyTexts(materiaR, creditosR);
                        }
                    }
                });

        materia = view.findViewById(R.id.nombreAgregarMateria);
        creditos = view.findViewById(R.id.nombreAgregarCreditos);

        return builder.create();
    }

    @Override
    public void onAttach(Context contexto){
        super.onAttach(contexto);

        try {
            dcml = (DialogoCrearMateriasListener)contexto;
        } catch (ClassCastException e) {
            throw new ClassCastException(contexto.toString() + "Error");
        }
    }

    public interface DialogoCrearMateriasListener{
        void applyTexts(String materia, String creditos);
    }

}