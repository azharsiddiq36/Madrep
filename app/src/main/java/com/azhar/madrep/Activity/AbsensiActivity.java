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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.madrep.Model.ResponseAbsensi;
import com.azhar.madrep.Model.ResponseAbsensi;
import com.azhar.madrep.Model.ResponseLogin;
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

public class AbsensiActivity extends AppCompatActivity implements LocationListener {
    SessionManager sessionManager;
    @BindView(R.id.textView2)
    TextView mParaLabael;
    @BindView(R.id.btnKunjungan)
    ImageView btnKunjungan;
    LocationManager locationManager;
    MadrepInterface madrepInterface;
    ProgressDialog progressDialog;
    private FingerprintManager mfingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "Android";
    ProgressDialog loading;
    HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        ButterKnife.bind(this);
        ActionBar actionBar;
        loading = new ProgressDialog(getApplicationContext());
        actionBar = getSupportActionBar();assert actionBar != null;
        actionBar.setTitle("Absensi");
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AbsensiActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        progressDialog = new ProgressDialog(AbsensiActivity.this);
        madrepInterface = CombineApi.getApiService();
        sessionManager = new SessionManager(getApplicationContext());
        map = sessionManager.getDetailsLoggin();

        btnKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParaLabael.getText().equals("Scan Berhasil, silahkan tekan tombol untuk melanjutkan")) {
                    mParaLabael.setText("Sedang Melakukan Absen, Harap Tunggu Proses");
                    loading = new ProgressDialog(AbsensiActivity.this);
                    loading.setMax(100);
                    loading.setTitle("Mengirim Data");
                    loading.setMessage("Jaringan yang Bagus akan mempercepat proses ini");
                    loading.setProgressStyle(loading.STYLE_SPINNER);
                    loading.show();
                    loading.setCancelable(false);
                    getCurrentLocation();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            mfingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (!mfingerprintManager.isHardwareDetected()){
                mParaLabael.setText("Perangkat Fingerprint tidak terdeteksi");
            }
            else if(ContextCompat.checkSelfPermission(AbsensiActivity.this,Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
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
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(AbsensiActivity.this);
                    fingerprintHandler.startAuth(mfingerprintManager,cryptoObject);
                }
            }
        }
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
    public void onProviderDisabled(String provider) {
        Toast.makeText(AbsensiActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            loading.dismiss();
            String TAG = "Kambing";
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d("Kambing", "onLocationChanged: "+addresses.get(0).getLocality());
            Log.d("Kambing", "onLocationChanged2: "+addresses.get(0).getSubLocality());
            Log.d("Kambing", "onLocationChanged3: "+addresses.get(0).getCountryName());
            Log.d("Kambing", "onLocationChanged4: "+addresses.get(0).getThoroughfare());
            Log.d("Kambing", "onLocationChanged5: "+addresses.get(0).getSubThoroughfare());
            Log.d(TAG, "onLocationChanged1: "+addresses.get(0).getAddressLine(0));
            if (addresses.get(0).getLocality()!=null){
                loading.dismiss();
                Call<ResponseAbsensi> data = madrepInterface.addAbsensi(map.get(sessionManager.KEY_PENGGUNA_ID),
                        ""+addresses.get(0).getLocality(),
                        ""+addresses.get(0).getLocality(),
                        ""+addresses.get(0).getThoroughfare(),
                        ""+addresses.get(0).getSubLocality(),
                        ""+location.getLatitude(),
                        ""+location.getLongitude());
                data.enqueue(new Callback<ResponseAbsensi>() {
                    @Override
                    public void onResponse(Call<ResponseAbsensi> call, Response<ResponseAbsensi> response) {
                        if (response.body().getStatus()==200){
                            Toast.makeText(AbsensiActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AbsensiActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                        else{
                            mParaLabael.setText("Scan Berhasil, silahkan tekan tombol untuk melanjutkan");
                            Toast.makeText(AbsensiActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAbsensi> call, Throwable t) {
                        mParaLabael.setText("Scan Berhasil, silahkan tekan tombol untuk melanjutkan");
                        Toast.makeText(AbsensiActivity.this, "Harap Periksa jaringan Anda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch(Exception e)
        {

        }

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}