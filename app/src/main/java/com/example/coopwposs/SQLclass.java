package com.example.coopwposs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLclass extends SQLiteOpenHelper {

    public SQLclass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase basededatos) {
        basededatos.execSQL("create table usuarios(cuenta int primary key, nombre text, documento int, clave text, saldo int, correo text, estado text, admin int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
