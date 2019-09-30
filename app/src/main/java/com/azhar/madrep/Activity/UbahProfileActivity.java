package com.azhar.madrep.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.azhar.madrep.Model.ResponseDokter;
import com.azhar.madrep.Model.ResponseLogin;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.ApiClient;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahProfileActivity extends AppCompatActivity {
    SessionManager sessionManager;
    @BindView(R.id.etNama)
    TextInputEditText etNama;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.etRePassword)
    TextInputEditText etRePassword;
    @BindView(R.id.btnUbah)
    Button btnUbah;
    MadrepInterface madrepInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_profile);

//        String title = actionBar.getTitle().toString(); // get the title
        ButterKnife.bind(UbahProfileActivity.this);
        ActionBar actionBar;
        actionBar = getSupportActionBar();assert actionBar != null;
        actionBar.setTitle("Ubah Profile");
        progressDialog = new ProgressDialog(UbahProfileActivity.this);
        madrepInterface = CombineApi.getApiService();
        sessionManager = new SessionManager(getApplicationContext());
        final HashMap<String,String> hashMap= sessionManager.getDetailsLoggin();
        etNama.setText(hashMap.get(sessionManager.KEY_PENGGUNA_USERNAME));
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                ubahPassword(hashMap.get(sessionManager.KEY_PENGGUNA_ID),etPassword.getText().toString(),etRePassword.getText().toString());
            }
        });
    }
    private void ubahPassword(String id,String pw, String rePw) {
        if (pw.equals(rePw)){
            Call<ResponseLogin> data = madrepInterface.ubahPassword(id,pw);
            data.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    if (response.body().getStatus()==200){
                        progressDialog.dismiss();
                        Toast.makeText(UbahProfileActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UbahProfileActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(UbahProfileActivity.this, "Gagal Merubah Data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(UbahProfileActivity.this, "Harap Periksa Jaringan", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(this, "Password dan Re-Password Tidak Sama", Toast.LENGTH_SHORT).show();
        }
    }
}