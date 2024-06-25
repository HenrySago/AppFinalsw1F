package com.uagrm.lectormedidor.retrofit;


import com.uagrm.lectormedidor.json.AsignacionJson;
import com.uagrm.lectormedidor.json.ClienteJson;
import com.uagrm.lectormedidor.json.ColaboradorJson;
import com.uagrm.lectormedidor.json.CostoMensualidadClienteJson;
import com.uagrm.lectormedidor.json.FacturaJson;
import com.uagrm.lectormedidor.pojo.EnviarCodigoRecuperacionPasswordPojo;
import com.uagrm.lectormedidor.pojo.GenerarFacturaPojo;
import com.uagrm.lectormedidor.pojo.ListarAsignacionPorColaboradorPojo;
import com.uagrm.lectormedidor.pojo.ListarFacturaPorClientePojo;
import com.uagrm.lectormedidor.pojo.LoginClientePojo;
import com.uagrm.lectormedidor.pojo.LoginColaboradorPojo;
import com.uagrm.lectormedidor.pojo.NuevoPasswordPojo;
import com.uagrm.lectormedidor.pojo.ObtenerTarifaPorCiudadPojo;
import com.uagrm.lectormedidor.pojo.RegistrarDispositivoClientePojo;
import com.uagrm.lectormedidor.pojo.RegistrarDispositivoColaboradorPojo;
import com.uagrm.lectormedidor.pojo.ValidarCodigoRecuperacionPasswordPojo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface LectorMedidorService {
    @POST("cliente/login")
    Call<Respuesta<ClienteJson>> loginCliente(@Body LoginClientePojo loginClientePojo);

    @POST("colaborador/login")
    Call<Respuesta<ColaboradorJson>> loginColaborador(@Body LoginColaboradorPojo loginColaboradorPojo);

    @POST("cliente/registroDispositivo")
    Call<Respuesta<String>> registrarDispositivoCliente(@Body RegistrarDispositivoClientePojo registrarDispositivoClientePojo);

    @POST("colaborador/registroDispositivo")
    Call<Respuesta<String>> registrarDispositivoColaborador(@Body RegistrarDispositivoColaboradorPojo registrarDispositivoColaboradorPojo);

    @POST("cliente/enviarCodigoRecuperacionPassword")
    Call<Respuesta<String>> enviarCodigoRecuperacionPassword(@Body EnviarCodigoRecuperacionPasswordPojo enviarCodigoRecuperacionPasswordPojo);

    @POST("cliente/validarCodigoRecuperacionPassword")
    Call<Respuesta<String>> validarCodigoRecuperacionPassword(@Body ValidarCodigoRecuperacionPasswordPojo validarCodigoRecuperacionPasswordPojo);

    @POST("cliente/nuevoPassword")
    Call<Respuesta<String>> nuevoPassword(@Body NuevoPasswordPojo NuevoPasswordPojo);

    @Multipart
    @POST("cliente/editarPerfil")
    Call<Respuesta<ClienteJson>> editarPerfilCliente(
    @Part("idCliente") RequestBody idCliente,
    @Part MultipartBody.Part perfil,
    @Part("nombres") RequestBody nombres,
    @Part("apellidos") RequestBody apellidos,
    @Part("nit") RequestBody nit,
    @Part("nombreFactura") RequestBody nombreFactura
    );

    @Multipart
    @POST("colaborador/editarPerfil")
    Call<Respuesta<ColaboradorJson>> editarPerfilColaborador(
            @Part("idColaborador") RequestBody idCliente,
            @Part MultipartBody.Part perfil,
            @Part("nombres") RequestBody nombres,
            @Part("apellidos") RequestBody apellidos
    );


    @POST("medicion/listar")
    Call<Respuesta<List<AsignacionJson>>> listarAsignacionesPorColaborador(@Body ListarAsignacionPorColaboradorPojo listarAsignacionPorColaboradorPojo);


    @POST("ciudad/precioMensualidad")
    Call<Respuesta<CostoMensualidadClienteJson>> obtenerTarifaPorCiudad(@Body ObtenerTarifaPorCiudadPojo obtenerTarifaPorCiudadPojo);

    @POST("factura/generarFactura")
    Call<Respuesta<String>> generarFactura(@Body GenerarFacturaPojo generarFacturaPojo);

    @POST("factura/listar")
    Call<Respuesta<List<FacturaJson>>> listarFacturaPorCliente(@Body ListarFacturaPorClientePojo listarFacturaPorClientePojo);

    @Multipart
    @POST("googleCloudVision/analizarImagen")
    Call<Respuesta<String>> analizarImagen(@Part("idCiudad") RequestBody idCiudad,@Part MultipartBody.Part imagen);


}
