package com.fenixu.logica_negocio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fenixu.R;
import com.fenixu.gui.CrearNotasMaterias;
import com.fenixu.recursos_datos.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorAgregarNotas extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    List<List<String>> itemNotas = new ArrayList<List<String>>();

    public AdaptadorAgregarNotas(Context contexto, List itemNotas) {
        this.contexto = contexto;
        this.itemNotas = itemNotas;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemNotas.get(0).size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent){

        final View vista = inflater.inflate(R.layout.elemento_lista_nota,null);

        TextView nota = (TextView) vista.findViewById(R.id.listaNota);
        TextView porcentaje = (TextView) vista.findViewById(R.id.listaPorcentaje);

        ImageButton btnElminarNota = (ImageButton) vista.findViewById(R.id.btnEliminarNotas);

        nota.setText(itemNotas.get(1).get(i));
        porcentaje.setText(itemNotas.get(2).get(i));

        int idActual = 0;
        int cont = 0;
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "adminNotas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor idm = db.rawQuery("select max(idNota) from notas", null);
        if(idm.moveToFirst()){
            int idmaximo = idm.getInt(0);
            for(int j = 0; j <= idmaximo; j++){
                Cursor id = db.rawQuery("select idNota from notas where idNota="+j, null);
                if(id.moveToFirst()){
                    if(cont<=i){
                        idActual = id.getInt(0);
                        cont++;
                    }
                }
            }
        }
        db.close();

        btnElminarNota.setTag(idActual+1000);

        btnElminarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notasMaterias = new Intent(contexto, CrearNotasMaterias.class);
                notasMaterias.putExtra("posicionEliminarNota", (Integer) v.getTag());
                contexto.startActivity(notasMaterias);
            }
        });

        return vista;
    }
}
