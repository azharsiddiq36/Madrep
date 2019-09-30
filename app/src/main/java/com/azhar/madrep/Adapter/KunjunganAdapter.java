package com.azhar.madrep.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import com.azhar.madrep.Activity.MainActivity;
import com.azhar.madrep.Model.Kunjungan;
import com.azhar.madrep.Model.ResponseKunjungan;
import com.azhar.madrep.R;
import com.azhar.madrep.Utils.SessionManager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.azhar.madrep.Rest.CombineApi.img_url;

public class KunjunganAdapter extends RecyclerView.Adapter<KunjunganAdapter.ViewHolder> implements Filterable {
//public class KunjunganAdapter extends RecyclerView.Adapter<KunjunganAdapter.ViewHolder>{
    private ArrayList<Kunjungan> rvData;
    Context context;
    Dialog myDialog;
    private int PLACE_PICKER_REQUEST = 1;
    private ArrayList<Kunjungan> rvDataList;
    SessionManager sessionManager;
    private List<ResponseKunjungan> data;
    public KunjunganAdapter(Context context, ArrayList<Kunjungan> inputData){
        this.context = context;

        rvData=inputData;

        rvDataList = new ArrayList<>(rvData);
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNama,tvTgl,tvLokasi,tvCabang;
        public CardView cvLetter;

//        public ImageView imgDoctor;
        public ViewHolder(View v){
            super(v);
            tvNama = (TextView) v.findViewById(R.id.tvNamaDokter);
            tvCabang = (TextView) v.findViewById(R.id.tvCabang);
            cvLetter=(CardView)v.findViewById(R.id.cvLetter);
            tvLokasi = (TextView)v.findViewById(R.id.tvLokasi);
            tvTgl = (TextView)v.findViewById(R.id.tvTglKunjungan);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kunjungan, parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int pofgdion) {
        final Kunjungan Kunjungan = rvData.get(pofgdion);
        holder.tvNama.setText(Kunjungan.getDokterNama());
        String bulan = getBulan(Kunjungan.getKunjunganBulan()).toUpperCase();
        holder.tvTgl.setText(""+bulan);
        holder.tvCabang.setText(Kunjungan.getKunjunganCabang());
        holder.tvLokasi.setText(""+Kunjungan.getKunjunganLatitude()+", "+Kunjungan.getKunjunganLongitude());
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
                final View popupView = inflater.inflate(R.layout.detail_kunjungan, null);
                //ImageView foto = (ImageView)popupView.findViewById(R.id.kunjunganFoto);
                ImageView close = (ImageView)popupView.findViewById(R.id.btnClose);
                TextView tvKunjunganMinggu,tvKunjunganBulan,tvCabang,tvMap,tvDriving,tvNamaDokter,tvNamaObat;
                Button tutup;
                sessionManager = new SessionManager(context);
                tvKunjunganMinggu = (TextView)popupView.findViewById(R.id.tvMinggu);
                tvKunjunganBulan = (TextView)popupView.findViewById(R.id.tvBulan);
                tvCabang = (TextView)popupView.findViewById(R.id.tvCabang);
                tvMap = (TextView)popupView.findViewById(R.id.tvMap);
                tvNamaDokter = (TextView)popupView.findViewById(R.id.tvNamaDokter);
                tvNamaObat = (TextView)popupView.findViewById(R.id.tvNamaObat);
                tvDriving = (TextView) popupView.findViewById(R.id.tvDrivingDirection);
                HashMap<String,String> map = sessionManager.getDetailsLoggin();
                final PopupWindow popupWindow = new PopupWindow(popupView);
                popupWindow.setWidth(width);
                popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
                popupWindow.setHeight(height);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                tvNamaDokter.setText(Kunjungan.getDokterNama());
                tvNamaObat.setText(Kunjungan.getKunjungan_obat());
//                String geo = "geo:0.482323,101.419719"; //isi dengan posisi awal bros
//                ab = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
//                //set app default untuk buka intent ini apabila ada beberapa aplikasi yang dapat membuka
//                ab.setPackage("com.google.android.apps.maps");
//                ab = new Intent(this,RSAwalBrosLocation.class);
                tvCabang.setText("Cabang "+Kunjungan.getKunjunganCabang());
                tvKunjunganMinggu.setText("Kunjungan Minggu Ke - "+Kunjungan.getKunjunganMinggu());
                String bulan = getBulan(Kunjungan.getKunjunganBulan()).toUpperCase();

                tutup = (Button) popupView.findViewById(R.id.btnTutup);
                tutup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                tvKunjunganBulan.setText(bulan);
                tvMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = null;
                        String maplLabel = "Lokasi Kunjungan";
                        String geo = "geo:"+Kunjungan.getKunjunganLatitude()+","+Kunjungan.getKunjunganLongitude()+"?q="+Kunjungan.getKunjunganLatitude()+","+Kunjungan.getKunjunganLongitude()+"("+maplLabel+")"+"?z=17";
                        i = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
                        i.setPackage("com.google.android.apps.maps");
                        context.startActivity(i);



                    }
                });
                tvDriving.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = null;
                        String geo = "google.navigation:q="+Kunjungan.getKunjunganLatitude()+","+Kunjungan.getKunjunganLongitude();
                        i = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
                        context.startActivity(i);

                    }
                });

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
            ArrayList<Kunjungan> filteredList =new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filteredList.addAll(rvDataList);
            }
            else {
                String [] bulanList = {"january","februari","maret","april","mei","juni","juli","agustus","september","oktober","november","desember"};
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Kunjungan item:rvDataList ){
                    if (item.getDokterNama().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    else if (item.getKunjunganCabang().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    else{

                        String bulan= getBulan(item.getKunjunganBulan());

                        for (int a = 0;a<bulanList.length;a++){
                            if (bulanList[a].toLowerCase().equals(bulan)){
                                if(bulanList[a].toLowerCase().contains(filterPattern))
                                filteredList.add(item);
                            }
                        }
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
    public String getBulan(String b){
        String bulan = "";
        switch (Integer.parseInt(b)){
            case 1:bulan="januari";break;
            case 2:bulan="februari";break;
            case 3:bulan="maret";break;
            case 4:bulan="april";break;
            case 5:bulan="mei";break;
            case 6:bulan="juni";break;
            case 7:bulan="juli";break;
            case 8:bulan="agustus";break;
            case 9:bulan="september";break;
            case 10:bulan="oktober";break;
            case 11:bulan="november";break;
            case 12:bulan="desember";break;
            default:bulan = "terjadi kesalahan";break;
        }
        return bulan;
    }

}
