package com.uagrm.lectormedidor.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.json.ClienteJson;
import com.uagrm.lectormedidor.json.ColaboradorJson;
import com.uagrm.lectormedidor.retrofit.BaseUrl;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.Preferencia;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar tb_barra;
    private TextView tv_bar_titulo,tv_titulo_perfil,tv_nombres,tv_apellidos,tv_nit,tv_nombres_factura;
    private Button btn_actualizar,btn_abrir_camara;
    private EditText et_nombres,et_apellidos,et_nit,et_nombres_factura;
    private Typeface typeFaceRegular, typeFaceBold;
    private Preferencia preferencia;
    private SweetAlertDialog sweetAlertDialog;
    private ImageView iv_perfil,iv_camara;
    private LinearLayout ll_cliente;
    private File file = null;
    private String filePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        iniciar();
    }


    private void iniciar() {
        preferencia = new Preferencia(this);
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        sweetAlertDialog.getProgressHelper().setBarWidth(8);
        typeFaceRegular = Typeface.createFromAsset(getAssets(), "fonts/MulishRegular.ttf");
        typeFaceBold = Typeface.createFromAsset(getAssets(), "fonts/MulishBold.ttf");

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

        iv_perfil = findViewById(R.id.iv_perfil);
        ll_cliente = findViewById(R.id.ll_cliente);
        tv_bar_titulo = findViewById(R.id.tv_bar_titulo);
        tv_bar_titulo.setTypeface(typeFaceBold);
        tv_bar_titulo.setText("Editar perfil");


        tv_titulo_perfil = findViewById(R.id.tv_titulo_perfil);
        tv_titulo_perfil.setTypeface(typeFaceBold);

        tv_nombres = findViewById(R.id.tv_nombres);
        tv_nombres.setTypeface(typeFaceBold);

        tv_apellidos = findViewById(R.id.tv_apellidos);
        tv_apellidos.setTypeface(typeFaceBold);

        tv_nit = findViewById(R.id.tv_nit);
        tv_nit.setTypeface(typeFaceBold);

        tv_nombres_factura = findViewById(R.id.tv_nombres_factura);
        tv_nombres_factura.setTypeface(typeFaceBold);


        et_nombres = findViewById(R.id.et_nombres);
        et_nombres.setTypeface(typeFaceRegular);


        et_apellidos = findViewById(R.id.et_apellidos);
        et_apellidos.setTypeface(typeFaceRegular);

        et_nit = findViewById(R.id.et_nit);
        et_nit.setTypeface(typeFaceRegular);

        et_nombres_factura = findViewById(R.id.et_nombres_factura);
        et_nombres_factura.setTypeface(typeFaceRegular);
                

        btn_actualizar = findViewById(R.id.btn_actualizar);
        btn_actualizar.setTypeface(typeFaceBold);
        btn_actualizar.setOnClickListener(this);


        btn_abrir_camara = findViewById(R.id.btn_abrir_camara);
        btn_abrir_camara.setTypeface(typeFaceBold);
        btn_abrir_camara.setOnClickListener(this);

        iv_camara = findViewById(R.id.iv_camara);
        iv_camara.setOnClickListener(this);


        cargarFotoPefil();
        cargarDatos();

    }

    private void cargarDatos() {
        if(preferencia.getTipoUsuario().equals("cliente")) {
            et_nombres.setText(preferencia.getNombres());
            et_apellidos.setText(preferencia.getApellidos());
            et_nit.setText(preferencia.getNit());
            et_nombres_factura.setText(preferencia.getNombreFactura());
            ll_cliente.setVisibility(View.VISIBLE);
        }else{
            et_nombres.setText(preferencia.getNombres());
            et_apellidos.setText(preferencia.getApellidos());
            ll_cliente.setVisibility(View.GONE);
        }

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
    private void cargarFotoPefil() {
        Glide.with(getApplicationContext())
                .load(BaseUrl.baseUrlRecursos+preferencia.getFotoPerfil())
                .centerCrop().dontAnimate()
                .placeholder(R.drawable.ic_logo).into(iv_perfil);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        atras();
    }

    private void atras() {
        EditarPerfilActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_actualizar:
                if(hayInternet()){
                    if(preferencia.getTipoUsuario().equals("cliente")){
                        editarDatosCliente();
                    }else{
                        editarDatosColaborador();
                    }
                }
                break;
            case R.id.btn_abrir_camara:
                if (verificarPermisoCamara()) {
                    ImagePicker.Companion.with(EditarPerfilActivity.this)
                            .cropSquare()
                            .cropOval()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                } else {
                    mostrarDialogoPermisoCamara();
                }
                break;
        }
    }

    private void editarDatosColaborador() {
        if (!hayCamposVaciosColaborador()) {
            RequestBody idColaborador =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, preferencia.getColaboradorId());
            RequestBody nombres =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, et_nombres.getText().toString());
            RequestBody apellidos =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, et_apellidos.getText().toString());
            MultipartBody.Part perfil = null;
            if (file != null) {
                RequestBody requestFile = null;
                requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                perfil = MultipartBody.Part.createFormData("perfil", file.getName(), requestFile);
            }

            sweetAlertDialog.setTitleText("Editar datos colaborador");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<ColaboradorJson>> call = service.editarPerfilColaborador(idColaborador,perfil,nombres,apellidos);
            call.enqueue(new Callback<Respuesta<ColaboradorJson>>() {
                @Override
                public void onResponse(Call<Respuesta<ColaboradorJson>> call, Response<Respuesta<ColaboradorJson>> response) {
                    if (response.isSuccessful()) {
                        try{
                            Respuesta<ColaboradorJson> respuesta = response.body();
                            if (respuesta.respuestaExitosa()) {
                                sweetAlertDialog.dismiss();
                                preferencia.setNombres(respuesta.getData().getNombres());
                                preferencia.setApellidos(respuesta.getData().getApellidos());
                                preferencia.setColaboradorId(respuesta.getData().getId().toString());
                                preferencia.setCelular(respuesta.getData().getCelular());
                                preferencia.setEmail(respuesta.getData().getEmail());
                                preferencia.setFotoPerfil(respuesta.getData().getPerfil());
                                abrirDialogoSuccess("Datos del colaborador actualizados correctamente");
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
                public void onFailure(Call<Respuesta<ColaboradorJson>> call, Throwable t) {
                    abrirDialogoError("On Failure",t.getMessage());
                    sweetAlertDialog.dismiss();
                }
            });
        }
    }

    private boolean hayCamposVaciosColaborador() {
        et_nombres.setError(null);
        et_apellidos.setError(null);


        if (et_nombres.getText().toString().length()<=0) {
            et_nombres.setError("Este campo es obligatorio");
            return true;
        } else if (et_apellidos.getText().toString().length()<=0) {
            et_apellidos.setError("Este campo es obligatorio");
            return true;
        }else {
            return false;
        }
    }

    private void editarDatosCliente() {
        if (!hayCamposVaciosCliente()) {
            RequestBody idCliente =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, preferencia.getClienteId());
            RequestBody nombres =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, et_nombres.getText().toString());
            RequestBody apellidos =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, et_apellidos.getText().toString());
            RequestBody nit =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, et_nit.getText().toString());
            RequestBody nombreFactura =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, et_nombres_factura.getText().toString());
            MultipartBody.Part perfil = null;
            if (file != null) {
                RequestBody requestFile = null;
                requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                perfil = MultipartBody.Part.createFormData("perfil", file.getName(), requestFile);
            }


            sweetAlertDialog.setTitleText("Editar datos cliente");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<ClienteJson>> call = service.editarPerfilCliente(idCliente,perfil,nombres,apellidos,nit,nombreFactura);
            call.enqueue(new Callback<Respuesta<ClienteJson>>() {
                @Override
                public void onResponse(Call<Respuesta<ClienteJson>> call, Response<Respuesta<ClienteJson>> response) {
                    if (response.isSuccessful()) {
                        try{
                            Respuesta<ClienteJson> respuesta = response.body();
                            if (respuesta.respuestaExitosa()) {
                                sweetAlertDialog.dismiss();
                                preferencia.setNombres(respuesta.getData().getNombres());
                                preferencia.setApellidos(respuesta.getData().getApellidos());
                                preferencia.setClienteId(respuesta.getData().getId().toString());
                                preferencia.setCelular(respuesta.getData().getCelular());
                                preferencia.setEmail(respuesta.getData().getEmail());
                                preferencia.setFotoPerfil(respuesta.getData().getPerfil());
                                preferencia.setNit(respuesta.getData().getNit());
                                preferencia.setNombreFactura(respuesta.getData().getNombreFactura());

                                abrirDialogoSuccess("Datos del cliente actualizados correctamente");
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
                public void onFailure(Call<Respuesta<ClienteJson>> call, Throwable t) {
                    abrirDialogoError("On Failure",t.getMessage());
                    sweetAlertDialog.dismiss();
                }
            });
        }
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
                        EditarPerfilActivity.this.finish();
                    }
                })
                .build();
    }

    private boolean hayCamposVaciosCliente() {
        et_nombres.setError(null);
        et_apellidos.setError(null);
        et_nit.setError(null);
        et_nombres_factura.setError(null);

        if (et_nombres.getText().toString().length()<=0) {
            et_nombres.setError("Este campo es obligatorio");
            return true;
        } else if (et_apellidos.getText().toString().length()<=0) {
            et_apellidos.setError("Este campo es obligatorio");
            return true;
        }else if (et_nit.getText().toString().length()<=0) {
            et_nit.setError("Este campo es obligatorio");
            return true;
        }else if (et_nombres_factura.getText().toString().length()<=0) {
            et_nombres_factura.setError("Este campo es obligatorio");
            return true;
        }else {
            return false;
        }
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


    private void abrirDialogoInternet(String titulo, String mensaje) {
        new DialogDefault.Builder(EditarPerfilActivity.this)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

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

    private void mostrarDialogoPermisoCamara() {
        new DialogDefault.Builder(EditarPerfilActivity.this)
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
                        ActivityCompat.requestPermissions(EditarPerfilActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                                225);
                    }
                })
                .build();
    }
}
