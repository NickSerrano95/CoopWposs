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

public class Retiros extends AppCompatActivity {
    public EditText et_montoret;
    public String ncuenta;
    public String email;
    public Integer monto;
    public Integer valorretiro;
    public String administrador_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiros);
        String datoenviado = getIntent().getStringExtra("cuenta");
        String datoenviado2 = getIntent().getStringExtra("email");
        String datoenviado3 = getIntent().getStringExtra("administra");
        et_montoret = (EditText) findViewById(R.id.et_montoret);
        ncuenta = datoenviado;
        email = datoenviado2;
        administrador_string = datoenviado3;

    }

    public void validarret(View view) {
        String monto = et_montoret.getText().toString();
        if (monto.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(monto) == 0) {
            Toast.makeText(this, "Debe ingresar un valor diferente a 0", Toast.LENGTH_SHORT).show();
        }else {
            this.guardarretiro(view);
        }
    }

    public void guardarretiro(View view) {
        valorretiro = Integer.parseInt(et_montoret.getText().toString());
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select saldo from usuarios where cuenta = '" + ncuenta + "'", null);
        if (fila.moveToFirst()) {
            Integer saldoactual = fila.getInt(0);
            int valortotal = (saldoactual - valorretiro) - 5000;
            if (valortotal < 0) {
                Toast.makeText(this, "No tiene saldo suficiente", Toast.LENGTH_SHORT).show();
            } else {
                SQLiteDatabase basededatos2 = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("saldo", valortotal);
                basededatos2.update("usuarios", registro, "cuenta=" + ncuenta, null);
                basededatos2.close();
                Toast.makeText(this, "El Retiro fue Exitoso!!", Toast.LENGTH_SHORT).show();
                et_montoret.setText("");
            }
        } else {
            Toast.makeText(this, "La cuenta NO existe.", Toast.LENGTH_SHORT).show();
            basededatos.close();
        }
    }

    public void salirret(View view) {
        Intent volver = new Intent(this, Acceso_normal.class);
        volver.putExtra("cuenta", ncuenta);
        volver.putExtra("email", email);
        volver.putExtra("administra", administrador_string);
        startActivity(volver);
        finish();
    }
}