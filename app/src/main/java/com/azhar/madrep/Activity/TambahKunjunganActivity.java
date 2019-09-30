package com.azhar.madrep.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.madrep.Adapter.CACDokterAdapter;
import com.azhar.madrep.Adapter.CBObatAdapter;
import com.azhar.madrep.Model.Dokter;
import com.azhar.madrep.Model.Obat;
import com.azhar.madrep.Model.ResponseDokter;
import com.azhar.madrep.Model.ResponseKunjungan;
import com.azhar.madrep.Model.CBObat;
import com.azhar.madrep.Model.ResponseObat;
import com.azhar.madrep.R;
import com.azhar.madrep.Rest.CombineApi;
import com.azhar.madrep.Rest.MadrepInterface;
import com.azhar.madrep.Utils.FingerprintHandler;
import com.azhar.madrep.Utils.SessionManager;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKunjunganActivity extends AppCompatActivity implements LocationListener{
    @BindView(R.id.btnKunjungan)
    ImageView btnKunjungan;
    @BindView(R.id.etKeterangan)
    TextInputEditText etKeterangan;
    @BindView(R.id.textView2)
    TextView mParaLabael;
    @BindView(R.id.etDokter)
    AutoCompleteTextView actvDokter;
    SessionManager sessionManager;
    LocationManager locationManager;
    MadrepInterface madrepInterface;

    private FingerprintManager mfingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "Android";
    private String id_madrep;
    ProgressDialog loading;
    List<Dokter> dokterArrayList = new ArrayList<>();
    List<Obat> obatArrayList = new ArrayList<>();
    CACDokterAdapter adapter;
    CBObatAdapter CBObatAdapter;
    Spinner spinner;
//    String []select_qualification = {
//            "---Pilih Obat---"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_kunjungan);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TambahKunjunganActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        madrepInterface = CombineApi.getApiService();
        sessionManager = new SessionManager(getApplicationContext());
        final HashMap<String,String> map = sessionManager.getDetailsLoggin();
        id_madrep = map.get(sessionManager.KEY_PENGGUNA_ID);
        spinner = (Spinner) findViewById(R.id.selectObat);
        loadObat();
        loadDokter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            mfingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (!mfingerprintManager.isHardwareDetected()){
                mParaLabael.setText("Perangkat Fingerprint tidak terdeteksi");
            }
            else if(ContextCompat.checkSelfPermission(TambahKunjunganActivity.this,Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                mParaLabael.setText("Izin penggunaan Fingerprint belum di setujui");
            }
            else if(!keyguardManager.isKeyguardSecure()){
                mParaLabael.setText("Tambahkan fingerprint di pengaturan");
            }
            else if(!mfingerprintManager.hasEnrolledFingerprints()){
                mParaLabael.setText("Kamu belum mendaftarkan fingerprint");
            }
            else{
                mParaLabael.setText("Letakkan jari pada perangkat fingerprint");
                generateKey();
                if (cipherInit()){
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(TambahKunjunganActivity.this);
                    fingerprintHandler.startAuth(mfingerprintManager,cryptoObject);

                }
            }
        }

        btnKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FingerprintHandler fingerprintHandler = new FingerprintHandler(TambahKunjunganActivity.this);
                if(mParaLabael.getText().equals("Scan Berhasil, silahkan tekan tombol untuk melanjutkan")){
                    loading = new ProgressDialog(TambahKunjunganActivity.this);
                    loading.setMax(100);
                    loading.setTitle("Mengirim Data");
                    loading.setMessage("Jaringan yang Bagus akan mempercepat proses ini");
                    loading.setProgressStyle(loading.STYLE_SPINNER);
                    loading.show();
                    loading.setCancelable(false);
                    getCurrentLocation();
                }
                else{
                    Toast.makeText(TambahKunjunganActivity.this, "Scan Finger Print untuk mengaktifkan tombol ini", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadObat() {
        Call<ResponseObat> getObat = madrepInterface.getListObat();
        getObat.enqueue(new Callback<ResponseObat>() {
            @Override
            public void onResponse(Call<ResponseObat> call, Response<ResponseObat> response) {
                obatArrayList = response.body().getData();
                ArrayList<CBObat> listVOs = new ArrayList<>();
                for (int i = 0; i <= obatArrayList.size(); i++) {

                    CBObat CBObat = new CBObat();
                    if (i==0){
                        CBObat.setTitle("----Pilih Obat----");
                    }
                    else{
                        CBObat.setTitle(obatArrayList.get(i-1).getObatNama());
                    }
                    CBObat.setSelected(false);
                    listVOs.add(CBObat);
                }
                CBObatAdapter = new CBObatAdapter(TambahKunjunganActivity.this, 0,
                        listVOs);
                spinner.setAdapter(CBObatAdapter);
//                for (int i = 1;i<obatArrayList.size();i++){
//                    select_qualification[i] = obatArrayList.get(i).getObatNama();
//                }
            }

            @Override
            public void onFailure(Call<ResponseObat> call, Throwable t) {

            }
        });
    }

    private void loadDokter() {
        Call<ResponseDokter> getDokter = madrepInterface.getListDokter();
        getDokter.enqueue(new Callback<ResponseDokter>() {
            @Override
            public void onResponse(Call<ResponseDokter> call, Response<ResponseDokter> response) {
                dokterArrayList = response.body().getData();
                adapter = new CACDokterAdapter(TambahKunjunganActivity.this,R.layout.add_kunjungan,dokterArrayList);
                actvDokter.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ResponseDokter> call, Throwable t) {

            }
        });
    }

    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.get(0).getLocality()!=null){
                loading.dismiss();
                String doctor = etKeterangan.getText().toString();
                Call<ResponseKunjungan> data = madrepInterface.addListKunjungan(id_madrep,
                        actvDokter.getText().toString(),
                        ""+addresses.get(0).getLocality(),
                        ""+location.getLatitude(),
                        ""+location.getLongitude(),
                        doctor,
                        CBObatAdapter.getSelectedObat());
                data.enqueue(new Callback<ResponseKunjungan>() {
                    @Override
                    public void onResponse(Call<ResponseKunjungan> call, Response<ResponseKunjungan> response) {
                        if (response.body().getStatus()==200){
                            Toast.makeText(TambahKunjunganActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TambahKunjunganActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                        else{
                            Toast.makeText(TambahKunjunganActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseKunjungan> call, Throwable t) {
                        Toast.makeText(TambahKunjunganActivity.this, "Harap Periksa jaringan Anda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch(Exception e)
        {

        }

    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(TambahKunjunganActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit(){
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            throw new RuntimeException("Failed to get Cipher");
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void generateKey(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        }
        catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e){
            e.printStackTrace();
        }
    }

}
