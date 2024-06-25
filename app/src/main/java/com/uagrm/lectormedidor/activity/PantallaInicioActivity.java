package com.uagrm.lectormedidor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.util.Preferencia;


public class PantallaInicioActivity extends AppCompatActivity {
    private Preferencia preferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        iniciar();
    }

    private void iniciar() {
        preferencia = new Preferencia(this);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent ventana;
                if(preferencia.getLogeado()){
                    if(preferencia.getTipoUsuario().equals("cliente")){
                        ventana = new Intent(PantallaInicioActivity.this, MenuClienteActivity.class);
                    }else{
                        ventana = new Intent(PantallaInicioActivity.this, MenuColaboradorActivity.class);
                    }
                }else{
                    ventana = new Intent(PantallaInicioActivity.this, LoginActivity.class);
                }
                startActivity(ventana);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                PantallaInicioActivity.this.finish();
            }
        }, 2000);

    }
}
