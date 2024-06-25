package com.uagrm.lectormedidor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.pojo.NuevoPasswordPojo;
import com.uagrm.lectormedidor.pojo.ValidarCodigoRecuperacionPasswordPojo;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.Preferencia;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NuevoPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar tb_barra;
    private TextView tv_nueva,tv_repetir,tv_bar_titulo;
    private Button btn_cambiar;
    private EditText et_nueva,et_repetir;
    private Typeface typeFaceRegular, typeFaceBold;
    private Preferencia preferencia;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_password);
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
        tv_bar_titulo = findViewById(R.id.tv_bar_titulo);
        tv_bar_titulo.setTypeface(typeFaceBold);
        tv_bar_titulo.setText("Nueva contraseña");

        tv_nueva = findViewById(R.id.tv_nueva);
        tv_nueva.setTypeface(typeFaceBold);

        tv_repetir = findViewById(R.id.tv_repetir);
        tv_repetir.setTypeface(typeFaceBold);

        et_nueva = findViewById(R.id.et_nueva);
        et_nueva.setTypeface(typeFaceRegular);

        et_repetir = findViewById(R.id.et_repetir);
        et_repetir.setTypeface(typeFaceRegular);


        btn_cambiar = findViewById(R.id.btn_cambiar);
        btn_cambiar.setTypeface(typeFaceBold);
        btn_cambiar.setOnClickListener(this);


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        atras();
    }

    private void atras() {
        startActivity(new Intent(NuevoPasswordActivity.this, RecuperarPasswordActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        NuevoPasswordActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_cambiar:
                if(hayInternet()){
                    nuevoPassword();
                }
                break;
        }
    }

    private void nuevoPassword() {
        if (!hayCamposVacios()) {
            Log.v("nuevoPassword",preferencia.getEmail());
            NuevoPasswordPojo nuevoPasswordPojo = new NuevoPasswordPojo();
            nuevoPasswordPojo.setCorreo(preferencia.getEmail());
            nuevoPasswordPojo.setPassword(et_nueva.getText().toString());
            nuevoPasswordPojo.setRepetirPassword(et_repetir.getText().toString());
            sweetAlertDialog.setTitleText("Cambiando contraseña");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            LectorMedidorService service = Cliente.getClient();
            Call<Respuesta<String>> call = service.nuevoPassword(nuevoPasswordPojo);
            call.enqueue(new Callback<Respuesta<String>>() {
                @Override
                public void onResponse(Call<Respuesta<String>> call, Response<Respuesta<String>> response) {
                    if (response.isSuccessful()) {
                        try{
                            Respuesta<String> respuesta = response.body();
                            if (respuesta.respuestaExitosa()) {
                                sweetAlertDialog.dismiss();
                                abrirDialogoSuccess("Contraseña cambiada correctamente");
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
                        preferencia.clearDatosLogin();
                        Intent ventana = new Intent(NuevoPasswordActivity.this, LoginActivity.class);
                        startActivity(ventana);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                        NuevoPasswordActivity.this.finish();
                    }
                })
                .build();
    }

    private boolean hayCamposVacios() {
        et_nueva.setError(null);
        et_repetir.setError(null);

        if (et_nueva.getText().toString().length()<=0) {
            et_nueva.setError("Este campo es obligatorio");
            return true;
        } else if (et_repetir.getText().toString().length()<=0) {
            et_repetir.setError("Este campo es obligatorio");
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
        new DialogDefault.Builder(NuevoPasswordActivity.this)
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
