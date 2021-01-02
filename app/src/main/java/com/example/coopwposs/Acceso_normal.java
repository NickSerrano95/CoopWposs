package com.example.coopwposs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Acceso_normal extends AppCompatActivity {

    public TextView tv_cuenta, tvsaldo, tvsaldo2;
    public Button bt_admin;
    public String email;
    public Long cuenta;
    public String ncuenta;
    public Integer saldo;
    public String administrador_string;
    public Integer administrador_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso_normal);
        bt_admin = (Button) findViewById(R.id.bt_admin);
        String datoenviado = getIntent().getStringExtra("email");
        String datoenviado2 = getIntent().getStringExtra("cuenta");
        String datoenviado3 = getIntent().getStringExtra("administra");
        tv_cuenta = (TextView) findViewById(R.id.tv_cuenta);
        tvsaldo = (TextView) findViewById(R.id.tvsaldo);
        tvsaldo2 = (TextView) findViewById(R.id.tvsaldo2);
        email = datoenviado;
        cuenta = Long.parseLong(datoenviado2);
        ncuenta = datoenviado2;
        administrador_string = datoenviado3;
        administrador_int = Integer.parseInt(administrador_string);


        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select cuenta from usuarios where correo='" + email + "'", null);
        if (fila.moveToFirst()) {
            tv_cuenta.setText("Su numero de cuenta es: " + fila.getString(0));
            basededatos.close();
            fila.close();
        } else {
            Toast.makeText(this, "Error buscando el # de cuenta", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
        }


        SQLclass admin2 = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos2 = admin2.getWritableDatabase();
        Cursor fila2 = basededatos2.rawQuery("select saldo from usuarios where cuenta='" + cuenta + "'", null);
        if (fila2.moveToFirst()) {
            tvsaldo.setText("Su saldo es: ");
            tvsaldo2.setText(fila2.getString(0));
            saldo = Integer.parseInt(fila2.getString(0));
            basededatos2.close();
            fila2.close();
        } else {
            Toast.makeText(this, "Error buscando # cuenta", Toast.LENGTH_SHORT).show();
        }

        if (administrador_int == 1) {
            bt_admin.setEnabled(true);
        } else {
            bt_admin.setEnabled(false);
        }
    }

    public void ir_retiros(View view) {
        Intent ir = new Intent(this, Retiros.class);
        ir.putExtra("cuenta", ncuenta);
        ir.putExtra("email", email);
        ir.putExtra("administra", administrador_string);
        startActivity(ir);
        finish();
    }

    public void ir_transferencias(View view) {
        Intent ir_transferencias = new Intent(this, Transferencias.class);
        ir_transferencias.putExtra("cuenta", ncuenta);
        ir_transferencias.putExtra("email", email);
        ir_transferencias.putExtra("administra", administrador_string);
        startActivity(ir_transferencias);
        finish();
    }

    public void ir_administrar(View view) {
        Intent ir_administrar = new Intent(this, AdministrarCuentas.class);
        ir_administrar.putExtra("cuenta", ncuenta);
        ir_administrar.putExtra("email", email);
        ir_administrar.putExtra("administra", administrador_string);
        startActivity(ir_administrar);
        finish();
    }

    public void salirp(View view) {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
        finish();
    }
}