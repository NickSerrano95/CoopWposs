package com.example.coopwposs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdministrarCuentas extends AppCompatActivity {

    public EditText et_buscar;
    public Button btact, btdesact;
    public TextView txtcuenta, txtemail, txtnombre, txtsaldo, txtestado, txtdocumento;
    public long numcuenta;
    public String administrador_string;
    public int administrador_int;
    public String email;
    public String cuenta;
    public Long cuentabloq;
    public String usuario;
    public Integer rango_int;
    public String activate = "Activo";
    public String desactivate = "Inactivo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_cuentas);
        et_buscar = (EditText) findViewById(R.id.txt_buscarcuenta);
        txtcuenta = (TextView) findViewById(R.id.txtcuenta);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtnombre = (TextView) findViewById(R.id.txtnombre);
        txtsaldo = (TextView) findViewById(R.id.txtsaldo);
        txtestado = (TextView) findViewById(R.id.txtestado);
        txtdocumento = (TextView) findViewById(R.id.txtdocumento);
        usuario = et_buscar.getText().toString();
        String datoenviado = getIntent().getStringExtra("cuenta");
        String datoenviado2 = getIntent().getStringExtra("email");
        String datoenviado3 = getIntent().getStringExtra("administra");
        numcuenta = Long.parseLong(datoenviado);
        email = datoenviado2;
        cuenta = datoenviado;
        administrador_string = datoenviado3;
        administrador_int = Integer.parseInt(administrador_string);
        btact = (Button) findViewById(R.id.bt_habil);
        btdesact = (Button) findViewById(R.id.bt_deshabil);
        btdesact.setEnabled(false);
        btact.setEnabled(false);
    }

    public void validarbusqueda(View view) {
        usuario = et_buscar.getText().toString();
        if (usuario.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            this.validarexistencia(view);
        }
    }

    public void validarexistencia(View view) {
        et_buscar.setText(et_buscar.getText().toString().toLowerCase());
        usuario = et_buscar.getText().toString();
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select * from usuarios where correo = '" + usuario + "'", null);
        if (fila.moveToFirst()) {
            txtcuenta.setText("Cuenta:    " + fila.getString(0));
            txtemail.setText("Email:       " + fila.getString(5));
            txtnombre.setText("Nombre:  " + fila.getString(1));
            txtdocumento.setText("DNI:          " + fila.getString(2));
            txtsaldo.setText("Saldo:       " + fila.getString(4));
            txtestado.setText("Estado:     " + fila.getString(6));
            String estado1 = (fila.getString(6));
            cuentabloq = Long.parseLong(fila.getString(0));
            basededatos.close();
            fila.close();
            et_buscar.setEnabled(false);
            if (estado1.equals("Activo")) {
                btdesact.setEnabled(true);
            } else {
                btact.setEnabled(true);
            }
        } else {
            Toast.makeText(this, "Error: el email de destino no existe.", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
            btdesact.setEnabled(false);
            btact.setEnabled(false);
        }
    }

    public void Nuevabusqueda(View view) {
        txtcuenta.setText("Cuenta: ");
        txtemail.setText("Email: ");
        txtnombre.setText("Nombre: ");
        txtdocumento.setText("DNI: ");
        txtsaldo.setText("Saldo: ");
        txtestado.setText("Estado: ");
        btdesact.setEnabled(false);
        btact.setEnabled(false);
        et_buscar.setEnabled(true);
        et_buscar.setText("");
    }


    public void Deshabilitar(View view) {
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select admin from usuarios where correo = '" + usuario + "'", null);
        fila.moveToFirst();
        Integer rango = fila.getInt(0);
        if (rango == 1) {
            Toast.makeText(this, "No puede inhabilitarse usted mismo ni a otro administrador con el mismo rango.", Toast.LENGTH_LONG).show();
            basededatos.close();
            fila.close();
        } else {
            ContentValues registro = new ContentValues();
            registro.put("estado", desactivate);
            basededatos.update("usuarios", registro, "cuenta=" + cuentabloq, null);
            basededatos.close();
            fila.close();
            Toast.makeText(this, "Usuario Inhabilitado correctamente", Toast.LENGTH_SHORT).show();
            btdesact.setEnabled(false);
            btact.setEnabled(true);
            this.validarexistencia(view);
        }

    }

    public void Habilitar(View view) {
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select admin from usuarios where correo = '" + usuario + "'", null);
        fila.moveToFirst();
        Integer rango = fila.getInt(0);
        if (rango == 1) {
            Toast.makeText(this, "No puede inhabilitar a otro administrador", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
        } else {
            ContentValues registro = new ContentValues();
            registro.put("estado", activate);
            basededatos.update("usuarios", registro, "cuenta=" + cuentabloq, null);
            basededatos.close();
            fila.close();
            Toast.makeText(this, "Usuario Habilitado correctamente", Toast.LENGTH_SHORT).show();
            btdesact.setEnabled(true);
            btact.setEnabled(false);
            this.validarexistencia(view);
        }

    }

    public ArrayList llenar_lv() {
        ArrayList<String> lista = new ArrayList<>();
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        String q = "select correo from usuarios";
        Cursor fila = database.rawQuery(q, null);
        if (fila.moveToFirst()) {
            do {
                lista.add((fila.getString(0)));
            } while ((fila.moveToNext()));
        }
        return lista;
    }

    public void irlistador(View view) {
        Intent listador = new Intent(this, Lista_Usuarios.class);
        startActivity(listador);
    }

    public void saliradmin(View view) {
        Intent volver = new Intent(this, Acceso_normal.class);
        volver.putExtra("cuenta", cuenta);
        volver.putExtra("email", email);
        volver.putExtra("administra", administrador_string);
        startActivity(volver);
        finish();
    }
}