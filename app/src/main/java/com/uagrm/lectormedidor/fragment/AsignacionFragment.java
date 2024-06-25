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
import com.uagrm.lectormedidor.adapter.AsignacionAdapter;
import com.uagrm.lectormedidor.dialog.Animacion;
import com.uagrm.lectormedidor.dialog.DialogDefault;
import com.uagrm.lectormedidor.dialog.DialogDefaultInterface;
import com.uagrm.lectormedidor.dialog.Icon;
import com.uagrm.lectormedidor.interfaces.ImagenCapturada;
import com.uagrm.lectormedidor.json.AsignacionJson;
import com.uagrm.lectormedidor.pojo.ListarAsignacionPorColaboradorPojo;
import com.uagrm.lectormedidor.retrofit.Cliente;
import com.uagrm.lectormedidor.retrofit.LectorMedidorService;
import com.uagrm.lectormedidor.retrofit.Respuesta;
import com.uagrm.lectormedidor.util.Preferencia;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AsignacionFragment extends Fragment implements View.OnClickListener {
    private Preferencia preferencia;
    private AsignacionAdapter asignacionAdapter;
    private Typeface typeFaceRegular,typeFaceBold;
    private TextView tv_pendiente,tv_pendiente_selected,tv_realizado,tv_realizado_selected;
    private RecyclerView rv_asignacion;
    private LinearLayout ly_pendiente,ly_realizado;
    private SweetAlertDialog sweetAlertDialog;
    private String estado = "0";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asignacion, container, false);
        iniciar(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
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

        tv_realizado  = view.findViewById(R.id.tv_realizado);
        tv_realizado.setTypeface(typeFaceBold);
        tv_realizado_selected  = view.findViewById(R.id.tv_realizado_selected);


        ly_pendiente = view.findViewById(R.id.ly_pendiente);
        ly_pendiente.setOnClickListener(this);
        ly_realizado = view.findViewById(R.id.ly_realizado);
        ly_realizado .setOnClickListener(this);

        tv_pendiente_selected.setVisibility(View.VISIBLE);
        tv_realizado_selected.setVisibility(View.INVISIBLE);
        estado = "0";

        rv_asignacion = view.findViewById(R.id.rv_asignacion);
        rv_asignacion.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        asignacionAdapter = new AsignacionAdapter(getContext());
        cargarDatos();
    }



    private void cargarDatos() {

        sweetAlertDialog.setTitleText("Listando asignaciones");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        ListarAsignacionPorColaboradorPojo listarAsignacionPorColaboradorPojo = new ListarAsignacionPorColaboradorPojo();
        listarAsignacionPorColaboradorPojo.setIdColaborador(preferencia.getColaboradorId());
        listarAsignacionPorColaboradorPojo.setEstado(estado);
        LectorMedidorService service = Cliente.getClient();
        Call<Respuesta<List<AsignacionJson>>> call = service.listarAsignacionesPorColaborador(listarAsignacionPorColaboradorPojo);
        call.enqueue(new Callback<Respuesta<List<AsignacionJson>>>() {
            @Override
            public void onResponse(Call<Respuesta<List<AsignacionJson>>> call, Response<Respuesta<List<AsignacionJson>>> response) {
                if (response.isSuccessful()) {
                    try{
                        Respuesta<List<AsignacionJson>> respuesta = response.body();
                        if (respuesta.respuestaExitosa()) {
                            sweetAlertDialog.dismiss();
                            asignacionAdapter.addAll(respuesta.getData());
                            rv_asignacion.setAdapter(asignacionAdapter);
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
            public void onFailure(Call<Respuesta<List<AsignacionJson>>> call, Throwable t) {
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
                tv_realizado_selected.setVisibility(View.INVISIBLE);
                estado = "0";
                cargarDatos();
                break;
            case R.id.ly_realizado:
                tv_pendiente_selected.setVisibility(View.INVISIBLE);
                tv_realizado_selected.setVisibility(View.VISIBLE);
                estado = "1";
                cargarDatos();
                break;
        }
    }

}