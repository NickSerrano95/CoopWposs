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

public class MainActivity extends AppCompatActivity {
    public EditText et_user, et_clave;
    public String usuario, clave;
    public String cuenta;
    public String administrador;
    public Long cuentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_user = (EditText) findViewById(R.id.et_correo);
        et_clave = (EditText) findViewById(R.id.et_contraseña);

        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select correo from usuarios where correo ='admin@wposs.com'", null);
        if (fila.moveToFirst()) {
            basededatos.close();
            fila.close();
        } else {
            long min = 1000000000L;
            long max = 9999999999L;
            long random = (long) (Math.random() * (max - min + 1) + min);
            cuentar = random;
            SQLiteDatabase base_de_datos = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("documento", 1095941123);
            registro.put("saldo", 1000000);
            registro.put("clave", "Admin123*");
            registro.put("nombre", "Administrador");
            registro.put("correo", "admin@wposs.com");
            registro.put("cuenta", cuentar);
            registro.put("estado", "Activo");
            registro.put("admin", 1);
            base_de_datos.insert("usuarios", null, registro);
            base_de_datos.close();
            fila.close();
            //Toast.makeText(this, "Usuario Administrador ha sido creado con exito.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para validar login
    public void buscar(View view) {
        et_user.setText(et_user.getText().toString().toLowerCase());
        usuario = et_user.getText().toString();
        clave = et_clave.getText().toString();
        if (usuario.isEmpty() || (clave.isEmpty())) {
            Toast.makeText(this, "Debe ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
        } else {
            SQLclass admin = new SQLclass(this, "Mapache", null, 1);
            SQLiteDatabase basededatos = admin.getWritableDatabase();
            Cursor fila = basededatos.rawQuery("select * from usuarios where correo='" + usuario + "'AND clave='" + clave + "'", null, null);
            if (fila.moveToFirst()) {
                this.estado(view);
                basededatos.close();
                fila.close();
            } else {
                Toast.makeText(this, "Usuario/Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                basededatos.close();
                fila.close();
            }
        }
    }

    //Metodo para validar el estado de la cuenta
    public void estado(View view) {
        usuario = et_user.getText().toString();
        clave = et_clave.getText().toString();
        if (usuario.isEmpty() && (clave.isEmpty())) {
            Toast.makeText(this, "Debe ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
        } else {
            SQLclass admin = new SQLclass(this, "Mapache", null, 1);
            SQLiteDatabase basededatos = admin.getWritableDatabase();
            Cursor fila = basededatos.rawQuery("select estado from usuarios where correo='" + usuario + "'AND clave='" + clave + "'", null, null);
            fila.moveToFirst();
            String estado = fila.getString(0);
            basededatos.close();
            fila.close();
            if (estado.equals("Activo")) {
                this.ingresar(view);
                basededatos.close();
                fila.close();
            } else {
                Toast.makeText(this, "Su cuenta ha sido deshabilitada, contacte con el administrador", Toast.LENGTH_LONG).show();
            }
        }
    }

    //este metodo captura la cuenta del usuario y la envia al sig activity para usar el valor.
    public void ingresar(View view) {
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select cuenta, admin from usuarios where correo='" + usuario + "'", null);
        if (fila.moveToFirst()) {
            cuenta = fila.getString(0);
            administrador = fila.getString(1);
            Intent entrar = new Intent(this, Acceso_normal.class);
            entrar.putExtra("email", et_user.getText().toString());
            entrar.putExtra("cuenta", cuenta);
            entrar.putExtra("administra", administrador);
            startActivity(entrar);
            basededatos.close();
            fila.close();
            finish();
        } else {
            Toast.makeText(this, "Error de ingreso", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
        }
    }

    public void registrarse(View view) {
        Intent regis = new Intent(this, Registro.class);
        startActivity(regis);
        finish();
    }

    public void depositar(View view) {
        Intent deposit = new Intent(this, Depositos.class);
        startActivity(deposit);
        finish();
    }

    public void salir(View view) {
        finish();
    }
}