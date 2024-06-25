package com.uagrm.lectormedidor.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.json.AsignacionJson;
import com.uagrm.lectormedidor.util.PasoParametro;
import com.uagrm.lectormedidor.util.Preferencia;


public class DetalleAsignacionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private Preferencia preferencia;
    private GoogleMap mMap;
    private Typeface typeFaceRegular, typeFaceBold;
    private Toolbar tb_barra;
    private TextView tv_bar_titulo;
    private TextView tv_titulo_cliente,tv_cliente,
            tv_titulo_direccion,tv_direccion,tv_titulo_referencia,tv_referencia;
    private Button btn_lecturar;
    private AsignacionJson asignacionJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_asignacion);
        asignacionJson = PasoParametro.Asignacion;
        iniciar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
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
        tv_bar_titulo.setText("Detalle de asignacion");

        tv_titulo_cliente = findViewById(R.id.tv_titulo_cliente);
        tv_cliente = findViewById(R.id.tv_cliente);
        tv_titulo_direccion = findViewById(R.id.tv_titulo_direccion);
        tv_direccion = findViewById(R.id.tv_direccion);
        tv_titulo_referencia = findViewById(R.id.tv_titulo_referencia);
        tv_referencia = findViewById(R.id.tv_referencia);
        btn_lecturar = findViewById(R.id.btn_lecturar);

        tv_titulo_cliente.setTypeface(typeFaceBold);
        tv_titulo_direccion.setTypeface(typeFaceBold);
        tv_titulo_referencia.setTypeface(typeFaceBold);

        tv_cliente.setTypeface(typeFaceRegular);
        tv_direccion.setTypeface(typeFaceRegular);
        tv_referencia.setTypeface(typeFaceRegular);

        btn_lecturar.setTypeface(typeFaceBold);
        btn_lecturar.setOnClickListener(this);
        if(asignacionJson.getEstado().equals(1)){
            btn_lecturar.setVisibility(View.GONE);
        }

        tv_cliente.setText(asignacionJson.getCliente().getNombres()+" "+asignacionJson.getCliente().getApellidos());
        tv_direccion.setText(asignacionJson.getCliente().getDireccion());
        tv_referencia.setText(asignacionJson.getCliente().getReferencia());


        SupportMapFragment fragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLng ubicacion = new LatLng(Double.parseDouble(asignacionJson.getLatitud()), Double.parseDouble(asignacionJson.getLongitud()));
        BitmapDrawable bitmapdrawOrigen = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marcador);
        Bitmap bOrigen = bitmapdrawOrigen.getBitmap();
        Bitmap smallMarkerOrigen = Bitmap.createScaledBitmap(bOrigen, 150, 150, false);

        mMap.addMarker(new MarkerOptions().position(ubicacion)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarkerOrigen))
                .title(asignacionJson.getDireccion())
                .snippet(asignacionJson.getReferencia()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16));

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_lecturar:
                PasoParametro.Asignacion = asignacionJson;
                startActivity(new Intent(DetalleAsignacionActivity.this,LecturarActivity.class));
            break;
        }
    }
}
