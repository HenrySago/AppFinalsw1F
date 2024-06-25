package com.uagrm.lectormedidor.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacturaJson {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("idMedicion")
    @Expose
    private Integer idMedicion;
    @SerializedName("pagado")
    @Expose
    private Integer pagado;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("medicion")
    @Expose
    private Medicion medicion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMedicion() {
        return idMedicion;
    }

    public void setIdMedicion(Integer idMedicion) {
        this.idMedicion = idMedicion;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Medicion getMedicion() {
        return medicion;
    }

    public void setMedicion(Medicion medicion) {
        this.medicion = medicion;
    }


    public class Medicion {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("latitud")
        @Expose
        private String latitud;
        @SerializedName("longitud")
        @Expose
        private String longitud;
        @SerializedName("direccion")
        @Expose
        private String direccion;
        @SerializedName("referencia")
        @Expose
        private String referencia;
        @SerializedName("consumo")
        @Expose
        private Integer consumo;
        @SerializedName("consumoReal")
        @Expose
        private Integer consumoReal;
        @SerializedName("total")
        @Expose
        private Double total;
        @SerializedName("idMensualidad")
        @Expose
        private Integer idMensualidad;
        @SerializedName("idAdministrador")
        @Expose
        private Integer idAdministrador;
        @SerializedName("idColaborador")
        @Expose
        private Integer idColaborador;
        @SerializedName("idCliente")
        @Expose
        private Integer idCliente;
        @SerializedName("estado")
        @Expose
        private Integer estado;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("mensualidad")
        @Expose
        private Mensualidad mensualidad;
        @SerializedName("cliente")
        @Expose
        private Cliente cliente;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLatitud() {
            return latitud;
        }

        public void setLatitud(String latitud) {
            this.latitud = latitud;
        }

        public String getLongitud() {
            return longitud;
        }

        public void setLongitud(String longitud) {
            this.longitud = longitud;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getReferencia() {
            return referencia;
        }

        public void setReferencia(String referencia) {
            this.referencia = referencia;
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

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public Integer getIdMensualidad() {
            return idMensualidad;
        }

        public void setIdMensualidad(Integer idMensualidad) {
            this.idMensualidad = idMensualidad;
        }

        public Integer getIdAdministrador() {
            return idAdministrador;
        }

        public void setIdAdministrador(Integer idAdministrador) {
            this.idAdministrador = idAdministrador;
        }

        public Integer getIdColaborador() {
            return idColaborador;
        }

        public void setIdColaborador(Integer idColaborador) {
            this.idColaborador = idColaborador;
        }

        public Integer getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(Integer idCliente) {
            this.idCliente = idCliente;
        }

        public Integer getEstado() {
            return estado;
        }

        public void setEstado(Integer estado) {
            this.estado = estado;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Mensualidad getMensualidad() {
            return mensualidad;
        }

        public void setMensualidad(Mensualidad mensualidad) {
            this.mensualidad = mensualidad;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

    }

    public class Mensualidad {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("nombre")
        @Expose
        private String nombre;
        @SerializedName("idGestion")
        @Expose
        private Integer idGestion;
        @SerializedName("eliminado")
        @Expose
        private Integer eliminado;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("gestion")
        @Expose
        private Gestion gestion;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Integer getIdGestion() {
            return idGestion;
        }

        public void setIdGestion(Integer idGestion) {
            this.idGestion = idGestion;
        }

        public Integer getEliminado() {
            return eliminado;
        }

        public void setEliminado(Integer eliminado) {
            this.eliminado = eliminado;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Gestion getGestion() {
            return gestion;
        }

        public void setGestion(Gestion gestion) {
            this.gestion = gestion;
        }

    }
    public class Cliente {

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
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("celular")
        @Expose
        private String celular;
        @SerializedName("latitud")
        @Expose
        private String latitud;
        @SerializedName("longitud")
        @Expose
        private String longitud;
        @SerializedName("direccion")
        @Expose
        private String direccion;
        @SerializedName("referencia")
        @Expose
        private String referencia;
        @SerializedName("nit")
        @Expose
        private String nit;
        @SerializedName("nombreFactura")
        @Expose
        private String nombreFactura;
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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCelular() {
            return celular;
        }

        public void setCelular(String celular) {
            this.celular = celular;
        }

        public String getLatitud() {
            return latitud;
        }

        public void setLatitud(String latitud) {
            this.latitud = latitud;
        }

        public String getLongitud() {
            return longitud;
        }

        public void setLongitud(String longitud) {
            this.longitud = longitud;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getReferencia() {
            return referencia;
        }

        public void setReferencia(String referencia) {
            this.referencia = referencia;
        }

        public String getNit() {
            return nit;
        }

        public void setNit(String nit) {
            this.nit = nit;
        }

        public String getNombreFactura() {
            return nombreFactura;
        }

        public void setNombreFactura(String nombreFactura) {
            this.nombreFactura = nombreFactura;
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

    public class Gestion {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("nombre")
        @Expose
        private String nombre;
        @SerializedName("eliminado")
        @Expose
        private Integer eliminado;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Integer getEliminado() {
            return eliminado;
        }

        public void setEliminado(Integer eliminado) {
            this.eliminado = eliminado;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}