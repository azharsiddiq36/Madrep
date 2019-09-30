package com.azhar.madrep.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.azhar.madrep.Model.Dokter;
import com.azhar.madrep.Model.ResponseDokter;
import com.azhar.madrep.R;
import com.azhar.madrep.Utils.SessionManager;
import com.squareup.picasso.Picasso;
import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.azhar.madrep.Rest.CombineApi.img_url;

public class DokterAdapter extends RecyclerView.Adapter<DokterAdapter.ViewHolder> implements Filterable {
//public class DokterAdapter extends RecyclerView.Adapter<DokterAdapter.ViewHolder>{
    private ArrayList<Dokter> rvData;
    Context context;
    Dialog myDialog;
    String akses;
    String tahun;
    private ArrayList<Dokter> rvDataList;
    SessionManager sessionManager;
    private List<ResponseDokter> data;
    public DokterAdapter(Context context, ArrayList<Dokter> inputData){
        this.context = context;

        rvData=inputData;

        rvDataList = new ArrayList<>(rvData);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNamaDokter,tvBidang,tvNomor;
        public CardView cvLetter;

        public ImageView imgDoctor;
        public ViewHolder(View v){
            super(v);
            tvNamaDokter = (TextView) v.findViewById(R.id.tvNamaDokter);
            imgDoctor = (ImageView)v.findViewById(R.id.imgDoctor);
            tvBidang = (TextView) v.findViewById(R.id.tvBidang);
            cvLetter=(CardView)v.findViewById(R.id.cvLetter);
            tvNomor = (TextView)v.findViewById(R.id.tvNomor);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dokter, parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pofgdion) {
        final Dokter Dokter = rvData.get(pofgdion);
        holder.tvNamaDokter.setText(Dokter.getDokterNama());
        holder.tvNomor.setText("0"+Dokter.getDokterNomor());
        holder.tvBidang.setText(Dokter.getDokterBidang());
        String gambar = Dokter.getDokterFoto();
        if (gambar.equals("")){
            gambar = "assets/images/user.png";
        }

        Picasso.get()
                .load(img_url+gambar)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.imgDoctor);

        holder.cvLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.detail_dokter, null);
                ImageView foto = (ImageView)popupView.findViewById(R.id.dokterFoto);
                ImageView close = (ImageView)popupView.findViewById(R.id.btnClose);
                TextView tvNama,tvBidang,tvDokterAlamat,tvDokterLahir,tvDokterNomor;
                Button tutup;
                sessionManager = new SessionManager(context);
                tvNama = (TextView)popupView.findViewById(R.id.tvNama);
                tvBidang = (TextView)popupView.findViewById(R.id.tvBidang);
                tvDokterAlamat = (TextView)popupView.findViewById(R.id.tvDokterAlamat);
                tvDokterLahir = (TextView)popupView.findViewById(R.id.tvDokterLahir);
                tvDokterNomor = (TextView)popupView.findViewById(R.id.tvDokterNomor);
                HashMap<String,String> map = sessionManager.getDetailsLoggin();
                final PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(width);
                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
//                popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod); dari bawah
//                popupWindow.setAnimationStyle(android.R.style.Animation_Toast); fadein,bounce
//                popupWindow.setAnimationStyle(android.R.style.Animation_Dialog); fadein,fadeout
//                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent); dari samping kanan
                popupWindow.setHeight(height-200);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                tvNama.setText(Dokter.getDokterNama());
                tvBidang.setText(Dokter.getDokterBidang());
                tvDokterLahir.setText(Dokter.getDokterTglLahir());
                tvDokterAlamat.setText(Dokter.getDokterAlamat());
                tvDokterNomor.setText("0"+Dokter.getDokterNomor());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                LinearLayout lyInflater = (LinearLayout)popupView.findViewById(R.id.lyInflater);
                lyInflater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                tutup = (Button) popupView.findViewById(R.id.btnTutup);
                tutup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                String gambar = "";
                if (Dokter.getDokterFoto().equals("")){
                    gambar = "assets/images/user.png";
                }
                else{
                    gambar = Dokter.getDokterFoto();
                }

                Picasso.get()
                        .load(img_url+gambar)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(foto);
            }
        });

    }
    @Override
    public int getItemCount() {
        return rvData.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Dokter> filteredList =new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filteredList.addAll(rvDataList);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Dokter item:rvDataList ){
                    if (item.getDokterNama().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    else if (item.getDokterBidang().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rvData.clear();
            rvData.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

}
