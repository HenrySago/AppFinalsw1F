package com.uagrm.lectormedidor.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.json.ClienteJson;
import com.uagrm.lectormedidor.json.ColaboradorJson;
import com.uagrm.lectormedidor.pojo.LoginClientePojo;
import com.uagrm.lectormedidor.pojo.LoginColaboradorPojo;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.Preferencia;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Typeface typeFaceRegular, typeFaceBold;
    private EditText et_correo, et_password;
    private TextView tv_correo, tv_password;
    private Button btn_ingresar,btn_olvide_password;
    private Preferencia preferencia;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniciar();
    }

    private void iniciar() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        sweetAlertDialog.getProgressHelper().setBarWidth(8);

        preferencia = new Preferencia(this);
        typeFaceRegular = Typeface.createFromAsset(getAssets(), "fonts/MulishRegular.ttf");
        typeFaceBold = Typeface.createFromAsset(getAssets(), "fonts/MulishBold.ttf");
        et_correo = findViewById(R.id.et_correo);
        et_correo.addTextChangedListener(onChangedListener);
        et_correo.setTypeface(typeFaceRegular);
        et_password = findViewById(R.id.et_password);
        et_password.addTextChangedListener(onChangedListener);

        et_password.setTypeface(typeFaceRegular);
        et_password.setOnEditorActionListener(new EditorActionListener());

        btn_ingresar = findViewById(R.id.btn_ingresar);
        btn_ingresar.setTypeface(typeFaceBold);
        btn_ingresar.setOnClickListener(this);


        btn_olvide_password = findViewById(R.id.btn_olvide_password);
        btn_olvide_password.setTypeface(typeFaceBold);
        btn_olvide_password.setOnClickListener(this);

        tv_correo = findViewById(R.id.tv_correo);
        tv_correo.setTypeface(typeFaceBold);
        tv_password = findViewById(R.id.tv_password);
        tv_password.setTypeface(typeFaceBold);


        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if(!verificarPermiso()){
            mostrarDialogoPermiso();
        }


    }


    private boolean verificarPermiso() {
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED || READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED
                || CAMERA != PackageManager.PERMISSION_GRANTED) {
            Log.v("parmisos", "No se tiene permiso");
            return false;
        } else {
            Log.v("parmisos", "Se tiene permiso");
            return true;
        }
    }


    private void mostrarDialogoPermiso() {
        new DialogDefault.Builder(LoginActivity.this)
                .setTitle("Para mejorar tu experiencia")
                .setMessage("Acepta los permisos de tu cámara")
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
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 225);
                    }
                })
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.CAMERA)) {
                    Log.v("verificarPermisoCamara", "shouldShowRequestPermissionRationale true");
                }else{
                    Log.v("verificarPermisoCamara", "shouldShowRequestPermissionRationale false");
                }
            }
        }
    }

    private void loginCliente() {
        if (!hayCamposVaciosLogin()) {
            LoginClientePojo loginClientePojo = new LoginClientePojo();
            loginClientePojo.setCorreo(et_correo.getText().toString());
            loginClientePojo.setPassword(et_password.getText().toString());
            sweetAlertDialog.setTitleText("Iniciando sesión");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<ClienteJson>> call = service.loginCliente(loginClientePojo);
            call.enqueue(new Callback<Respuesta<ClienteJson>>() {
                @Override
                public void onResponse(Call<Respuesta<ClienteJson>> call, Response<Respuesta<ClienteJson>> response) {
                    if (response.isSuccessful()) {
                        try{
                            Respuesta<ClienteJson> respuesta = response.body();
                            if (respuesta.respuestaExitosa()) {
                                sweetAlertDialog.dismiss();

                                preferencia.setLogeado(true);
                                preferencia.setNombres(respuesta.getData().getNombres());
                                preferencia.setApellidos(respuesta.getData().getApellidos());
                                preferencia.setClienteId(respuesta.getData().getId().toString());
                                preferencia.setCelular(respuesta.getData().getCelular());
                                preferencia.setEmail(respuesta.getData().getEmail());
                                preferencia.setFotoPerfil(respuesta.getData().getPerfil());
                                preferencia.setTipoUsuario("cliente");
                                preferencia.setPassword(et_password.getText().toString());
                                preferencia.setNit(respuesta.getData().getNit());
                                preferencia.setNombreFactura(respuesta.getData().getNombreFactura());

                                Intent ventana = new Intent(LoginActivity.this, MenuClienteActivity.class);
                                startActivity(ventana);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                                LoginActivity.this.finish();

                            }else{
                                sweetAlertDialog.dismiss();
                                loginColaborador();
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


    private void loginColaborador() {
        LoginColaboradorPojo loginColaboradorPojo = new LoginColaboradorPojo();
        loginColaboradorPojo.setCorreo(et_correo.getText().toString());
        loginColaboradorPojo.setPassword(et_password.getText().toString());
        LectorMedidorService service = Cliente.getClient();
        Call<Respuesta<ColaboradorJson>> call = service.loginColaborador(loginColaboradorPojo);
        call.enqueue(new Callback<Respuesta<ColaboradorJson>>() {
            @Override
            public void onResponse(Call<Respuesta<ColaboradorJson>> call, Response<Respuesta<ColaboradorJson>> response) {
                if (response.isSuccessful()) {
                    try{
                        Respuesta<ColaboradorJson> respuesta = response.body();
                        if (respuesta.respuestaExitosa()) {

                            preferencia.setLogeado(true);
                            preferencia.setNombres(respuesta.getData().getNombres());
                            preferencia.setApellidos(respuesta.getData().getApellidos());
                            preferencia.setColaboradorId(respuesta.getData().getId().toString());
                            preferencia.setCelular(respuesta.getData().getCelular());
                            preferencia.setEmail(respuesta.getData().getEmail());
                            preferencia.setFotoPerfil(respuesta.getData().getPerfil());
                            preferencia.setTipoUsuario("colaborador");
                            preferencia.setPassword(et_password.getText().toString());

                            Intent ventana = new Intent(LoginActivity.this, MenuColaboradorActivity.class);
                            startActivity(ventana);
                            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                            LoginActivity.this.finish();

                        }else{
                            abrirDialogoError("Lo sentimos",respuesta.getMensaje());
                        }
                    } catch (Exception e) {
                        abrirDialogoError("Exception",e.getMessage());
                    }
                }else{
                    abrirDialogoError("Not Successful",response.message());
                }
            }

            @Override
            public void onFailure(Call<Respuesta<ColaboradorJson>> call, Throwable t) {
                abrirDialogoError("On Failure",t.getMessage());
            }
        });
    }


    private void abrirDialogoInternet(String titulo, String mensaje) {
        new DialogDefault.Builder(LoginActivity.this)
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_ingresar:
                if(hayInternet()){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    loginCliente();
                }
                break;
            case R.id.btn_olvide_password:
                Intent ventana = new Intent(LoginActivity.this, RecuperarPasswordActivity.class);
                startActivity(ventana);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                LoginActivity.this.finish();
                break;
        }
    }



    private TextWatcher onChangedListener = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(et_password.getText().toString().length()>0 && et_correo.getText().toString().length()>0){
                btn_ingresar.setBackground(getResources().getDrawable(R.drawable.button_primary));
            }else{
                btn_ingresar.setBackground(getResources().getDrawable(R.drawable.button_plumb_dark));
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };



    class EditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
            boolean action = false;
            int stringId = -1;
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    if(hayInternet()) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                        loginCliente();
                    }
                    break;
                default:
                    break;
            }
            if (stringId != -1) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                action = true;
            }
            return action;
        }
    }


    private boolean hayCamposVaciosLogin()
    {
        et_correo.setError(null);
        et_password.setError(null);

        if (et_correo.getText().toString().length()<=0) {
            et_correo.setError("Este campo es obligatorio");
            return true;
        } else if (et_password.getText().toString().equals("")) {
            et_password.setError("Este campo es obligatorio");
            return true;
        }  else {
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



}
