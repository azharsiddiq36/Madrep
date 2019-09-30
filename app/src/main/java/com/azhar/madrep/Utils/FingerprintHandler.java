package com.azhar.madrep.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.azhar.madrep.Activity.AbsensiActivity;
import com.azhar.madrep.R;


@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
    private Context mContext;
    public boolean akses;
    public FingerprintHandler(Context mContext){
        this.mContext = mContext;
    }
    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);
    }
    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an auth Error "+errString,false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Pengenalan Identitas Gagal ",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error "+helpString,false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Scan Berhasil, silahkan tekan tombol untuk melanjutkan",true);
    }
    private void update(String s, boolean b) {
        TextView paralabel = (TextView) ((Activity)mContext).findViewById(R.id.textView2);
        ImageView imageView = (ImageView) ((Activity)mContext).findViewById(R.id.btnKunjungan);
        paralabel.setText(s);
        if (b==false){
            paralabel.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
        }
        else{
            paralabel.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            imageView.setImageResource(R.mipmap.action_done);
        }
    }
}
