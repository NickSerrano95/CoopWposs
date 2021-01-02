package com.example.coopwposs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Depositos extends AppCompatActivity {

    public EditText et_cuenta, et_monto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositos);
        et_cuenta = (EditText) findViewById(R.id.et_cuenta);
        et_monto = (EditText) findViewById(R.id.et_montodep);

    }

    public void validardep(View view) {
        String cuenta = et_cuenta.getText().toString();
        String monto = et_monto.getText().toString();
        if (cuenta.isEmpty() || monto.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(monto) <= 2000) {
            Toast.makeText(this, "Debe ingresar un valor superior a la tarifa de depositos (2000)", Toast.LENGTH_LONG).show();
        } else {
            this.guardardep(view);
        }
    }

    public void estado(View view) {
        Long cuenta = Long.parseLong(et_cuenta.getText().toString());
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select estado from usuarios where cuenta='" + cuenta + "'", null, null);
        fila.moveToFirst();
        String estado = fila.getString(0);
        basededatos.close();
        fila.close();
        if (estado.equals("Activo")) {
            this.guardardep(view);
            basededatos.close();
            fila.close();
        } else {

        }
    }

    public void guardardep(View view) {
        String cuenta = et_cuenta.getText().toString();
        String monto = et_monto.getText().toString();
        long cuenta_int = Long.parseLong(cuenta);
        int monto_int = Integer.parseInt(monto);
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select cuenta, estado, saldo from usuarios where cuenta =" + cuenta_int, null);
        if (fila.moveToFirst()) {
            String estado = (fila.getString(1));
            if (estado.equals("Activo")) {
                String saldo_actual = fila.getString(2);
                Integer saldo_actual_int = Integer.parseInt(saldo_actual);
                int saldototal = (saldo_actual_int + monto_int) - 2000;
                SQLiteDatabase basededatos2 = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("saldo", saldototal);
                basededatos2.update("usuarios", registro, "cuenta=" + cuenta_int, null);
                basededatos2.close();
                Toast.makeText(this, "El deposito fue Exitoso!!", Toast.LENGTH_SHORT).show();
                basededatos.close();
            }else{
                Toast.makeText(this, "La cuenta de destino está deshabilitada, no puede depositar en ella.", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "La cuenta NO existe.", Toast.LENGTH_SHORT).show();
            basededatos.close();
        }

    }

    public void salirdep(View view) {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
        finish();
    }
}