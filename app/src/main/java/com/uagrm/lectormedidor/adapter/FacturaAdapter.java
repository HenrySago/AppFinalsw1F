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
import com.uagrm.lectormedidor.activity.DetalleFacturaActivity;
import com.uagrm.lectormedidor.activity.PagarActivity;
import com.uagrm.lectormedidor.json.FacturaJson;
import com.uagrm.lectormedidor.retrofit.BaseUrl;
import com.uagrm.lectormedidor.util.PasoParametro;

import java.util.ArrayList;
import java.util.List;


public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.RevistaViewHolder> {
    private List<FacturaJson> items;
    private Context context;
    public static class RevistaViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nro,tv_titulo_mensualidad,tv_mensualidad,tv_titulo_direccion,tv_direccion,tv_titulo_total,tv_total;
        private Button btn_detalle,btn_pagar,btn_imprimir;
        public RevistaViewHolder(View v) {
            super(v);
            tv_nro = v.findViewById(R.id.tv_nro);
            tv_titulo_mensualidad = v.findViewById(R.id.tv_titulo_mensualidad);
            tv_mensualidad = v.findViewById(R.id.tv_mensualidad);
            tv_titulo_direccion = v.findViewById(R.id.tv_titulo_direccion);
            tv_direccion = v.findViewById(R.id.tv_direccion);
            tv_titulo_total = v.findViewById(R.id.tv_titulo_total);
            tv_total = v.findViewById(R.id.tv_total);
            btn_detalle = v.findViewById(R.id.btn_detalle);
            btn_pagar = v.findViewById(R.id.btn_pagar);
            btn_imprimir= v.findViewById(R.id.btn_imprimir);

            tv_nro.setTypeface(Typeface.createFromAsset(tv_nro.getContext().getAssets(),"fonts/MulishBold.ttf"));
            tv_titulo_mensualidad.setTypeface(Typeface.createFromAsset(tv_titulo_mensualidad.getContext().getAssets(),"fonts/MulishBold.ttf"));
            tv_mensualidad.setTypeface(Typeface.createFromAsset(tv_mensualidad.getContext().getAssets(),"fonts/MulishRegular.ttf"));
            tv_titulo_direccion.setTypeface(Typeface.createFromAsset(tv_titulo_direccion.getContext().getAssets(),"fonts/MulishBold.ttf"));
            tv_direccion.setTypeface(Typeface.createFromAsset(tv_direccion.getContext().getAssets(),"fonts/MulishRegular.ttf"));
            tv_titulo_total.setTypeface(Typeface.createFromAsset(tv_titulo_total.getContext().getAssets(),"fonts/MulishBold.ttf"));
            tv_total.setTypeface(Typeface.createFromAsset(tv_total.getContext().getAssets(),"fonts/MulishRegular.ttf"));

            btn_detalle.setTypeface(Typeface.createFromAsset(btn_detalle.getContext().getAssets(),"fonts/MulishBold.ttf"));
            btn_pagar.setTypeface(Typeface.createFromAsset(btn_pagar.getContext().getAssets(),"fonts/MulishBold.ttf"));
            btn_imprimir.setTypeface(Typeface.createFromAsset(btn_imprimir.getContext().getAssets(),"fonts/MulishBold.ttf"));

        }
    }

    public FacturaAdapter(Context c) {
        this.items = new ArrayList<>();
        this.context = c;
        notifyDataSetChanged();
    }


    public void addAll(List<FacturaJson> lista) {
        this.items = new ArrayList<>();
        items.addAll(lista);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public FacturaAdapter.RevistaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_factura, viewGroup, false);
        return new FacturaAdapter.RevistaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RevistaViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.tv_nro.setText("Factura Nro. "+items.get(i).getId());
        viewHolder.tv_mensualidad.setText(items.get(i).getMedicion().getMensualidad().getNombre()+" "
                +items.get(i).getMedicion().getMensualidad().getGestion().getNombre());
        viewHolder.tv_direccion.setText(items.get(i).getMedicion().getDireccion());
        viewHolder.tv_total.setText(items.get(i).getMedicion().getTotal()+" Bs");
        if(items.get(i).getPagado().equals(0)){
            viewHolder.btn_pagar.setVisibility(View.VISIBLE);
            viewHolder.btn_imprimir.setVisibility(View.GONE);
        }else{
            viewHolder.btn_pagar.setVisibility(View.GONE);
            viewHolder.btn_imprimir.setVisibility(View.VISIBLE);
        }
        viewHolder.btn_detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasoParametro.Factura = items.get(i);
                context.startActivity(new Intent(context, DetalleFacturaActivity.class));
            }
        });
        viewHolder.btn_pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasoParametro.token = items.get(i).getToken();
                context.startActivity(new Intent(context, PagarActivity.class));
            }
        });
        viewHolder.btn_imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(
                        context,
                        BaseUrl.baseUrlReportes+"reporteFactura/"+items.get(i).getId(),
                        "Reporte factura Nro. "+items.get(i).getId(),
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



