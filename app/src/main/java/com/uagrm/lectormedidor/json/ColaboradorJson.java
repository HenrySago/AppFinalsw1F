package com.uagrm.lectormedidor.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColaboradorJson {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombres")
    @Expose
    private String nombres;
    @SerializedName("apellidos")
    @Expose
    private String apellidos;
    @SerializedName("perfil")
    @Expose
    private String perfil;
    @SerializedName("ci")
    @Expose
    private String ci;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("celular")
    @Expose
    private String celular;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("idCiudad")
    @Expose
    private Integer idCiudad;
    @SerializedName("eliminado")
    @Expose
    private Integer eliminado;
    @SerializedName("inhabilitado")
    @Expose
    private Integer inhabilitado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    public Integer getEliminado() {
        return eliminado;
    }

    public void setEliminado(Integer eliminado) {
        this.eliminado = eliminado;
    }

    public Integer getInhabilitado() {
        return inhabilitado;
    }

    public void setInhabilitado(Integer inhabilitado) {
        this.inhabilitado = inhabilitado;
    }
}