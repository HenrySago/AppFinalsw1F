package com.uagrm.lectormedidor.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.json.FacturaJson;
import com.uagrm.lectormedidor.util.PasoParametro;
import com.uagrm.lectormedidor.util.Preferencia;


public class DetalleFacturaActivity extends AppCompatActivity {
    private Toolbar tb_barra;
    private TextView tv_bar_titulo,tv_titulo,tv_titulo_consumo,tv_titulo_consumo_lecturado,tv_consumo,tv_consumo_lecturado,tv_titulo_costo,tv_costo,tv_titulo_facturacion,
            tv_nit,tv_nombre,tv_titulo_direccion,tv_direccion;
    private Typeface typeFaceRegular, typeFaceBold;
    private Preferencia preferencia;
    private FacturaJson facturaJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_factura);
        facturaJson = PasoParametro.Factura;
        iniciar();
    }


    private void iniciar() {
        preferencia = new Preferencia(this);
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
        tv_bar_titulo.setText("Detalle de la factura");
        
        tv_titulo = findViewById(R.id.tv_titulo);
        tv_titulo.setTypeface(typeFaceBold);
        tv_titulo.setText("Factura nro. "+facturaJson.getId());

        tv_titulo_consumo_lecturado = findViewById(R.id.tv_titulo_consumo_lecturado);
        tv_titulo_consumo_lecturado.setTypeface(typeFaceBold);

        tv_consumo_lecturado = findViewById(R.id.tv_consumo_lecturado);
        tv_consumo_lecturado.setTypeface(typeFaceRegular);
        tv_consumo_lecturado.setText(facturaJson.getMedicion().getConsumo()+" m3");

        tv_titulo_consumo = findViewById(R.id.tv_titulo_consumo);
        tv_titulo_consumo.setTypeface(typeFaceBold);

        tv_consumo = findViewById(R.id.tv_consumo);
        tv_consumo.setTypeface(typeFaceRegular);
        tv_consumo.setText(facturaJson.getMedicion().getConsumoReal()+" m3");

        tv_titulo_costo = findViewById(R.id.tv_titulo_costo);
        tv_titulo_costo.setTypeface(typeFaceBold);

        tv_costo = findViewById(R.id.tv_costo);
        tv_costo.setTypeface(typeFaceRegular);
        tv_costo.setText(facturaJson.getMedicion().getTotal()+" Bs");

        tv_titulo_facturacion = findViewById(R.id.tv_titulo_facturacion);
        tv_titulo_facturacion.setTypeface(typeFaceBold);

        tv_nit = findViewById(R.id.tv_nit);
        tv_nit.setTypeface(typeFaceRegular);
        tv_nit.setText("Ni: "+facturaJson.getMedicion().getCliente().getNit());

        tv_nombre = findViewById(R.id.tv_nombre);
        tv_nombre.setTypeface(typeFaceRegular);
        tv_nombre.setText("Nombre: "+facturaJson.getMedicion().getCliente().getNombreFactura());

        tv_titulo_direccion = findViewById(R.id.tv_titulo_direccion);
        tv_titulo_direccion.setTypeface(typeFaceBold);

        tv_direccion = findViewById(R.id.tv_direccion);
        tv_direccion.setTypeface(typeFaceRegular);
        tv_direccion.setText(facturaJson.getMedicion().getDireccion());


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        atras();
    }

    private void atras() {
        DetalleFacturaActivity.this.finish();
    }

   



   
   
}
