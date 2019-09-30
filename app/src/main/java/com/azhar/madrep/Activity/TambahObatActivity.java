package com.azhar.madrep.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.azhar.madrep.Model.Kategori;
import com.azhar.madrep.Model.ResponseKategori;
import com.azhar.madrep.Model.ResponseObat;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.SessionManager;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahObatActivity extends AppCompatActivity {
    @BindView(R.id.btnKirim)
    Button btnKirim;
    @BindView(R.id.ivFotoObat)
    ImageView ivFotoObat;
    @BindView(R.id.etNamaObat)
    TextInputEditText etNamaObat;
    @BindView(R.id.etKeterangan)
    TextInputEditText etKeterangan;
    @BindView(R.id.tvUpload)
    TextView tvUpload;
    @BindView(R.id.kateogriObat)
    Spinner kategoriObat;
    MadrepInterface madrepInterface;
    SessionManager sessionManager;
    ArrayList<Kategori> listObat = new ArrayList<>();
    String [] obat;
    String partimage;
    ProgressDialog pd;
    final int REQUEST_GALLERY = 9544;
    private final String TAG = "kambing";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_obat);
        ButterKnife.bind(this);
        if (ActivityCompat.checkSelfPermission(TambahObatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TambahObatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY);
        }
        /*
        else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
        }
        */
        madrepInterface = CombineApi.getApiService();
        loadObat();
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"open gallery"),REQUEST_GALLERY);

//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"open gallery"),REQUEST_GALLERY);
            }
        });
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivFotoObat!=null){

                    File imageFile = new File(partimage);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"),imageFile);

                    MultipartBody.Part partGambar = MultipartBody.Part.createFormData("foto_obat",imageFile.getName(),requestBody);
                    //File imageFile = new File(partimage);
                    //RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"),imageFile);
                    //MultipartBody.Part partGambar = MultipartBody.Part.createFormData("foto_obat",imageFile.getName(),requestBody);
                    RequestBody obat_nama = RequestBody.create(
                            MediaType.parse("text/plain"),
                            ""+etNamaObat.getText().toString());
                    RequestBody obat_kategori = RequestBody.create(
                            MediaType.parse("text/plain"),
                            ""+kategoriObat.getSelectedItem().toString());
                    RequestBody keterangan = RequestBody.create(
                            MediaType.parse("text/plain"),
                            ""+etKeterangan.getText().toString());
                    Log.d(TAG, "onClick: "+partGambar);
                    madrepInterface.uploadObat(partGambar,obat_nama,obat_kategori,keterangan).enqueue(new Callback<ResponseObat>() {
                        @Override
                        public void onResponse(Call<ResponseObat> call, Response<ResponseObat> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(TambahObatActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: "+response.body().getStatus());
                                Intent i = new Intent(TambahObatActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(TambahObatActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: "+response.body());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseObat> call, Throwable t) {
                            Log.d(TAG, "onResponse: "+t.toString());
                            Toast.makeText(TambahObatActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(TambahObatActivity.this, "Pilih Foto", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void loadObat() {
         Call<ResponseKategori> data = madrepInterface.getListKategori();
        data.enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                if (response.body().getStatus()==200){
                    listObat = response.body().getData();
                    obat = new String [listObat.size()+1];
                    for (int i = 0; i<=listObat.size();i++){
                        if (i == 0 ){
                            obat[i] = "---Pilih Kategori---";
                        }
                        else{
                            obat[i] = listObat.get(i-1).getKategoriNama();
                        }
                    }
                    final ArrayAdapter<String> jabatanAdapter = new ArrayAdapter<>(TambahObatActivity.this,
                            R.layout.sp_obat,obat);
                    kategoriObat.setAdapter(jabatanAdapter);
                }
                else{
                    Toast.makeText(TambahObatActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                Toast.makeText(TambahObatActivity.this, "Periksa Jaringan", Toast.LENGTH_SHORT).show();
            }
        });

    }
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
                ivFotoObat.setImageBitmap(scaled);
                /*Uri dataImage = data.getData();
                String[] imageprojection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(dataImage,imageprojection,null,null,null);
                if (cursor !=null ){
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageprojection[0]);
                    partimage = cursor.getString(indexImage);
                    cursor.close();
                    if (partimage != null){
//                        File image = new File(partimage);
                        ivFotoObat.setImageBitmap(BitmapFactory.decodeFile(partimage));
                        Picasso.get()
                                .load(dataImage)
                                .resize(6000,2000)
                                .onlyScaleDown()
                                .centerInside()
                                .into((ImageView) findViewById(R.id.ivFotoObat));
                                */
                    }
                }
            }
        }
    }
}