package com.uagrm.lectormedidor.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.adapter.FacturaAdapter;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.json.FacturaJson;
import com.uagrm.lectormedidor.pojo.ListarFacturaPorClientePojo;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.Preferencia;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FacturaFragment extends Fragment implements View.OnClickListener {
    private Preferencia preferencia;
    private FacturaAdapter facturaAdapter;
    private Typeface typeFaceRegular,typeFaceBold;
    private TextView tv_pendiente,tv_pendiente_selected,tv_pagado,tv_pagado_selected;
    private RecyclerView rv_factura;
    private LinearLayout ly_pendiente,ly_pagado;
    private SweetAlertDialog sweetAlertDialog;
    private String estado = "0";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_factura, container, false);
        iniciar(view);
        return view;
    }

    private void iniciar(View view) {
        preferencia = new Preferencia(getContext());
        typeFaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/MulishBold.ttf");
        typeFaceRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/MulishRegular.ttf");

        sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        sweetAlertDialog.getProgressHelper().setBarWidth(8);

        tv_pendiente  = view.findViewById(R.id.tv_pendiente);
        tv_pendiente.setTypeface(typeFaceBold);
        tv_pendiente_selected  = view.findViewById(R.id.tv_pendiente_selected);

        tv_pagado  = view.findViewById(R.id.tv_pagado);
        tv_pagado.setTypeface(typeFaceBold);
        tv_pagado_selected  = view.findViewById(R.id.tv_pagado_selected);

        rv_factura = view.findViewById(R.id.rv_factura);
        rv_factura.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        facturaAdapter = new FacturaAdapter(getContext());

        ly_pendiente = view.findViewById(R.id.ly_pendiente);
        ly_pendiente.setOnClickListener(this);
        ly_pagado = view.findViewById(R.id.ly_pagado);
        ly_pagado .setOnClickListener(this);

        tv_pendiente_selected.setVisibility(View.VISIBLE);
        tv_pagado_selected.setVisibility(View.INVISIBLE);
        estado = "0";
        cargarDatos();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {

        sweetAlertDialog.setTitleText("Listando facturas");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        ListarFacturaPorClientePojo listarFacturaPorClientePojo = new ListarFacturaPorClientePojo();
        listarFacturaPorClientePojo.setIdCliente(preferencia.getClienteId());
        listarFacturaPorClientePojo.setPagado(estado);
        LectorMedidorService service = Cliente.getClient();
        Call<Respuesta<List<FacturaJson>>> call = service.listarFacturaPorCliente(listarFacturaPorClientePojo);
        call.enqueue(new Callback<Respuesta<List<FacturaJson>>>() {
            @Override
            public void onResponse(Call<Respuesta<List<FacturaJson>>> call, Response<Respuesta<List<FacturaJson>>> response) {
                if (response.isSuccessful()) {
                    try{
                        Respuesta<List<FacturaJson>> respuesta = response.body();
                        if (respuesta.respuestaExitosa()) {
                            sweetAlertDialog.dismiss();
                            facturaAdapter.addAll(respuesta.getData());
                            rv_factura.setAdapter(facturaAdapter);
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
            public void onFailure(Call<Respuesta<List<FacturaJson>>> call, Throwable t) {
                abrirDialogoError("On Failure",t.getMessage());
                sweetAlertDialog.dismiss();
            }
        });


    }


    private void abrirDialogoError(String titulo, String mensaje) {
        new DialogDefault.Builder(getActivity())
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
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ly_pendiente:
                tv_pendiente_selected.setVisibility(View.VISIBLE);
                tv_pagado_selected.setVisibility(View.INVISIBLE);
                estado = "0";
                cargarDatos();
                break;
            case R.id.ly_pagado:
                tv_pendiente_selected.setVisibility(View.INVISIBLE);
                tv_pagado_selected.setVisibility(View.VISIBLE);
                estado = "1";
                cargarDatos();
                break;
        }
    }




}