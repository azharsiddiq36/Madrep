package com.azhar.madrep.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
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

import com.azhar.madrep.Model.Obat;
import com.azhar.madrep.Model.ResponseObat;
import com.azhar.madrep.R;
import com.azhar.madrep.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.azhar.madrep.Rest.CombineApi.img_url;

public class ObatAdapter extends RecyclerView.Adapter<ObatAdapter.ViewHolder> implements Filterable {
//public class ObatAdapter extends RecyclerView.Adapter<ObatAdapter.ViewHolder>{
    private ArrayList<Obat> rvData;
    Context context;
    Dialog myDialog;

    private ArrayList<Obat> rvDataList;
    SessionManager sessionManager;
    private List<ResponseObat> data;
    public ObatAdapter(Context context, ArrayList<Obat> inputData){
        this.context = context;

        rvData=inputData;

        rvDataList = new ArrayList<>(rvData);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNamaObat,tvKategori,tvNomor;
        public CardView cvLetter;
        public ImageView imgDoctor;
        public ViewHolder(View v){
            super(v);
            tvNamaObat = (TextView) v.findViewById(R.id.tvNamaObat);
            imgDoctor = (ImageView)v.findViewById(R.id.imgObat);
            tvKategori = (TextView) v.findViewById(R.id.tvKategori);
            cvLetter=(CardView)v.findViewById(R.id.cvLetter);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_obat, parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pofgdion) {
        final Obat Obat = rvData.get(pofgdion);
        holder.tvNamaObat.setText(Obat.getObatNama());
        holder.tvKategori.setText(Obat.getKategoriNama());
        String gambar = Obat.getObatFoto();
        if (gambar.equals("")){
            gambar = "assets/images/user.png";
        }

        Picasso.get()
                .load(img_url+gambar)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.imgDoctor);
//

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
                final View popupView = inflater.inflate(R.layout.detail_obat, null);
                ImageView foto = (ImageView)popupView.findViewById(R.id.obatFoto);
                ImageView close = (ImageView)popupView.findViewById(R.id.btnClose);
                Button tutup;
                TextView tvObatType,tvObatNama,tvObatKeterangan;
                sessionManager = new SessionManager(context);
                tvObatNama = (TextView)popupView.findViewById(R.id.tvNamaObat);
                tvObatType = (TextView)popupView.findViewById(R.id.tvTypeObat);
                tvObatKeterangan = (TextView)popupView.findViewById(R.id.tvKeterangan);
                tvObatKeterangan.setText(Obat.getObatRincian());
                HashMap<String,String> map = sessionManager.getDetailsLoggin();
                final PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(width);
                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
                popupWindow.setHeight(height-200);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                tvObatNama.setText(Obat.getObatNama());
                tvObatType.setText(Obat.getKategoriNama());
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
                if (Obat.getObatFoto().equals("")){
                    gambar = "assets/images/user.png";
                }
                else{
                    gambar = Obat.getObatFoto();
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
            ArrayList<Obat> filteredList =new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filteredList.addAll(rvDataList);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Obat item:rvDataList ){
                    if (item.getObatNama().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    else if (item.getKategoriNama().toLowerCase().contains(filterPattern)){
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
