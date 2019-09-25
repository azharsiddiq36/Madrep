package com.azhar.madrep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.azhar.madrep.Activity.AbsensiActivity;
import com.azhar.madrep.Activity.MainActivity;
import com.azhar.madrep.Model.ResponseAbsensi;
import com.azhar.madrep.Model.ResponseDokter;
import com.azhar.madrep.Model.ResponseKunjungan;
import com.azhar.madrep.Model.ResponseObat;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    @BindView(R.id.tvAbsen)
    TextView tvAbsen;
    @BindView(R.id.tvTotalDokter)
    TextView tvTotalDokter;
    @BindView(R.id.tvTotalKunjungan)
    TextView tvTotalKunjungan;
    @BindView(R.id.tvTotalObat)
    TextView tvTotalObat;
    @BindView(R.id.cv2)
    CardView cvDokter;
    @BindView(R.id.cv3)
    CardView cvObat;
    @BindView(R.id.cv4)
    CardView cvKunjungan;

    MadrepInterface madrepInterface;
    SessionManager sessionManager;
    HashMap <String,String> map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android.support.v7.app.ActionBar actionBar =
                ((MainActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;actionBar.setTitle("Home");
        madrepInterface = CombineApi.getApiService();
        sessionManager = new SessionManager(getActivity());
        map = sessionManager.getDetailsLoggin();
        checkAbsen();
        checkTotal();
        cvKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new KunjunganFragment();
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });
        cvObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ObatFragment();
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });
        cvDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DokterFragment();
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });
    }

    private void checkTotal() {
        Call<ResponseDokter> dataDokter = madrepInterface.getTotalDokter();
        dataDokter.enqueue(new Callback<ResponseDokter>() {
            @Override
            public void onResponse(Call<ResponseDokter> call, Response<ResponseDokter> response) {
                if (response.body().getStatus() == 200){
                    tvTotalDokter.setText(response.body().getMessage()+" Total Dokter");
                }
                else{
                    tvTotalDokter.setText("Belum ada Dokter");
                }
            }
            @Override
            public void onFailure(Call<ResponseDokter> call, Throwable t) {

            }
        });

        Call<ResponseObat> dataObat = madrepInterface.getTotalObat();
        dataObat.enqueue(new Callback<ResponseObat>() {
            @Override
            public void onResponse(Call<ResponseObat> call, Response<ResponseObat> response) {
                if (response.body().getStatus() == 200){
                    tvTotalObat.setText(response.body().getMessage()+" Total Obat");
                }
                else{
                    tvTotalObat.setText("Belum ada Obat");
                }
            }

            @Override
            public void onFailure(Call<ResponseObat> call, Throwable t) {

            }
        });
        if(map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES).equals("spv")){
            Call<ResponseKunjungan> dataKunjungan = madrepInterface.getSemuaKunjungan();
            dataKunjungan.enqueue(new Callback<ResponseKunjungan>() {
                @Override
                public void onResponse(Call<ResponseKunjungan> call, Response<ResponseKunjungan> response) {
                    if (response.body().getStatus() == 200) {
                        tvTotalKunjungan.setText(response.body().getMessage() + " Total Kunjungan");
                    } else {
                        tvTotalKunjungan.setText("Belum ada Kunjungan");
                    }
                }

                @Override
                public void onFailure(Call<ResponseKunjungan> call, Throwable t) {

                }
            });
        }else{
            Call<ResponseKunjungan> dataKunjungan = madrepInterface.getTotalKunjungan(map.get(sessionManager.KEY_PENGGUNA_ID),
                    map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES));
            dataKunjungan.enqueue(new Callback<ResponseKunjungan>() {
                @Override
                public void onResponse(Call<ResponseKunjungan> call, Response<ResponseKunjungan> response) {
                    if (response.body().getStatus() == 200) {
                        tvTotalKunjungan.setText(response.body().getMessage() + " Total Kunjungan");
                    } else {
                        tvTotalKunjungan.setText("Belum ada Kunjungan");
                    }
                }

                @Override
                public void onFailure(Call<ResponseKunjungan> call, Throwable t) {

                }
            });
        }



    }

    private void checkAbsen() {
        Call<ResponseAbsensi> data = madrepInterface.checkAbsen(map.get(sessionManager.KEY_PENGGUNA_ID));
        data.enqueue(new Callback<ResponseAbsensi>() {
            @Override
            public void onResponse(Call<ResponseAbsensi> call, Response<ResponseAbsensi> response) {
                if (response.body().getStatus() == 200){
                    tvAbsen.setText("Anda Sudah Melakukan Absen Har ini");
                }
                else{
                    tvAbsen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getActivity(),AbsensiActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseAbsensi> call, Throwable t) {
                Log.d("kambing", "onFailure: "+t.toString());
                Toast.makeText(getContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
