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
import com.fenixu.gui.CrearNotasMaterias;

public class DialogoAgregarNota extends AppCompatDialogFragment {

    private EditText nota;
    private EditText porcentaje;
    private DialogoCrearNotasListener dcm2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_agregar_nota, null);

        nota = view.findViewById(R.id.notaMateria);
        porcentaje = view.findViewById(R.id.porcentajeNota);

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
                        String notaR = nota.getText().toString();
                        String porcentajeR = porcentaje.getText().toString();
                        float nt = Float.parseFloat(notaR);
                        if(nt>=0 && nt<=5){
                            dcm2.applyTexts2(notaR, porcentajeR);
                        }
                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(Context contexto){
        super.onAttach(contexto);

        try {
            dcm2 = (DialogoCrearNotasListener)contexto;
        } catch (ClassCastException e) {
            throw new ClassCastException(contexto.toString() + "Error");
        }
    }

    public interface DialogoCrearNotasListener{
        void applyTexts2(String nota, String porcentaje);
    }

}