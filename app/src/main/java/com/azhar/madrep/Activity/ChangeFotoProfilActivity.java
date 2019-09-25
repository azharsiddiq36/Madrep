package com.azhar.madrep.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.madrep.Model.ResponseChangeFotoProfil;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.azhar.madrep.Rest.CombineApi.img_url;

public class ChangeFotoProfilActivity extends AppCompatActivity {
    @BindView(R.id.tvUpload)
    TextView tvUpload;
    @BindView(R.id.ivFoto)
    ImageView ivFoto;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;
    SessionManager sessionManager;
    MadrepInterface madrepInterface;
    HashMap<String,String> map;
    String partimage;
    ProgressDialog pd;
    final int REQUEST_GALLERY = 9544;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_foto_profile);
        ButterKnife.bind(this);
        //meminta izin untuk mengakses gallery/file di smartphone
        if (ActivityCompat.checkSelfPermission(ChangeFotoProfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChangeFotoProfilActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY);
        }
        madrepInterface = CombineApi.getApiService();
        sessionManager = new SessionManager(getApplicationContext());
        map = sessionManager.getDetailsLoggin();
        getFoto(map.get(sessionManager.KEY_PENGGUNA_FOTO));
        //tvUpload untuk memanggil gallery
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"open gallery"),REQUEST_GALLERY);
            }
        });
        //mengirim file foto ke server
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final File imageFile = new File(partimage);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"),imageFile);
                MultipartBody.Part partGambar = MultipartBody.Part.createFormData("foto",imageFile.getName(),requestBody);
                RequestBody pengguna_id = RequestBody.create(
                        MediaType.parse("text/plain"),
                        ""+map.get(sessionManager.KEY_PENGGUNA_ID));
                madrepInterface.changeFoto(partGambar,pengguna_id).enqueue(new Callback<ResponseChangeFotoProfil>() {
                    @Override
                    public void onResponse(Call<ResponseChangeFotoProfil> call, Response<ResponseChangeFotoProfil> response) {
                        if (response.body().getStatus() ==200){
                            Toast.makeText(ChangeFotoProfilActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            updateSession(imageFile.getName());
                            Intent i = new Intent(ChangeFotoProfilActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(ChangeFotoProfilActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseChangeFotoProfil> call, Throwable t) {
                        Log.d("kambing", "onFailure: "+t.toString());
                        Toast.makeText(ChangeFotoProfilActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    //perbarui session
    private void updateSession(String name) {
        String id = map.get(sessionManager.KEY_PENGGUNA_ID);
        String nama = map.get(sessionManager.KEY_PENGGUNA_USERNAME);;
        String hak_akses = map.get(sessionManager.KEY_PENGGUNA_HAK_AKSES);;
        String token = "a";
        String foto = "assets/images/"+name;
        Log.d("kambing", "updateSession: "+foto);
        String email = map.get(sessionManager.KEY_PENGGUNA_EMAIL);;
        sessionManager.saveLogin(id,nama,email,foto,token,hak_akses);
    }
    //code akses file dan select gambar di gallery (wajib)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode ==REQUEST_GALLERY){
                Uri dataImage = data.getData();
                String[] imageprojection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(dataImage,imageprojection,null,null,null);
                if (cursor !=null ){
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageprojection[0]);
                    partimage = cursor.getString(indexImage);
                    cursor.close();
                    if (partimage != null){
                        File image = new File(partimage);

                        //ivFotoObat.setImageBitmap(BitmapFactory.decodeFile(partimage));
                        Bitmap bitmapImage = BitmapFactory.decodeFile(partimage);
                        int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                        ivFoto.setImageBitmap(scaled);
                    }
                }
            }
        }
    }
    //menampilkan foto yang di upload
    private void getFoto(String foto) {
        Picasso.get()
                .load(img_url+foto)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(ivFoto);
    }
}
