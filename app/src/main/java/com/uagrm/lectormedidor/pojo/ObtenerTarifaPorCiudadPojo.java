package com.uagrm.lectormedidor.pojo;

import java.math.BigInteger;

public class ObtenerTarifaPorCiudadPojo {
    private String idCiudad;
    private String idCliente;
    private String consumo;

    public String getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }
}
