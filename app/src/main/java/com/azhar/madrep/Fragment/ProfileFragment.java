package com.azhar.madrep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.madrep.Activity.ChangeFotoProfilActivity;
import com.azhar.madrep.Activity.LoginActivity;
import com.azhar.madrep.Activity.MainActivity;
import com.azhar.madrep.Activity.UbahProfileActivity;
import com.azhar.madrep.R;
import com.azhar.madrep.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.azhar.madrep.Rest.CombineApi.img_url;


public class ProfileFragment extends Fragment {
    @BindView(R.id.btnLogout)
    Button logout;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.GantiFoto)
    TextView tvGantiFoto;
    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvHakAkses)
    TextView tvAkses;
    @BindView(R.id.btnUbahProfile)
    Button btnUbahProfile;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android.support.v7.app.ActionBar actionBar =
                ((MainActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;actionBar.setTitle("Profile");
        sessionManager = new SessionManager(getActivity());
        final HashMap<String, String> map = sessionManager.getDetailsLoggin();
        tvNama.setText(map.get(sessionManager.KEY_PENGGUNA_USERNAME));
        if(map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES).toLowerCase().equals("spv")){
            tvAkses.setText("Supervisor");
        }
        else{
            tvAkses.setText(map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES));
        }

        tvEmail.setText(map.get(sessionManager.KEY_PENGGUNA_EMAIL));
        getFoto(map.get(sessionManager.KEY_PENGGUNA_FOTO));
        tvGantiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),ChangeFotoProfilActivity.class);
                startActivity(i);
            }
        });
        btnUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),UbahProfileActivity.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
                Intent intent = new Intent(getActivity(),LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void getFoto(String foto) {
        Picasso.get()
                .load(img_url+foto)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(ivProfile);
    }

}
