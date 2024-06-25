package com.uagrm.lectormedidor.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.retrofit.BaseUrl;
import com.uagrm.lectormedidor.util.PasoParametro;

public class PagarActivity extends AppCompatActivity {
    private Toolbar tb_barra;
    private TextView tv_bar_titulo;
    private WebView myWebView;
    private String url;
    private Typeface typeFaceRegular, typeFaceBold;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar);
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
        tv_bar_titulo.setText("Pagar factura");

        myWebView = findViewById(R.id.wv_web);
        myWebView.getSettings().setJavaScriptEnabled(true);
        url = BaseUrl.baseUrlPago+ PasoParametro.token;
        Log.v("pagoWeb",url);
        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if(url.contains(BaseUrl.baseUrlPago+"finalizar")){
                    finish();
                }
                return true;
            }
        });
        myWebView.loadUrl(url);
    }

    private void atras() {
        finish();
    }
}
