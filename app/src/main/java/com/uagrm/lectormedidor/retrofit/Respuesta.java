package com.uagrm.lectormedidor.retrofit;



public class Respuesta<T> {

    public int codigo;
    public String fecha;
    public String mensaje;
    public T data;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean respuestaExitosa() {

        if (codigo==0) {
            return true;
        } else if (codigo==1) {
            return false;
        }
        return true;
    }

}
