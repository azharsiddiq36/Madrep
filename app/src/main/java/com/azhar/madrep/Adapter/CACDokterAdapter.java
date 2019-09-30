package com.azhar.madrep.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.azhar.madrep.Model.Dokter;
import com.azhar.madrep.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.azhar.madrep.Rest.CombineApi.img_url;

public class CACDokterAdapter extends ArrayAdapter<Dokter> {
    private LayoutInflater layoutInflater;
    List<Dokter> mDokters;

    private String idDokter;

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String nama =((Dokter)resultValue).getDokterNama();
            setDokter(((Dokter)resultValue).getDokterId());
            return nama;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Dokter> suggestions = new ArrayList<Dokter>();
                for (Dokter Dokter : mDokters) {
                    if (Dokter.getDokterNama().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(Dokter);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<Dokter>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(mDokters);

            }

            notifyDataSetChanged();
        }
    };

    public String setDokter(String dokterNama) {
        this.idDokter = dokterNama;
        return idDokter;
    }
    public String getDokter(){
        return idDokter;
    }


    public CACDokterAdapter(Context context, int textViewResourceId, List<Dokter> Dokters) {
        super(context, textViewResourceId, Dokters);
        // copy all the Dokters into a master list
        mDokters = new ArrayList<Dokter>(Dokters.size());
        mDokters.addAll(Dokters);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cac_dokter, null);
        }

        final Dokter Dokter = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.actvDokter);
        ImageView ivDokter = (ImageView)view.findViewById(R.id.ivFotoDokter);
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
                .into(ivDokter);
        name.setText(Dokter.getDokterNama());
        return view;
    }

    private static String getId(String id) {
        return id;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
