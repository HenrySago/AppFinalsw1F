package com.uagrm.lectormedidor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.uagrm.lectormedidor.R;
import com.uagrm.lectormedidor.activity.DetalleAsignacionActivity;
import com.uagrm.lectormedidor.activity.LecturarActivity;
import com.uagrm.lectormedidor.json.AsignacionJson;
import com.uagrm.lectormedidor.retrofit.BaseUrl;
import com.uagrm.lectormedidor.util.PasoParametro;

import java.util.ArrayList;
import java.util.List;


public class AsignacionAdapter extends RecyclerView.Adapter<AsignacionAdapter.RevistaViewHolder> {
    private List<AsignacionJson> items;
    private Context context;

    public static class RevistaViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nro,tv_cliente,tv_direccion;
        private Button btn_detalle,btn_lecturar,btn_imprimir;
        public RevistaViewHolder(View v) {
            super(v);
            tv_nro = v.findViewById(R.id.tv_nro);
            tv_cliente = v.findViewById(R.id.tv_cliente);
            tv_direccion = v.findViewById(R.id.tv_direccion);
            btn_detalle = v.findViewById(R.id.btn_detalle);
            btn_lecturar = v.findViewById(R.id.btn_lecturar);
            btn_imprimir = v.findViewById(R.id.btn_imprimir);
            tv_nro.setTypeface(Typeface.createFromAsset(tv_nro.getContext().getAssets(),"fonts/MulishBold.ttf"));
            tv_cliente.setTypeface(Typeface.createFromAsset(tv_cliente.getContext().getAssets(),"fonts/MulishBold.ttf"));
            tv_direccion.setTypeface(Typeface.createFromAsset(tv_direccion.getContext().getAssets(),"fonts/MulishRegular.ttf"));
            btn_detalle.setTypeface(Typeface.createFromAsset(btn_detalle.getContext().getAssets(),"fonts/MulishBold.ttf"));
            btn_lecturar.setTypeface(Typeface.createFromAsset(btn_lecturar.getContext().getAssets(),"fonts/MulishBold.ttf"));
            btn_imprimir.setTypeface(Typeface.createFromAsset(btn_imprimir.getContext().getAssets(),"fonts/MulishBold.ttf"));
        }
    }

    public AsignacionAdapter(Context c) {
        this.items = new ArrayList<>();
        this.context = c;
        notifyDataSetChanged();
    }


    public void addAll(List<AsignacionJson> lista) {
        this.items = new ArrayList<>();
        items.addAll(lista);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public AsignacionAdapter.RevistaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_asignacion, viewGroup, false);
        return new AsignacionAdapter.RevistaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RevistaViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.tv_nro.setText("Asignación Nro. "+items.get(i).getId());
        viewHolder.tv_cliente.setText(items.get(i).getCliente().getNombres()+" "+items.get(i).getCliente().getApellidos());
        viewHolder.tv_direccion.setText(items.get(i).getDireccion());
        if(items.get(i).getEstado().equals(0)){
            viewHolder.btn_lecturar.setVisibility(View.VISIBLE);
            viewHolder.btn_imprimir.setVisibility(View.GONE);
        }else{
            viewHolder.btn_lecturar.setVisibility(View.GONE);
            viewHolder.btn_imprimir.setVisibility(View.VISIBLE);
        }
        viewHolder.btn_detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasoParametro.Asignacion = items.get(i);
                context.startActivity(new Intent(context, DetalleAsignacionActivity.class));
            }
        });
        viewHolder.btn_lecturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasoParametro.Asignacion = items.get(i);
                context.startActivity(new Intent(context, LecturarActivity.class));

            }
        });
        viewHolder.btn_imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(
                        context,
                        BaseUrl.baseUrlReportes+"reporteMedicion/"+items.get(i).getId(),
                        "Reporte medicion Nro. "+items.get(i).getId(),
                        "assets",
                        true
                ));
            }
        });
    }


    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }



}



