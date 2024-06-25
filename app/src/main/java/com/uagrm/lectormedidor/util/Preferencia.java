package com.uagrm.lectormedidor.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencia {
    private SharedPreferences sharedPreferences;

    public Preferencia(Context context) {
        sharedPreferences = context.getSharedPreferences("datosPersonales", MODE_PRIVATE);
    }



    public void setLogeado(boolean b) {
        sharedPreferences.edit().putBoolean("logeado", b).apply();
    }

    public Boolean getLogeado() {
        return sharedPreferences.getBoolean("logeado", false);
    }


    public void setNombres(String b) {
        sharedPreferences.edit().putString("nombres", b).apply();

    }
    public String getNombres() {
        return sharedPreferences.getString("nombres", "");
    }



    public void setApellidos(String b) {
        sharedPreferences.edit().putString("apellidos", b).apply();

    }
    public String getApellidos() {
        return sharedPreferences.getString("apellidos", "");
    }

    public String getCelular() {
        return sharedPreferences.getString("celular", "");
    }


    public void setCelular(String b) {
        sharedPreferences.edit().putString("celular", b).apply();

    }


    public void setEmail(String b) {
        sharedPreferences.edit().putString("email", b).apply();

    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public void setNit(String b) {
        sharedPreferences.edit().putString("nit", b).apply();

    }

    public String getNit() {
        return sharedPreferences.getString("nit", "");
    }

    public void setNombreFactura(String b) {
        sharedPreferences.edit().putString("nombrefactura", b).apply();

    }

    public String getNombreFactura() {
        return sharedPreferences.getString("nombrefactura", "");
    }

    public void setClienteId(String b) {
        sharedPreferences.edit().putString("clienteid", b).apply();
    }

    public String getClienteId() {
        return sharedPreferences.getString("clienteid", "0");
    }


    public void setColaboradorId(String b) {
        sharedPreferences.edit().putString("colaboradorid", b).apply();
    }

    public String getColaboradorId() {
        return sharedPreferences.getString("colaboradorid", "0");
    }

    public void setPassword(String b) {
        sharedPreferences.edit().putString("password", b).apply();
    }
    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }


    public void setFotoPerfil(String b) {
        sharedPreferences.edit().putString("fotoperfil", b).apply();
    }

    public String getFotoPerfil() {
        return sharedPreferences.getString("fotoperfil", "");
    }

    public void setTipoUsuario(String b) {
        sharedPreferences.edit().putString("tipousuario", b).apply();
    }

    public String getTipoUsuario() {
        return sharedPreferences.getString("tipousuario", "");
    }


    public void clearDatosLogin() {
        sharedPreferences.edit().putString("nombres", "").apply();
        sharedPreferences.edit().putString("apellidos", "").apply();
        sharedPreferences.edit().putString("email", "").apply();
        sharedPreferences.edit().putString("celular", "").apply();
        sharedPreferences.edit().putString("password", "").apply();
        sharedPreferences.edit().putString("clienteid", "").apply();
        sharedPreferences.edit().putString("colaboradorid", "").apply();
        sharedPreferences.edit().putString("fotoperfil", "").apply();
        sharedPreferences.edit().putBoolean("logeado", false).apply();
        sharedPreferences.edit().putString("tipousuario", "").apply();
        sharedPreferences.edit().putString("nit", "").apply();
        sharedPreferences.edit().putString("nombrefactura", "").apply();

    }

}
