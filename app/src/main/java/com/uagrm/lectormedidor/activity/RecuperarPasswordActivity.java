package com.uagrm.lectormedidor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.pojo.EnviarCodigoRecuperacionPasswordPojo;
import com.uagrm.lectormedidor.pojo.ValidarCodigoRecuperacionPasswordPojo;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.Preferencia;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecuperarPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar tb_barra;
    private TextView tv_correo,tv_codigo,tv_sms,tv_conteo,tv_bar_titulo;
    private Button btn_enviar_codigo,btn_verificar_codigo;
    private EditText et_correo,et_codigo;
    private Typeface typeFaceRegular, typeFaceBold;
    private LinearLayout ll_codigo;
    private Handler handler = null;
    private int contador;
    private SweetAlertDialog sweetAlertDialog;
    private Preferencia preferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);
        iniciar();
    }


    private void iniciar() {
        preferencia = new Preferencia(this);
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        sweetAlertDialog.getProgressHelper().setBarWidth(8);

        typeFaceRegular = Typeface.createFromAsset(getAssets(), "fonts/MulishRegular.ttf");
        typeFaceBold = Typeface.createFromAsset(getAssets(), "fonts/MulishBold.ttf");

        ll_codigo = findViewById(R.id.ll_codigo);

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
        tv_bar_titulo.setText("Recuperar contraseña");

        tv_correo = findViewById(R.id.tv_correo);
        tv_correo.setTypeface(typeFaceBold);

        tv_codigo = findViewById(R.id.tv_codigo);
        tv_codigo.setTypeface(typeFaceBold);

        tv_sms = findViewById(R.id.tv_sms);
        tv_sms.setTypeface(typeFaceBold);

        tv_conteo = findViewById(R.id.tv_conteo);
        tv_conteo.setTypeface(typeFaceBold);

        et_correo = findViewById(R.id.et_correo);
        et_correo.setTypeface(typeFaceRegular);

        et_codigo = findViewById(R.id.et_codigo);
        et_codigo.setTypeface(typeFaceRegular);

        btn_enviar_codigo = findViewById(R.id.btn_enviar_codigo);
        btn_enviar_codigo.setTypeface(typeFaceBold);
        btn_enviar_codigo.setOnClickListener(this);

        btn_verificar_codigo = findViewById(R.id.btn_verificar_codigo);
        btn_verificar_codigo.setTypeface(typeFaceBold);
        btn_verificar_codigo.setOnClickListener(this);

        btn_enviar_codigo.setVisibility(View.VISIBLE);
        btn_verificar_codigo.setVisibility(View.GONE);
        tv_sms.setVisibility(View.GONE);
        tv_conteo.setVisibility(View.GONE);
        ll_codigo.setVisibility(View.GONE);
    }

    private void ejecutarConteo(){
        contador = 25;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                metodoConteo();
                if(handler!=null){
                    handler.postDelayed(this,1000);
                }
            }
        },0);
    }

    private void metodoConteo() {
        if(contador==0){
            handler = null;
            btn_enviar_codigo.setVisibility(View.VISIBLE);
            btn_verificar_codigo.setVisibility(View.VISIBLE);
            tv_sms.setVisibility(View.GONE);
            tv_conteo.setVisibility(View.GONE);
            tv_conteo.setText("");
        }
        tv_conteo.setText("En "+contador+" segundos podrás reenviar el codigo, verifica si tu correo electrónico es correcto.");
        contador--;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        atras();
    }

    private void atras() {
        startActivity(new Intent(RecuperarPasswordActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        RecuperarPasswordActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_enviar_codigo:
                if(hayInternet()){
                    enviarCodigoRecuperacionPassword();
                }
                break;
            case R.id.btn_verificar_codigo:
                if(hayInternet()){
                    validarCodigoRecuperacionPassword();
                }
                break;
        }
    }

    private void validarCodigoRecuperacionPassword() {
        if (!hayCamposVaciosValidarCodigo()) {
            ValidarCodigoRecuperacionPasswordPojo validarCodigoRecuperacionPasswordPojo = new ValidarCodigoRecuperacionPasswordPojo();
            validarCodigoRecuperacionPasswordPojo.setCorreo(et_correo.getText().toString());
            validarCodigoRecuperacionPasswordPojo.setCodigo(et_codigo.getText().toString());
            sweetAlertDialog.setTitleText("Validando codigo");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<String>> call = service.validarCodigoRecuperacionPassword(validarCodigoRecuperacionPasswordPojo);
            call.enqueue(new Callback<Respuesta<String>>() {
                @Override
                public void onResponse(Call<Respuesta<String>> call, Response<Respuesta<String>> response) {
                    if (response.isSuccessful()) {
                        try{
                            Respuesta<String> respuesta = response.body();
                            if (respuesta.respuestaExitosa()) {
                                sweetAlertDialog.dismiss();
                                preferencia.setEmail(et_correo.getText().toString().trim());
                                handler = null;
                                Intent ventana = new Intent(RecuperarPasswordActivity.this, NuevoPasswordActivity.class);
                                startActivity(ventana);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                                RecuperarPasswordActivity.this.finish();

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
    }


    private void enviarCodigoRecuperacionPassword() {
        if (!hayCamposVaciosEnviarCodigo()) {
            EnviarCodigoRecuperacionPasswordPojo enviarCodigoRecuperacionPasswordPojo = new EnviarCodigoRecuperacionPasswordPojo();
            enviarCodigoRecuperacionPasswordPojo.setCorreo(et_correo.getText().toString());
            sweetAlertDialog.setTitleText("Validando correo");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<String>> call = service.enviarCodigoRecuperacionPassword(enviarCodigoRecuperacionPasswordPojo);
            call.enqueue(new Callback<Respuesta<String>>() {
                @Override
                public void onResponse(Call<Respuesta<String>> call, Response<Respuesta<String>> response) {
                    if (response.isSuccessful()) {
                        try{
                            Respuesta<String> respuesta = response.body();
                            if (respuesta.respuestaExitosa()) {
                                sweetAlertDialog.dismiss();

                                btn_enviar_codigo.setVisibility(View.GONE);
                                btn_verificar_codigo.setVisibility(View.VISIBLE);
                                tv_sms.setVisibility(View.VISIBLE);
                                tv_conteo.setVisibility(View.VISIBLE);
                                ll_codigo.setVisibility(View.VISIBLE);
                                ejecutarConteo();

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
    }


    private boolean hayCamposVaciosEnviarCodigo()
    {
        et_correo.setError(null);
        et_codigo.setError(null);

        if (et_correo.getText().toString().length()<=0) {
            et_correo.setError("Este campo es obligatorio");
            return true;
        } else {
            return false;
        }
    }

    private boolean hayCamposVaciosValidarCodigo() {
        et_correo.setError(null);
        et_codigo.setError(null);

        if (et_correo.getText().toString().length()<=0) {
            et_correo.setError("Este campo es obligatorio");
            return true;
        } else if (et_codigo.getText().toString().length()<=0) {
            et_codigo.setError("Este campo es obligatorio");
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
        new DialogDefault.Builder(RecuperarPasswordActivity.this)
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

}
