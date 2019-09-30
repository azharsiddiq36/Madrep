package com.azhar.madrep.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.azhar.madrep.Model.ResponseLogin;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;
import com.google.gson.JsonObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etUsername)
    TextInputEditText etUsername;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    SessionManager sessionManager;
    MadrepInterface madrepInterface;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        sessionManager = new SessionManager(LoginActivity.this);
        if(sessionManager.isLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        else{
            madrepInterface = CombineApi.getApiService();
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loading = new ProgressDialog(LoginActivity.this);
                    loading.setMax(100);
                    loading.setTitle("Harap Tunggu");
                    loading.setMessage("Loading...");
                    loading.setProgressStyle(loading.STYLE_SPINNER);
                    loading.show();
                    loading.setCancelable(false);
                    authLogin();
                }
            });
        }
    }

    private void authLogin() {
        final String username,password;
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        madrepInterface.loginRequest(username,password).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                int status = response.body().getStatus();
                if (status == 200){
                    loading.dismiss();
                    String id = response.body().getData().getPenggunaId().toString();
                    String nama = response.body().getData().getPenggunaUsername().toString();
                    String hak_akses = response.body().getData().getPenggunaHakAkses().toString();
                    String token = "a";
                    String foto = response.body().getFoto().toString();
                    String email = response.body().getData().getPenggunaEmail().toString();
                    sessionManager.saveLogin(id,nama,email,foto,token,hak_akses);
                    final HashMap<String, String> map = sessionManager.getDetailsLoggin();
                    Log.d("kambing", "onResponse: "+map.get(sessionManager.KEY_PENGGUNA_FOTO));
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    loading.dismiss();

                    Toast.makeText(LoginActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Harap Periksa Jaringan Anda", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                Log.d("Kambing",""+t.toString());
            }
        });
    }
}


