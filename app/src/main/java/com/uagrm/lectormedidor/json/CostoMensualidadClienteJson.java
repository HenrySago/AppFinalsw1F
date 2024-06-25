package com.uagrm.lectormedidor.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CostoMensualidadClienteJson {

@SerializedName("consumoAnterior")
@Expose
private Integer consumoAnterior;
@SerializedName("consumo")
@Expose
private Integer consumo;
@SerializedName("consumoReal")
@Expose
private Integer consumoReal;
@SerializedName("tarifa")
@Expose
private Double tarifa;
@SerializedName("costo")
@Expose
private Double costo;

public Integer getConsumoAnterior() {
return consumoAnterior;
}

public void setConsumoAnterior(Integer consumoAnterior) {
this.consumoAnterior = consumoAnterior;
}

public Integer getConsumo() {
return consumo;
}

public void setConsumo(Integer consumo) {
this.consumo = consumo;
}

public Integer getConsumoReal() {
return consumoReal;
}

public void setConsumoReal(Integer consumoReal) {
this.consumoReal = consumoReal;
}

public Double getTarifa() {
return tarifa;
}

public void setTarifa(Double tarifa) {
this.tarifa = tarifa;
}

public Double getCosto() {
return costo;
}

public void setCosto(Double costo) {
this.costo = costo;
}

}