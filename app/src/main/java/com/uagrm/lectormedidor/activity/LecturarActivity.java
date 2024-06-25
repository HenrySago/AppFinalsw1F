package com.uagrm.lectormedidor.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.json.AsignacionJson;
import com.uagrm.lectormedidor.json.CostoMensualidadClienteJson;
import com.uagrm.lectormedidor.pojo.GenerarFacturaPojo;
import com.uagrm.lectormedidor.pojo.ObtenerTarifaPorCiudadPojo;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.PasoParametro;
import com.uagrm.lectormedidor.util.Preferencia;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LecturarActivity extends AppCompatActivity implements View.OnClickListener {

    private Preferencia preferencia;
    private Typeface typeFaceRegular, typeFaceBold;
    private Toolbar tb_barra;
    private ImageView iv_camara;
    private TextView tv_bar_titulo;
    private Button btn_verificar_datos,btn_cancelar,btn_abrir_camara;
    private SweetAlertDialog sweetAlertDialog;
    private File file = null;
    private String filePath = "";
    private AsignacionJson asignacionJson = null;
    private String Consumo = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura);
        asignacionJson = PasoParametro.Asignacion;
        iniciar();
    }


    private void iniciar() {
        preferencia = new Preferencia(this);
        typeFaceRegular = Typeface.createFromAsset(getAssets(), "fonts/MulishRegular.ttf");
        typeFaceBold = Typeface.createFromAsset(getAssets(), "fonts/MulishBold.ttf");

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        sweetAlertDialog.getProgressHelper().setBarWidth(8);


        tb_barra = findViewById(R.id.tb_barra);
        setSupportActionBar(tb_barra);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tb_barra.setNavigationIcon(R.drawable.ic_icon_atras);
        tb_barra.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atras();
            }
        });
        tv_bar_titulo = findViewById(R.id.tv_bar_titulo);
        tv_bar_titulo.setTypeface(typeFaceBold);
        tv_bar_titulo.setText("Lecturar");

        iv_camara = findViewById(R.id.iv_camara);


        btn_verificar_datos = findViewById(R.id.btn_verificar_datos);
        btn_cancelar = findViewById(R.id.btn_cancelar);
        btn_abrir_camara = findViewById(R.id.btn_abrir_camara);

        btn_verificar_datos.setTypeface(typeFaceBold);
        btn_cancelar.setTypeface(typeFaceBold);
        btn_abrir_camara.setTypeface(typeFaceBold);


        btn_verificar_datos.setOnClickListener(this);
        btn_cancelar.setOnClickListener(this);
        btn_abrir_camara.setOnClickListener(this);

    }


    private void atras() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        atras();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_verificar_datos:
                if(hayInternet()) {
                    analizarFoto();
                }
                break;
            case R.id.btn_abrir_camara:
                if (verificarPermisoCamara()) {
                    ImagePicker.Companion.with(LecturarActivity.this)
                            .cropSquare()
                            .cropOval()
                            .compress(2048)
                            .maxResultSize(1080, 1080)
                            .start();
                } else {
                    mostrarDialogoPermisoCamara();
                }
                break;
            case R.id.btn_cancelar:
                    atras();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.v("takeFoto","onActivityResult");

            Uri fileUri = data.getData();
            iv_camara.setImageURI(fileUri);

            file = ImagePicker.Companion.getFile(data);
            filePath = ImagePicker.Companion.getFilePath(data);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tarea Cancelada", Toast.LENGTH_SHORT).show();
        }
    }

    private void analizarFoto() {
        Log.v("takeFoto","analizarFoto");

        MultipartBody.Part imagen = null;
        if (file != null) {
            RequestBody requestFile = null;
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            imagen = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);
        }
        RequestBody idCiudad =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, asignacionJson.getCliente().getIdCiudad().toString());

        sweetAlertDialog.setTitleText("Analizando imagen");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        LectorMedidorService service = Cliente.getClient();
        Call<Respuesta<String>> call = service.analizarImagen(idCiudad,imagen);
        call.enqueue(new Callback<Respuesta<String>>() {
            @Override
            public void onResponse(Call<Respuesta<String>> call, Response<Respuesta<String>> response) {
                if (response.isSuccessful()) {
                    try{
                        Respuesta<String> respuesta = response.body();
                        if (respuesta.respuestaExitosa()) {
                            sweetAlertDialog.dismiss();
                            Consumo = respuesta.getData();
                            Log.v("takeFoto", respuesta.getData());
                            obtenerPrecioPorCiudad();
                        }else{
                            abrirDialogoError("Lo sentimos",respuesta.getMensaje());
                            sweetAlertDialog.dismiss();
                        }
                    } catch (Exception e) {
                        abrirDialogoError("Exception",e.getMessage());
                        sweetAlertDialog.dismiss();
                    }
                }else{
                    abrirDialogoError("Not Successful",response.message());
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Respuesta<String>> call, Throwable t) {
                abrirDialogoError("On Failure",t.getMessage());
                sweetAlertDialog.dismiss();
            }
        });
    }

    private void obtenerPrecioPorCiudad() {
        ObtenerTarifaPorCiudadPojo obtenerTarifaPorCiudadPojo = new ObtenerTarifaPorCiudadPojo();
        obtenerTarifaPorCiudadPojo.setIdCiudad(asignacionJson.getCliente().getIdCiudad().toString());
        obtenerTarifaPorCiudadPojo.setIdCliente(asignacionJson.getCliente().getId().toString());
        obtenerTarifaPorCiudadPojo.setConsumo(Consumo);
        sweetAlertDialog.setTitleText("Verificando datos");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        LectorMedidorService service = Cliente.getClient();
        Call<Respuesta<CostoMensualidadClienteJson>> call = service.obtenerTarifaPorCiudad(obtenerTarifaPorCiudadPojo);
        call.enqueue(new Callback<Respuesta<CostoMensualidadClienteJson>>() {
            @Override
            public void onResponse(Call<Respuesta<CostoMensualidadClienteJson>> call, Response<Respuesta<CostoMensualidadClienteJson>> response) {
                if (response.isSuccessful()) {
                    try{
                        Respuesta<CostoMensualidadClienteJson> respuesta = response.body();
                        if (respuesta.respuestaExitosa()) {
                            sweetAlertDialog.dismiss();
                            mostrarDialogoVerificarDatos(respuesta.getData().getCosto(),respuesta.getData().getConsumoReal());
                        }else{
                            abrirDialogoError("Lo sentimos",respuesta.getMensaje());
                            sweetAlertDialog.dismiss();
                        }
                    } catch (Exception e) {
                        abrirDialogoError("Exception",e.getMessage());
                        sweetAlertDialog.dismiss();
                    }
                }else{
                    abrirDialogoError("Not Successful",response.message());
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Respuesta<CostoMensualidadClienteJson>> call, Throwable t) {
                abrirDialogoError("On Failure",t.getMessage());
                sweetAlertDialog.dismiss();
            }
        });
    }

    private void mostrarDialogoVerificarDatos(double costo,double consumoReal) {

        final BottomSheetDialog bottomSheerDialog = new BottomSheetDialog(this);
        View parentView = getLayoutInflater().inflate(R.layout.dialog_verificar_datos, null);
        parentView.setBackgroundColor(Color.TRANSPARENT);
        bottomSheerDialog.setContentView(parentView);
        bottomSheerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        final TextView tv_titulo,tv_titulo_consumo,tv_titulo_consumo_lecturado,tv_consumo,tv_consumo_lecturado,tv_titulo_costo,tv_costo,tv_titulo_facturacion,
                tv_nit,tv_nombre,tv_titulo_direccion,tv_direccion;
        Button btn_generar_factura,btn_cancelar;
        tv_titulo = bottomSheerDialog.findViewById(R.id.tv_titulo);
        tv_titulo.setTypeface(typeFaceBold);

        tv_titulo_consumo = bottomSheerDialog.findViewById(R.id.tv_titulo_consumo);
        tv_titulo_consumo.setTypeface(typeFaceBold);


        tv_titulo_consumo_lecturado = bottomSheerDialog.findViewById(R.id.tv_titulo_consumo_lecturado);
        tv_titulo_consumo_lecturado.setTypeface(typeFaceBold);


        tv_consumo_lecturado = bottomSheerDialog.findViewById(R.id.tv_consumo_lecturado);
        tv_consumo_lecturado.setTypeface(typeFaceRegular);
        tv_consumo_lecturado.setText(Consumo+" m3");


        tv_consumo = bottomSheerDialog.findViewById(R.id.tv_consumo);
        tv_consumo.setTypeface(typeFaceRegular);
        tv_consumo.setText(consumoReal+" m3");

        tv_titulo_costo = bottomSheerDialog.findViewById(R.id.tv_titulo_costo);
        tv_titulo_costo.setTypeface(typeFaceBold);

        tv_costo = bottomSheerDialog.findViewById(R.id.tv_costo);
        tv_costo.setTypeface(typeFaceRegular);
        tv_costo.setText(costo+" Bs");

        tv_titulo_facturacion = bottomSheerDialog.findViewById(R.id.tv_titulo_facturacion);
        tv_titulo_facturacion.setTypeface(typeFaceBold);

        tv_nit = bottomSheerDialog.findViewById(R.id.tv_nit);
        tv_nit.setTypeface(typeFaceRegular);
        tv_nit.setText("Ni: "+asignacionJson.getCliente().getNit());

        tv_nombre = bottomSheerDialog.findViewById(R.id.tv_nombre);
        tv_nombre.setTypeface(typeFaceRegular);
        tv_nombre.setText("Nombre: "+asignacionJson.getCliente().getNombreFactura());

        tv_titulo_direccion = bottomSheerDialog.findViewById(R.id.tv_titulo_direccion);
        tv_titulo_direccion.setTypeface(typeFaceBold);

        tv_direccion = bottomSheerDialog.findViewById(R.id.tv_direccion);
        tv_direccion.setTypeface(typeFaceRegular);
        tv_direccion.setText(asignacionJson.getCliente().getDireccion());


        btn_generar_factura = bottomSheerDialog.findViewById(R.id.btn_generar_factura);
        btn_generar_factura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hayInternet()) {
                    bottomSheerDialog.cancel();
                    generarFactura();
                }
            }
        });
        btn_cancelar = bottomSheerDialog.findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheerDialog.cancel();
            }
        });

        bottomSheerDialog.show();
    }

    private void generarFactura() {
        GenerarFacturaPojo generarFacturaPojo = new GenerarFacturaPojo();
        generarFacturaPojo.setIdMedicion(asignacionJson.getId().toString());
        generarFacturaPojo.setConsumo(Consumo);
        sweetAlertDialog.setTitleText("Generar factura");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        LectorMedidorService service = Cliente.getClient();
        Call<Respuesta<String>> call = service.generarFactura(generarFacturaPojo);
        call.enqueue(new Callback<Respuesta<String>>() {
            @Override
            public void onResponse(Call<Respuesta<String>> call, Response<Respuesta<String>> response) {
                if (response.isSuccessful()) {
                    try{
                        Respuesta<String> respuesta = response.body();
                        if (respuesta.respuestaExitosa()) {
                            sweetAlertDialog.dismiss();
                            abrirDialogoSuccess(respuesta.getData());
                        }else{
                            abrirDialogoError("Lo sentimos",respuesta.getMensaje());
                            sweetAlertDialog.dismiss();
                        }
                    } catch (Exception e) {
                        abrirDialogoError("Exception",e.getMessage());
                        sweetAlertDialog.dismiss();
                    }
                }else{
                    abrirDialogoError("Not Successful",response.message());
                    sweetAlertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Respuesta<String>> call, Throwable t) {
                abrirDialogoError("On Failure",t.getMessage());
                sweetAlertDialog.dismiss();
            }
        });
    }


    private void abrirDialogoInternet(String titulo, String mensaje) {
        new DialogDefault.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(R.drawable.ic_sin_conexion, Icon.Visible)
                .setPositiveBtnText("Aceptar")
                .setAnimation(Animacion.POP)
                .isCancellable(false)
                .OnPositiveClicked(new DialogDefaultInterface() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }

    private void abrirDialogoError(String titulo, String mensaje) {
        new DialogDefault.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(R.drawable.ic_alerta, Icon.Visible)
                .setPositiveBtnText("Aceptar")
                .setAnimation(Animacion.POP)
                .isCancellable(false)
                .OnPositiveClicked(new DialogDefaultInterface() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();
    }


    private void abrirDialogoSuccess(String titulo) {
        new DialogDefault.Builder(this)
                .setTitle(titulo)
                .setIcon(R.drawable.ic_success_check, Icon.Visible)
                .setPositiveBtnText("Aceptar")
                .setAnimation(Animacion.POP)
                .isCancellable(false)
                .OnPositiveClicked(new DialogDefaultInterface() {
                    @Override
                    public void OnClick() {
                        finish();
                    }
                })
                .build();
    }

    private boolean hayInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean network = (actNetInfo != null && actNetInfo.isConnected());
        if(!network){
            abrirDialogoInternet("Not Networking","Revise su conexión a internet");
        }
        return network;
    }


    private boolean verificarPermisoCamara() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
        ) {
            Log.v("verificarPermisoCamara", "Se tiene permiso wiii!");
            return true;
        } else {
            Log.v("verificarPermisoCamara", "No se tiene permiso :( ");
            return false;
        }
    }


    private void mostrarDialogoPermisoCamara() {
        new DialogDefault.Builder(LecturarActivity.this)
                .setTitle("Por favor, acepte los permisos de Camara de su dispositivo")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnText("Aceptar")
                .setAnimation(Animacion.POP)
                .isCancellable(false)
                .OnNegativeClicked(new DialogDefaultInterface() {
                    @Override
                    public void OnClick() {
                    }
                })
                .OnPositiveClicked(new DialogDefaultInterface() {
                    @Override
                    public void OnClick() {
                        ActivityCompat.requestPermissions(LecturarActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                                225);
                    }
                })
                .build();
    }


}
