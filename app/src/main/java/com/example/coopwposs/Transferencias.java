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

public class Transferencias extends AppCompatActivity {

    public EditText et_destino, et_monto;
    public String valortrans;
    public long destinotrans;
    public long numcuenta;
    public String email;
    public String cuenta;
    public String administrador_string;
    public int administrador_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencias);
        et_destino = (EditText) findViewById(R.id.et_destinotrans);
        et_monto = (EditText) findViewById(R.id.et_montotrans);
        String datoenviado = getIntent().getStringExtra("cuenta");
        String datoenviado2 = getIntent().getStringExtra("email");
        String datoenviado3 = getIntent().getStringExtra("administra");
        numcuenta = Long.parseLong(datoenviado);
        email = datoenviado2;
        cuenta = datoenviado;
        administrador_string = datoenviado3;
        administrador_int = Integer.parseInt(administrador_string);
    }

    public void validartrans(View view) {
        destinotrans = Long.parseLong(et_destino.getText().toString());
        String destino = et_destino.getText().toString();
        String monto = et_monto.getText().toString();
        if (destino.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (monto.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(monto) <= 0) {
            Toast.makeText(this, "Debe ingresar un valor superior a la tarifa de transferencias (8000)", Toast.LENGTH_SHORT).show();
        } else if (destinotrans == numcuenta) {
            Toast.makeText(this, "Debe ingresar una cuenta diferente a la suya", Toast.LENGTH_SHORT).show();
        } else
            this.validar_saldo(view);
    }

    //Metodo para Validar el saldo de mi cuenta
    public void validar_saldo(View view) {
        //long destinotrans = Long.parseLong(et_destino.getText().toString());
        valortrans = et_monto.getText().toString();
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select saldo from usuarios where cuenta = '" + numcuenta + "'", null);
        if (fila.moveToFirst()) {
            int saldo = Integer.parseInt(fila.getString(0));
            int valortrans2 = Integer.parseInt(valortrans);
            int valortotal = (saldo - valortrans2) - 8000;
            if (valortotal < 0) {
                Toast.makeText(this, "No tiene saldo suficiente", Toast.LENGTH_SHORT).show();
                basededatos.close();
                fila.close();
            } else {
                this.validar_cuenta_destino(view);
            }
        } else {
            Toast.makeText(this, "Error inesperado en la instruccion para validar nuestro saldo", Toast.LENGTH_LONG).show();
            basededatos.close();
            fila.close();
        }
    }

    //Metodo para guardar en la cuenta de destino
    public void validar_cuenta_destino(View view) {
        valortrans = et_monto.getText().toString();
        destinotrans = Long.parseLong(et_destino.getText().toString());
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select saldo, estado from usuarios where cuenta = '" + destinotrans + "'", null);
        if (fila.moveToFirst()) {
            String estado = (fila.getString(1));
            if(estado.equals("Inactivo")){
                Toast.makeText(this, "La cuenta de destino ha sido deshabilitada, no puede recibir su transferencia.", Toast.LENGTH_LONG).show();
            }else if (estado.equals("Activo")){
                int saldoactual = Integer.parseInt(fila.getString(0));
                int valortrans2 = Integer.parseInt(valortrans);
                int valortotal = (saldoactual + valortrans2);
                SQLiteDatabase basededatos2 = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("saldo", valortotal);
                basededatos2.update("usuarios", registro, "cuenta=" + destinotrans, null);
                basededatos2.close();
                fila.close();
                Toast.makeText(this, "Transferencia entregada correctamente", Toast.LENGTH_SHORT).show();
                this.descontar_saldo(view);
            }
        } else {
            Toast.makeText(this, "Error: cuenta de destino no existe.", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
        }
    }

    //Este metodo descuenta el saldo de mi cuenta despues de guardar en la cta de destino
    public void descontar_saldo(View view) {
        valortrans = et_monto.getText().toString();
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select saldo from usuarios where cuenta = '" + numcuenta + "'", null);
        if (fila.moveToFirst()) {
            int saldoactual = Integer.parseInt(fila.getString(0));
            int valortrans2 = Integer.parseInt(valortrans);
            int valortotal = (saldoactual - valortrans2) - 8000;
            ContentValues registro = new ContentValues();
            registro.put("saldo", valortotal);
            basededatos.update("usuarios", registro, "cuenta=" + numcuenta, null);
            basededatos.close();
            fila.close();
            Toast.makeText(this, "Saldo descontado correctamente", Toast.LENGTH_SHORT).show();
            et_monto.setText("");
        } else {
            Toast.makeText(this, "Error inesperado en la instruccion para descontar nuestro saldo", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
        }
    }

    public void salirdetrans(View view) {
        Intent salirtrans = new Intent(this, Acceso_normal.class);
        salirtrans.putExtra("cuenta", cuenta);
        salirtrans.putExtra("email", email);
        salirtrans.putExtra("administra", administrador_string);
        startActivity(salirtrans);
        finish();
    }
}