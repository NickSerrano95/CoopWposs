package com.example.coopwposs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    public EditText et_nom, et_contra, et_indenti, et_correee;
    public Integer saldo = 1000000;
    public long cuenta;
    public String nombre;
    public String documento;
    public String correo;
    public String clave;
    public String estado = "Activo";
    public Integer administrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        et_nom = (EditText) findViewById(R.id.et_nombre);
        et_correee = (EditText) findViewById(R.id.et_correoreg);
        et_contra = (EditText) findViewById(R.id.et_contraseña);
        et_indenti = (EditText) findViewById(R.id.et_cedula);
    }

    //Validacion para campos vacios y longitud minima de textos
    //Eliminamos espacion vacios en el NOMBRE
    //pasamos toda la cadena del correo a minusculas
    public void validar(View view) {
        et_nom.setText(et_nom.getText().toString().trim());
        et_nom.setText(et_nom.getText().toString().replaceAll(" +", " "));
        nombre = et_nom.getText().toString();
        et_correee.setText(et_correee.getText().toString().toLowerCase());
        et_nom.setText(et_nom.getText().toString().toLowerCase());
        documento = et_indenti.getText().toString();
        correo = et_correee.getText().toString();
        clave = et_contra.getText().toString();
        if ((nombre.length() < 3)) {
            Toast.makeText(this, "El nombre debe contener minimo 3 digitos", Toast.LENGTH_SHORT).show();
        } else if ((documento.length() < 10)) {
            Toast.makeText(this, "El documento debe contener minimo 10 digitos", Toast.LENGTH_SHORT).show();
        } else {
            this.letras_nombre(view);

        }
    }

    //Esta pone las primeras letras en mayuscula pero no las demas ejemplo: NorMaN
    public void letras_nombre(View view) {
        String str = et_nom.getText().toString();
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        et_nom.setText(builder.toString());
        this.validarEmail(view);
    }

    //validamos que el email tenga una estructura valida
    public boolean validarEmail(View view) {
        String emailingresado = correo;
        if (Patterns.EMAIL_ADDRESS.matcher(emailingresado).matches()) {
            this.validarnumeros(view);
            return true;
        } else {
            Toast.makeText(this, "Email Invalido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Validacion para buscar letras en la clave
    public void validarnumeros(View view) {
        if ((clave.length() < 8)) {
            Toast.makeText(this, "La contraseña debe contener minimo 8 digitos entre numeros y letras.", Toast.LENGTH_LONG).show();
        } else {
            this.validarnumeros1(view);
        }
    }

    public boolean validarnumeros1(View view) {
        String cadena = clave;
        boolean resultado;
        try {
            Integer.parseInt(cadena);

            if (resultado = true) {
                Toast.makeText(this, "La contraseña debe contener letras tambien", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException excepcion) {
            this.validarletras(view);
            resultado = false;
        }
        return resultado;
    }

    //Validacion para buscar numeros en la clave
    public boolean validarletras(View view) {
        String cadena = et_contra.getText().toString();
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
                this.generarrandom(view);
                return false;
            }
        }
        Toast.makeText(this, "La contraseña debe contener numeros tambien", Toast.LENGTH_SHORT).show();
        return true;
    }

    //Generamos un numero random de 10 digitos
    public void generarrandom(View view) {
        long min = 1000000000L;
        long max = 9999999999L;
        long random = (long) (Math.random() * (max - min + 1) + min);
        cuenta = random;
        this.randomvalido(view);
    }

    //Validamos que el numero de cuenta Random no exista previamente
    public void randomvalido(View view) {
        long cuentas = cuenta;
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select cuenta from usuarios where cuenta ='" + cuentas + "'", null);
        if (fila.moveToFirst()) {
            Toast.makeText(this, "Generando numero de cuenta unico...", Toast.LENGTH_SHORT).show();
            this.generarrandom(view);
        } else {
            this.administrador_in(view);
        }
    }

    // Validamos credenciales de admin para otorgar permisos.
    public void administrador_in(View view) {
        correo = et_correee.getText().toString();
        clave = et_contra.getText().toString();
        if (correo.equals("admin@wposs.com") && (clave.equals("Admin123"))) {
            administrador = 1;
            this.registrar(view);
        } else {
            administrador = 0;
            this.registrar(view);
        }
    }

    //Hechas todas las validaciones, se guarda toda la informacion en la BD
    public void registrar(View view) {
        long cuentas = cuenta;
        saldo = Integer.parseInt(saldo.toString());
        nombre = et_nom.getText().toString();
        documento = et_indenti.getText().toString();
        correo = et_correee.getText().toString();
        clave = et_contra.getText().toString();
        SQLclass admin = new SQLclass(this, "Mapache", null, 1);
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        Cursor fila = basededatos.rawQuery("select correo from usuarios where correo ='" + correo + "'", null);
        if (fila.moveToFirst()) {
            Toast.makeText(this, "El correo ya existe, debe usar otro.", Toast.LENGTH_SHORT).show();
            basededatos.close();
            fila.close();
        } else {
            SQLiteDatabase base_de_datos = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("documento", documento);
            registro.put("saldo", saldo);
            registro.put("clave", clave);
            registro.put("nombre", nombre);
            registro.put("correo", correo);
            registro.put("cuenta", cuentas);
            registro.put("estado", estado);
            registro.put("admin", administrador);
            base_de_datos.insert("usuarios", null, registro);
            base_de_datos.close();
            fila.close();
            Toast.makeText(this, "El registro fue Exitoso!!", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para regresar a la pantalla de inicio
    public void Regresar(View view) {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
        finish();
    }

}