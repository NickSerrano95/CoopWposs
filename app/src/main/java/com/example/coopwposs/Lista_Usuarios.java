package com.example.coopwposs;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lista_Usuarios extends AppCompatActivity {
    public ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__usuarios);
        cargar();
    }

    public void cargar() {
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        if (database != null) {
            Cursor fila = database.rawQuery("select correo, estado from usuarios", null);
            int cantidad = fila.getCount();
            int i = 0;
            String[] arreglo = new String[cantidad];
            if (fila.moveToFirst()) {
                do {
                    String linea = fila.getString(0) + " - " + fila.getString(1);
                    arreglo[i] = linea;
                    i++;
                } while (fila.moveToNext());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
            ListView lista = (ListView) findViewById(R.id.Lista);
            lista.setAdapter(adapter);
        }
    }

}