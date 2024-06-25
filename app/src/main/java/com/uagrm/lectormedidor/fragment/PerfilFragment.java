package com.uagrm.lectormedidor.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.activity.CambiarPasswordActivity;
import com.uagrm.lectormedidor.activity.EditarPerfilActivity;

import com.uagrm.lectormedidor.retrofit.BaseUrl;
import com.uagrm.lectormedidor.util.Preferencia;


public class PerfilFragment extends Fragment implements View.OnClickListener {
    private Preferencia preferencia;
    private ImageView iv_perfil;
    private Typeface typeFaceRegular,typeFaceBold;
    private TextView tv_titulo_perfil,tv_titulo_nombres,tv_nombres,tv_titulo_apellidos,tv_apellidos,tv_titulo_celular,tv_celular,tv_titulo_correo,tv_correo,
            tv_titulo_nit,tv_nit,tv_titulo_nombre_factura,tv_nombre_factura;
    private Button btn_cambiar_password,btn_editar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        iniciar(view);
        return view;
    }

    private void iniciar(View view) {
        preferencia = new Preferencia(getContext());
        typeFaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/MulishBold.ttf");
        typeFaceRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/MulishRegular.ttf");

        btn_cambiar_password  = view.findViewById(R.id.btn_cambiar_password);
        btn_cambiar_password.setTypeface(typeFaceBold);
        btn_cambiar_password.setOnClickListener(this);

        btn_editar  = view.findViewById(R.id.btn_editar);
        btn_editar.setTypeface(typeFaceBold);
        btn_editar.setOnClickListener(this);

        iv_perfil = view.findViewById(R.id.iv_perfil);

        tv_titulo_perfil = view.findViewById(R.id.tv_titulo_perfil);
        tv_titulo_perfil.setTypeface(typeFaceBold);

        tv_titulo_nombres = view.findViewById(R.id.tv_titulo_nombres);
        tv_titulo_nombres.setTypeface(typeFaceBold);

        tv_nombres = view.findViewById(R.id.tv_nombres);
        tv_nombres.setTypeface(typeFaceRegular);


        tv_titulo_apellidos = view.findViewById(R.id.tv_titulo_apellidos);
        tv_titulo_apellidos.setTypeface(typeFaceBold);

        tv_apellidos = view.findViewById(R.id.tv_apellidos);
        tv_apellidos.setTypeface(typeFaceRegular);


        tv_titulo_celular = view.findViewById(R.id.tv_titulo_celular);
        tv_titulo_celular.setTypeface(typeFaceBold);

        tv_celular = view.findViewById(R.id.tv_celular);
        tv_celular.setTypeface(typeFaceRegular);

        tv_titulo_correo = view.findViewById(R.id.tv_titulo_correo);
        tv_titulo_correo.setTypeface(typeFaceBold);

        tv_correo = view.findViewById(R.id.tv_correo);
        tv_correo.setTypeface(typeFaceRegular);

        tv_titulo_nit = view.findViewById(R.id.tv_titulo_nit);
        tv_titulo_nit.setTypeface(typeFaceBold);

        tv_nit = view.findViewById(R.id.tv_nit);
        tv_nit.setTypeface(typeFaceRegular);

        tv_titulo_nombre_factura = view.findViewById(R.id.tv_titulo_nombre_factura);
        tv_titulo_nombre_factura.setTypeface(typeFaceBold);

        tv_nombre_factura = view.findViewById(R.id.tv_nombre_factura);
        tv_nombre_factura.setTypeface(typeFaceRegular);

        cargarFotoPefil();
        cargarDatos();
    }

    private void cargarDatos() {
        if(preferencia.getTipoUsuario().equals("cliente")){
            tv_nombres.setText(preferencia.getNombres());
            tv_apellidos.setText(preferencia.getApellidos());
            tv_celular.setText(preferencia.getCelular());
            tv_correo.setText(preferencia.getEmail());
            tv_nit.setText(preferencia.getNit());
            tv_nombre_factura.setText(preferencia.getNombreFactura());
            tv_nit.setVisibility(View.VISIBLE);
            tv_nombre_factura.setVisibility(View.VISIBLE);
            tv_titulo_nit.setVisibility(View.VISIBLE);
            tv_titulo_nombre_factura.setVisibility(View.VISIBLE);
        }else{
            tv_nombres.setText(preferencia.getNombres());
            tv_apellidos.setText(preferencia.getApellidos());
            tv_celular.setText(preferencia.getCelular());
            tv_correo.setText(preferencia.getEmail());
            tv_nit.setVisibility(View.GONE);
            tv_nombre_factura.setVisibility(View.GONE);
            tv_titulo_nit.setVisibility(View.GONE);
            tv_titulo_nombre_factura.setVisibility(View.GONE);
        }

    }

    private void cargarFotoPefil() {
        Glide.with(getContext())
                .load(BaseUrl.baseUrlRecursos+preferencia.getFotoPerfil())
                .centerCrop().dontAnimate()
                .placeholder(R.drawable.ic_logo).into(iv_perfil);
    }

    @Override
    public void onStart() {
        super.onStart();
        cargarFotoPefil();
        cargarDatos();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_editar:
                startActivity(new Intent(getContext(), EditarPerfilActivity.class));
                break;
            case R.id.btn_cambiar_password:
                startActivity(new Intent(getContext(), CambiarPasswordActivity.class));
                break;
        }
    }




}